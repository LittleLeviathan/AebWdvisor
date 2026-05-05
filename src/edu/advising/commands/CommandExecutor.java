package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - Command Executor (The Invoker)
// ============================================================================
//
// PATTERN ROLE: The INVOKER.
//   In the classic Command Pattern:
//     Client  → creates concrete Command (RegisterCommand, PaymentCommand, …)
//     Invoker → triggers execute() and manages history
//     Receiver → does the actual work (Section, DatabaseManager, …)
//
//   CommandExecutor IS the Invoker. It is the single entry point for all
//   user-initiated actions in the application. By routing every action
//   through this class, we guarantee:
//     1. Every action is recorded in command_history for auditing.
//     2. Undo and Redo work consistently across the whole app.
//     3. The UI/Service layer never touches business logic directly —
//        it only creates a command and hands it to the executor.
//
// ─────────────────────────────────────────────────────────────────────────────
// LIFECYCLE — Singleton: one shared instance for the whole session.
//
//   // At login:
//   CommandExecutor.init(loggedInUser.getId());
//
//   // From anywhere in the app:
//   CommandExecutor.getInstance().execute(new RegisterCommand(student, section));
//
// ─────────────────────────────────────────────────────────────────────────────
// OPEN/CLOSED PRINCIPLE:
//   Adding a new user action (e.g. Week 8's TranscriptRequestCommand) requires
//   ONLY creating a new BaseCommand subclass. CommandExecutor never changes.
//
// ============================================================================

import java.util.List;

public class CommandExecutor {

    // -------------------------------------------------------------------------
    // Singleton — single shared instance
    // -------------------------------------------------------------------------

    private static volatile CommandExecutor instance;

    private final CommandHistory history;

    private CommandExecutor(int userId) {
        this.history = new CommandHistory(userId);
    }

    private CommandExecutor(CommandHistory history) {
        this.history = history;
    }

    /**
     * Initialise the singleton for a logged-in user session.
     * Call once at login before any calls to getInstance().
     * @param userId The logged-in user's numeric primary key.
     */
    public static synchronized void init(int userId) {
        instance = new CommandExecutor(userId);
    }

    /**
     * Initialise with a provided CommandHistory — used by unit tests
     * to inject a mock history without requiring a real database.
     */
    public static synchronized void initForTesting(CommandHistory history) {
        instance = new CommandExecutor(history);
    }

    /**
     * Returns the single shared CommandExecutor for this session.
     * @throws IllegalStateException if init() has not been called yet.
     */
    public static synchronized CommandExecutor getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    "CommandExecutor has not been initialized. " +
                            "Call CommandExecutor.init(userId) at login before using getInstance()."
            );
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // Command Execution — the primary API for the UI layer
    // -------------------------------------------------------------------------

    /**
     * Execute a command and record it in history.
     *
     * Example:
     *   RegisterCommand cmd = new RegisterCommand(student, section);
     *   CommandExecutor.getInstance().execute(cmd);
     *   if (!cmd.wasSuccessful()) showErrorDialog(cmd.getErrorMessage());
     *
     * @param command Any concrete BaseCommand subclass.
     */
    public void execute(BaseCommand command) {
        history.executeCommand(command);
    }

    // -------------------------------------------------------------------------
    // Undo / Redo
    // -------------------------------------------------------------------------

    /**
     * Undo the last executed undoable command.
     * @return true if something was undone.
     */
    public boolean undo() {
        return history.undo();
    }

    /**
     * Redo the last undone command.
     * @return true if something was redone.
     */
    public boolean redo() {
        return history.redo();
    }

    // -------------------------------------------------------------------------
    // State Queries — for enabling/disabling toolbar buttons
    // -------------------------------------------------------------------------

    /** @return true if the Undo button should be enabled. */
    public boolean canUndo() {
        return history.canUndo();
    }

    /** @return true if the Redo button should be enabled. */
    public boolean canRedo() {
        return history.canRedo();
    }

    /**
     * Human-readable label for the next action that would be undone.
     * Useful for dynamic button tooltips: "Undo: Register for CIS-12 SP26-01"
     */
    public String peekUndoDescription() {
        return history.peekUndoDescription();
    }

    /** Human-readable label for the next action that would be redone. */
    public String peekRedoDescription() {
        return history.peekRedoDescription();
    }

    // -------------------------------------------------------------------------
    // History Access
    // -------------------------------------------------------------------------

    /**
     * Returns the live in-session undo stack (most recent first).
     * Useful for a "Recent Actions" panel in the UI.
     */
    public List<BaseCommand> getSessionHistory() {
        return history.getUndoStack();
    }

    /**
     * Load full audit history from the database for the current user.
     * @param limit Maximum records to return (most recent first).
     */
    public List<CommandRecord> getAuditHistory(int limit) {
        return history.getAuditHistory(limit);
    }
}