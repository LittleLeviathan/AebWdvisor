package edu.advising.commands;

import java.time.LocalDateTime;

/**
 * Command - Interface for all command objects
 */
interface Command {
    void execute();
    void undo();
    boolean isUndoable();
    String getDescription();
    LocalDateTime getExecutionTime();
    boolean wasSuccessful();
}
