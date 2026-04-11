import edu.advising.core.DatabaseManager;
import edu.advising.notifications.NotificationManager;
import edu.advising.state.*;
import edu.advising.state.facultyWaitlistPermissions.*;
import edu.advising.users.Student;
import edu.advising.users.UserFactory;

import java.sql.SQLException;

// ============================================================================
// WEEK 6: STATE PATTERN — Integration Test Application
// ============================================================================
//
// PURPOSE:
//   Exercises the Transcript Request State Machine and Registration Period
//   State Machine implemented in Week 6.
//   Structured as a plain runnable main() — no JUnit required.
//   Run it with: mvn exec:java@run-week6-test
//
// TESTS COVERED:
//   GROUP 1  — Valid state transitions (PENDING → PROCESSING → READY → SENT)
//   GROUP 2  — Cancel from PENDING, PROCESSING, and READY
//   GROUP 3  — Fail from PROCESSING, retry back to PROCESSING
//   GROUP 4  — Illegal transitions (blocked and logged)
//   GROUP 5  — Terminal states (SENT and CANCELLED block all transitions)
//   GROUP 6  — StateFactory mapping (all 6 status strings + null + unknown)
//   GROUP 7  — Load existing request from DB by ID
//   GROUP 8  — Tracking number format (TR-XXXXXXXX)
//   GROUP 9  — Registration Period valid state transitions
//   GROUP 10 — Registration canRegister() and canDrop()
//   GROUP 11 — checkAndAdvance() auto-advances state
//   GROUP 12 — Terminal state (CLOSED blocks all transitions)
//   GROUP 13 — StateFactory registration mapping
//   GROUP 14 — Enrollment valid state transitions (PENDING → ENROLLED → DROPPED/WITHDRAWN/COMPLETED)
//   GROUP 15 — Enrollment guard methods (canDrop, canWithdraw, canComplete, canReenroll)
//   GROUP 16 — Enrollment illegal transitions (throw IllegalStateException)
//   GROUP 17 — Enrollment StateFactory mapping (all 5 status strings + null + unknown)
//   GROUP 18 — FacultyPermission ORM integration (create → persist → reload from DB)
//   GROUP 19 — FacultyPermission valid state transitions (all paths in state diagram)
//   GROUP 20 — FacultyPermission isValid() (true only for APPROVED)
//   GROUP 21 — FacultyPermission illegal transitions (blocked and logged)
//   GROUP 22 — FacultyPermission StateFactory mapping (all 4 status strings + null + unknown)
//   GROUP 23 — FacultyPermission isExpiredByTime() and checkAndAdvance()
// ============================================================================

public class Week6Test {

    // ── Counters ─────────────────────────────────────────────────────────────
    private static int passed = 0;
    private static int failed = 0;

    // ── Shared fixtures ───────────────────────────────────────────────────────
    private static DatabaseManager db;
    private static NotificationManager notificationManager;
    private static UserFactory userFactory;
    private static Student student;

    private static int fpFacultyId;
    private static int fpStudentId;
    private static int fpSectionId;

