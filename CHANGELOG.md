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