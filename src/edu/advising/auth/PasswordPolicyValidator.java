package edu.advising.auth;

import edu.advising.common.ValidationResult;
import edu.advising.core.DatabaseManager;

import java.sql.SQLException;


public class PasswordPolicyValidator {

    public static ValidationResult validateAgainstPolicy(String password) throws SQLException {
        String sql = "SELECT * FROM password_policies WHERE is_active = TRUE LIMIT 1";
        return DatabaseManager.getInstance().executeQuery(sql, rs -> {
            if (!rs.next()) {
                return ValidationResult.success(); // No policy set
            }

            ValidationResult result = new ValidationResult(true, "Password meets requirements");

            if (password.length() < rs.getInt("min_length")) {
                result.addError("Password must be at least " + rs.getInt("min_length") + " characters");
            }

            if (rs.getBoolean("require_uppercase") && !password.matches(".*[A-Z].*")) {
                result.addError("Password must contain uppercase letter");
            }

            if (rs.getBoolean("require_lowercase") && !password.matches(".*[a-z].*")) {
                result.addError("Password must contain lowercase letter");
            }

            if (rs.getBoolean("require_digit") && !password.matches(".*\\d.*")) {
                result.addError("Password must contain digit");
            }

            if (rs.getBoolean("require_special") && !password.matches(".*[!@#$%^&*].*")) {
                result.addError("Password must contain special character");
            }

            return result;
        });
    }
}