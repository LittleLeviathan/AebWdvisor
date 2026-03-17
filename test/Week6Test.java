import edu.advising.core.DatabaseManager;
import edu.advising.notifications.NotificationManager;
import edu.advising.state.*;
import edu.advising.users.Student;
import edu.advising.users.UserFactory;

// ============================================================================
// WEEK 6: STATE PATTERN — Integration Test Application
// ============================================================================
//
// PURPOSE:
//   Exercises the Transcript Request State Machine implemented in Week 6.
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
//
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

    // =========================================================================
    // ENTRY POINT
    // =========================================================================

    public static void main(String[] args) {
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

        // Create a new request — should start in PENDING
        TranscriptRequestContext ctx = TranscriptRequestContext.create(
                student.getId(), "OFFICIAL", "MIT", "77 Mass Ave", false);

        check("1.1  new request starts in PENDING",
                "PENDING".equals(ctx.getCurrentStateName()));

        // PENDING → PROCESSING
        ctx.process();
        check("1.2  process() moves to PROCESSING",
                "PROCESSING".equals(ctx.getCurrentStateName()));

        // PROCESSING → READY
        ctx.prepare();
        check("1.3  prepare() moves to READY",
                "READY".equals(ctx.getCurrentStateName()));

        // READY → SENT
        ctx.dispatch();
        check("1.4  dispatch() moves to SENT",
                "SENT".equals(ctx.getCurrentStateName()));
    }

    // =========================================================================
    // GROUP 2 — Cancel Transitions
    // =========================================================================

    private static void testCancelTransitions() {
        header("GROUP 2 — Cancel Transitions");

        // Cancel from PENDING
        TranscriptRequestContext ctx1 = TranscriptRequestContext.create(
                student.getId(), "OFFICIAL", "Harvard", "Cambridge MA", false);
        ctx1.cancel();
        check("2.1  cancel from PENDING moves to CANCELLED",
                "CANCELLED".equals(ctx1.getCurrentStateName()));

        // Cancel from PROCESSING
        TranscriptRequestContext ctx2 = TranscriptRequestContext.create(
                student.getId(), "OFFICIAL", "Harvard", "Cambridge MA", false);
        ctx2.process();
        ctx2.cancel();
        check("2.2  cancel from PROCESSING moves to CANCELLED",
                "CANCELLED".equals(ctx2.getCurrentStateName()));

        // Cancel from READY
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

        // PROCESSING → FAILED
        ctx.fail("Missing signature on form");
        check("3.1  fail() moves to FAILED",
                "FAILED".equals(ctx.getCurrentStateName()));

        check("3.2  failure reason stored on request",
                "Missing signature on form".equals(ctx.getRequest().getFailureReason()));

        // FAILED → PROCESSING via retry
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

        // Cannot prepare from PENDING
        TranscriptRequestContext ctx1 = TranscriptRequestContext.create(
                student.getId(), "UNOFFICIAL", "UCLA", "Los Angeles CA", false);
        ctx1.prepare();
        check("4.1  prepare() from PENDING does not change state",
                "PENDING".equals(ctx1.getCurrentStateName()));

        // Cannot dispatch from PENDING
        ctx1.dispatch();
        check("4.2  dispatch() from PENDING does not change state",
                "PENDING".equals(ctx1.getCurrentStateName()));

        // Cannot retry from PENDING
        ctx1.retry();
        check("4.3  retry() from PENDING does not change state",
                "PENDING".equals(ctx1.getCurrentStateName()));

        // Cannot dispatch from PROCESSING
        TranscriptRequestContext ctx2 = TranscriptRequestContext.create(
                student.getId(), "UNOFFICIAL", "UCLA", "Los Angeles CA", false);
        ctx2.process();
        ctx2.dispatch();
        check("4.4  dispatch() from PROCESSING does not change state",
                "PROCESSING".equals(ctx2.getCurrentStateName()));

        // Cannot fail from READY
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

        // SENT blocks everything
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

        // CANCELLED blocks everything
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

        // Null input returns default PENDING state
        check("6.7  null input returns PendingTranscriptState",
                StateFactory.transcriptStateFor(null) instanceof PendingTranscriptState);

        // Unknown string throws IllegalArgumentException
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

        // Create a request and drive it to PROCESSING
        TranscriptRequestContext original = TranscriptRequestContext.create(
                student.getId(), "OFFICIAL", "Columbia", "New York NY", false);
        original.process();

        int id = original.getRequest().getId();
        check("7.1  request saved to DB with valid ID", id > 0);

        // Load it back from DB
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
}
