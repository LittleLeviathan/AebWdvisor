Week 9 - State Pattern & StateFactory
======================================
***working on UserStory [Transcript Request State Machine]. 

- TranscriptRequestState.java - State Pattern interface, defines 7 transition methods:
- This is the interface — it's the rulebook that every state
  class has to follow. It lists all 7 possible transitions. Each state class will implement these methods and either do
  the transition or log an error if it's illegal from that state."

- TranscriptRequest.java - ORM Entity (Class)
    - Represents one row in the transcript_requests database table.
      Every column in the table is a field in this class. Constructor
      automatically sets status to PENDING and records the request
      date when a new request is created. failure_reason and
      processed_by are two new columns we add to the DB later.

TranscriptRequestContext.java - Context Class (Week 9)
- The most important file in the pattern. Holds the current
  state of a transcript request and delegates all transitions
  to whatever state it is currently in. Has two factory methods:
  create() for new requests and load() for loading an existing
  request from the database by ID. Every transition is saved
  to the database immediately after it happens.

- PendingTranscriptState.java - Concrete State Class (Week 9)
    - Represents a request that has been submitted but not yet
      picked up by the registrar. Only process() and cancel()
      are allowed from this state. All other transitions log
      a descriptive error. Implemented as a singleton so only
      one instance ever exists in memory.

ProcessingTranscriptState.java - Concrete State Class (Week 9)
- Represents a request the registrar is actively working on.
  prepare() moves it to READY, cancel() moves it to CANCELLED,
  and fail() moves it to FAILED and stores the failure reason.
  All other transitions log a descriptive error. Implemented
  as a singleton.

ReadyTranscriptState.java - Concrete State Class (Week 9)
- Represents a request that has been prepared and is waiting
  to be dispatched. dispatch() moves it to SENT and records
  the completed date. cancel() moves it to CANCELLED. All
  other transitions log a descriptive error. Implemented
  as a singleton.

- SentTranscriptState.java - Concrete State Class (Week 9)
    - Terminal state. Once a request reaches SENT no further
      transitions are allowed. All 7 methods log a descriptive
      error if attempted. Implemented as a singleton.

CancelledTranscriptState.java - Concrete State Class (Week 9)
- Terminal state. Once a request reaches CANCELLED no further
  transitions are allowed. All 7 methods log a descriptive
  error if attempted. Implemented as a singleton.

- FailedTranscriptState.java - Concrete State Class (Week 9)
    - Represents a request that failed during processing.
      retry() is the only allowed transition, which clears
      the failure reason and moves the request back to
      PROCESSING so the registrar can try again. All other
      transitions log a descriptive error. Implemented as
      a singleton.

- DatabaseManager.java - wk9
    - Added failure_reason TEXT and processed_by INT columns
      to the transcript_requests table. failure_reason stores
      why a request failed. processed_by stores the registrar
      user ID who handled the request.

- NotificationManager.java - wk9
    - Added notifyTranscriptStatusChange() method. Gets called
      every time a transcript request changes state. Sends a
      notification to the student with the tracking number
      and the new status. Also added import for
      edu.advising.state.TranscriptRequest at the top.


***working on UserStory [Centralized DB Status String].

- StateFactory.java - Utility Class (Week 9)
    - Maps DB status strings to the correct State singleton.
      Private constructor so it cannot be instantiated.
      Returns PENDING state for null inputs. Throws
      IllegalArgumentException for unknown status strings.
  

FIXES:
- TranscriptRequestState.java - removed accidental "---"
  that got pasted in at the bottom of the file, was causing
  a build error.

- FailedTranscriptState.java - added missing closing "}"
  at the end of the file, was causing "reached end of file
  while parsing" build error.

- PendingTransactions.java - this file was created with the
  wrong name by accident, deleted it and recreated it as
  PendingTranscriptState.java with the correct name.

- All state files physically moved into the state folder in
  Windows File Explorer so the file locations matched the
  package declaration "edu.advising.state" in each file.
  This was the root cause of most of the errors we saw.

- TranscriptRequestContext.java - FIXED (Week 9)
    - Removed full package path from TranscriptRequest and
      StateFactory references inside the load() method since
      they are all in the same package and don't need it.

- StateFactory.java - FIXED (Week 9)
    - Added imports for all state classes so it could see
      them correctly.

- Week6Test.java - Added (Week 9)
    - Test file for State Pattern. 37 tests covering all valid
      state transitions, cancel transitions, fail and retry,
      illegal transitions, terminal states, StateFactory
      mapping, loading from DB, and tracking number format.
      All 37 tests passing.

