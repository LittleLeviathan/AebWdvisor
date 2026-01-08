package edu.advising.commands;

import java.time.LocalDateTime;

/**
 * Abstract base command with common functionality
 */
abstract class BaseCommand implements Command {
    protected LocalDateTime executionTime;
    protected boolean executed;
    protected boolean successful;

    public BaseCommand() {
        this.executed = false;
        this.successful = false;
    }

    @Override
    public LocalDateTime getExecutionTime() {
        return executionTime;
    }

    @Override
    public boolean wasSuccessful() {
        return successful;
    }

    protected void logExecution(String action) {
        System.out.println(String.format("[%s] %s",
                LocalDateTime.now().toString().substring(0, 19), action));
    }
}