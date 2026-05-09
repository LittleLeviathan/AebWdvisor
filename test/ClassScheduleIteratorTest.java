import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;
import edu.advising.iterator.*;

import java.util.ArrayList;
import java.util.List;

// ============================================================================
// ITERATOR PATTERN — Class Schedule Iterator Test Suite
// ============================================================================
//
// PURPOSE:
//   Exercises all four iterators implemented for the View Class Schedule
//   user story. Structured as a plain runnable main() — no JUnit required.
//   Run it with: mvn exec:java@run-iterator-test
//
// TESTS COVERED:
//   GROUP 1  — WeeklyScheduleIterator (sorts Mon–Fri then by start time)
//   GROUP 2  — BySemesterIterator (sorts by year then semester)
//   GROUP 3  — ByDeliveryModeIterator (filters by ONLINE, IN_PERSON, HYBRID)
//   GROUP 4  — ByStatusIterator (filters by ENROLLED, DROPPED, WITHDRAWN)
//   GROUP 5  — hasNext() and reset() behavior across all iterators
//   GROUP 6  — Empty list edge cases
//   GROUP 7  — StudentSchedule factory methods
//
// ============================================================================

public class ClassScheduleIteratorTest {

    // ── Counters ─────────────────────────────────────────────────────────────
    private static int passed = 0;
    private static int failed = 0;

    // =========================================================================
    // ENTRY POINT
    // =========================================================================

    public static void main(String[] args) {
        banner("ITERATOR PATTERN  |  BetterAdvisor Test Suite");

        testWeeklyIterator();
        testBySemesterIterator();
        testByDeliveryModeIterator();
        testByStatusIterator();
        testHasNextAndReset();
        testEmptyListEdgeCases();
        testStudentScheduleFactory();

        banner("RESULTS");
        System.out.printf("  Total passed : %d%n", passed);
        System.out.printf("  Total failed : %d%n", failed);
        System.out.printf("  Total tests  : %d%n", passed + failed);
        if (failed == 0) {
            System.out.println("\n  ALL TESTS PASSED");
        } else {
            System.out.println("\n  SOME TESTS FAILED — see above");
        }
    }

    // =========================================================================
    // GROUP 1 — WeeklyScheduleIterator
    // =========================================================================

    private static void testWeeklyIterator() {
        header("GROUP 1 — WeeklyScheduleIterator");

        Enrollment friday    = buildEnrollment(1, "FA", 2025, "ONLINE",    "FRIDAY",    "10:00", "ENROLLED");
        Enrollment monday    = buildEnrollment(2, "FA", 2025, "IN_PERSON", "MONDAY",    "09:00", "ENROLLED");
        Enrollment wednesday = buildEnrollment(3, "FA", 2025, "HYBRID",    "WEDNESDAY", "08:00", "ENROLLED");
        Enrollment tuesday   = buildEnrollment(4, "FA", 2025, "ONLINE",    "TUESDAY",   "11:00", "ENROLLED");

        List<Enrollment> list = new ArrayList<>();
        list.add(friday);
        list.add(monday);
        list.add(wednesday);
        list.add(tuesday);

        WeeklyScheduleIterator it = new WeeklyScheduleIterator(list);

        Enrollment first  = it.next();
        Enrollment second = it.next();
        Enrollment third  = it.next();
        Enrollment fourth = it.next();

        try {
            String firstDay  = first.getSection()  != null ? first.getSection().getDayOfWeek()  : "";
            String secondDay = second.getSection() != null ? second.getSection().getDayOfWeek() : "";
            String thirdDay  = third.getSection()  != null ? third.getSection().getDayOfWeek()  : "";
            String fourthDay = fourth.getSection() != null ? fourth.getSection().getDayOfWeek() : "";
            check("1.1  First enrollment is MONDAY",    "MONDAY".equals(firstDay));
            check("1.2  Second enrollment is TUESDAY",  "TUESDAY".equals(secondDay));
            check("1.3  Third enrollment is WEDNESDAY", "WEDNESDAY".equals(thirdDay));
            check("1.4  Fourth enrollment is FRIDAY",   "FRIDAY".equals(fourthDay));
        } catch (Exception ex) {
            System.out.println("  ✗  GROUP 1 section lookup failed: " + ex.getMessage());
            failed += 4;
        }

        Enrollment mondayLate  = buildEnrollment(5, "FA", 2025, "ONLINE", "MONDAY", "13:00", "ENROLLED");
        Enrollment mondayEarly = buildEnrollment(6, "FA", 2025, "ONLINE", "MONDAY", "08:00", "ENROLLED");

        List<Enrollment> sameDay = new ArrayList<>();
        sameDay.add(mondayLate);
        sameDay.add(mondayEarly);

        WeeklyScheduleIterator sameDayIt = new WeeklyScheduleIterator(sameDay);
        Enrollment earlierClass = sameDayIt.next();
        try {
            String earlierTime = earlierClass.getSection() != null ? earlierClass.getSection().getStartTime() : "";
            check("1.5  Earlier start time comes first on same day", "08:00".equals(earlierTime));
        } catch (Exception ex) {
            System.out.println("  ✗  GROUP 1.5 section lookup failed: " + ex.getMessage());
            failed++;
        }
    }