- pom.xml - MODIFIED (Week 9)
    - Added run-week6-test execution entry so Week6Test can
      be run with mvn exec:java@run-week6-test

- PATH environment variable - FIXED (Week 9)
    - Windows update wiped the Maven PATH variable. Re-added
      C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.1.1\
      plugins\maven\lib\maven3\bin to user PATH variable.


Week 9 - Registration Period State Machine (Issue #33)
======================================
***working on UserStory [Registration Period State Machine].
- RegistrationPeriodState.java - State Pattern Interface (Week 9)
    - Defines every possible action a registration period can take.
      Interface methods: open(), transitionToLate(), close(),
      canRegister(), canDrop(), isOpen(), checkAndAdvance(), and
      getStateName(). Each concrete state implements these and
      decides what is legal vs illegal from that particular state.

- RegistrationPeriod.java - ORM Entity (Week 9)
    - Represents one row in the registration_periods table.
      Fields: id, semester, year, openDate, closeDate,
      lateRegistrationEnd, and status. Uses custom @Table, @Id,
      and @Column annotations to map to the existing DB schema.

- NotOpenRegistrationState.java - Concrete State Class (Week 9)
    - Registration has not yet opened. canRegister() and canDrop()
      return false and print a contextual message. open() transitions
      to OPEN state. checkAndAdvance() auto-advances to OPEN if
      wall-clock time is past openDate. Implemented as a singleton.

- OpenRegistrationState.java - Concrete State Class (Week 9)
    - Registration is open. canRegister() and canDrop() return true.
      transitionToLate() moves to LATE state. close() moves to
      CLOSED state. checkAndAdvance() auto-advances to LATE if
      closeDate has passed, or CLOSED if lateRegistrationEnd has
      passed. Implemented as a singleton.

- LateRegistrationState.java - Concrete State Class (Week 9)
    - Late registration period. canRegister() and canDrop() return
      true but print a warning that late fees may apply. close()
      transitions to CLOSED. checkAndAdvance() auto-advances to
      CLOSED if lateRegistrationEnd has passed. Implemented as
      a singleton.

- ClosedRegistrationState.java - Concrete State Class (Week 9)
    - Terminal state. Registration is closed for the semester.
      canRegister() and canDrop() return false and print a message
      to contact the registrar. No further transitions are possible.
      checkAndAdvance() logs that no transitions are available.
      Implemented as a singleton.

- RegistrationPeriodContext.java - Context Class (Week 9)
    - Wraps the RegistrationPeriod ORM entity and delegates all
      state-dependent behavior to the current state. Factory method
      forPeriod() loads a period from DB via raw SQL by semester
      and year. Factory method currentPeriod() calls forPeriod()
      then immediately calls checkAndAdvance() to auto-advance
      state. setState() updates current state and persists status
      string back to DB. persist() saves current status via
      raw SQL UPDATE.

- StateFactory.java - UPDATED (Week 9)
    - Added registrationStateFor(String) method that maps all four
      registration status strings to the correct State singleton.
      Returns NOT_OPEN for null inputs. Throws
      IllegalArgumentException for unknown status strings.

FIXES:
- RegistrationPeriodContext.java - FIXED (Week 9)
    - Changed SQL column reference from "status" to "current_state"
      to match the existing registration_periods table schema in
      DatabaseManager.java.
    - Wrapped "year" in backticks in all SQL statements because
      year is a reserved keyword in H2 SQL.
    - Changed constructor from private to public to support
      unit testing without DB connection.
    - Added setPeriod() method to support unit testing with
      manually constructed RegistrationPeriod objects.


Week 9 - Enrollment State Machine (Issue #32)
======================================
***working on UserStory [Enrollment State Machine].

- EnrollmentState.java - State Pattern Interface (Week 9)
    - Defines every possible action an enrollment can take.
      Interface methods: confirm(), drop(reason), withdraw(),
      complete(finalGrade), reenroll(), canDrop(), canWithdraw(),
      canComplete(), and canReenroll(). Each concrete state
      implements only the transitions it allows. All others
      throw IllegalStateException via default methods.

- PendingEnrollmentState.java - Concrete State Class (Week 9)
    - Newly created enrollment awaiting confirmation. The only
      legal transition is confirm() → ENROLLED. Fires a
      notifyEnrollmentUpdate() notification on transition.
      Implemented as a stateless singleton.

- EnrolledState.java - Concrete State Class (Week 9)
    - Active confirmed enrollment. Legal transitions are drop()
      → DROPPED, withdraw() → WITHDRAWN, and complete() →
      COMPLETED. drop() records the drop reason and timestamp.
      withdraw() records a W final grade. complete() records
      the final grade and fires both notifyEnrollmentUpdate()
      and notifyGradePosted(). Guard methods canDrop(),
      canWithdraw(), and canComplete() all return true.
      Implemented as a stateless singleton.

- DroppedState.java - Concrete State Class (Week 9)
    - Enrollment that was actively dropped by the student. The
      only legal transition is reenroll() → ENROLLED, but only
      if the section still has available seats. Throws
      IllegalStateException if section is at capacity. Clears
      dropReason and droppedAt on re-enrollment. Guard method
      canReenroll() returns true. Implemented as a stateless
      singleton.

- WithdrawnState.java - Concrete State Class (Week 9)
    - Terminal state for an enrollment where the student formally
      withdrew. A W grade has been recorded. No further
      transitions are permitted. All guard methods return false
      and all transition methods throw IllegalStateException
      via inherited defaults. Implemented as a stateless
      singleton.

- CompletedState.java - Concrete State Class (Week 9)
    - Terminal state for an enrollment where a final grade has
      been posted and the course is finished. No further
      transitions are permitted. All guard methods return false
      and all transition methods throw IllegalStateException
      via inherited defaults. Implemented as a stateless
      singleton.

- EnrollmentContext.java - Context Class (Week 9)
    - Wraps the existing Enrollment ORM entity and delegates all
      state-sensitive operations to the current EnrollmentState
      singleton. Factory method create() persists a new PENDING
      enrollment to the DB and returns a context ready to use.
      Factory method load() loads an existing enrollment from DB
      by id and reconstructs the correct state from the stored
      status string. setState() updates the current state.
      persist() saves the wrapped Enrollment via the project ORM.
      Guard methods canDrop(), canWithdraw(), canComplete(), and
      canReenroll() delegate to the current state for use by UI
      to show/hide buttons without duplicating business logic.

- StateFactory.java - UPDATED (Week 9)
    - Added enrollmentStateFor(String) method that maps all five
      enrollment status strings to the correct State singleton.
      Handles: PENDING, ENROLLED, DROPPED, WITHDRAWN, COMPLETED.
      Returns PendingEnrollmentState for null inputs. Throws
      IllegalArgumentException for unknown status strings.

- NotificationManager.java - UPDATED (Week 9)
    - Added notifyEnrollmentUpdate() method that fires an
      ENROLLMENT_UPDATE notification whenever an enrollment
      changes status. Added notifyGradePosted() method that
      fires a GRADE_POSTED HIGH priority notification when a
      final grade is recorded on a completed enrollment.

- RegisterCommand.java - UPDATED (Week 9)
    - Updated execute() to use EnrollmentContext.create() and
      ctx.confirm() instead of calling section.enroll() directly.
      New enrollments now begin in PENDING state and transition
      to ENROLLED through the state machine. Added import for
      EnrollmentContext. Fixed notificationManager field to be
      final. Fixed unchecked Map assignment warning in
      deserializeCommandData().
- EnrollmentContext.java - FIXED (Week 9)
    - Made constructor public to allow unit tests to build an
      EnrollmentContext from a manually constructed Enrollment
      without hitting the database.
    - Added sectionId == 0 guard to persist() so unit tests
      skip the database save entirely when using test enrollments.
    - Added final keyword to enrollment field.

- Week6Test.java - UPDATED (Week 9)
    - Added GROUP 14 — Enrollment valid state transitions.
      Tests confirm(), drop(), withdraw(), and complete()
      all transition to the correct state and record the
      correct data.
    - Added GROUP 15 — Enrollment guard methods. Tests all
      four guard methods across all five states to verify
      correct true/false returns.
    - Added GROUP 16 — Enrollment illegal transitions. Tests
      that illegal transitions throw IllegalStateException
      and do not modify state.
    - Added GROUP 17 — Enrollment StateFactory mapping. Tests
      all five status strings plus null and unknown inputs.
    - Updated buildEnrollment() helper to use sectionId 0
      so tests run without a real database section.

Week 10 - View Navigation State Machine (Issue #34)
======================================
***working on UserStory [Web and JavaFX View Navigation State Machine].

- ViewContext.java - Context Class (Week 10)
    - The brain of the entire navigation system. Holds the current
      screen the user is on (currentState) and a Deque history stack
      that tracks previous screens so back() can return to them.
      start() sets the app to GuestViewState on launch, calls enter()
      and render() to show the guest screen.
      navigateTo() is the most important method — checks if the new
      screen requires authentication before switching. If it does and
      no user is logged in, redirects to GuestViewState instead.
      Calls exit() on the current screen, pushes it onto the history
      stack, sets the new screen, then calls enter() and render() on it.
      back() pops the previous screen off the history stack, calls
      exit() on the current screen, sets the popped screen as current,
      then calls enter() and render() on it. Does nothing if history
      is empty.
      logout() clears currentUser, clears the history stack, then
      calls navigateTo(GuestViewState.INSTANCE) to return to the
      guest screen.
      handleAction() delegates the action and args directly to
      currentState.handleAction() — the context never makes decisions,
      it always delegates to the current state.
      render() delegates directly to currentState.render().

- GuestViewState.java - Concrete State Class (Week 10)
    - The screen the user sees when the app first launches or after
      logging out. The only screen that does not require authentication.
      Implemented as a stateless singleton — only one instance ever
      exists in memory because the screen itself never changes.
      enter() prints a message confirming we arrived at the guest screen.
      exit() prints a message confirming we are leaving the guest screen.
      render() prints the welcome message and lists available actions
      LOGIN and EXIT.
      handleAction() is the only screen that handles LOGIN. Grabs
      username, password, and ip from args, passes them to
      AuthenticationContext.login(), and checks the result. If login
      succeeds it sets the current user on the context then routes
      STUDENT users to StudentDashboardViewState and FACULTY users to
      FacultyDashboardViewState. If login fails it prints the failure
      message. LOGOUT does nothing since we are already on the guest
      screen. Unknown actions print an error message.

- StudentDashboardViewState.java - Concrete State Class (Week 10)
    - The main dashboard screen for logged in students. Requires
      authentication — unauthenticated access redirects to guest.
      Implemented as a stateless singleton.
      enter() prints a welcome message when the student arrives
      at their dashboard.
      exit() prints a message confirming we are leaving the dashboard.
      render() prints the dashboard header and lists available actions
      NAVIGATE REGISTRATION, NAVIGATE TRANSCRIPT, and LOGOUT.
      handleAction() handles NAVIGATE by checking args[0] to determine
      which screen to go to. REGISTRATION navigates to
      RegistrationViewState, TRANSCRIPT navigates to TranscriptViewState.
      LOGOUT calls context.logout() which clears the current user,
      clears the history stack, and returns to GuestViewState.
      Unknown actions print an error message.

- FacultyDashboardViewState.java - Concrete State Class (Week 10)
    - The main dashboard screen for logged in faculty members. Requires
      authentication — unauthenticated access redirects to guest.
      Implemented as a stateless singleton.
      enter() prints a welcome message when the faculty member arrives
      at their dashboard.
      exit() prints a message confirming we are leaving the dashboard.
      render() prints the dashboard header and lists available actions
      NAVIGATE PERMISSIONS and LOGOUT.
      handleAction() handles NAVIGATE by checking args[0] to determine
      which screen to go to. PERMISSIONS navigates to
      PermissionManagementViewState. LOGOUT calls context.logout()
      which clears the current user, clears the history stack, and
      returns to GuestViewState. Faculty cannot navigate to student
      screens because those actions are simply not handled here —
      access control happens naturally through the State Pattern
      without any extra permission checking code.
      Unknown actions print an error message.

- RegistrationViewState.java - Concrete State Class (Week 10)
    - The registration screen where students can check the current
      registration period status. Requires authentication —
      unauthenticated access redirects to guest. Implemented as
      a stateless singleton.
      enter() prints a message confirming arrival at the registration
      screen.
      exit() prints a message confirming we are leaving the screen.
      render() prints the screen header and lists available actions
      CHECK_STATUS, BACK, and LOGOUT.
      handleAction() handles CHECK_STATUS by grabbing semester from
      args[0] and year from args[1], then calling
      RegistrationPeriodContext.currentPeriod() which loads the
      registration period from the database and auto-advances the
      state if dates have passed. Prints the current state name so
      the student can see whether registration is OPEN, CLOSED, LATE,
      or NOT_OPEN. This is where the View Navigation State Machine
      connects directly to the Registration Period State Machine —
      two state machines working together. BACK calls context.back()
      to return to the previous screen. LOGOUT calls context.logout()
      to end the session. Unknown actions print an error message.

- TranscriptViewState.java - Concrete State Class (Week 10)
    - The transcript screen where students can view and order
      transcripts. Requires authentication — unauthenticated access
      redirects to guest. Implemented as a stateless singleton.
      enter() prints a message confirming arrival at the transcript
      screen.
      exit() prints a message confirming we are leaving the screen.
      render() prints the screen header and lists available actions
      BACK and LOGOUT. Transcript viewing and ordering actions are
      intentionally left as placeholders to be filled in during the
      Template Method pattern sprint.
      handleAction() handles BACK by calling context.back() to return
      to the previous screen. LOGOUT calls context.logout() to end
      the session. Unknown actions print an error message.

- PermissionManagementViewState.java - Concrete State Class (Week 10)
    - The screen where faculty can approve or deny waitlist permission
      requests. Requires authentication — unauthenticated access
      redirects to guest. Only reachable from FacultyDashboardViewState
      since students do not have a NAVIGATE PERMISSIONS action available
      to them. Implemented as a stateless singleton.
      enter() prints a message confirming arrival at the permission
      management screen.
      exit() prints a message confirming we are leaving the screen.
      render() prints the screen header and lists available actions
      APPROVE, DENY, BACK, and LOGOUT.
      handleAction() handles APPROVE by grabbing permissionId from
      args[0], loading FacultyPermissionContext for that id, and
      calling approve() which transitions the permission from
      REQUESTED to APPROVED. DENY grabs permissionId from args[0]
      and reason from args[1] and calls deny(reason) which transitions
      it to DENIED. This is where the View Navigation State Machine
      connects directly to the Faculty Permission State Machine.
      BACK calls context.back() to return to the faculty dashboard.
      LOGOUT calls context.logout() to end the session.
      NOTE: FacultyPermissionContext import will show a red error
      until LittleLeviathan's branch is merged into main. Error
      will resolve automatically after fetch and pull.

- LoginViewState.java - Concrete State Class (Week 10)
    - Alias for GuestViewState — the login screen and the guest
      screen are the same thing in this application. Implemented
      as a stateless singleton.
      Rather than duplicating all the guest screen logic every
      method simply delegates directly to GuestViewState.INSTANCE.
      enter() delegates to GuestViewState.INSTANCE.enter().
      exit() delegates to GuestViewState.INSTANCE.exit().
      render() delegates to GuestViewState.INSTANCE.render().
      handleAction() delegates to GuestViewState.INSTANCE.handleAction().
      This way navigating to LOGIN gives the exact same experience
      as navigating to GUEST without any duplicated code.

- StateFactory.java - UPDATED (Week 10)
    - Added viewStateFor(String) method that maps all seven view
      name strings to the correct ViewState singleton.
      Handles: GUEST, LOGIN, STUDENT_DASHBOARD, FACULTY_DASHBOARD,
      REGISTRATION, TRANSCRIPT, PERMISSION_MANAGEMENT.
      Returns GuestViewState for null inputs. Throws
      IllegalArgumentException for unknown view name strings.
      Follows the exact same pattern as the existing
      transcriptStateFor(), enrollmentStateFor(), and
      registrationStateFor() methods already in this file.

FIXES:
- ViewContext.java - FIXED (Week 10)
    - logout() method was calling navigateTo(GuestViewState.INSTANCE)
      which was pushing the current state onto the history stack before
      clearing it. Fixed by replacing the navigateTo() call with direct
      state switching — calls exit() on current state, sets currentState
      to GuestViewState, then calls enter() and render() directly.
      This ensures the history stack is fully empty after logout.
    - Added navigateToWithoutHistory() method. Used for login transitions
      so GuestViewState does not get pushed onto the history stack when
      a student or faculty member logs in. Without this fix back() from
      the first authenticated screen would leave GuestViewState sitting
      in the history stack instead of being empty.

- GuestViewState.java - FIXED (Week 10)
    - Updated handleAction() LOGIN case to call
      context.navigateToWithoutHistory() instead of context.navigateTo()
      when routing to StudentDashboardViewState or FacultyDashboardViewState
      after successful login. This prevents GuestViewState from being
      pushed onto the history stack during login, which was causing
      test 7.2 to fail.

- PermissionManagementViewState.java - UPDATED (Week 10)
    - Commented out FacultyPermissionContext.load() calls in the APPROVE
      and DENY cases of handleAction() because FacultyPermissionContext
      does not exist in main yet — LittleLeviathan's branch has not been
      merged. Added TODO comments on the commented lines so they are easy
      to find and uncomment once their branch is merged and a fetch and
      pull is done.
    - Removed import for edu.advising.state.FacultyPermissionContext
      for the same reason.