    static {
        try {
            fpFacultyId = createFacultyUser();
            fpStudentId = createStudentUser();
            fpSectionId = createSection(fpFacultyId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // =========================================================================
    // ENTRY POINT
    // =========================================================================

    public static void main(String[] args) throws Exception {
        banner("WEEK 6 — STATE PATTERN  |  BetterAdvisor Test Suite");

        try {
            setUp();
        } catch (Exception e) {
            System.err.println("FATAL: setUp() failed — cannot run tests.");
            e.printStackTrace();
            return;
        }

        testValidTransitions();
        testCancelTransitions();
        testFailAndRetry();
        testIllegalTransitions();
        testTerminalStates();
        testStateFactory();
        testLoadFromDatabase();
        testTrackingNumberFormat();
        testRegistrationStateTransitions();
        testRegistrationCanRegisterAndDrop();
        testRegistrationCheckAndAdvance();
        testRegistrationTerminalState();
        testRegistrationStateFactory();
        testEnrollmentStateTransitions();
        testEnrollmentGuardMethods();
        testEnrollmentIllegalTransitions();
        testEnrollmentStateFactory();
        testFacultyPermissionORM();
        runGroup19();
        runGroup20();
        runGroup21();
        runGroup22();
        runGroup23();

        // ── Final report ──────────────────────────────────────────────────────
        banner("RESULTS");
        System.out.printf("  Total  : %d%n", passed + failed);
        System.out.printf("  Passed : %d  ✓%n", passed);
        System.out.printf("  Failed : %d  ✗%n", failed);
        System.out.println(failed == 0
                ? "\n  ALL TESTS PASSED  ✅\n"
                : "\n  SOME TESTS FAILED  ❌\n");

        db.shutdown();
    }

    // =========================================================================
    // SETUP
    // =========================================================================

    private static void setUp() throws Exception {
        header("SET UP");

        db = DatabaseManager.getInstance();
        db.seedDatabase();
        notificationManager = NotificationManager.getInstance();
        userFactory = new UserFactory();

        Student raw = (Student) userFactory.createUser(
                "STUDENT", "jsmith", "Password1!",
                "jsmith@college.edu", "Jane", "Smith", "S10001");
        student = raw;
        note("Student: " + student.getFullName() + " (id=" + student.getId() + ")");
        note("setUp() complete\n");
    }

    // =========================================================================
    // GROUP 1 — Valid State Transitions
    // =========================================================================

    private static void testValidTransitions() {
        header("GROUP 1 — Valid State Transitions");

        TranscriptRequestContext ctx = TranscriptRequestContext.create(
                student.getId(), "OFFICIAL", "MIT", "77 Mass Ave", false);

        check("1.1  new request starts in PENDING",
                "PENDING".equals(ctx.getCurrentStateName()));

        ctx.process();
        check("1.2  process() moves to PROCESSING",
                "PROCESSING".equals(ctx.getCurrentStateName()));

        ctx.prepare();
        check("1.3  prepare() moves to READY",
                "READY".equals(ctx.getCurrentStateName()));

        ctx.dispatch();
        check("1.4  dispatch() moves to SENT",
                "SENT".equals(ctx.getCurrentStateName()));
    }

    // =========================================================================
    // GROUP 2 — Cancel Transitions
    // =========================================================================

    private static void testCancelTransitions() {
        header("GROUP 2 — Cancel Transitions");

        TranscriptRequestContext ctx1 = TranscriptRequestContext.create(
                student.getId(), "OFFICIAL", "Harvard", "Cambridge MA", false);
        ctx1.cancel();
        check("2.1  cancel from PENDING moves to CANCELLED",
                "CANCELLED".equals(ctx1.getCurrentStateName()));

        TranscriptRequestContext ctx2 = TranscriptRequestContext.create(
                student.getId(), "OFFICIAL", "Harvard", "Cambridge MA", false);
        ctx2.process();
        ctx2.cancel();
        check("2.2  cancel from PROCESSING moves to CANCELLED",
                "CANCELLED".equals(ctx2.getCurrentStateName()));

        TranscriptRequestContext ctx3 = TranscriptRequestContext.create(
                student.getId(), "OFFICIAL", "Harvard", "Cambridge MA", false);
        ctx3.process();
        ctx3.prepare();
        ctx3.cancel();
        check("2.3  cancel from READY moves to CANCELLED",
                "CANCELLED".equals(ctx3.getCurrentStateName()));
    }

    // =========================================================================
    // GROUP 3 — Fail and Retry
    // =========================================================================

    private static void testFailAndRetry() {
        header("GROUP 3 — Fail and Retry");

        TranscriptRequestContext ctx = TranscriptRequestContext.create(
                student.getId(), "OFFICIAL", "Stanford", "Stanford CA", false);
        ctx.process();

        ctx.fail("Missing signature on form");
        check("3.1  fail() moves to FAILED",
                "FAILED".equals(ctx.getCurrentStateName()));

        check("3.2  failure reason stored on request",
                "Missing signature on form".equals(ctx.getRequest().getFailureReason()));

        ctx.retry();
        check("3.3  retry() moves back to PROCESSING",
                "PROCESSING".equals(ctx.getCurrentStateName()));

        check("3.4  failure reason cleared after retry",
                ctx.getRequest().getFailureReason() == null);
    }

    // =========================================================================
    // GROUP 4 — Illegal Transitions
    // =========================================================================

    private static void testIllegalTransitions() {
        header("GROUP 4 — Illegal Transitions");

        TranscriptRequestContext ctx1 = TranscriptRequestContext.create(
                student.getId(), "UNOFFICIAL", "UCLA", "Los Angeles CA", false);
        ctx1.prepare();
        check("4.1  prepare() from PENDING does not change state",
                "PENDING".equals(ctx1.getCurrentStateName()));

        ctx1.dispatch();
        check("4.2  dispatch() from PENDING does not change state",
                "PENDING".equals(ctx1.getCurrentStateName()));

        ctx1.retry();
        check("4.3  retry() from PENDING does not change state",
                "PENDING".equals(ctx1.getCurrentStateName()));

        TranscriptRequestContext ctx2 = TranscriptRequestContext.create(
                student.getId(), "UNOFFICIAL", "UCLA", "Los Angeles CA", false);
        ctx2.process();
        ctx2.dispatch();
        check("4.4  dispatch() from PROCESSING does not change state",
                "PROCESSING".equals(ctx2.getCurrentStateName()));

        TranscriptRequestContext ctx3 = TranscriptRequestContext.create(
                student.getId(), "UNOFFICIAL", "UCLA", "Los Angeles CA", false);
        ctx3.process();
        ctx3.prepare();
        ctx3.fail("some reason");
        check("4.5  fail() from READY does not change state",
                "READY".equals(ctx3.getCurrentStateName()));
    }

    // =========================================================================
    // GROUP 5 — Terminal States
    // =========================================================================

    private static void testTerminalStates() {
        header("GROUP 5 — Terminal States");

        TranscriptRequestContext sent = TranscriptRequestContext.create(
                student.getId(), "OFFICIAL", "Yale", "New Haven CT", false);
        sent.process();
        sent.prepare();
        sent.dispatch();
        check("5.1  request is SENT",
                "SENT".equals(sent.getCurrentStateName()));

        sent.cancel();
        check("5.2  cancel() on SENT does not change state",
                "SENT".equals(sent.getCurrentStateName()));

        sent.retry();
        check("5.3  retry() on SENT does not change state",
                "SENT".equals(sent.getCurrentStateName()));

        TranscriptRequestContext cancelled = TranscriptRequestContext.create(
                student.getId(), "OFFICIAL", "Yale", "New Haven CT", false);
        cancelled.cancel();
        check("5.4  request is CANCELLED",
                "CANCELLED".equals(cancelled.getCurrentStateName()));

        cancelled.process();
        check("5.5  process() on CANCELLED does not change state",
                "CANCELLED".equals(cancelled.getCurrentStateName()));

        cancelled.retry();
        check("5.6  retry() on CANCELLED does not change state",
                "CANCELLED".equals(cancelled.getCurrentStateName()));
    }

    // =========================================================================
    // GROUP 6 — StateFactory
    // =========================================================================

    private static void testStateFactory() {
        header("GROUP 6 — StateFactory");

        check("6.1  PENDING maps to PendingTranscriptState",
                StateFactory.transcriptStateFor("PENDING") instanceof PendingTranscriptState);
        check("6.2  PROCESSING maps to ProcessingTranscriptState",
                StateFactory.transcriptStateFor("PROCESSING") instanceof ProcessingTranscriptState);
        check("6.3  READY maps to ReadyTranscriptState",
                StateFactory.transcriptStateFor("READY") instanceof ReadyTranscriptState);
        check("6.4  SENT maps to SentTranscriptState",
                StateFactory.transcriptStateFor("SENT") instanceof SentTranscriptState);
        check("6.5  CANCELLED maps to CancelledTranscriptState",
                StateFactory.transcriptStateFor("CANCELLED") instanceof CancelledTranscriptState);
        check("6.6  FAILED maps to FailedTranscriptState",
                StateFactory.transcriptStateFor("FAILED") instanceof FailedTranscriptState);

        check("6.7  null input returns PendingTranscriptState",
                StateFactory.transcriptStateFor(null) instanceof PendingTranscriptState);

        boolean threwException = false;
        try {
            StateFactory.transcriptStateFor("UNKNOWN_STATUS");
        } catch (IllegalArgumentException e) {
            threwException = true;
        }
        check("6.8  unknown status throws IllegalArgumentException", threwException);
    }

    // =========================================================================
    // GROUP 7 — Load From Database
    // =========================================================================

    private static void testLoadFromDatabase() {
        header("GROUP 7 — Load From Database");

        TranscriptRequestContext original = TranscriptRequestContext.create(
                student.getId(), "OFFICIAL", "Columbia", "New York NY", false);
        original.process();

        int id = original.getRequest().getId();
        check("7.1  request saved to DB with valid ID", id > 0);

        TranscriptRequestContext loaded = TranscriptRequestContext.load(id);
        check("7.2  loaded request is not null", loaded != null);
        check("7.3  loaded request state is PROCESSING",
                "PROCESSING".equals(loaded.getCurrentStateName()));
        check("7.4  loaded request ID matches original",
                loaded.getRequest().getId() == id);
    }

    // =========================================================================
    // GROUP 8 — Tracking Number Format
    // =========================================================================

    private static void testTrackingNumberFormat() {
        header("GROUP 8 — Tracking Number Format");

        TranscriptRequestContext ctx = TranscriptRequestContext.create(
                student.getId(), "UNOFFICIAL", "Berkeley", "Berkeley CA", true);

        String tracking = ctx.getRequest().getTrackingNumber();
        check("8.1  tracking number is not null", tracking != null);
        check("8.2  tracking number starts with TR-",
                tracking != null && tracking.startsWith("TR-"));
        check("8.3  tracking number is 11 characters (TR-XXXXXXXX)",
                tracking != null && tracking.length() == 11);
    }

    // =========================================================================
    // GROUP 9 — Registration Period Valid State Transitions
    // =========================================================================

    private static void testRegistrationStateTransitions() {
        header("GROUP 9 — Registration Period Valid State Transitions");

        RegistrationPeriodContext ctx = new RegistrationPeriodContext();
        ctx.setState(NotOpenRegistrationState.INSTANCE);
        ctx.open();
        check("9.1  open() from NOT_OPEN moves to OPEN",
                "OPEN".equals(ctx.getCurrentState().getStateName()));

        ctx.transitionToLate();
        check("9.2  transitionToLate() from OPEN moves to LATE",
                "LATE".equals(ctx.getCurrentState().getStateName()));

        ctx.close();
        check("9.3  close() from LATE moves to CLOSED",
                "CLOSED".equals(ctx.getCurrentState().getStateName()));

        RegistrationPeriodContext ctx2 = new RegistrationPeriodContext();
        ctx2.setState(OpenRegistrationState.INSTANCE);
        ctx2.close();
        check("9.4  close() from OPEN moves directly to CLOSED",
                "CLOSED".equals(ctx2.getCurrentState().getStateName()));
    }

    // =========================================================================
    // GROUP 10 — Registration canRegister() and canDrop()
    // =========================================================================

    private static void testRegistrationCanRegisterAndDrop() {
        header("GROUP 10 — canRegister() and canDrop()");

        RegistrationPeriodContext notOpen = new RegistrationPeriodContext();
        notOpen.setState(NotOpenRegistrationState.INSTANCE);
        check("10.1  canRegister() returns false from NOT_OPEN",
                !notOpen.canRegister());
        check("10.2  canDrop() returns false from NOT_OPEN",
                !notOpen.canDrop());

        RegistrationPeriodContext open = new RegistrationPeriodContext();
        open.setState(OpenRegistrationState.INSTANCE);
        check("10.3  canRegister() returns true from OPEN",
                open.canRegister());
        check("10.4  canDrop() returns true from OPEN",
                open.canDrop());

        RegistrationPeriodContext late = new RegistrationPeriodContext();
        late.setState(LateRegistrationState.INSTANCE);
        check("10.5  canRegister() returns true from LATE",
                late.canRegister());
        check("10.6  canDrop() returns true from LATE",
                late.canDrop());

        RegistrationPeriodContext closed = new RegistrationPeriodContext();
        closed.setState(ClosedRegistrationState.INSTANCE);
        check("10.7  canRegister() returns false from CLOSED",
                !closed.canRegister());
        check("10.8  canDrop() returns false from CLOSED",
                !closed.canDrop());
    }

    // =========================================================================
    // GROUP 11 — checkAndAdvance() Auto-Advances State
    // =========================================================================

    private static void testRegistrationCheckAndAdvance() {
        header("GROUP 11 — checkAndAdvance() Auto-Advances State");

        RegistrationPeriod p1 = new RegistrationPeriod();
        p1.setOpenDate(java.time.LocalDateTime.now().minusDays(5));
        p1.setCloseDate(java.time.LocalDateTime.now().plusDays(5));
        p1.setLateRegistrationEnd(java.time.LocalDateTime.now().plusDays(10));
        p1.setStatus("NOT_OPEN");
        RegistrationPeriodContext ctx1 = new RegistrationPeriodContext();
        ctx1.setState(NotOpenRegistrationState.INSTANCE);
        ctx1.setPeriod(p1);
        ctx1.checkAndAdvance();
        check("11.1  checkAndAdvance() advances NOT_OPEN to OPEN when openDate is past",
                "OPEN".equals(ctx1.getCurrentState().getStateName()));

        RegistrationPeriod p2 = new RegistrationPeriod();
        p2.setOpenDate(java.time.LocalDateTime.now().minusDays(10));
        p2.setCloseDate(java.time.LocalDateTime.now().minusDays(2));
        p2.setLateRegistrationEnd(java.time.LocalDateTime.now().plusDays(5));
        p2.setStatus("OPEN");
        RegistrationPeriodContext ctx2 = new RegistrationPeriodContext();
        ctx2.setState(OpenRegistrationState.INSTANCE);
        ctx2.setPeriod(p2);
        ctx2.checkAndAdvance();
        check("11.2  checkAndAdvance() advances OPEN to LATE when closeDate is past",
                "LATE".equals(ctx2.getCurrentState().getStateName()));

        RegistrationPeriod p3 = new RegistrationPeriod();
        p3.setOpenDate(java.time.LocalDateTime.now().minusDays(15));
        p3.setCloseDate(java.time.LocalDateTime.now().minusDays(10));
        p3.setLateRegistrationEnd(java.time.LocalDateTime.now().minusDays(2));
        p3.setStatus("LATE");
        RegistrationPeriodContext ctx3 = new RegistrationPeriodContext();
        ctx3.setState(LateRegistrationState.INSTANCE);
        ctx3.setPeriod(p3);
        ctx3.checkAndAdvance();
        check("11.3  checkAndAdvance() advances LATE to CLOSED when lateRegistrationEnd is past",
                "CLOSED".equals(ctx3.getCurrentState().getStateName()));
    }

    // =========================================================================
    // GROUP 12 — Terminal State (CLOSED)
    // =========================================================================

    private static void testRegistrationTerminalState() {
        header("GROUP 12 — Terminal State (CLOSED)");

        RegistrationPeriodContext closed = new RegistrationPeriodContext();
        closed.setState(ClosedRegistrationState.INSTANCE);

        closed.open();
        check("12.1  open() on CLOSED does not change state",
                "CLOSED".equals(closed.getCurrentState().getStateName()));

        closed.transitionToLate();
        check("12.2  transitionToLate() on CLOSED does not change state",
                "CLOSED".equals(closed.getCurrentState().getStateName()));

        closed.close();
        check("12.3  close() on CLOSED does not change state",
                "CLOSED".equals(closed.getCurrentState().getStateName()));
    }

    // =========================================================================
    // GROUP 13 — StateFactory Registration Mapping
    // =========================================================================

    private static void testRegistrationStateFactory() {
        header("GROUP 13 — StateFactory Registration Mapping");

        check("13.1  NOT_OPEN maps to NotOpenRegistrationState",
                StateFactory.registrationStateFor("NOT_OPEN") instanceof NotOpenRegistrationState);
        check("13.2  OPEN maps to OpenRegistrationState",
                StateFactory.registrationStateFor("OPEN") instanceof OpenRegistrationState);
        check("13.3  LATE maps to LateRegistrationState",
                StateFactory.registrationStateFor("LATE") instanceof LateRegistrationState);
        check("13.4  CLOSED maps to ClosedRegistrationState",
                StateFactory.registrationStateFor("CLOSED") instanceof ClosedRegistrationState);

        check("13.5  null input returns NotOpenRegistrationState",
                StateFactory.registrationStateFor(null) instanceof NotOpenRegistrationState);

        boolean threwException = false;
        try {
            StateFactory.registrationStateFor("UNKNOWN_STATUS");
        } catch (IllegalArgumentException e) {
            threwException = true;
        }
        check("13.6  unknown status throws IllegalArgumentException", threwException);
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private static void check(String label, boolean condition) {
        if (condition) {
            System.out.printf("  ✓  %s%n", label);
            passed++;
        } else {
            System.out.printf("  ✗  FAIL: %s%n", label);
            failed++;
        }
    }

    private static void banner(String text) {
        String line = "═".repeat(62);
        System.out.printf("%n%s%n  %s%n%s%n", line, text, line);
    }

    private static void header(String text) {
        System.out.printf("%n  ── %s ──%n", text);
    }

    private static void note(String text) {
        System.out.printf("  » %s%n", text);
    }

    // =========================================================================
    // GROUP 14 — Enrollment Valid State Transitions
    // =========================================================================

    private static void testEnrollmentStateTransitions() {
        header("GROUP 14 — Enrollment Valid State Transitions");

        EnrollmentContext ctx1 = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "PENDING"));
        ctx1.confirm();
        check("14.1  confirm() moves PENDING → ENROLLED",
                "ENROLLED".equals(ctx1.getEnrollment().getStatus()));

        EnrollmentContext ctx2 = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "ENROLLED"));
        ctx2.drop("schedule conflict");
        check("14.2  drop() moves ENROLLED → DROPPED",
                "DROPPED".equals(ctx2.getEnrollment().getStatus()));
        check("14.3  drop reason is recorded",
                "schedule conflict".equals(ctx2.getEnrollment().getDropReason()));

        EnrollmentContext ctx3 = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "ENROLLED"));
        ctx3.withdraw();
        check("14.4  withdraw() moves ENROLLED → WITHDRAWN",
                "WITHDRAWN".equals(ctx3.getEnrollment().getStatus()));
        check("14.5  W grade recorded on withdrawal",
                "W".equals(ctx3.getEnrollment().getFinalGrade()));

        EnrollmentContext ctx4 = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "ENROLLED"));
        ctx4.complete("A");
        check("14.6  complete() moves ENROLLED → COMPLETED",
                "COMPLETED".equals(ctx4.getEnrollment().getStatus()));
        check("14.7  final grade recorded on completion",
                "A".equals(ctx4.getEnrollment().getFinalGrade()));
    }

    // =========================================================================
    // GROUP 15 — Enrollment Guard Methods
    // =========================================================================

    private static void testEnrollmentGuardMethods() {
        header("GROUP 15 — Enrollment Guard Methods");

        EnrollmentContext pending = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "PENDING"));
        check("15.1  canDrop() returns false from PENDING",     !pending.canDrop());
        check("15.2  canWithdraw() returns false from PENDING", !pending.canWithdraw());
        check("15.3  canComplete() returns false from PENDING", !pending.canComplete());
        check("15.4  canReenroll() returns false from PENDING", !pending.canReenroll());

        EnrollmentContext enrolled = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "ENROLLED"));
        check("15.5  canDrop() returns true from ENROLLED",      enrolled.canDrop());
        check("15.6  canWithdraw() returns true from ENROLLED",  enrolled.canWithdraw());
        check("15.7  canComplete() returns true from ENROLLED",  enrolled.canComplete());
        check("15.8  canReenroll() returns false from ENROLLED", !enrolled.canReenroll());

        EnrollmentContext dropped = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "DROPPED"));
        check("15.9  canDrop() returns false from DROPPED",    !dropped.canDrop());
        check("15.10 canReenroll() returns true from DROPPED",  dropped.canReenroll());

        EnrollmentContext withdrawn = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "WITHDRAWN"));
        check("15.11 canDrop() returns false from WITHDRAWN",    !withdrawn.canDrop());
        check("15.12 canReenroll() returns false from WITHDRAWN",!withdrawn.canReenroll());

        EnrollmentContext completed = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "COMPLETED"));
        check("15.13 canDrop() returns false from COMPLETED",    !completed.canDrop());
        check("15.14 canComplete() returns false from COMPLETED",!completed.canComplete());
    }

    // =========================================================================
    // GROUP 16 — Enrollment Illegal Transitions
    // =========================================================================

    private static void testEnrollmentIllegalTransitions() {
        header("GROUP 16 — Enrollment Illegal Transitions");

        EnrollmentContext withdrawn = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "WITHDRAWN"));
        String stateBefore = withdrawn.getEnrollment().getStatus();
        try { withdrawn.drop("attempt"); } catch (IllegalStateException e) { /* expected */ }
        check("16.1  drop() on WITHDRAWN does not change state",
                stateBefore.equals(withdrawn.getEnrollment().getStatus()));

        EnrollmentContext completed = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "COMPLETED"));
        String stateBefore2 = completed.getEnrollment().getStatus();
        try { completed.drop("attempt"); } catch (IllegalStateException e) { /* expected */ }
        check("16.2  drop() on COMPLETED does not change state",
                stateBefore2.equals(completed.getEnrollment().getStatus()));

        EnrollmentContext enrolled = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "ENROLLED"));
        String stateBefore3 = enrolled.getEnrollment().getStatus();
        try { enrolled.confirm(); } catch (IllegalStateException e) { /* expected */ }
        check("16.3  confirm() on ENROLLED does not change state",
                stateBefore3.equals(enrolled.getEnrollment().getStatus()));

        EnrollmentContext pending = new EnrollmentContext(
                buildEnrollment(student.getId(), 0, "PENDING"));
        boolean threw = false;
        try { pending.drop("attempt"); } catch (IllegalStateException e) { threw = true; }
        check("16.4  drop() on PENDING throws IllegalStateException", threw);
    }

    // =========================================================================
    // GROUP 17 — Enrollment StateFactory Mapping
    // =========================================================================

    private static void testEnrollmentStateFactory() {
        header("GROUP 17 — Enrollment StateFactory Mapping");

        check("17.1  PENDING maps to PendingEnrollmentState",
                StateFactory.enrollmentStateFor("PENDING") instanceof PendingEnrollmentState);
        check("17.2  ENROLLED maps to EnrolledState",
                StateFactory.enrollmentStateFor("ENROLLED") instanceof EnrolledState);
        check("17.3  DROPPED maps to DroppedState",
                StateFactory.enrollmentStateFor("DROPPED") instanceof DroppedState);
        check("17.4  WITHDRAWN maps to WithdrawnState",
                StateFactory.enrollmentStateFor("WITHDRAWN") instanceof WithdrawnState);
        check("17.5  COMPLETED maps to CompletedState",
                StateFactory.enrollmentStateFor("COMPLETED") instanceof CompletedState);

        check("17.6  null input returns PendingEnrollmentState",
                StateFactory.enrollmentStateFor(null) instanceof PendingEnrollmentState);

        boolean threw = false;
        try {
            StateFactory.enrollmentStateFor("UNKNOWN_STATUS");
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check("17.7  unknown status throws IllegalArgumentException", threw);
    }

    // =========================================================================
    // HELPER — Build a test Enrollment without hitting the DB
    // =========================================================================

    private static edu.advising.commands.Enrollment buildEnrollment(
            int studentId, int sectionId, String status) {
        edu.advising.commands.Enrollment e =
                new edu.advising.commands.Enrollment(studentId, sectionId);
        e.setStatus(status);
        return e;
    }

    // ============================================================================
    // GROUP 18 — FacultyPermission ORM: create, persist, and reload from DB
    // ============================================================================

    private static void testFacultyPermissionORM() throws Exception {
        header("FacultyPermission ORM (create → save → load)");
        
        // ── Prerequisite seed rows ──────────────────────────────────────────────
        // Insert a minimal user (faculty), student row, and section so the FK
        // constraints on faculty_permissions are satisfied.

        int facultyUserId = db.executeInsert(
                "INSERT INTO users (username, password, user_type, first_name, last_name, email) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                "fp_faculty_test", "pw", "FACULTY", "Test", "Faculty", "fp_faculty@test.edu");

        db.executeInsert(
                "INSERT INTO faculty (id, employee_id, department) VALUES (?, ?, ?)",
                facultyUserId, "EMP-FP-01", "CS");

        int studentUserId = db.executeInsert(
                "INSERT INTO users (username, password, user_type, first_name, last_name, email) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                "fp_student_test", "pw", "STUDENT", "Test", "Student", "fp_student@test.edu");

        db.executeInsert(
                "INSERT INTO students (id, student_id, gpa) VALUES (?, ?, ?)",
                studentUserId, "S-FP-01", 3.0);

        // Grab any existing course/department, or insert minimal ones
        int deptId = db.executeInsert(
                "INSERT INTO departments (code, name) VALUES (?, ?)",
                "FP_DEPT", "FP Test Dept");

        int courseId = db.executeInsert(
                "INSERT INTO courses (code, name, credits, department_id) VALUES (?, ?, ?, ?)",
                "FP-101", "FP Test Course", 3, deptId);

        int sectionId = db.executeInsert(
                "INSERT INTO sections (course_id, section_number, semester, `year`, capacity, enrolled, faculty_id, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                courseId, "001", "FALL", 2025, 30, 30, facultyUserId, "OPEN");

        // ── Create ───────────────────────────────────────────────────────────────
        FacultyPermission fp = new FacultyPermission(studentUserId, sectionId, facultyUserId);

        check("18-1: initial status is REQUESTED", "REQUESTED".equals(fp.getStatus()));

        long diffMinutes = java.time.Duration.between(fp.getRequestDate(), fp.getExpiryDate()).toMinutes();
        check("18-2: expiryDate is 48 hours after requestDate", diffMinutes >= 2879 && diffMinutes <= 2881);

        // ── Persist ──────────────────────────────────────────────────────────────
        db.upsert(fp);

        check("18-3: upsert assigned a generated id", fp.getId() > 0);

        // ── Reload ───────────────────────────────────────────────────────────────
        FacultyPermission loaded = db.fetchOne(FacultyPermission.class, "id", fp.getId());

        check("18-4: fetchOne returned a non-null row",         loaded != null);
        check("18-5: loaded status is REQUESTED",               loaded != null && "REQUESTED".equals(loaded.getStatus()));
        check("18-6: loaded FK ids match saved values",         loaded != null && loaded.getStudentId() == studentUserId
                && loaded.getSectionId() == sectionId
                && loaded.getFacultyId() == facultyUserId);
        check("18-7: loaded requestDate matches saved value",   loaded != null
                && loaded.getRequestDate() != null
                && loaded.getRequestDate().getMinute() == fp.getRequestDate().getMinute());
    }

    // ============================================================================
// GROUP 19 — FacultyPermission valid state transitions
// ============================================================================

    private static void runGroup19() {
        System.out.println("\n--- GROUP 19: FacultyPermission valid state transitions ---");

        // REQUESTED → APPROVED
        FacultyPermission fp1 = buildPermission();
        FacultyPermissionContext ctx1 = new FacultyPermissionContext(fp1);
        ctx1.approve();
        check("19-1: REQUESTED → APPROVED", "APPROVED".equals(fp1.getStatus()));

        // REQUESTED → DENIED
        FacultyPermission fp2 = buildPermission();
        FacultyPermissionContext ctx2 = new FacultyPermissionContext(fp2);
        ctx2.deny("Class full of required prereq students");
        check("19-2: REQUESTED → DENIED", "DENIED".equals(fp2.getStatus()));

        // deny() records the denial reason
        check("19-3: denial reason is recorded", "Class full of required prereq students".equals(fp2.getDenialReason()));

        // APPROVED → EXPIRED
        FacultyPermission fp3 = buildPermission();
        FacultyPermissionContext ctx3 = new FacultyPermissionContext(fp3);
        ctx3.approve();
        ctx3.expire();
        check("19-4: APPROVED → EXPIRED", "EXPIRED".equals(fp3.getStatus()));

        // APPROVED → DENIED via revoke()
        FacultyPermission fp4 = buildPermission();
        FacultyPermissionContext ctx4 = new FacultyPermissionContext(fp4);
        ctx4.approve();
        ctx4.revoke("Admin override");
        check("19-5: APPROVED → DENIED via revoke()", "DENIED".equals(fp4.getStatus()));
        check("19-6: revoke() records denial reason",  "Admin override".equals(fp4.getDenialReason()));

        // DENIED → REQUESTED via resubmit()
        FacultyPermission fp5 = buildPermission();
        FacultyPermissionContext ctx5 = new FacultyPermissionContext(fp5);
        ctx5.deny("Not allowed");
        ctx5.resubmit();
        check("19-7: DENIED → REQUESTED via resubmit()", "REQUESTED".equals(fp5.getStatus()));

        // resubmit() resets expiryDate to +48h
        long diffMinutes = java.time.Duration.between(
                java.time.LocalDateTime.now(), fp5.getExpiryDate()).toMinutes();
        check("19-8: resubmit() resets expiryDate to +48h", diffMinutes >= 2878 && diffMinutes <= 2880);

        // resubmit() clears denial reason
        check("19-9: resubmit() clears denialReason", fp5.getDenialReason() == null);

        // EXPIRED → REQUESTED via resubmit()
        FacultyPermission fp6 = buildPermission();
        FacultyPermissionContext ctx6 = new FacultyPermissionContext(fp6);
        ctx6.approve();
        ctx6.expire();
        ctx6.resubmit();
        check("19-10: EXPIRED → REQUESTED via resubmit()", "REQUESTED".equals(fp6.getStatus()));
    }


// ============================================================================
// GROUP 20 — FacultyPermission isValid()
// ============================================================================

    private static void runGroup20() {
        System.out.println("\n--- GROUP 20: FacultyPermission isValid() ---");

        FacultyPermission fp = buildPermission();
        FacultyPermissionContext ctx = new FacultyPermissionContext(fp);

        check("20-1: REQUESTED  → isValid() false", !ctx.isValid());

        ctx.approve();
        check("20-2: APPROVED   → isValid() true",  ctx.isValid());

        ctx.expire();
        check("20-3: EXPIRED    → isValid() false", !ctx.isValid());

        // start fresh for DENIED check
        FacultyPermissionContext ctx2 = new FacultyPermissionContext(buildPermission());
        ctx2.deny("reason");
        check("20-4: DENIED     → isValid() false", !ctx2.isValid());
    }


// ============================================================================
// GROUP 21 — FacultyPermission illegal transitions
// ============================================================================

    private static void runGroup21() {
        System.out.println("\n--- GROUP 21: FacultyPermission illegal transitions ---");

        // approve() on APPROVED should not change state
        FacultyPermission fp1 = buildPermission();
        FacultyPermissionContext ctx1 = new FacultyPermissionContext(fp1);
        ctx1.approve();
        ctx1.approve(); // illegal
        check("21-1: approve() on APPROVED does not change state", "APPROVED".equals(fp1.getStatus()));

        // expire() on REQUESTED should not change state
        FacultyPermission fp2 = buildPermission();
        FacultyPermissionContext ctx2 = new FacultyPermissionContext(fp2);
        ctx2.expire(); // illegal
        check("21-2: expire() on REQUESTED does not change state", "REQUESTED".equals(fp2.getStatus()));

        // resubmit() on REQUESTED should not change state
        FacultyPermission fp3 = buildPermission();
        FacultyPermissionContext ctx3 = new FacultyPermissionContext(fp3);
        ctx3.resubmit(); // illegal
        check("21-3: resubmit() on REQUESTED does not change state", "REQUESTED".equals(fp3.getStatus()));

        // revoke() on REQUESTED should not change state
        FacultyPermission fp4 = buildPermission();
        FacultyPermissionContext ctx4 = new FacultyPermissionContext(fp4);
        ctx4.revoke("bad call"); // illegal
        check("21-4: revoke() on REQUESTED does not change state", "REQUESTED".equals(fp4.getStatus()));

        // approve() on DENIED should not change state
        FacultyPermission fp5 = buildPermission();
        FacultyPermissionContext ctx5 = new FacultyPermissionContext(fp5);
        ctx5.deny("reason");
        ctx5.approve(); // illegal
        check("21-5: approve() on DENIED does not change state", "DENIED".equals(fp5.getStatus()));

        // resubmit() on APPROVED should not change state
        FacultyPermission fp6 = buildPermission();
        FacultyPermissionContext ctx6 = new FacultyPermissionContext(fp6);
        ctx6.approve();
        ctx6.resubmit(); // illegal
        check("21-6: resubmit() on APPROVED does not change state", "APPROVED".equals(fp6.getStatus()));

        // expire() on EXPIRED should not change state
        FacultyPermission fp7 = buildPermission();
        FacultyPermissionContext ctx7 = new FacultyPermissionContext(fp7);
        ctx7.approve();
        ctx7.expire();
        ctx7.expire(); // illegal
        check("21-7: expire() on EXPIRED does not change state", "EXPIRED".equals(fp7.getStatus()));
    }


// ============================================================================
// GROUP 22 — FacultyPermission StateFactory mapping
// ============================================================================

    private static void runGroup22() {
        System.out.println("\n--- GROUP 22: FacultyPermission StateFactory mapping ---");

        check("22-1: REQUESTED maps to RequestedPermissionState",
                StateFactory.permissionStateFor("REQUESTED") instanceof FacultyPermissionRequestedState);
        check("22-2: APPROVED  maps to ApprovedPermissionState",
                StateFactory.permissionStateFor("APPROVED")  instanceof FacultyPermissionApprovedState);
        check("22-3: DENIED    maps to DeniedPermissionState",
                StateFactory.permissionStateFor("DENIED")    instanceof FacultyPermissionDeniedState);
        check("22-4: EXPIRED   maps to ExpiredPermissionState",
                StateFactory.permissionStateFor("EXPIRED")   instanceof FacultyPermissionExpiredState);

        // null input defaults to REQUESTED
        check("22-5: null defaults to RequestedPermissionState",
                StateFactory.permissionStateFor(null) instanceof FacultyPermissionRequestedState);

        // unknown string throws IllegalArgumentException
        boolean threw = false;
        try {
            StateFactory.permissionStateFor("BOGUS");
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check("22-6: unknown status throws IllegalArgumentException", threw);
    }


// ============================================================================
// GROUP 23 — FacultyPermission isExpiredByTime() and checkAndAdvance()
// ============================================================================

    private static void runGroup23() {
        System.out.println("\n--- GROUP 23: FacultyPermission isExpiredByTime() and checkAndAdvance() ---");

        // isExpiredByTime() false when REQUESTED, even with past expiryDate
        FacultyPermission fp1 = buildPermission();
        fp1.setExpiryDate(java.time.LocalDateTime.now().minusHours(1));
        FacultyPermissionContext ctx1 = new FacultyPermissionContext(fp1);
        check("23-1: isExpiredByTime() false when REQUESTED with past expiryDate", !ctx1.isExpiredByTime());

        // isExpiredByTime() false when APPROVED but expiryDate is in the future
        FacultyPermission fp2 = buildPermission();
        FacultyPermissionContext ctx2 = new FacultyPermissionContext(fp2);
        ctx2.approve();
        check("23-2: isExpiredByTime() false when APPROVED with future expiryDate", !ctx2.isExpiredByTime());

        // isExpiredByTime() true when APPROVED and expiryDate is in the past
        FacultyPermission fp3 = buildPermission();
        fp3.setExpiryDate(java.time.LocalDateTime.now().minusHours(1));
        FacultyPermissionContext ctx3 = new FacultyPermissionContext(fp3);
        ctx3.getPermission().setStatus("APPROVED"); // force APPROVED without calling approve() to avoid persist
        ctx3.setState(FacultyPermissionApprovedState.INSTANCE);
        check("23-3: isExpiredByTime() true when APPROVED with past expiryDate", ctx3.isExpiredByTime());

        // checkAndAdvance() transitions APPROVED → EXPIRED when past expiryDate
        ctx3.checkAndAdvance();
        check("23-4: checkAndAdvance() transitions to EXPIRED when past expiryDate",
                "EXPIRED".equals(fp3.getStatus()));

        // checkAndAdvance() does nothing when REQUESTED
        FacultyPermission fp4 = buildPermission();
        fp4.setExpiryDate(java.time.LocalDateTime.now().minusHours(1));
        FacultyPermissionContext ctx4 = new FacultyPermissionContext(fp4);
        ctx4.checkAndAdvance();
        check("23-5: checkAndAdvance() does nothing when REQUESTED", "REQUESTED".equals(fp4.getStatus()));
    }


// ============================================================================
// Helper — builds a FacultyPermission with sectionId=0 to skip DB saves
// ============================================================================

    private static FacultyPermission buildPermission() {
        return new FacultyPermission(fpStudentId , fpSectionId, fpFacultyId);
    }
    private static int createFacultyUser() throws SQLException {
        int facultyUserId = DatabaseManager.getInstance().executeInsert(
                "INSERT INTO users (username, password, user_type, first_name, last_name, email) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                "fp_unit_faculty", "pw", "FACULTY", "Test", "Faculty", "fp_unit_faculty@test.edu");

        DatabaseManager.getInstance().executeInsert(
                "INSERT INTO faculty (id, employee_id, department) VALUES (?, ?, ?)",
                facultyUserId, "EMP-UNIT-01", "CS");

        return facultyUserId;
    }
    private static int createStudentUser() throws SQLException {
        int studentUserId = DatabaseManager.getInstance().executeInsert(
                "INSERT INTO users (username, password, user_type, first_name, last_name, email) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                "fp_unit_student", "pw", "STUDENT", "Test", "Student", "fp_unit_student@test.edu");
        DatabaseManager.getInstance().executeInsert(
                "INSERT INTO students (id, student_id, gpa) VALUES (?, ?, ?)",
                studentUserId, "S-UNIT-01", 3.0);
        return studentUserId;
    }
    private static int createSection(int facultyId) throws SQLException {
        int deptId = DatabaseManager.getInstance().executeInsert(
                "INSERT INTO departments (code, name) VALUES (?, ?)",
                "UNIT_DEPT", "Unit Test Dept");

        int courseId = DatabaseManager.getInstance().executeInsert(
                "INSERT INTO courses (code, name, credits, department_id) VALUES (?, ?, ?, ?)",
                "UNIT-101", "Unit Test Course", 3, deptId);

        return DatabaseManager.getInstance().executeInsert(
                "INSERT INTO sections (course_id, section_number, semester, `year`, capacity, enrolled, faculty_id, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                courseId, "001", "FALL", 2025, 30, 30, facultyId, "OPEN");
    }

}
