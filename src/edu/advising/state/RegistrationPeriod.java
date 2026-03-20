package edu.advising.state;

import edu.advising.core.Table;
import edu.advising.core.Id;
import edu.advising.core.Column;

import java.time.LocalDateTime;

/**
 * RegistrationPeriod - ORM Entity (Week 9)
 *
 * Represents a row in the registration_periods table.
 * Maps database columns to Java fields using custom annotations.
 */
@Table(name = "registration_periods")
public class RegistrationPeriod {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "semester")
    private String semester;

    @Column(name = "year")
    private int year;

    @Column(name = "open_date")
    private LocalDateTime openDate;

    @Column(name = "close_date")
    private LocalDateTime closeDate;

    @Column(name = "late_registration_end")
    private LocalDateTime lateRegistrationEnd;

    @Column(name = "status")
    private String status;

    // ----------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------

    /** @return the registration period ID */
    public int getId() { return id; }

    /** @param id the registration period ID */
    public void setId(int id) { this.id = id; }

    /** @return the semester (e.g. "FALL", "SPRING") */
    public String getSemester() { return semester; }

    /** @param semester the semester string */
    public void setSemester(String semester) { this.semester = semester; }

    /** @return the academic year */
    public int getYear() { return year; }

    /** @param year the academic year */
    public void setYear(int year) { this.year = year; }

    /** @return the date registration opens */
    public LocalDateTime getOpenDate() { return openDate; }

    /** @param openDate the date registration opens */
    public void setOpenDate(LocalDateTime openDate) { this.openDate = openDate; }

    /** @return the date standard registration closes */
    public LocalDateTime getCloseDate() { return closeDate; }

    /** @param closeDate the date standard registration closes */
    public void setCloseDate(LocalDateTime closeDate) { this.closeDate = closeDate; }

    /** @return the date late registration ends */
    public LocalDateTime getLateRegistrationEnd() { return lateRegistrationEnd; }

    /** @param lateRegistrationEnd the date late registration ends */
    public void setLateRegistrationEnd(LocalDateTime lateRegistrationEnd) {
        this.lateRegistrationEnd = lateRegistrationEnd;
    }

    /** @return the current status string from the DB */
    public String getStatus() { return status; }

    /** @param status the current status string */
    public void setStatus(String status) { this.status = status; }
}
