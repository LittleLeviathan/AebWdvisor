import edu.advising.auth.AuthenticationContext;
import edu.advising.auth.BasicAuthentication;
import edu.advising.core.DatabaseManager;
import edu.advising.notifications.NotificationManager;
import edu.advising.state.*;
import edu.advising.users.Faculty;
import edu.advising.users.Student;
import edu.advising.users.UserFactory;

// ============================================================================
// WEEK 10: STATE PATTERN — View Navigation State Machine Test
// ============================================================================
//
// PURPOSE:
//   Exercises the View Navigation State Machine implemented in Week 10.
//   Structured as a plain runnable main() — no JUnit required.
//   Run it with: mvn exec:java@run-view-navigation-test
//
// TESTS COVERED:
//   GROUP 1  — start() initializes on GuestViewState
//   GROUP 2  — Successful student login routes to StudentDashboardViewState
//   GROUP 3  — Successful faculty login routes to FacultyDashboardViewState
//   GROUP 4  — Failed login stays on GuestViewState
//   GROUP 5  — Unauthenticated navigation redirects to GuestViewState
//   GROUP 6  — NAVIGATE from StudentDashboard adds dashboard to history
//   GROUP 7  — back() from RegistrationViewState returns to StudentDashboard
//   GROUP 8  — LOGOUT from authenticated view returns to GUEST and clears history
//   GROUP 9  — StateFactory viewStateFor() mapping
//   GROUP 10 — Integration: full login → navigate → check status → back → logout
//
// ============================================================================

public class ViewNavigationStateTest {

    // ── Counters ─────────────────────────────────────────────────────────────
    private static int passed = 0;
    private static int failed = 0;

    // ── Shared fixtures ───────────────────────────────────────────────────────
    private static DatabaseManager db;
    private static NotificationManager notificationManager;
    private static UserFactory userFactory;
    private static Student student;
    private static Faculty faculty;
    private static AuthenticationContext authContext;

    // =========================================================================
    // ENTRY POINT
    // =========================================================================

    public static void main(String[] args) {
        banner("WEEK 10 — VIEW NAVIGATION STATE MACHINE  |  BetterAdvisor Test Suite");

        try {
            setUp();
        } catch (Exception e) {
            System.err.println("FATAL: setUp() failed — cannot run tests.");
            e.printStackTrace();
            return;
        }

        testStartInitializesOnGuest();
        testStudentLoginRoutesToStudentDashboard();
        testFacultyLoginRoutesToFacultyDashboard();
        testFailedLoginStaysOnGuest();
        testUnauthenticatedNavigationRedirectsToGuest();
        testNavigateAddsDashboardToHistory();
        testBackReturnsToStudentDashboard();
        testLogoutReturnsToGuestAndClearsHistory();
        testStateFactoryViewStateFor();
        testFullIntegrationFlow();

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
        authContext = new AuthenticationContext(new BasicAuthentication());

        student = (Student) userFactory.createUser(
                "STUDENT", "navstudent", "Password1!",
                "navstudent@college.edu", "Nav", "Student", "S20001");

        faculty = (Faculty) userFactory.createUser(
                "FACULTY", "navfaculty", "Password1!",
                "navfaculty@college.edu", "Nav", "Faculty", "E20001", "Computer Science");

        note("Student: " + student.getFullName() + " (id=" + student.getId() + ")");
        note("Faculty: " + faculty.getFullName() + " (id=" + faculty.getId() + ")");
        note("setUp() complete\n");
    }

    // =========================================================================
    // GROUP 1 — start() Initializes on GuestViewState
    // =========================================================================

    private static void testStartInitializesOnGuest() {
        header("GROUP 1 — start() Initializes on GuestViewState");

        ViewContext ctx = buildContext();
        ctx.start();

        check("1.1  start() sets currentState to GuestViewState",
                ctx.getCurrentState() instanceof GuestViewState);
        check("1.2  start() history stack is empty",
                ctx.getHistory().isEmpty());
        check("1.3  start() currentUser is null",
                ctx.getCurrentUser() == null);
    }

    // =========================================================================
    // GROUP 2 — Successful Student Login Routes to StudentDashboardViewState
    // =========================================================================