    // =========================================================================
    // GROUP 2 — BySemesterIterator
    // =========================================================================

    private static void testBySemesterIterator() {
        header("GROUP 2 — BySemesterIterator");

        Enrollment fa2025 = buildEnrollment(1, "FA", 2025, "ONLINE",    "MONDAY", "09:00", "ENROLLED");
        Enrollment sp2025 = buildEnrollment(2, "SP", 2025, "IN_PERSON", "MONDAY", "09:00", "ENROLLED");
        Enrollment su2024 = buildEnrollment(3, "SU", 2024, "HYBRID",    "MONDAY", "09:00", "DROPPED");
        Enrollment sp2024 = buildEnrollment(4, "SP", 2024, "ONLINE",    "MONDAY", "09:00", "ENROLLED");

        List<Enrollment> list = new ArrayList<>();
        list.add(fa2025);
        list.add(sp2025);
        list.add(su2024);
        list.add(sp2024);

        BySemesterIterator it = new BySemesterIterator(list);

        Enrollment first  = it.next();
        Enrollment second = it.next();
        Enrollment third  = it.next();
        Enrollment fourth = it.next();

        try {
            Section s1 = first.getSection();
            Section s2 = second.getSection();
            Section s3 = third.getSection();
            Section s4 = fourth.getSection();
            check("2.1  First is SP2024",
                    s1 != null && "SP".equals(s1.getSemester()) && s1.getYear() == 2024);
            check("2.2  Second is SU2024",
                    s2 != null && "SU".equals(s2.getSemester()) && s2.getYear() == 2024);
            check("2.3  Third is SP2025",
                    s3 != null && "SP".equals(s3.getSemester()) && s3.getYear() == 2025);
            check("2.4  Fourth is FA2025",
                    s4 != null && "FA".equals(s4.getSemester()) && s4.getYear() == 2025);
        } catch (Exception ex) {
            System.out.println("  ✗  GROUP 2 section lookup failed: " + ex.getMessage());
            failed += 4;
        }
    }

    // =========================================================================
    // GROUP 3 — ByDeliveryModeIterator
    // =========================================================================

