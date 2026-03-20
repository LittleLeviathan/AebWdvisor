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