    private static void testStudentLoginRoutesToStudentDashboard() {
        header("GROUP 2 — Student Login Routes to StudentDashboardViewState");

        ViewContext ctx = buildContext();
        ctx.start();
        ctx.handleAction("LOGIN", "navstudent", "Password1!", "127.0.0.1");

        check("2.1  student login navigates to StudentDashboardViewState",
                ctx.getCurrentState() instanceof StudentDashboardViewState);
        check("2.2  currentUser is set after login",
                ctx.getCurrentUser() != null);
        check("2.3  currentUser username matches",
                "navstudent".equals(ctx.getCurrentUser().getUsername()));
    }

    // =========================================================================
    // GROUP 3 — Successful Faculty Login Routes to FacultyDashboardViewState
    // =========================================================================

    private static void testFacultyLoginRoutesToFacultyDashboard() {
        header("GROUP 3 — Faculty Login Routes to FacultyDashboardViewState");

        ViewContext ctx = buildContext();
        ctx.start();
        ctx.handleAction("LOGIN", "navfaculty", "Password1!", "127.0.0.1");

        check("3.1  faculty login navigates to FacultyDashboardViewState",
                ctx.getCurrentState() instanceof FacultyDashboardViewState);
        check("3.2  currentUser is set after faculty login",
                ctx.getCurrentUser() != null);
        check("3.3  currentUser username matches",
                "navfaculty".equals(ctx.getCurrentUser().getUsername()));
    }

    // =========================================================================
    // GROUP 4 — Failed Login Stays on GuestViewState
    // =========================================================================

    private static void testFailedLoginStaysOnGuest() {
        header("GROUP 4 — Failed Login Stays on GuestViewState");

        ViewContext ctx = buildContext();
        ctx.start();
        ctx.handleAction("LOGIN", "navstudent", "wrongpassword", "127.0.0.1");

        check("4.1  failed login stays on GuestViewState",
                ctx.getCurrentState() instanceof GuestViewState);
        check("4.2  currentUser is null after failed login",
                ctx.getCurrentUser() == null);
    }

    // =========================================================================
    // GROUP 5 — Unauthenticated Navigation Redirects to GuestViewState
    // =========================================================================

    private static void testUnauthenticatedNavigationRedirectsToGuest() {
        header("GROUP 5 — Unauthenticated Navigation Redirects to GuestViewState");

        ViewContext ctx = buildContext();
        ctx.start();
        ctx.navigateTo(RegistrationViewState.INSTANCE);

        check("5.1  unauthenticated navigation to REGISTRATION redirects to GUEST",
                ctx.getCurrentState() instanceof GuestViewState);
        check("5.2  currentUser is still null after redirect",
                ctx.getCurrentUser() == null);
    }

    // =========================================================================
    // GROUP 6 — NAVIGATE Adds Dashboard to History Stack
    // =========================================================================

    private static void testNavigateAddsDashboardToHistory() {
        header("GROUP 6 — NAVIGATE Adds Dashboard to History Stack");

        ViewContext ctx = buildContext();
        ctx.start();
        ctx.handleAction("LOGIN", "navstudent", "Password1!", "127.0.0.1");
        ctx.handleAction("NAVIGATE", "REGISTRATION");

        check("6.1  after NAVIGATE currentState is RegistrationViewState",
                ctx.getCurrentState() instanceof RegistrationViewState);
        check("6.2  history stack is not empty after NAVIGATE",
                !ctx.getHistory().isEmpty());
        check("6.3  history stack top is StudentDashboardViewState",
                ctx.getHistory().peek() instanceof StudentDashboardViewState);
    }

    // =========================================================================
    // GROUP 7 — back() Returns to StudentDashboard
    // =========================================================================

    private static void testBackReturnsToStudentDashboard() {
        header("GROUP 7 — back() Returns to StudentDashboardViewState");

        ViewContext ctx = buildContext();
        ctx.start();
        ctx.handleAction("LOGIN", "navstudent", "Password1!", "127.0.0.1");
        ctx.handleAction("NAVIGATE", "REGISTRATION");
        ctx.handleAction("BACK");

        check("7.1  back() from REGISTRATION returns to STUDENT_DASHBOARD",
                ctx.getCurrentState() instanceof StudentDashboardViewState);
        check("7.2  history stack is empty after back()",
                ctx.getHistory().isEmpty());
    }