    private static void testByDeliveryModeIterator() {
        header("GROUP 3 — ByDeliveryModeIterator");

        Enrollment online1   = buildEnrollment(1, "FA", 2025, "ONLINE",    "MONDAY",    "09:00", "ENROLLED");
        Enrollment inPerson1 = buildEnrollment(2, "FA", 2025, "IN_PERSON", "TUESDAY",   "10:00", "ENROLLED");
        Enrollment hybrid1   = buildEnrollment(3, "FA", 2025, "HYBRID",    "WEDNESDAY", "11:00", "ENROLLED");
        Enrollment online2   = buildEnrollment(4, "FA", 2025, "ONLINE",    "THURSDAY",  "12:00", "ENROLLED");

        List<Enrollment> list = new ArrayList<>();
        list.add(online1);
        list.add(inPerson1);
        list.add(hybrid1);
        list.add(online2);

        ByDeliveryModeIterator onlineIt = new ByDeliveryModeIterator(list, "ONLINE");
        int onlineCount = 0;
        while (onlineIt.hasNext()) { onlineIt.next(); onlineCount++; }
        check("3.1  ONLINE filter returns 2 enrollments", onlineCount == 2);

        ByDeliveryModeIterator inPersonIt = new ByDeliveryModeIterator(list, "IN_PERSON");
        int inPersonCount = 0;
        while (inPersonIt.hasNext()) { inPersonIt.next(); inPersonCount++; }
        check("3.2  IN_PERSON filter returns 1 enrollment", inPersonCount == 1);

        ByDeliveryModeIterator hybridIt = new ByDeliveryModeIterator(list, "HYBRID");
        int hybridCount = 0;
        while (hybridIt.hasNext()) { hybridIt.next(); hybridCount++; }
        check("3.3  HYBRID filter returns 1 enrollment", hybridCount == 1);

        ByDeliveryModeIterator lowerIt = new ByDeliveryModeIterator(list, "online");
        int lowerCount = 0;
        while (lowerIt.hasNext()) { lowerIt.next(); lowerCount++; }
        check("3.4  lowercase 'online' matches same as 'ONLINE'", lowerCount == 2);

        ByDeliveryModeIterator noMatchIt = new ByDeliveryModeIterator(list, "TELEPRESENCE");
        check("3.5  Unknown mode returns empty iterator", !noMatchIt.hasNext());
    }

    // =========================================================================
    // GROUP 4 — ByStatusIterator
    // =========================================================================

    private static void testByStatusIterator() {
        header("GROUP 4 — ByStatusIterator");

        Enrollment enrolled1  = buildEnrollment(1, "FA", 2025, "ONLINE",    "MONDAY",    "09:00", "ENROLLED");
        Enrollment dropped1   = buildEnrollment(2, "FA", 2025, "IN_PERSON", "TUESDAY",   "10:00", "DROPPED");
        Enrollment withdrawn1 = buildEnrollment(3, "FA", 2025, "HYBRID",    "WEDNESDAY", "11:00", "WITHDRAWN");
        Enrollment enrolled2  = buildEnrollment(4, "SP", 2025, "ONLINE",    "THURSDAY",  "12:00", "ENROLLED");

        List<Enrollment> list = new ArrayList<>();
        list.add(enrolled1);
        list.add(dropped1);
        list.add(withdrawn1);
        list.add(enrolled2);

        ByStatusIterator enrolledIt = new ByStatusIterator(list, "ENROLLED");
        int enrolledCount = 0;
        while (enrolledIt.hasNext()) { enrolledIt.next(); enrolledCount++; }
        check("4.1  ENROLLED filter returns 2 enrollments", enrolledCount == 2);

        ByStatusIterator droppedIt = new ByStatusIterator(list, "DROPPED");
        int droppedCount = 0;
        while (droppedIt.hasNext()) { droppedIt.next(); droppedCount++; }
        check("4.2  DROPPED filter returns 1 enrollment", droppedCount == 1);

        ByStatusIterator withdrawnIt = new ByStatusIterator(list, "WITHDRAWN");
        int withdrawnCount = 0;
        while (withdrawnIt.hasNext()) { withdrawnIt.next(); withdrawnCount++; }
        check("4.3  WITHDRAWN filter returns 1 enrollment", withdrawnCount == 1);

        ByStatusIterator lowerIt = new ByStatusIterator(list, "enrolled");
        int lowerCount = 0;
        while (lowerIt.hasNext()) { lowerIt.next(); lowerCount++; }
        check("4.4  lowercase 'enrolled' matches same as 'ENROLLED'", lowerCount == 2);

        ByStatusIterator noMatchIt = new ByStatusIterator(list, "PENDING");
        check("4.5  Status with no matches returns empty iterator", !noMatchIt.hasNext());
    }

    // =========================================================================
    // GROUP 5 — hasNext() and reset() behavior
    // =========================================================================

