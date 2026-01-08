package edu.advising.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ValidationResult - Result of validation pipeline
 */
public class ValidationResult {
    private boolean valid;
    private String message;
    private List<String> errors;
    private List<String> warnings;
    private Map<String, Object> metadata;

    public ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.metadata = new HashMap<>();
    }

    public static ValidationResult success() {
        return new ValidationResult(true, "Validation passed");
    }

    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }

    public void addError(String error) {
        errors.add(error);
        valid = false;
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    // Getters
    public boolean isValid() { return valid; }
    public String getMessage() { return message; }
    public List<String> getErrors() { return new ArrayList<>(errors); }
    public List<String> getWarnings() { return new ArrayList<>(warnings); }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(valid ? "✓ VALID" : "✗ INVALID").append(": ").append(message).append("\n");

        if (!errors.isEmpty()) {
            sb.append("  Errors:\n");
            for (String error : errors) {
                sb.append("    • ").append(error).append("\n");
            }
        }

        if (!warnings.isEmpty()) {
            sb.append("  Warnings:\n");
            for (String warning : warnings) {
                sb.append("    ⚠ ").append(warning).append("\n");
            }
        }

        return sb.toString();
    }
}