    // =========================================================================
    // GROUP 8 — LOGOUT Returns to GUEST and Clears History
    // =========================================================================

    private static void testLogoutReturnsToGuestAndClearsHistory() {
        header("GROUP 8 — LOGOUT Returns to GUEST and Clears History");

        ViewContext ctx = buildContext();
        ctx.start();
        ctx.handleAction("LOGIN", "navstudent", "Password1!", "127.0.0.1");
        ctx.handleAction("NAVIGATE", "REGISTRATION");
        ctx.handleAction("LOGOUT");

        check("8.1  LOGOUT returns to GuestViewState",
                ctx.getCurrentState() instanceof GuestViewState);
        check("8.2  history stack is empty after LOGOUT",
                ctx.getHistory().isEmpty());
        check("8.3  currentUser is null after LOGOUT",
                ctx.getCurrentUser() == null);
    }

    // =========================================================================
    // GROUP 9 — StateFactory viewStateFor() Mapping
    // =========================================================================

    private static void testStateFactoryViewStateFor() {
        header("GROUP 9 — StateFactory viewStateFor() Mapping");

        check("9.1  GUEST maps to GuestViewState",
                StateFactory.viewStateFor("GUEST") instanceof GuestViewState);
        check("9.2  LOGIN maps to LoginViewState",
                StateFactory.viewStateFor("LOGIN") instanceof LoginViewState);
        check("9.3  STUDENT_DASHBOARD maps to StudentDashboardViewState",
                StateFactory.viewStateFor("STUDENT_DASHBOARD") instanceof StudentDashboardViewState);
        check("9.4  FACULTY_DASHBOARD maps to FacultyDashboardViewState",
                StateFactory.viewStateFor("FACULTY_DASHBOARD") instanceof FacultyDashboardViewState);
        check("9.5  REGISTRATION maps to RegistrationViewState",
                StateFactory.viewStateFor("REGISTRATION") instanceof RegistrationViewState);
        check("9.6  TRANSCRIPT maps to TranscriptViewState",
                StateFactory.viewStateFor("TRANSCRIPT") instanceof TranscriptViewState);
        check("9.7  PERMISSION_MANAGEMENT maps to PermissionManagementViewState",
                StateFactory.viewStateFor("PERMISSION_MANAGEMENT") instanceof PermissionManagementViewState);
        check("9.8  null input returns GuestViewState",
                StateFactory.viewStateFor(null) instanceof GuestViewState);

        boolean threw = false;
        try {
            StateFactory.viewStateFor("UNKNOWN_VIEW");
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check("9.9  unknown view name throws IllegalArgumentException", threw);
    }

    // =========================================================================
    // GROUP 10 — Integration: Full Login → Navigate → Back → Logout Flow
    // =========================================================================

    private static void testFullIntegrationFlow() {
        header("GROUP 10 — Integration: Full Login → Navigate → Back → Logout Flow");

        ViewContext ctx = buildContext();

        ctx.start();
        check("10.1  app starts on GuestViewState",
                ctx.getCurrentState() instanceof GuestViewState);

        ctx.handleAction("LOGIN", "navstudent", "Password1!", "127.0.0.1");
        check("10.2  student login navigates to StudentDashboardViewState",
                ctx.getCurrentState() instanceof StudentDashboardViewState);

        ctx.handleAction("NAVIGATE", "REGISTRATION");
        check("10.3  NAVIGATE to REGISTRATION navigates to RegistrationViewState",
                ctx.getCurrentState() instanceof RegistrationViewState);

        ctx.handleAction("BACK");
        check("10.4  BACK returns to StudentDashboardViewState",
                ctx.getCurrentState() instanceof StudentDashboardViewState);

        ctx.handleAction("LOGOUT");
        check("10.5  LOGOUT returns to GuestViewState",
                ctx.getCurrentState() instanceof GuestViewState);
        check("10.6  currentUser is null after LOGOUT",
                ctx.getCurrentUser() == null);
        check("10.7  history is empty after LOGOUT",
                ctx.getHistory().isEmpty());
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private static ViewContext buildContext() {
        ViewContext ctx = new ViewContext();
        ctx.setAuthContext(authContext);
        return ctx;
    }

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