    private static void testHasNextAndReset() {
        header("GROUP 5 — hasNext() and reset()");

        Enrollment e1 = buildEnrollment(1, "FA", 2025, "ONLINE", "MONDAY",  "09:00", "ENROLLED");
        Enrollment e2 = buildEnrollment(2, "FA", 2025, "ONLINE", "TUESDAY", "10:00", "ENROLLED");

        List<Enrollment> list = new ArrayList<>();
        list.add(e1);
        list.add(e2);

        ByStatusIterator it = new ByStatusIterator(list, "ENROLLED");

        check("5.1  hasNext() returns true when items remain", it.hasNext());
        it.next();
        it.next();
        check("5.2  hasNext() returns false when all items consumed", !it.hasNext());
        it.reset();
        check("5.3  hasNext() returns true again after reset()", it.hasNext());

        int countAfterReset = 0;
        while (it.hasNext()) { it.next(); countAfterReset++; }
        check("5.4  Full walk after reset() returns same count", countAfterReset == 2);
    }

    // =========================================================================
    // GROUP 6 — Empty list edge cases
    // =========================================================================

    private static void testEmptyListEdgeCases() {
        header("GROUP 6 — Empty list edge cases");

        List<Enrollment> empty = new ArrayList<>();

        WeeklyScheduleIterator weeklyIt = new WeeklyScheduleIterator(empty);
        check("6.1  WeeklyScheduleIterator hasNext() false on empty list", !weeklyIt.hasNext());

        BySemesterIterator semIt = new BySemesterIterator(empty);
        check("6.2  BySemesterIterator hasNext() false on empty list", !semIt.hasNext());

        ByDeliveryModeIterator modeIt = new ByDeliveryModeIterator(empty, "ONLINE");
        check("6.3  ByDeliveryModeIterator hasNext() false on empty list", !modeIt.hasNext());

        ByStatusIterator statusIt = new ByStatusIterator(empty, "ENROLLED");
        check("6.4  ByStatusIterator hasNext() false on empty list", !statusIt.hasNext());
    }

    // =========================================================================
    // GROUP 7 — StudentSchedule factory methods
    // =========================================================================

    private static void testStudentScheduleFactory() {
        header("GROUP 7 — StudentSchedule factory methods");

        Enrollment e1 = buildEnrollment(1, "FA", 2025, "ONLINE",    "MONDAY",  "09:00", "ENROLLED");
        Enrollment e2 = buildEnrollment(2, "SP", 2025, "IN_PERSON", "TUESDAY", "10:00", "DROPPED");

        List<Enrollment> list = new ArrayList<>();
        list.add(e1);
        list.add(e2);

        StudentSchedule schedule = new StudentSchedule(list);

        ScheduleIterator weeklyIt = schedule.createWeeklyIterator();
        check("7.1  createWeeklyIterator() returns a ScheduleIterator",    weeklyIt != null);
        check("7.2  createWeeklyIterator() hasNext() true with data",       weeklyIt.hasNext());

        ScheduleIterator semIt = schedule.createBySemesterIterator();
        check("7.3  createBySemesterIterator() returns a ScheduleIterator", semIt != null);
        check("7.4  createBySemesterIterator() hasNext() true with data",   semIt.hasNext());

        ScheduleIterator modeIt = schedule.createByDeliveryModeIterator("ONLINE");
        check("7.5  createByDeliveryModeIterator() returns a ScheduleIterator", modeIt != null);
        check("7.6  createByDeliveryModeIterator() filters correctly",          modeIt.hasNext());

        ScheduleIterator statusIt = schedule.createByStatusIterator("ENROLLED");
        check("7.7  createByStatusIterator() returns a ScheduleIterator",  statusIt != null);
        check("7.8  createByStatusIterator() filters correctly",            statusIt.hasNext());
    }

    // =========================================================================
    // HELPER — Build a test Enrollment with a pre-attached Section
    // =========================================================================

    private static Enrollment buildEnrollment(int id, String semester, int year,
                                              String deliveryMode, String dayOfWeek,
                                              String startTime, String status) {
        Section section = new Section("S" + id, semester, year, 30);
        section.setDeliveryMode(deliveryMode);
        section.setDayOfWeek(dayOfWeek);
        section.setStartTime(startTime);

        Enrollment e = new Enrollment();
        e.setId(id);
        e.setStatus(status);
        e.setSection(section);
        return e;
    }

    // =========================================================================
    // HELPERS — Output formatting
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
}