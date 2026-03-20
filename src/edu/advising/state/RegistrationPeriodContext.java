package edu.advising.state;

import edu.advising.core.DatabaseManager;

/**
 * RegistrationPeriodContext - State Pattern Context (Week 9)
 * Wraps the RegistrationPeriod ORM entity and delegates all
 * state-dependent behavior to the current RegistrationPeriodState.
 */
public class RegistrationPeriodContext {

    private RegistrationPeriod period;
    private RegistrationPeriodState currentState;

    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------
    public RegistrationPeriodContext() {}

    // ----------------------------------------------------------------
    // Factory Methods
    // ----------------------------------------------------------------

    /**
     * forPeriod() - loads a registration period from the DB
     * by semester and year via raw SQL query.
     */
    public static RegistrationPeriodContext forPeriod(String semester, int year) {
        RegistrationPeriodContext ctx = new RegistrationPeriodContext();

        String sql = "SELECT id, semester, `year`, open_date, close_date, " +
                "late_registration_end, current_state " +
                "FROM registration_periods " +
                "WHERE semester = ? AND `year` = ?";

        try {
            RegistrationPeriod p = DatabaseManager.getInstance().fetch(sql, rs -> {
                RegistrationPeriod period = new RegistrationPeriod();
                period.setId(rs.getInt("id"));
                period.setSemester(rs.getString("semester"));
                period.setYear(rs.getInt("year"));
                period.setOpenDate(rs.getTimestamp("open_date").toLocalDateTime());
                period.setCloseDate(rs.getTimestamp("close_date").toLocalDateTime());
                period.setLateRegistrationEnd(rs.getTimestamp("late_registration_end").toLocalDateTime());
                period.setStatus(rs.getString("current_state"));
                return period;
            }, semester, year);

            if (p != null) {
                ctx.period = p;
                ctx.currentState = StateFactory.registrationStateFor(p.getStatus());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load registration period: " + e.getMessage(), e);
        }

        return ctx;
    }

    /**
     * currentPeriod() - loads the period via forPeriod() then
     * immediately calls checkAndAdvance() to auto-advance state.
     */
    public static RegistrationPeriodContext currentPeriod(String semester, int year) {
        RegistrationPeriodContext ctx = forPeriod(semester, year);
        ctx.currentState.checkAndAdvance(ctx);
        return ctx;
    }

    // ----------------------------------------------------------------
    // State Delegation Methods
    // ----------------------------------------------------------------

    /** Opens the registration period. Delegates to current state. */
    public void open() {
        currentState.open(this);
    }

    /** Transitions the period to late registration. Delegates to current state. */
    public void transitionToLate() {
        currentState.transitionToLate(this);
    }

    /** Closes the registration period. Delegates to current state. */
    public void close() {
        currentState.close(this);
    }

    /** Returns true if the student can register in the current state. */
    public boolean canRegister() {
        return currentState.canRegister(this);
    }

    /** Returns true if the student can drop in the current state. */
    public boolean canDrop() {
        return currentState.canDrop(this);
    }

    /** Auto-advances state based on wall-clock dates. */
    public void checkAndAdvance() {
        currentState.checkAndAdvance(this);
    }

    // ----------------------------------------------------------------
    // State Management
    // ----------------------------------------------------------------

    /**
     * Sets the current state — called by concrete state classes
     * during transitions.
     */
    public void setState(RegistrationPeriodState newState) {
        this.currentState = newState;
        if (this.period != null) {
            this.period.setStatus(newState.getStateName());
        }
    }

    /**
     * Persists the current status back to the DB.
     */
    public void persist() {
        if (this.period == null) return;

        String sql = "UPDATE registration_periods SET current_state = ? " +
                "WHERE semester = ? AND `year` = ?";
        try {
            DatabaseManager.getInstance().executeUpdate(sql,
                    currentState.getStateName(),
                    period.getSemester(),
                    period.getYear());
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist registration state: " + e.getMessage(), e);
        }
    }

    // ----------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------

    /** @return the wrapped RegistrationPeriod ORM entity */
    public RegistrationPeriod getPeriod() { return period; }

    /** @param period the RegistrationPeriod ORM entity to wrap */
    public void setPeriod(RegistrationPeriod period) { this.period = period; }

    /** @return the current state */
    public RegistrationPeriodState getCurrentState() { return currentState; }
}