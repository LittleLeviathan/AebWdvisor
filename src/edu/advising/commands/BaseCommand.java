package edu.advising.commands;

import edu.advising.core.Column;
import edu.advising.core.Id;
import edu.advising.core.Table;

import java.time.LocalDateTime;


/**
 * Abstract base command with common functionality
 *
 * ORM PERSISTENCE
 *
 * Commands are different than User -> Student -> ObservableStudent hierarchy:
 *   * BaseCommand is the annotated Superclass for command_history.
 *   * Concrete commands RegisterCommand, DropCommand, etc. extend it
 *     with runtime behaviour but don't add annotated fields like Student.
 *     ALL command specific state (i.e. fields) is serialised into the
 *     inherited commandData JSON column via serializeCommandData().
 *   * Thus, the ORM only needs to write the BaseCommand fields and no new
 *     table or columns from concrete Subclasses.
 *
 * I'm still adding fromSuperType and toSubType methods, simply because I
 * feel like I may need them in the future.
 */
@Table(name = "command_history")
public abstract class BaseCommand implements Command {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    protected int id;
    @Column(name = "user_id", foreignKey = true)
    protected int userId;
    @Column(name = "command_type")
    protected String commandType;
    @Column(name = "command_data")
    protected String commandData;
    @Column(name = "executed_at")
    protected LocalDateTime executionTime;
    @Column(name = "undone_at")
    protected LocalDateTime undoneAt;
    @Column(name = "is_undone")
    protected boolean isUndone;
    @Column(name = "success")
    protected boolean successful;
    @Column(name = "error_message")
    protected String errorMessage;

    // This filed is not persisted to the DB
    // It's used for execute/undo checks.
    protected boolean executed;

    public BaseCommand() {
        this.executed = false;
        this.successful = false;
    }

    /**
     * Prepares this command for ORM persistence (i.e. as a command_history record)
     */
    public BaseCommand toSubType() {
        prepareForStorage();
        return this;
    }

    /**
     * Copies all BaseCommand metadata fields from base class onto target concrete class
     * Concrete commands can call this inside their own static factory after constructing
     * the concrete instance.
     */
    protected static void copyBaseFields(BaseCommand source, BaseCommand target) {
        target.id            = source.id;
        target.userId        = source.userId;
        target.commandType   = source.commandType;
        target.commandData   = source.commandData;
        target.executionTime = source.executionTime;
        target.undoneAt      = source.undoneAt;
        target.isUndone      = source.isUndone;
        target.successful    = source.successful;
        target.errorMessage  = source.errorMessage;
        target.executed      = source.executed;
    }

    // ── Serialisation hooks ───────────────────────────────────────────────────

    // Serialise a command's fields into the commandData JSON.
    protected abstract String serializeCommandData();

    // Restore a commands fields from the commandData JSON.
    protected abstract void deserializeCommandData(String json);

    // Call this before saving to the database
    public void prepareForStorage() {
        this.commandData = serializeCommandData();
    }

    // Call this after loading from the database
    public void initAfterLoad() {
        if (this.commandData != null && !this.commandData.isBlank()) {
            deserializeCommandData(this.commandData);
        }
    }

    // ── Getters and Setters ───────────────────────────────────────────────────

    @Override
    public LocalDateTime getExecutionTime() { return executionTime; }
    public void setExecutionTime(LocalDateTime executedAt) { this.executionTime = executedAt; }

    @Override
    public boolean wasSuccessful() { return successful; }
    public void setSuccess(boolean success) { this.successful = success; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCommandType() { return commandType; }
    public void setCommandType(String commandType) { this.commandType = commandType; }

    public String getCommandData() { return commandData; }
    public void setCommandData(String commandData) { this.commandData = commandData; }

    public LocalDateTime getUndoneAt() { return undoneAt; }
    public void setUndoneAt(LocalDateTime undoneAt) { this.undoneAt = undoneAt; }

    public boolean isUndone() { return isUndone; }
    public void setUndone(boolean undone) { isUndone = undone; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}