

File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\.github\ISSUE_TEMPLATE\1-user-story.yml
```yaml
name: User Story
description: Independent, negotiable, Valuable, Estimable, Small and Testable description of work that needs to be done from the perspective of the user.
title: "[User Story] <title>"
labels: ["user-story"]
body:
  - type: input
    id: epic_label
    attributes:
      label: Epic Label
      description: Create an epic label for this user story. A workflow task will apply this label.
      placeholder: "Epic: Feature A"
    validations:
      required: true
  - type: input
    id: persona
    attributes:
      label: As a ...
      description: List the persona this user story applies to.
      placeholder: "Who?"
    validations:
      required: true
  - type: input
    id: accomplishment
    attributes:
      label: I want to ...
      description: What the persona wants to accomplish?
      placeholder: "What?"
    validations:
      required: true
  - type: input
    id: why
    attributes:
      label: So that ...
      description: Why they want to accomplish that thing?
      placeholder: "Why?"
    validations:
      required: true
  - type: textarea
    id: tasks
    attributes:
      label: Tasks
      description: "List of task-issues necessary to complete part of this user story."
      placeholder: |
        Create a "Task Issue" then use the text-area's create a task list linking each task to this user-story with the # syntax."
        e.g. - [ ] #5555"
    validations:
      required: true
  - type: textarea
    id: acceptance-criteria
    attributes:
      label: Acceptance Criteria
      description: Metric of Story Progress
      placeholder: |
        * List of requirements at user or system level
        * May also include screenshots, system flow diagrams, or sequence diagrams here.
        * Must be specific to the User Story
        * Must be met to ensure story is complete.
    validations:
      required: false
  - type: textarea
    id: progress
    attributes:
      label: Definition of Done
      description: Metric of Sprint Progress
      placeholder: |
        * List of criteria for ALL User Stories
        * Sprint work only complete once all criteria are met
    validations:
      required: false

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\.github\ISSUE_TEMPLATE\2-bug-report.yml
```yaml
name: "🐛 Bug Report"
description: Create a new ticket for a bug.
title: "🐛 [BUG] - <title>"
labels: [
  "bug"
]
body:
  - type: textarea
    id: description
    attributes:
      label: "Description"
      description: Please enter an explicit description of your issue
      placeholder: Short and explicit description of your incident...
    validations:
      required: true
  - type: textarea
    id: reprod
    attributes:
      label: "Reproduction steps"
      description: Please enter an explicit description of your issue
      value: |
        1. Go to '...'
        2. Click on '....'
        3. Scroll down to '....'
        4. See error
      render: bash
    validations:
      required: true
  - type: textarea
    id: screenshot
    attributes:
      label: "Screenshots"
      description: If applicable, add screenshots to help explain your problem.
      value: |
        ![DESCRIPTION](LINK.png)
      render: bash
    validations:
      required: false
  - type: textarea
    id: logs
    attributes:
      label: "Logs"
      description: Please copy and paste any relevant log output. This will be automatically formatted into code, so no need for backticks.
      render: bash
    validations:
      required: false
  - type: dropdown
    id: browsers
    attributes:
      label: "Browsers"
      description: What browsers are you seeing the problem on ?
      multiple: true
      options:
        - Firefox
        - Chrome
        - Safari
        - Microsoft Edge
        - Opera
    validations:
      required: false
  - type: dropdown
    id: os
    attributes:
      label: "OS"
      description: What is the impacted environment ?
      multiple: true
      options:
        - Windows
        - Linux
        - Mac
    validations:
      required: false

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\.github\ISSUE_TEMPLATE\config.yml
```yaml
blank_issues_enabled: false

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\.github\workflows\add-to-project.yml
```yaml
name: Auto-Add to Project

on:
  issues:
    types:
      - opened

jobs:
  add-to-project:
    runs-on: ubuntu-latest
    env:
      GH_TOKEN: ${{ secrets.WORKFLOW_TOKEN }}
    steps:
    - uses: actions/checkout@v3
    - name: Add to Project
      run: |
        # Set your project name
        project_name="@THartmanOfTheRedwoods's BetterAdvisor"
        # Add the issue to the project using gh
        gh issue edit ${{ github.event.issue.number }} --add-project "$project_name"

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\.github\workflows\label-epic.yml
```yaml
name: Label User-Story with Epic

on:
  issues:
    types: [opened]

jobs:
  process_issue:
    runs-on: ubuntu-latest
    env:
      GH_TOKEN: ${{ secrets.WORKFLOW_TOKEN }}
    steps:
      - name: Verify Issue Type
        id: type_check
        run: |
          issue_body="${{ github.event.issue.body }}"
          echo "Issue body: $issue_body"
          if [[ $issue_body == *"Epic Label"* ]]; then
            echo "Issue is a User Story, proceeding with the workflow..."
          else
            echo "Issue is not a User Story. Skipping workflow..."
            exit 78  # Exit with a neutral status code to indicate a skipped run
          fi
      - name: Install gh
        run: |
          sudo apt-get install gh  # For Ubuntu-based runners
      - uses: actions/checkout@v3    
      - name: Apply epic label
        run: |
          epic_label=$(echo "${{ github.event.issue.body }}" | awk -v RS='###' '/Epic Label/ {gsub(/^[[:space:]]*Epic Label[[:space:]]*/, "epic:", $0); gsub(/^[[:space:]]+|[[:space:]]+$/, "", $0); print $0}'"")
          echo "Applying Epic Label: $epic_label"
          label_search_output=$(gh label list --json name -q ".[] | .name" | grep "$epic_label" || true)
          if [ "$label_search_output" = "$epic_label" ]; then
            echo "Label already exists, so skipping creation...";
          else
            echo "Creating label $epic_label";
            gh label create "$epic_label" --description "New Epic $epic_label" --color E99695
          fi
          gh issue edit ${{ github.event.issue.number }} --add-label "$epic_label"
          

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\BetterAdvisor.md


File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\.github\ISSUE_TEMPLATE\1-user-story.yml
```yaml
name: User Story
description: Independent, negotiable, Valuable, Estimable, Small and Testable description of work that needs to be done from the perspective of the user.
title: "[User Story] <title>"
labels: ["user-story"]
body:
  - type: input
    id: epic_label
    attributes:
      label: Epic Label
      description: Create an epic label for this user story. A workflow task will apply this label.
      placeholder: "Epic: Feature A"
    validations:
      required: true
  - type: input
    id: persona
    attributes:
      label: As a ...
      description: List the persona this user story applies to.
      placeholder: "Who?"
    validations:
      required: true
  - type: input
    id: accomplishment
    attributes:
      label: I want to ...
      description: What the persona wants to accomplish?
      placeholder: "What?"
    validations:
      required: true
  - type: input
    id: why
    attributes:
      label: So that ...
      description: Why they want to accomplish that thing?
      placeholder: "Why?"
    validations:
      required: true
  - type: textarea
    id: tasks
    attributes:
      label: Tasks
      description: "List of task-issues necessary to complete part of this user story."
      placeholder: |
        Create a "Task Issue" then use the text-area's create a task list linking each task to this user-story with the # syntax."
        e.g. - [ ] #5555"
    validations:
      required: true
  - type: textarea
    id: acceptance-criteria
    attributes:
      label: Acceptance Criteria
      description: Metric of Story Progress
      placeholder: |
        * List of requirements at user or system level
        * May also include screenshots, system flow diagrams, or sequence diagrams here.
        * Must be specific to the User Story
        * Must be met to ensure story is complete.
    validations:
      required: false
  - type: textarea
    id: progress
    attributes:
      label: Definition of Done
      description: Metric of Sprint Progress
      placeholder: |
        * List of criteria for ALL User Stories
        * Sprint work only complete once all criteria are met
    validations:
      required: false

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\.github\ISSUE_TEMPLATE\2-bug-report.yml
```yaml
name: "🐛 Bug Report"
description: Create a new ticket for a bug.
title: "🐛 [BUG] - <title>"
labels: [
  "bug"
]
body:
  - type: textarea
    id: description
    attributes:
      label: "Description"
      description: Please enter an explicit description of your issue
      placeholder: Short and explicit description of your incident...
    validations:
      required: true
  - type: textarea
    id: reprod
    attributes:
      label: "Reproduction steps"
      description: Please enter an explicit description of your issue
      value: |
        1. Go to '...'
        2. Click on '....'
        3. Scroll down to '....'
        4. See error
      render: bash
    validations:
      required: true
  - type: textarea
    id: screenshot
    attributes:
      label: "Screenshots"
      description: If applicable, add screenshots to help explain your problem.
      value: |
        ![DESCRIPTION](LINK.png)
      render: bash
    validations:
      required: false
  - type: textarea
    id: logs
    attributes:
      label: "Logs"
      description: Please copy and paste any relevant log output. This will be automatically formatted into code, so no need for backticks.
      render: bash
    validations:
      required: false
  - type: dropdown
    id: browsers
    attributes:
      label: "Browsers"
      description: What browsers are you seeing the problem on ?
      multiple: true
      options:
        - Firefox
        - Chrome
        - Safari
        - Microsoft Edge
        - Opera
    validations:
      required: false
  - type: dropdown
    id: os
    attributes:
      label: "OS"
      description: What is the impacted environment ?
      multiple: true
      options:
        - Windows
        - Linux
        - Mac
    validations:
      required: false

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\.github\ISSUE_TEMPLATE\config.yml
```yaml
blank_issues_enabled: false

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\.github\workflows\add-to-project.yml
```yaml
name: Auto-Add to Project

on:
  issues:
    types:
      - opened

jobs:
  add-to-project:
    runs-on: ubuntu-latest
    env:
      GH_TOKEN: ${{ secrets.WORKFLOW_TOKEN }}
    steps:
    - uses: actions/checkout@v3
    - name: Add to Project
      run: |
        # Set your project name
        project_name="@THartmanOfTheRedwoods's BetterAdvisor"
        # Add the issue to the project using gh
        gh issue edit ${{ github.event.issue.number }} --add-project "$project_name"

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\.github\workflows\label-epic.yml
```yaml
name: Label User-Story with Epic

on:
  issues:
    types: [opened]

jobs:
  process_issue:
    runs-on: ubuntu-latest
    env:
      GH_TOKEN: ${{ secrets.WORKFLOW_TOKEN }}
    steps:
      - name: Verify Issue Type
        id: type_check
        run: |
          issue_body="${{ github.event.issue.body }}"
          echo "Issue body: $issue_body"
          if [[ $issue_body == *"Epic Label"* ]]; then
            echo "Issue is a User Story, proceeding with the workflow..."
          else
            echo "Issue is not a User Story. Skipping workflow..."
            exit 78  # Exit with a neutral status code to indicate a skipped run
          fi
      - name: Install gh
        run: |
          sudo apt-get install gh  # For Ubuntu-based runners
      - uses: actions/checkout@v3    
      - name: Apply epic label
        run: |
          epic_label=$(echo "${{ github.event.issue.body }}" | awk -v RS='###' '/Epic Label/ {gsub(/^[[:space:]]*Epic Label[[:space:]]*/, "epic:", $0); gsub(/^[[:space:]]+|[[:space:]]+$/, "", $0); print $0}'"")
          echo "Applying Epic Label: $epic_label"
          label_search_output=$(gh label list --json name -q ".[] | .name" | grep "$epic_label" || true)
          if [ "$label_search_output" = "$epic_label" ]; then
            echo "Label already exists, so skipping creation...";
          else
            echo "Creating label $epic_label";
            gh label create "$epic_label" --description "New Epic $epic_label" --color E99695
          fi
          gh issue edit ${{ github.event.issue.number }} --add-label "$epic_label"
          

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\README.md
## Running Tests

```bash
mvn exec:java@run-week5-test
```




File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\AuthenticationContext.java
```java
package edu.advising.auth;

import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * AuthenticationContext - Context Class
 * Manages the current authentication strategy
 */
public class AuthenticationContext {
    private AuthenticationStrategy strategy;
    private DatabaseManager dbManager;
    private UserFactory userFactory;

    public AuthenticationContext(AuthenticationStrategy strategy) {
        this.strategy = strategy;
        this.dbManager = DatabaseManager.getInstance();
        this.userFactory = new UserFactory();
    }

    // Allow runtime strategy switching
    public void setStrategy(AuthenticationStrategy strategy) {
        this.strategy = strategy;
        System.out.println("Authentication strategy changed to: " +
                strategy.getClass().getSimpleName());
    }

    public AuthenticationStrategy getStrategy() {
        return strategy;
    }

    /**
     * Login with current strategy
     */
    public AuthenticationResult login(String username, String password, String ipAddress) {
        AuthenticationResult authResult =  strategy.authenticate(username, password);

        // Log attempt
        try {
            String sql = "INSERT INTO login_attempts (username, status, ip_address, failure_reason) " +
                    "VALUES (?, ?, ?, ?)";
            dbManager.executeInsert(sql, username, authResult.getState().name(), ipAddress, authResult.getMessage());
            if (authResult.isFullyAuthenticated()) {
                // Update last_login
                User user = authResult.getUser();
                String updateSql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
                dbManager.executeUpdate(updateSql, user.getId());
            }
        } catch (SQLException e) {
            System.err.println("Failed to log attempt: " + e.getMessage());
        }

        return authResult;
    }

    /**
     * Continue authentication with additional credential
     */
    public AuthenticationResult verify(String authToken, String credential) {
        return strategy.continueAuthentication(authToken, credential);
    }

    private boolean isPasswordInHistory(int userId, String newHash) throws SQLException {
        String sql = "SELECT password_hash FROM password_history " +
                "WHERE user_id = ? ORDER BY changed_at DESC LIMIT 5";

        return dbManager.executeQuery(sql, rs -> {
            while (rs.next()) {
                if (rs.getString("password_hash").equals(newHash)) {
                    return true; // Password was used recently
                }
            }
            return false;
        }, userId);
    }

    /**
     * Change password functionality
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) throws SQLException {
        // Verify old password
        AuthenticationResult authResult = strategy.authenticate(username, oldPassword);
        if (!authResult.isFullyAuthenticated()) {
            System.out.println("Old password is incorrect");
            return false;
        }
        // Now that the user is authenticated, get the user object to verify history.
        User user = authResult.getUser();
        // Use auth strategy to get our new password hash for old pass verification/update.
        String newHash = strategy.hashPassword(newPassword);
        // Verify that this is not an old password re-used.
        if (isPasswordInHistory(user.getId(), newHash)) {
            System.out.println("Cannot reuse recent passwords");
            return false;
        }

        // Validate new password strength
        if (!strategy.validatePasswordStrength(newPassword)) {
            System.out.println("New password does not meet strength requirements");
            return false;
        }

        // Update password in database
        try {
            String updateSql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            int updated = dbManager.executeUpdate(updateSql, newHash, user.getId());
            if (updated > 0) {
                System.out.println("Password changed successfully");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error changing password: " + e.getMessage());
        }
        return false;
    }

    /**
     * Password recovery - "What's My Password?" feature
     */
    public boolean initiatePasswordReset(String username, String email) {
        // Verify user exists and email matches
        User user = userFactory.getUserByUsername(username);
        if (user == null || !user.getEmail().equals(email)) {
            System.out.println("✗ User not found or email doesn't match");
            return false;
        }

        try {
            // Generate secure reset token
            String resetToken = generateResetToken();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiresAt = now.plusHours(24);

            // Invalidate any existing tokens for this user
            String invalidateSql = "UPDATE password_reset_tokens SET is_used = TRUE " +
                    "WHERE user_id = ? AND is_used = FALSE";
            dbManager.executeUpdate(invalidateSql, user.getId());

            // Store new reset token
            String insertSql = "INSERT INTO password_reset_tokens " +
                    "(user_id, token, expires_at) VALUES (?, ?, ?)";
            dbManager.executeUpdate(
                    insertSql, user.getId(), resetToken, Timestamp.valueOf(expiresAt));

            System.out.println("✓ Password reset link sent to: " + email);
            System.out.println("  Reset token: " + resetToken);
            System.out.println("  Expires: " + expiresAt);

            // In real system, send email with reset link:
            // emailService.sendPasswordResetEmail(email, resetToken);

            return true;

        } catch (SQLException e) {
            System.err.println("✗ Error creating reset token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify reset token and allow password reset
     * @param token Reset token from email link
     * @param newPassword New password to set
     * @return true if reset successful, false if token invalid/expired
     */
    public boolean resetPasswordWithToken(String token, String newPassword) {
        try {
            // Find valid token
            String sql = "SELECT user_id, expires_at FROM password_reset_tokens " +
                    "WHERE token = ? AND is_used = FALSE";
            return dbManager.executeQuery(sql, rs -> {
                if (!rs.next()) {
                    System.out.println("✗ Invalid or already used reset token");
                    return false;
                }

                int userId = rs.getInt("user_id");
                Timestamp expiresAt = rs.getTimestamp("expires_at");

                // Check if token expired
                if (expiresAt.before(Timestamp.valueOf(LocalDateTime.now()))) {
                    System.out.println("✗ Reset token has expired");
                    return false;
                }

                // Validate new password strength
                if (!strategy.validatePasswordStrength(newPassword)) {
                    System.out.println("✗ New password does not meet requirements");
                    return false;
                }

                if( isPasswordInHistory(userId, newPassword) ) {
                    System.out.println("✗ Cannot reuse recent passwords");
                    return false;
                }

                // Hash and update password
                String hashedPassword = strategy.hashPassword(newPassword);
                String updatePasswordSql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP " +
                        "WHERE id = ?";
                dbManager.executeUpdate(updatePasswordSql, hashedPassword, userId);

                // Mark token as used
                String markUsedSql = "UPDATE password_reset_tokens SET is_used = TRUE, " +
                        "used_at = CURRENT_TIMESTAMP WHERE token = ?";
                dbManager.executeUpdate(markUsedSql, token);

                // Add to password history
                String historySql = "INSERT INTO password_history (user_id, password_hash) VALUES (?, ?)";
                dbManager.executeUpdate(historySql, userId, hashedPassword);

                System.out.println("✓ Password reset successful");
                return true;

            },token);

        } catch (SQLException e) {
            System.err.println("✗ Error resetting password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clean up expired reset tokens (should be run periodically)
     */
    public void cleanupExpiredTokens() {
        try {
            String sql = "DELETE FROM password_reset_tokens " +
                    "WHERE expires_at < CURRENT_TIMESTAMP AND is_used = FALSE";
            int deleted = dbManager.executeUpdate(sql);
            System.out.println("✓ Cleaned up " + deleted + " expired reset tokens");
        } catch (SQLException e) {
            System.err.println("Error cleaning up tokens: " + e.getMessage());
        }
    }

    public void logout() {
        //TODO: Figure out what it means to logout.
        // It probably means to delegate to the strategy a logout, which will likely make sure auth_session tables are
        // updated properly to reflect dead stateless sessions.
    }

    private String generateResetToken() {
        // Generate random token
        byte[] token = new byte[32];
        new java.security.SecureRandom().nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }


    private boolean isAccountLocked(String username) throws SQLException {
        String sql = "SELECT COUNT(*) as failed_count FROM login_attempts " +
                "WHERE username = ? AND status = 'FAILED' " +
                "AND attempt_time > DATEADD('MINUTE', -15, CURRENT_TIMESTAMP)";

        return dbManager.executeQuery(sql, rs -> {
            if (rs.next()) {
                return rs.getInt("failed_count") >= 5; // Lock after 5 failures in 15 min
            }
            return false;
        }, username);
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\AuthenticationResult.java
```java
package edu.advising.auth;

import edu.advising.users.User;

/**
 * AuthenticationResult - returned by all authentication attempts
 * Contains state and token for stateless tracking
 */
public class AuthenticationResult {
    private AuthenticationState state;
    private String authToken; // JWT or session token for stateless tracking
    private String message;
    private User user;

    public AuthenticationResult(AuthenticationState state, String message) {
        this.state = state;
        this.message = message;
    }

    public static AuthenticationResult failed(String message) {
        return new AuthenticationResult(AuthenticationState.FAILED, message);
    }

    public static AuthenticationResult awaitingTwoFactor(String authToken) {
        AuthenticationResult result = new AuthenticationResult(
                AuthenticationState.AWAITING_TWO_FACTOR,
                "2FA code required");
        result.authToken = authToken;
        return result;
    }

    public static AuthenticationResult success(User user) {
        AuthenticationResult result = new AuthenticationResult(
                AuthenticationState.FULLY_AUTHENTICATED,
                "Authentication successful");
        result.user = user;
        return result;
    }

    // Getters
    public AuthenticationState getState() { return state; }
    public String getAuthToken() { return authToken; }
    public String getMessage() { return message; }
    public User getUser() { return user; }
    public boolean isFullyAuthenticated() {
        return state == AuthenticationState.FULLY_AUTHENTICATED;
    }
    public boolean requiresTwoFactor() {
        return state == AuthenticationState.AWAITING_TWO_FACTOR;
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\AuthenticationState.java
```java
package edu.advising.auth;

/**
 * Authentication State - represents where user is in auth flow
 */
enum AuthenticationState {
    UNAUTHENTICATED,
    PASSWORD_VERIFIED,
    AWAITING_TWO_FACTOR,
    FULLY_AUTHENTICATED,
    FAILED
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\AuthenticationStrategy.java
```java
// Week 3: STRATEGY PATTERN
// Features Implemented: Multiple Authentication Methods, Change Password
// Why Now: Need flexible authentication system that can swap algorithms at runtime

package edu.advising.auth;

/**
 * AuthenticationStrategy - Strategy Interface
 * Defines the contract for all authentication algorithms
 */
public interface AuthenticationStrategy {
    /**
     * Initiate authentication - may return partial success if 2FA like algorithms needed
     */
    AuthenticationResult authenticate(String username, String password);
    /**
     * Continue authentication with additional factor(s)
     */
    AuthenticationResult continueAuthentication(String authToken, String credential);
    /**
     * Utility methods
     */
    String hashPassword(String password);
    boolean validatePasswordStrength(String password);
}


```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\BasicAuthentication.java
```java
package edu.advising.auth;

import edu.advising.common.ValidationResult;
import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.sql.SQLException;

/**
 * BasicAuthentication - Concrete Strategy
 * Simple username/password authentication (for development/testing)
 */
public class BasicAuthentication implements AuthenticationStrategy {
    private DatabaseManager dbManager;
    private UserFactory userFactory = new UserFactory();

    public BasicAuthentication() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public AuthenticationResult authenticate(String username, String password) {
        try {
            String sql = "SELECT password FROM users WHERE username = ?";
            return dbManager.executeQuery(sql, rs -> {
                if (rs.next() && rs.getString("password").equals(password)) {
                    User user = userFactory.getUserByUsername(username);
                    return AuthenticationResult.success(user);
                }
                return AuthenticationResult.failed("Invalid credentials");
            }, username);
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            return AuthenticationResult.failed("Authentication error");
        }
    }

    @Override
    public AuthenticationResult continueAuthentication(String authToken, String credential) {
        return AuthenticationResult.failed("Basic auth doesn't support continuation");
    }


    @Override
    public String hashPassword(String password) {
        // Basic strategy: no hashing (not secure, for demo only)
        return password;
    }

    @Override
    public boolean validatePasswordStrength(String password) {
        // Strong validation: length, uppercase, lowercase, digit, special char
        if (password == null) {
            return false;
        }
        try {
            ValidationResult vr = PasswordPolicyValidator.validateAgainstPolicy(password);
            return vr.isValid();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\PasswordPolicyValidator.java
```java
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
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\SecureAuthentication.java
```java
package edu.advising.auth;

import edu.advising.common.ValidationResult;
import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;

/**
 * SecureAuthentication - Concrete Strategy
 * SHA-256 hashed password authentication (production-ready)
 */
public class SecureAuthentication implements AuthenticationStrategy {
    private DatabaseManager dbManager;
    private UserFactory userFactory = new UserFactory();

    public SecureAuthentication() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public AuthenticationResult authenticate(String username, String password) {
        try {
            String sql = "SELECT password FROM users WHERE username = ?";
            return dbManager.executeQuery(sql, rs -> {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String inputHash = hashPassword(password);
                    if (storedHash.equals(inputHash)) {
                        User user = userFactory.getUserByUsername(username);
                        return AuthenticationResult.success(user);
                    }
                }
                return AuthenticationResult.failed("Invalid credentials");
            }, username);
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            return AuthenticationResult.failed("Authentication error");
        }
    }

    @Override
    public AuthenticationResult continueAuthentication(String authToken, String credential) {
        return AuthenticationResult.failed("Secure auth doesn't support continuation");
    }

    @Override
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    @Override
    public boolean validatePasswordStrength(String password) {
        // Strong validation: length, uppercase, lowercase, digit, special char
        if (password == null) {
            return false;
        }
        try {
            ValidationResult vr = PasswordPolicyValidator.validateAgainstPolicy(password);
            return vr.isValid();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\TwoFactorAuthentication.java
```java
package edu.advising.auth;

import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * TwoFactorAuthentication - Concrete Strategy
 * Two-factor authentication with temporary codes (simulated)
 */
public class TwoFactorAuthentication implements AuthenticationStrategy {
    private AuthenticationStrategy baseAuth;
    private DatabaseManager dbManager = DatabaseManager.getInstance();
    private UserFactory userFactory = new UserFactory();
    private static final int CODE_VALIDITY_MINUTES = 5; // 5 minutes

    public TwoFactorAuthentication(AuthenticationStrategy baseAuth) {
        this.baseAuth = baseAuth;
    }

    /**
     * Step 1: Authenticate with username/password, then send 2FA code
     */
    @Override
    public AuthenticationResult authenticate(String username, String password) {
        // First, validate with base authentication
        AuthenticationResult baseResult = baseAuth.authenticate(username, password);
        if (!baseResult.isFullyAuthenticated()) {
            return baseResult; // Password was wrong
        }
        // Let's get the user that just authenticated.
        User user = baseResult.getUser();

        try {
            // Generate auth token for stateless tracking
            String authToken = generateAuthToken();

            // Generate and store 2FA code
            String twoFactorCode = generateTwoFactorCode();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expires = now.plusMinutes(CODE_VALIDITY_MINUTES);
            // TODO: Update DB Manager to CREATE auth_sessions table, or use something like Redis
            // Store auth session in database
            String insertSession = "INSERT INTO auth_sessions (auth_token, user_id, state, expires_at) " +
                    "VALUES (?, ?, ?, ?)";
            //TODO: Get Enum label for AWAITING_TWO_FACTOR instead of this string.
            dbManager.executeUpdate(
                    insertSession, authToken, user.getId(), "AWAITING_TWO_FACTOR",
                    Timestamp.valueOf(expires));

            // Store 2FA code
            String sql = "INSERT INTO two_factor_codes (user_id, code, code_type, generated_at, expires_at) " +
                    "VALUES (?, ?, ?, ?, ?)";

            int codeId = dbManager.executeInsert(sql, user.getId(), twoFactorCode, "SMS",
                    Timestamp.valueOf(now), Timestamp.valueOf(expires));
            System.out.printf("Generated codeId: %d%n", codeId);

            // Notify the user of this code.
            sendTwoFactorCode(user, twoFactorCode); // SMS, email, etc.

            System.out.println("✓ 2FA code sent. Please verify with code");
            return AuthenticationResult.awaitingTwoFactor(authToken); // Not fully authenticated with this strategy.
        } catch (SQLException e) {
            System.err.println("✗ Error generating 2FA code: " + e.getMessage());
            return AuthenticationResult.failed("2FA setup error");
        }
    }

    /**
     * Step 2: Verify 2FA code using auth token
     */
    @Override
    public AuthenticationResult continueAuthentication(String authToken, String code) {
        try {
            // Retrieve session from database
            String sessionSql = "SELECT user_id, state FROM auth_sessions " +
                    "WHERE auth_token = ? AND expires_at > CURRENT_TIMESTAMP";
            return dbManager.executeQuery(sessionSql, sessionRs -> {
                if (!sessionRs.next()) {
                    return AuthenticationResult.failed("Invalid or expired auth token");
                }

                int userId = sessionRs.getInt("user_id");
                String state = sessionRs.getString("state");

                //TODO: Make this an Enum state comparison.
                if (!"AWAITING_TWO_FACTOR".equals(state)) {
                    return AuthenticationResult.failed("Invalid authentication state");
                }

                // Verify 2FA code
                String codeSql = "SELECT id FROM two_factor_codes " +
                        "WHERE user_id = ? AND code = ? AND is_used = FALSE " +
                        "AND expires_at > CURRENT_TIMESTAMP";
                return dbManager.executeQuery(codeSql, codeRs -> {
                    if (!codeRs.next()) {
                        // Increment failed attempts
                        incrementFailedAttempts(userId);
                        return AuthenticationResult.failed("Invalid or expired 2FA code");
                    }

                    int codeId = codeRs.getInt("id");

                    // Mark code as used
                    String markUsed = "UPDATE two_factor_codes SET is_used = TRUE, " +
                            "used_at = CURRENT_TIMESTAMP WHERE id = ?";
                    dbManager.executeUpdate(markUsed, codeId);

                    // Update session state
                    String updateSession = "UPDATE auth_sessions SET state = ? WHERE auth_token = ?";
                    dbManager.executeUpdate(updateSession, "FULLY_AUTHENTICATED", authToken);

                    // Get user and return success
                    User user = userFactory.getUserById(userId);
                    System.out.println("✓ 2FA verification successful");
                    return AuthenticationResult.success(user);

                }, userId, code);
            }, authToken);
        } catch (SQLException e) {
            System.err.println("Error verifying 2FA: " + e.getMessage());
            return AuthenticationResult.failed("2FA verification error");
        }
    }

    @Override
    public String hashPassword(String password) {
        return baseAuth.hashPassword(password);
    }

    @Override
    public boolean validatePasswordStrength(String password) {
        return baseAuth.validatePasswordStrength(password);
    }

    // Helper methods

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    private String generateTwoFactorCode() {
        return String.format("%06d", (int) (Math.random() * 900000) + 100000);
    }

    private void sendTwoFactorCode(User user, String code) {
        // In real system: twilioService.sendSMS(user.getPhone(), code);
        System.out.printf("📱 SMS sent to user %s with code: %s (valid for %d minutes)%n",
                user.getEmail(), code, CODE_VALIDITY_MINUTES);
    }

    private void incrementFailedAttempts(int userId) throws SQLException {
        String sql = "UPDATE two_factor_codes SET attempts = attempts + 1 " +
                "WHERE user_id = ? AND is_used = FALSE " +
                "ORDER BY generated_at DESC LIMIT 1";
        dbManager.executeUpdate(sql, userId);
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\BaseCommand.java
```java
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
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\Command.java
```java
package edu.advising.commands;

import java.time.LocalDateTime;

/**
 * Command - Interface for all command objects
 */
public interface Command {
    void execute();
    void undo();
    boolean isUndoable();
    String getDescription();
    LocalDateTime getExecutionTime();
    boolean wasSuccessful();
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\CommandExecutor.java
```java
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
// LIFECYCLE — One CommandExecutor per user session:
//
//   // When user logs in:
//   CommandExecutor executor = new CommandExecutor(loggedInUser.getId());
//   session.setCommandExecutor(executor);
//
//   // Store on the session so any screen can retrieve it:
//   session.getCommandExecutor().execute(new RegisterCommand(student, section));
//
// ─────────────────────────────────────────────────────────────────────────────
// GUI BUTTON WIRING (Swing example, works the same for JavaFX/Web):
//
//   // "Register" button
//   registerButton.addActionListener(e -> {
//       Section selected = sectionTable.getSelectedSection();
//       executor.execute(new RegisterCommand(student, selected));
//       undoButton.setEnabled(executor.canUndo());
//       redoButton.setEnabled(executor.canRedo());
//       refreshScheduleView();
//   });
//
//   // "Undo" button (always in the toolbar)
//   undoButton.addActionListener(e -> {
//       undoButton.setToolTipText("Undo: " + executor.peekUndoDescription());
//       executor.undo();
//       undoButton.setEnabled(executor.canUndo());
//       redoButton.setEnabled(executor.canRedo());
//       refreshScheduleView();
//   });
//
//   // "Redo" button
//   redoButton.addActionListener(e -> {
//       executor.redo();
//       undoButton.setEnabled(executor.canUndo());
//       redoButton.setEnabled(executor.canRedo());
//       refreshScheduleView();
//   });
//
// ─────────────────────────────────────────────────────────────────────────────
// OPEN/CLOSED PRINCIPLE:
//   Adding a new user action (e.g. Week 8's TranscriptRequestCommand) requires
//   ONLY creating a new BaseCommand subclass. CommandExecutor never changes.
//   This is the real power of the Command Pattern — the invoker is sealed.
//
// ============================================================================

import java.util.List;

public class CommandExecutor {

    private final CommandHistory history;

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------

    /**
     * Create an executor for a specific user session.
     * @param userId The logged-in user's numeric primary key.
     */
    public CommandExecutor(int userId) {
        this.history = new CommandHistory(userId);
    }

    /**
     * Convenience constructor when you already have a CommandHistory instance
     * (e.g. for testing with a mock history).
     */
    public CommandExecutor(CommandHistory history) {
        this.history = history;
    }

    // -------------------------------------------------------------------------
    // Command Execution — the primary API for the UI layer
    // -------------------------------------------------------------------------

    /**
     * Execute a command and record it in history.
     *
     * This is the ONLY method the UI/Service layer should call to trigger
     * business logic. The caller creates the appropriate Command object,
     * passes it here, and then queries wasSuccessful() on the command
     * (or canUndo() on the executor) to update the UI state.
     *
     * Example:
     *   RegisterCommand cmd = new RegisterCommand(student, section);
     *   executor.execute(cmd);
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

    /**
     * @return true if the Undo button should be enabled.
     *
     * GUI Usage:
     *   undoButton.setEnabled(executor.canUndo());
     *   undoButton.setToolTipText("Undo: " + executor.peekUndoDescription());
     */
    public boolean canUndo() {
        return history.canUndo();
    }

    /**
     * @return true if the Redo button should be enabled.
     */
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

    /**
     * Human-readable label for the next action that would be redone.
     */
    public String peekRedoDescription() {
        return history.peekRedoDescription();
    }

    // -------------------------------------------------------------------------
    // History Access
    // -------------------------------------------------------------------------

    /**
     * Returns the live in-session undo stack (most recent first).
     * Useful for a "Recent Actions" panel that lists what can currently be undone.
     *
     * GUI Usage:
     *   List<BaseCommand> recent = executor.getSessionHistory();
     *   recentActionsPanel.populate(recent);
     */
    public List<BaseCommand> getSessionHistory() {
        return history.getUndoStack();
    }

    /**
     * Load full audit history from the database for the current user.
     * Unlike getSessionHistory(), this survives session boundaries and
     * returns ALL historical records up to `limit`.
     *
     * GUI Usage (Transaction History screen):
     *   List<CommandRecord> records = executor.getAuditHistory(50);
     *   transactionTable.setModel(new CommandRecordTableModel(records));
     *
     * @param limit Maximum records to return (most recent first).
     */
    public List<CommandRecord> getAuditHistory(int limit) {
        return history.getAuditHistory(limit);
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\CommandHistory.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - Command History (Invoker's Memory)
// ============================================================================
//
// PATTERN ROLE: This class is the "Invoker's memory" in the Command Pattern.
//   The Invoker (CommandExecutor) delegates every execute/undo/redo call here.
//   CommandHistory owns the undo and redo stacks and knows how to persist
//   commands to the `command_history` table via the ORM.
//
// HOW UNDO/REDO STACKS WORK:
//
//   START:    undoStack=[]        redoStack=[]
//
//   User registers for CIS-12:
//             undoStack=[REG]     redoStack=[]
//
//   User registers for MATH-10:
//             undoStack=[MATH,REG] redoStack=[]
//
//   User clicks Undo (MATH-10):
//             undoStack=[REG]     redoStack=[MATH]
//
//   User clicks Redo (MATH-10):
//             undoStack=[MATH,REG] redoStack=[]
//
//   User takes a NEW action (drops CIS-12) — redo chain breaks:
//             undoStack=[DROP,MATH,REG] redoStack=[]   (MATH redo is gone)
//
// PERSISTENCE:
//   Each command is inserted into `command_history` on execute.
//   On undo the row is updated (is_undone=TRUE, undone_at=now).
//   This gives faculty/admins a full audit trail even if the user
//   navigates away, and lets analysts see exactly what happened.
//
// GUI INTEGRATION:
//   After any execute/undo/redo call, check canUndo()/canRedo() to decide
//   whether the toolbar Undo and Redo buttons should be enabled:
//
//     executor.execute(new RegisterCommand(student, section));
//     undoButton.setEnabled(executor.canUndo());   // Swing example
//     redoButton.setEnabled(executor.canRedo());
//
// ============================================================================

import edu.advising.core.DatabaseManager;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CommandHistory {

    // In-memory stacks scoped to the current user session.
    // ArrayDeque is used as a LIFO stack: push() adds to front, pop() removes from front.
    private final Deque<BaseCommand> undoStack;
    private final Deque<BaseCommand> redoStack;

    private final int userId;
    private final int maxStackSize;           // Keeps memory bounded
    private final DatabaseManager dbManager;

    /** Default: keep up to 20 actions in the live undo stack. */
    private static final int DEFAULT_MAX_SIZE = 20;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public CommandHistory(int userId) {
        this(userId, DEFAULT_MAX_SIZE);
    }

    public CommandHistory(int userId, int maxStackSize) {
        this.userId       = userId;
        this.maxStackSize = maxStackSize;
        this.undoStack    = new ArrayDeque<>();
        this.redoStack    = new ArrayDeque<>();
        this.dbManager    = DatabaseManager.getInstance();
    }

    // -------------------------------------------------------------------------
    // Core Execute / Undo / Redo
    // -------------------------------------------------------------------------

    /**
     * Execute a command and record it in history.
     *
     * Called by CommandExecutor — not directly by the UI.
     * After execution:
     *   - Successful commands go onto the undo stack and are persisted to DB.
     *   - Any pending redo stack is cleared (new action breaks redo chain).
     *   - Failed commands are persisted for audit purposes but NOT pushed
     *     onto the undo stack (nothing to undo if nothing happened).
     */
    public void executeCommand(BaseCommand command) {
        command.setUserId(userId);
        command.execute();

        // Always persist — even failures go into the audit log.
        persistNewCommand(command);

        if (command.wasSuccessful()) {
            // Enforce cap: evict the oldest entry before pushing new one.
            if (undoStack.size() >= maxStackSize) {
                undoStack.pollLast(); // remove oldest (back of deque)
            }
            undoStack.push(command);

            // A new action breaks the forward timeline — redo is no longer valid.
            redoStack.clear();
        }
    }

    /**
     * Undo the most recently executed undoable command.
     *
     * @return true if undo succeeded, false if nothing to undo or not undoable.
     */
    public boolean undo() {
        if (undoStack.isEmpty()) {
            System.out.println("↶ Nothing to undo.");
            return false;
        }

        BaseCommand command = undoStack.peek();

        if (!command.isUndoable()) {
            System.out.println("↶ Command cannot be undone: " + command.getDescription());
            return false;
        }

        undoStack.pop();
        command.undo();

        // Update the persisted record to mark it as reversed.
        markCommandUndone(command);

        // The undone command is pushed onto the redo stack so it can be re-applied.
        redoStack.push(command);
        return true;
    }

    /**
     * Redo the most recently undone command.
     *
     * @return true if redo succeeded, false if nothing to redo.
     */
    public boolean redo() {
        if (redoStack.isEmpty()) {
            System.out.println("↷ Nothing to redo.");
            return false;
        }

        BaseCommand command = redoStack.pop();

        // Re-run the command's execute logic from scratch.
        // The command's own execute() validates pre-conditions (capacity, conflicts, etc.)
        // so it's safe to call again — it won't blindly re-do something invalid.
        command.execute();

        if (command.wasSuccessful()) {
            command.setUndone(false);
            command.setUndoneAt(null);
            markCommandRedone(command);
            undoStack.push(command);
        } else {
            // Redo failed (e.g. section is now full); discard rather than loop.
            System.out.println("↷ Redo failed: " + command.getDescription());
        }

        return command.wasSuccessful();
    }

    // -------------------------------------------------------------------------
    // State Queries — used by GUI to enable/disable Undo/Redo buttons
    // -------------------------------------------------------------------------

    /** @return true if there is at least one undoable command in history. */
    public boolean canUndo() {
        return !undoStack.isEmpty() && undoStack.peek().isUndoable();
    }

    /** @return true if there is at least one redoable command in history. */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /** @return description of the next command that would be undone, or null. */
    public String peekUndoDescription() {
        BaseCommand top = undoStack.peek();
        return top != null ? top.getDescription() : null;
    }

    /** @return description of the next command that would be redone, or null. */
    public String peekRedoDescription() {
        BaseCommand top = redoStack.peek();
        return top != null ? top.getDescription() : null;
    }

    /**
     * Returns an ordered snapshot of the in-memory undo stack (most recent first).
     * Useful for showing a "Recent Actions" panel in the UI.
     */
    public List<BaseCommand> getUndoStack() {
        return new ArrayList<>(undoStack);
    }

    // -------------------------------------------------------------------------
    // Audit Trail — load historical records from DB
    // -------------------------------------------------------------------------

    /**
     * Loads a page of past commands from the `command_history` table.
     *
     * WHY THIS IS SEPARATE FROM getUndoStack():
     *   The in-memory stack only holds the current session's actions, bounded by
     *   maxStackSize. The database holds every action ever taken by this user.
     *   This method powers audit dashboards, "My Transaction History" screens,
     *   and admin review panels.
     *
     * GUI INTEGRATION:
     *   List<CommandRecord> history = commandHistory.getAuditHistory(50);
     *   // Bind history to a JTable model or a RecyclerView adapter.
     *
     * @param limit Max number of records to return (most recent first).
     */
    public List<CommandRecord> getAuditHistory(int limit) {
        String sql = "SELECT id, command_type, command_data, executed_at, " +
                "undone_at, is_undone, success, error_message " +
                "FROM command_history WHERE user_id = ? " +
                "ORDER BY executed_at DESC LIMIT ?";
        try {
            return dbManager.fetchList(sql, rs -> new CommandRecord(
                    rs.getInt("id"),
                    rs.getString("command_type"),
                    rs.getString("command_data"),
                    rs.getTimestamp("executed_at") != null
                            ? rs.getTimestamp("executed_at").toLocalDateTime() : null,
                    rs.getTimestamp("undone_at") != null
                            ? rs.getTimestamp("undone_at").toLocalDateTime() : null,
                    rs.getBoolean("is_undone"),
                    rs.getBoolean("success"),
                    rs.getString("error_message")
            ), userId, limit);
        } catch (SQLException e) {
            System.err.println("CommandHistory: failed to load audit history - " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // -------------------------------------------------------------------------
    // Private DB Helpers
    // -------------------------------------------------------------------------

    /**
     * Insert a new command record into command_history.
     * Uses ORM upsert via BaseCommand's @Table / @Column annotations.
     * Falls back to manual SQL if ORM fails (defensive coding).
     */
    private void persistNewCommand(BaseCommand command) {
        try {
            // prepareForStorage() calls serializeCommandData() on the concrete subclass,
            // storing the JSON payload into command.commandData before we persist.
            command.prepareForStorage();
            dbManager.upsert(command);
        } catch (SQLException | IllegalAccessException e) {
            System.err.println("CommandHistory: ORM upsert failed, trying fallback SQL — " + e.getMessage());
            persistCommandFallback(command);
        }
    }

    /**
     * Fallback insertion when ORM upsert cannot be used (e.g. subclass not directly annotated).
     */
    private void persistCommandFallback(BaseCommand command) {
        String sql = "INSERT INTO command_history " +
                "(user_id, command_type, command_data, executed_at, is_undone, success, error_message) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            int generatedId = dbManager.executeInsert(sql,
                    command.getUserId(),
                    command.getCommandType(),
                    command.getCommandData(),
                    command.getExecutionTime() != null
                            ? Timestamp.valueOf(command.getExecutionTime()) : null,
                    false,
                    command.wasSuccessful(),
                    command.getErrorMessage());

            // Write the generated DB id back onto the command so markCommandUndone()
            // can find the correct row by id later.
            if (generatedId > 0) command.setId(generatedId);

        } catch (SQLException ex) {
            System.err.println("CommandHistory: fallback persist also failed — " + ex.getMessage());
        }
    }

    /**
     * Update an existing command_history row to mark the command as undone.
     */
    private void markCommandUndone(BaseCommand command) {
        if (command.getId() <= 0) return;  // Row was never persisted — skip.
        String sql = "UPDATE command_history " +
                "SET is_undone = TRUE, undone_at = CURRENT_TIMESTAMP " +
                "WHERE id = ?";
        try {
            dbManager.executeUpdate(sql, command.getId());
            command.setUndone(true);
            command.setUndoneAt(LocalDateTime.now());
        } catch (SQLException e) {
            System.err.println("CommandHistory: failed to mark command undone — " + e.getMessage());
        }
    }

    /**
     * Update a command_history row to reflect that a previously-undone command was redone.
     */
    private void markCommandRedone(BaseCommand command) {
        if (command.getId() <= 0) return;
        String sql = "UPDATE command_history " +
                "SET is_undone = FALSE, undone_at = NULL " +
                "WHERE id = ?";
        try {
            dbManager.executeUpdate(sql, command.getId());
        } catch (SQLException e) {
            System.err.println("CommandHistory: failed to mark command redone — " + e.getMessage());
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\CommandRecord.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - Command Record (Audit/History DTO)
// ============================================================================
// WHY THIS CLASS EXISTS:
//   When we load command history from the database for display (audit trails,
//   "My Transaction History", admin dashboards), we only need the metadata
//   about each command — not a fully reconstructed, executable command object.
//
//   Trying to reconstruct a full RegisterCommand or PaymentCommand from the DB
//   just to display a list would require re-fetching Student, Section, and other
//   objects unnecessarily. Instead, we load this lightweight DTO.
//
// GUI INTEGRATION NOTE:
//   A "Transaction History" screen would call:
//       CommandExecutor executor = session.getCommandExecutor();
//       List<CommandRecord> history = executor.getHistory(20);
//   Then bind the list to a JTable or ListView. Each row shows:
//       - What was done (commandType)
//       - When it happened (executedAt)
//       - Whether it succeeded (success)
//       - Whether it was reversed (undone)
// ============================================================================

import java.time.LocalDateTime;

public class CommandRecord {
    private final int id;
    private final String commandType;
    private final String commandData;   // Raw JSON payload for debugging
    private final LocalDateTime executedAt;
    private final LocalDateTime undoneAt;
    private final boolean undone;
    private final boolean success;
    private final String errorMessage;

    public CommandRecord(int id, String commandType, String commandData,
                  LocalDateTime executedAt, LocalDateTime undoneAt,
                  boolean undone, boolean success, String errorMessage) {
        this.id           = id;
        this.commandType  = commandType;
        this.commandData  = commandData;
        this.executedAt   = executedAt;
        this.undoneAt     = undoneAt;
        this.undone       = undone;
        this.success      = success;
        this.errorMessage = errorMessage;
    }

    // -------------------------------------------------------------------------
    // Getters — read-only, this is a value object
    // -------------------------------------------------------------------------

    public int getId()                  { return id; }
    public String getCommandType()      { return commandType; }
    public String getCommandData()      { return commandData; }
    public LocalDateTime getExecutedAt(){ return executedAt; }
    public LocalDateTime getUndoneAt()  { return undoneAt; }
    public boolean isUndone()           { return undone; }
    public boolean isSuccess()          { return success; }
    public String getErrorMessage()     { return errorMessage; }

    /** Human-readable status badge for display in a UI table cell. */
    public String getStatusLabel() {
        if (!success)  return "✗ Failed";
        if (undone)    return "↶ Reversed";
        return "✓ Completed";
    }

    @Override
    public String toString() {
        return String.format("[%s] %-20s %s  %s",
                executedAt, commandType, getStatusLabel(),
                errorMessage != null ? "| " + errorMessage : "");
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\Course.java
```java
package edu.advising.commands;

import edu.advising.core.*;

import java.sql.SQLException;
import java.util.List;

/**
 * ADD ANNOTATIONS during Command Pattern Week
 * -
 * Course Section - Represents a course section
 */
@Table(name = "courses")
public class Course {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "credits")
    private double credits;
    @Column(name = "department_id", foreignKey = true)
    private int departmentId;
    @Column(name = "level")
    private String level;
    @Column(name = "is_active")
    private boolean isActive;
    @OneToMany(targetEntity = Section.class, mappedBy = "course_id")
    private List<Section> sections; // Cached list of available sections.
    @ManyToOne(targetEntity = Department.class, joinColumn = "department_id")
    private Department department;

    public Course() {}

    public Course(String code, String name, String description, int credits, int departmentId, String level) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.departmentId = departmentId;
        this.level = level;
        this.isActive = true;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Section> getSections() throws SQLException {
        if (this.sections == null) {
            // Lazy Load: Use the generic fetchMany from DatabaseManager
            this.sections = DatabaseManager.getInstance()
                    .fetchMany(Section.class, "course_id", this.id);
        }
        return this.sections;
    }

    protected void ensureId() throws SQLException, IllegalAccessException {
        if(this.getId() == 0) {
            // If the id is not set, we need to save this object to get an id to set on the list items.
            DatabaseManager.getInstance().upsert(this);
        }
    }

    public void setSections(List<Section> sections) throws SQLException, IllegalAccessException {
        ensureId();
        // Now, let's add this object's id to the related list items foreign key id
        for(Section s : sections) { s.setCourseId(this.getId()); }
        // Now let's upsertAll of these list items (i.e. a batch) and set as this object's related field.
        DatabaseManager.getInstance().upsertAll(sections);
        this.sections = sections;
    }

    public Department getDepartment() throws SQLException {
        if (this.department == null) {
            // Lazy Load: Use the generic fetchOne from DatabaseManager
            this.department = DatabaseManager.getInstance()
                    .fetchOne(Department.class, "id", this.departmentId);
        }
        return (this.department != null) ? this.department : null;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\Department.java
```java
package edu.advising.commands;

import edu.advising.core.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "departments")
public class Department {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "chair_id")  // References User/Faculty id
    private int chairId;
    @Column(name = "budget")
    private double budget;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(targetEntity = Course.class, mappedBy = "department_id")
    private List<Course> courses; // Cached list of available courses.

    public Department() {}

    private Department(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Department(String code, String name) {
        this(0, code, name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChairId() {
        return chairId;
    }

    public void setChairId(int chairId) {
        this.chairId = chairId;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Course> getCourses() throws SQLException {
        if (this.courses == null) {
            // Lazy Load: Use the generic fetchMany from DatabaseManager
            this.courses = DatabaseManager.getInstance()
                    .fetchMany(Course.class, "department_id", this.id);
        }
        return this.courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\DropCommand.java
```java
package edu.advising.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * DropCommand - Drop a course section
 */
@Table(name = "command_history", isSubTable = true)
public class DropCommand extends BaseCommand {
    private ObservableStudent student;
    private Section section;
    private int previousEnrollmentId;
    private DatabaseManager dbManager;

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public DropCommand() {
        this(null, null);
    }

    public DropCommand(ObservableStudent student, Section section) {
        super();
        this.commandType = "DROP";
        this.student = student;
        this.section = section;
        this.dbManager = DatabaseManager.getInstance();
    }

    public static DropCommand fromSuperType(BaseCommand base) {
        DropCommand cmd = new DropCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        if (section.drop(student)) {
            // Update database
            updateEnrollmentStatus("DROPPED");

            executed = true;
            successful = true;

            System.out.printf("✓ Student %s dropped %s%n",
                    student.getStudentId(), section.getCourseCode());

            // Check waitlist and promote next student
            try {
                promoteFromWaitlist();
            } catch (SQLException | IllegalAccessException e) {
                e.printStackTrace();
                System.out.println("Failed to promote from waitlist.");
            }
        } else {
            successful   = false;
            errorMessage = String.format("Drop failed — student not enrolled in %s",
                    section.getCourseCode());
            System.out.println("✗ " + errorMessage);
        }
    }

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo - command not executed or failed");
            return;
        }

        // Re-enroll
        if (section.enroll(student) > 0) {
            updateEnrollmentStatus("ENROLLED");
            System.out.printf("↶ Undone: Drop of %s - student re-enrolled%n",
                    section.getCourseCode());
            this.undoneAt = LocalDateTime.now();
            this.isUndone = true;
        }
    }

    @Override
    public boolean isUndoable() {
        return executed && successful && section.hasCapacity();
    }

    @Override
    public String getDescription() {
        return String.format("Drop %s (%s)", section.getCourseCode(), section.getCourseName());
    }

    private void updateEnrollmentStatus(String status) {
        // Section.drop() already updates the enrollment via ORM upsert.
        // This method exists as a safety net for direct DropCommand use outside Section.
        try {
            String sql = "UPDATE enrollments SET status = ? " +
                    "WHERE student_id = ? AND section_id = ? AND status = 'ENROLLED'";
            dbManager.executeUpdate(sql, status, student.getId(), section.getId());
        } catch (SQLException e) {
            System.err.println("DropCommand: enrollment status sync failed — " + e.getMessage());
        }
    }

    private void promoteFromWaitlist() throws SQLException, IllegalAccessException {
        if (!section.getWaitlist().isEmpty() && section.hasCapacity()) {
            // Get the next waitlist entry
            WaitlistEntry nextWaitlistEntry = section.getWaitlist().get(0);
            // Lookup the student for this entry
            Student student = nextWaitlistEntry.getStudent();
            // Remove that student from the waitlist
            section.removeFromWaitlist(student);
            section.enroll(student);
            System.out.println(String.format("↑ Student ID %s promoted from waitlist", student.getStudentId()));

            // In real implementation, notify the student with observer!!!
        }
    }

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk",            student.getId());   // int PK
        data.put("studentId", student.getStudentId());
        data.put("sectionId", section.getId()); // Assuming Section has an id
        data.put("previousEnrollmentId", previousEnrollmentId);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize DropCommand data", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            int studentPk = (int) data.get("studentPk");
            int sectionId = (int) data.get("sectionId");
            this.previousEnrollmentId = (int) data.get("previousEnrollmentId");

            Student raw  = DatabaseManager.getInstance().fetchOne(Student.class, "id", studentPk);
            this.student = ObservableStudent.fromSuperType(raw);
            this.section = DatabaseManager.getInstance().fetchOne(Section.class, "id", sectionId);
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("Failed to deserialize DropCommand data", e);
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\Enrollment.java
```java
package edu.advising.commands;

import edu.advising.core.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Course Section Enrollment - Represents an enrollment in a course section
 */
@Table(name = "enrollments")
public class Enrollment {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "student_id")
    private int studentId; // References sudents
    @Id
    @Column(name = "section_id") // References sections
    private int sectionId;
    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;
    @Column(name = "status")
    private String status; // ENROLLED, DROPPED, WITHDRAWN, COMPLETED
    @Column(name = "grade")
    private String grade;
    @Column(name = "grade_points")
    private BigDecimal gradePoints;
    @Column(name = "midterm_grade")
    private String midtermGrade;
    @Column(name = "final_grade")
    private String finalGrade;
    @Column(name = "graded_at")
    private LocalDateTime gradedAt;
    @Column(name = "dropped_at")
    private LocalDateTime droppedAt;
    @Column(name = "drop_reason")
    private String dropReason;
    @ManyToOne(targetEntity = Section.class, joinColumn = "section_id")
    private Section section; // Cached object representing this enrollment's course section.

    public Enrollment() {}

    public Enrollment(int studentId, int sectionId) {
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.status = "ENROLLED";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public BigDecimal getGradePoints() {
        return gradePoints;
    }

    public void setGradePoints(BigDecimal gradePoints) {
        this.gradePoints = gradePoints;
    }

    public String getMidtermGrade() {
        return midtermGrade;
    }

    public void setMidtermGrade(String midtermGrade) {
        this.midtermGrade = midtermGrade;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public LocalDateTime getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }

    public LocalDateTime getDroppedAt() {
        return droppedAt;
    }

    public void setDroppedAt(LocalDateTime droppedAt) {
        this.droppedAt = droppedAt;
    }

    public String getDropReason() {
        return dropReason;
    }

    public void setDropReason(String dropReason) {
        this.dropReason = dropReason;
    }

    public Section getSection() throws SQLException {
        if (this.section == null) {
            // Lazy Load: Use the generic fetchOne from DatabaseManager
            this.section = DatabaseManager.getInstance()
                    .fetchOne(Section.class, "section_id", this.sectionId);
        }
        return (this.section != null) ? this.section : null;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\FacultyDropCommand.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - FacultyDropCommand (Concrete Command)
// ============================================================================
//
// FEATURE:  Faculty Information → Faculty Drop/Census Roster
//           (Faculty administratively drops a student from their section)
//
// WHY THIS IS A SEPARATE COMMAND FROM DropCommand:
//   A student dropping themselves (DropCommand) and a faculty member
//   administratively dropping a student are conceptually different:
//     1. AUTHORIZATION: Faculty drops need a different permission check
//        (faculty must own the section). Week 7 Decorator will wrap this.
//     2. REASON CODE: Faculty drops require a documented reason
//        (no-show, census, academic) logged in the enrollment record.
//     3. NOTIFICATION: The student must be notified that they were dropped
//        by faculty — this is a different notification type and message.
//     4. AUDIT: Faculty drops are surfaced in admin reports separately from
//        student self-drops. The command_type "FACULTY_DROP" makes queries easy.
//     5. UNDO POLICY: Faculty may want to reinstate a student within the
//        census period — undo re-enrolls the student.
//
// GUI INTEGRATION:
//   // On faculty class roster → right-click → "Administrative Drop":
//   Student selectedStudent = rosterTable.getSelectedStudent();
//   String reason = reasonDialog.getSelectedReason(); // "NO_SHOW", "CENSUS", etc.
//   FacultyDropCommand cmd = new FacultyDropCommand(faculty, student, section, reason);
//   executor.execute(cmd);
//
//   if (cmd.wasSuccessful()) {
//       showConfirmation("Student dropped. They have been notified.");
//       rosterTable.removeStudent(selectedStudent);
//   }
//
// ============================================================================

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Faculty;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FacultyDropCommand extends BaseCommand {

    // ── State needed for execute and undo ────────────────────────────────────

    private final Faculty faculty;
    private ObservableStudent student;
    private Section section;
    private final String dropReason;  // NO_SHOW, CENSUS, ACADEMIC_INTEGRITY, OTHER

    // Captured during execute() for use in undo() and serialization
    private int droppedEnrollmentId;

    private final NotificationManager notificationManager;
    private final DatabaseManager     dbManager;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * @param faculty    The faculty member performing the drop.
     * @param student    The student being dropped.
     * @param section    The course section they are being dropped from.
     * @param dropReason Documented reason for the administrative drop.
     */
    public FacultyDropCommand(Faculty faculty, ObservableStudent student,
                              Section section, String dropReason) {
        super();
        this.commandType         = "FACULTY_DROP";
        this.faculty             = faculty;
        this.student             = student;
        this.section             = section;
        this.dropReason          = (dropReason != null) ? dropReason : "UNSPECIFIED";
        this.notificationManager = NotificationManager.getInstance();
        this.dbManager           = DatabaseManager.getInstance();
    }

    // -------------------------------------------------------------------------
    // Command Interface — execute()
    // -------------------------------------------------------------------------

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        // ── Authorization check: faculty must own this section ───────────────
        // NOTE: In Week 7 (Decorator Pattern), this check will be handled by
        // FacultyPermissions.canDrop(section). For now, we check inline.
        if (section.getFacultyId() != faculty.getId()) {
            successful   = false;
            errorMessage = String.format("Faculty %s does not own section %s.",
                    faculty.getFullName(), section.getCourseCode());
            System.out.println("✗ " + errorMessage);
            return;
        }

        // ── Capture the enrollment ID before dropping, for undo purposes ─────
        try {
            Optional<Enrollment> enrollment = section.getEnrollments().stream()
                    .filter(e -> e.getStudentId() == student.getId()
                            && "ENROLLED".equals(e.getStatus()))
                    .findFirst();

            if (enrollment.isEmpty()) {
                successful   = false;
                errorMessage = String.format("Student %s is not enrolled in %s.",
                        student.getStudentId(), section.getCourseCode());
                System.out.println("✗ " + errorMessage);
                return;
            }

            droppedEnrollmentId = enrollment.get().getId();

            // ── Update enrollment record with drop details ───────────────────
            Enrollment e = enrollment.get();
            e.setStatus("DROPPED");
            e.setDroppedAt(LocalDateTime.now());
            e.setDropReason(dropReason + " (Faculty: " + faculty.getFullName() + ")");
            dbManager.upsert(e);

            // ── Update section's enrolled count ──────────────────────────────
            // section.drop() handles the in-memory list and upserts the section.
            section.drop(student);

            executed   = true;
            successful = true;

            System.out.printf("✓ Faculty drop: %s dropped %s from %s (Reason: %s)%n",
                    faculty.getFullName(), student.getFullName(),
                    section.getCourseCode(), dropReason);

            // ── Notify the student they were administratively dropped ─────────
            // This is a high-priority notification — student needs to know ASAP.
            notificationManager.notifyRegistration(student, section.getCourseCode(), false);
            // TODO Week 4 enhancement: add a faculty-drop-specific notification type
            //   that includes the reason code, so the student can respond if needed.

            // ── Check if anyone on the waitlist should be promoted ────────────
            promoteFromWaitlistIfAvailable();

        } catch (SQLException | IllegalAccessException e) {
            successful   = false;
            errorMessage = "Faculty drop failed: " + e.getMessage();
            System.err.println("✗ " + errorMessage);
        }
    }

    // -------------------------------------------------------------------------
    // Command Interface — undo()
    // -------------------------------------------------------------------------

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo — drop was not completed.");
            return;
        }

        // Re-enroll the student in the section (reverses the drop).
        if (section.hasCapacity()) {
            int newEnrollmentId = section.enroll(student);
            if (newEnrollmentId > 0) {
                undoneAt = LocalDateTime.now();
                isUndone = true;
                System.out.printf("↶ Undone: %s re-enrolled in %s%n",
                        student.getFullName(), section.getCourseCode());
                notificationManager.notifyRegistration(student, section.getCourseCode(), true);
            } else {
                System.out.println("✗ Undo failed — could not re-enroll student.");
            }
        } else {
            System.out.printf("✗ Cannot undo — %s is now full.%n", section.getCourseCode());
        }
    }

    @Override
    public boolean isUndoable() {
        // Can only reinstate if the section still has capacity.
        return executed && successful && section.hasCapacity();
    }

    @Override
    public String getDescription() {
        return String.format("Faculty drop: %s from %s (Reason: %s)",
                student.getFullName(), section.getCourseCode(), dropReason);
    }

    // -------------------------------------------------------------------------
    // Serialization
    // -------------------------------------------------------------------------

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("facultyId",           faculty.getId());
        data.put("studentPk",           student.getId());
        data.put("sectionId",           section.getId());
        data.put("dropReason",          dropReason);
        data.put("droppedEnrollmentId", droppedEnrollmentId);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("FacultyDropCommand: serialization failed", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            int studentPk = (int) data.get("studentPk");
            int sectionId = (int) data.get("sectionId");

            Student raw = dbManager.fetchOne(Student.class, "id", studentPk);
            if (raw != null) this.student = ObservableStudent.fromSuperType(raw);
            this.section             = dbManager.fetchOne(Section.class, "id", sectionId);
            this.droppedEnrollmentId = (int) data.get("droppedEnrollmentId");

        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("FacultyDropCommand: deserialization failed", e);
        }
    }

    // -------------------------------------------------------------------------
    // Private Helper
    // -------------------------------------------------------------------------

    /**
     * After a drop frees a seat, promote the next eligible waitlist student.
     * Mirrors the logic in DropCommand.promoteFromWaitlist() but also notifies
     * the promoted student via the Observer system.
     */
    private void promoteFromWaitlistIfAvailable() {
        try {
            if (!section.getWaitlist().isEmpty() && section.hasCapacity()) {
                WaitlistEntry next = section.getWaitlist().get(0);
                Student waitlisted = next.getStudent();
                section.removeFromWaitlist(waitlisted);
                int newEnrollmentId = section.enroll(waitlisted);
                if (newEnrollmentId > 0) {
                    System.out.printf("↑ %s promoted from waitlist into %s%n",
                            waitlisted.getFullName(), section.getCourseCode());
                    // Notify the promoted student via the Observer chain.
                    // TODO: wrap waitlisted student in ObservableStudent before notifying
                    notificationManager.notifyRegistration(
                            ObservableStudent.fromSuperType((Student) waitlisted),
                            section.getCourseCode(), true);
                }
            }
        } catch (SQLException e) {
            System.err.println("FacultyDropCommand: waitlist promotion failed — " + e.getMessage());
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\GrantWaitlistPermissionCommand.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - GrantWaitlistPermissionCommand (Concrete Command)
// ============================================================================
//
// FEATURE:  Faculty Information → Permission to Add Waitlisted Students
//           (Faculty explicitly grants a waitlisted student permission to enroll
//            in a section that is at or over capacity)
//
// REAL-WORLD CONTEXT (WebAdvisor):
//   Some sections have "permission-required" flags or a faculty override flow.
//   A student on the waitlist can see they are position #1 and contact the
//   instructor. The instructor reviews their situation and grants permission.
//   That permission shows up for the student as a one-time-use enrollment token.
//
// WHY COMMAND PATTERN HERE:
//   1. REVERSIBLE: Revoking a permission grant before the student acts on it
//      should be an undo, not a separate "revoke" flow.
//   2. LOGGED: Faculty overrides must be audited ("Prof. Smith over-enrolled CS101").
//   3. TIME-BOUNDED: The permission expires if unused — the command record
//      stores the expiry and can be queried by the registration flow.
//   4. FUTURE PIPELINE HOOK: Week 14's registration pipeline will check for
//      a valid PermissionGrant before allowing enrollment in a full section.
//
// GUI INTEGRATION:
//   // Faculty roster → select waitlisted student → "Grant Permission" button:
//   GrantWaitlistPermissionCommand cmd =
//       new GrantWaitlistPermissionCommand(faculty, student, section, "Student has prerequisite waiver");
//   executor.execute(cmd);
//
//   if (cmd.wasSuccessful()) {
//       showInfo(student.getFullName() + " can now enroll within 48 hours.");
//   }
//
//   // Student portal checks permissions when attempting to register:
//   boolean canOverride = PermissionGrant.hasActiveGrant(student.getId(), section.getId());
//
// ============================================================================

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.Column;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Id;
import edu.advising.core.Table;
import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Faculty;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class GrantWaitlistPermissionCommand extends BaseCommand {

    // ── State ────────────────────────────────────────────────────────────────

    private final Faculty faculty;
    private ObservableStudent student;
    private Section section;
    private final String notes;         // Optional faculty note ("prerequisite waived")
    private final int validForHours;    // How long the permission is active

    // Populated during execute() — needed for undo and student-facing display
    private int grantId;                // PK of the permission_grants row

    private final NotificationManager notificationManager;
    private final DatabaseManager     dbManager;

    // Default permission window: 48 hours before it expires unused
    private static final int DEFAULT_VALID_HOURS = 48;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public GrantWaitlistPermissionCommand(Faculty faculty, ObservableStudent student,
                                          Section section, String notes) {
        this(faculty, student, section, notes, DEFAULT_VALID_HOURS);
    }

    public GrantWaitlistPermissionCommand(Faculty faculty, ObservableStudent student,
                                          Section section, String notes, int validForHours) {
        super();
        this.commandType      = "GRANT_WAITLIST_PERMISSION";
        this.faculty          = faculty;
        this.student          = student;
        this.section          = section;
        this.notes            = notes;
        this.validForHours    = validForHours;
        this.notificationManager = NotificationManager.getInstance();
        this.dbManager           = DatabaseManager.getInstance();
    }

    // -------------------------------------------------------------------------
    // Command Interface — execute()
    // -------------------------------------------------------------------------

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        // ── Verify faculty owns the section ──────────────────────────────────
        if (section.getFacultyId() != faculty.getId()) {
            successful   = false;
            errorMessage = "Only the section instructor can grant enrollment permission.";
            System.out.println("✗ " + errorMessage);
            return;
        }

        // ── Check student is actually on the waitlist ─────────────────────────
        try {
            boolean onWaitlist = section.getWaitlist().stream()
                    .anyMatch(we -> we.getStudentId() == student.getId());
            if (!onWaitlist) {
                successful   = false;
                errorMessage = String.format("%s is not on the waitlist for %s.",
                        student.getFullName(), section.getCourseCode());
                System.out.println("✗ " + errorMessage);
                return;
            }
        } catch (SQLException e) {
            successful   = false;
            errorMessage = "Could not verify waitlist status: " + e.getMessage();
            System.err.println("✗ " + errorMessage);
            return;
        }

        // ── Persist a permission_grants record ───────────────────────────────
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(validForHours);
        String sql = "INSERT INTO permission_grants " +
                "(faculty_id, student_id, section_id, granted_at, expires_at, notes, is_used, is_active) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, FALSE, TRUE)";
        try {
            grantId = dbManager.executeInsert(sql,
                    faculty.getId(),
                    student.getId(),
                    section.getId(),
                    Timestamp.valueOf(expiresAt),
                    notes);

            if (grantId <= 0) {
                successful   = false;
                errorMessage = "Permission grant could not be saved.";
                return;
            }

            executed   = true;
            successful = true;

            System.out.printf("✓ Permission granted: %s may enroll in %s within %d hours.%n",
                    student.getFullName(), section.getCourseCode(), validForHours);

            // ── Notify the student that they may now register ─────────────────
            // This fires the Observer chain → email / push notification to student.
            notificationManager.notifyWaitlistUpdate(
                    student, section.getCourseCode(),
                    0); // Position 0 signals "you have a permission override"

        } catch (SQLException e) {
            successful   = false;
            errorMessage = "Failed to persist permission grant: " + e.getMessage();
            System.err.println("✗ " + errorMessage);
        }
    }

    // -------------------------------------------------------------------------
    // Command Interface — undo()
    // -------------------------------------------------------------------------

    @Override
    public void undo() {
        if (!executed || !successful || grantId <= 0) {
            System.out.println("Cannot undo — permission was not granted.");
            return;
        }

        // Deactivate the permission grant so the student can no longer use it.
        String sql = "UPDATE permission_grants SET is_active = FALSE WHERE id = ? AND is_used = FALSE";
        try {
            int updated = dbManager.executeUpdate(sql, grantId);
            if (updated > 0) {
                undoneAt = LocalDateTime.now();
                isUndone = true;
                System.out.printf("↶ Undone: Permission revoked for %s in %s.%n",
                        student.getFullName(), section.getCourseCode());
                // Notify student the permission was revoked.
                notificationManager.notifyWaitlistUpdate(
                        student, section.getCourseCode(),
                        section.getEnrolled()); // Show actual position again
            } else {
                System.out.println("✗ Permission could not be revoked — student may have already used it.");
            }
        } catch (SQLException e) {
            System.err.println("✗ Undo failed: " + e.getMessage());
        }
    }

    @Override
    public boolean isUndoable() {
        // Can only revoke if the permission hasn't been used by the student yet.
        if (!executed || !successful || grantId <= 0) return false;
        try {
            return dbManager.executeQuery(
                    "SELECT is_used FROM permission_grants WHERE id = ?",
                    rs -> rs.next() && !rs.getBoolean("is_used"),
                    grantId);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return String.format("Grant waitlist permission: %s → %s (valid %dh)",
                student.getFullName(), section.getCourseCode(), validForHours);
    }

    // -------------------------------------------------------------------------
    // Serialization
    // -------------------------------------------------------------------------

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("facultyId",    faculty.getId());
        data.put("studentPk",    student.getId());
        data.put("sectionId",    section.getId());
        data.put("notes",        notes);
        data.put("validForHours",validForHours);
        data.put("grantId",      grantId);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("GrantWaitlistPermissionCommand: serialization failed", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            int studentPk = (int) data.get("studentPk");
            int sectionId = (int) data.get("sectionId");

            Student raw = dbManager.fetchOne(Student.class, "id", studentPk);
            if (raw != null) this.student = ObservableStudent.fromSuperType(raw);
            this.section  = dbManager.fetchOne(Section.class, "id", sectionId);
            this.grantId  = (int) data.get("grantId");
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("GrantWaitlistPermissionCommand: deserialization failed", e);
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\MacroCommand.java
```java
package edu.advising.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MacroCommand - Executes multiple commands as one transaction
 */
@Table(name = "command_history", isSubTable = true)
public class MacroCommand extends BaseCommand {
    private List<BaseCommand> commands;
    private String description;

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public MacroCommand() {
        this("Initialized Macro");
    }

    public MacroCommand(String description) {
        super();
        this.commandType = "MACRO";
        this.description = description;
        this.commands    = new ArrayList<>();
    }

    public void addCommand(BaseCommand command) {
        commands.add(command);
    }

    public static MacroCommand fromSuperType(BaseCommand base) {
        MacroCommand cmd = new MacroCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();
        System.out.printf("▶ Executing macro: %s (%d commands)%n", description, commands.size());

        for (BaseCommand command : commands) {
            command.execute();
            if (!command.wasSuccessful()) {
                System.out.println("  ✗ Sub-command failed: " + command.getDescription());
                successful = false;
                executed   = true;
                System.out.println("✗ Macro failed — rolling back completed sub-commands");
                undo();
                return;
            }
        }

        executed   = true;
        successful = true;
        System.out.println("✓ Macro completed successfully");
    }

    @Override
    public void undo() {
        if (!executed) return;
        System.out.printf("↶ Undoing macro: %s%n", description);
        // Undo in reverse order (i.e. only commands that actually succeeded)
        for (int i = commands.size() - 1; i >= 0; i--) {
            BaseCommand cmd = commands.get(i);
            if (cmd.wasSuccessful()) {
                cmd.undo();
            }
        }
        this.undoneAt = LocalDateTime.now();
        this.isUndone = true;
    }

    @Override
    public boolean isUndoable() {
        return executed && commands.stream().allMatch(BaseCommand::isUndoable);
    }

    @Override
    public String getDescription() {
        return String.format("%s (Macro: %d commands)", description, commands.size());
    }

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < commands.size(); i++) {
            BaseCommand bc = commands.get(i);
            Map<String, Object> entry = new HashMap<>();
            entry.put("type",  bc.getClass().getName());  // fully-qualified, no "class " prefix
            entry.put("index", i);
            entry.put("data",  bc.serializeCommandData());
            list.add(entry);
        }
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize MacroCommand data", e);
        }
    }

    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Map<String, Object>> list =
                    mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
            this.commands = new ArrayList<>(list.size());

            for (Map<String, Object> entry : list) {
                String className    = (String) entry.get("type");
                String subData      = mapper.writeValueAsString(entry.get("data"));
                BaseCommand subCmd  = instantiateCommand(className);
                subCmd.setCommandData(subData);
                subCmd.initAfterLoad();
                this.commands.add(subCmd);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize MacroCommand data", e);
        }
    }

    private BaseCommand instantiateCommand(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return (BaseCommand) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate command class: " + className, e);
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\Payment.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - Payment Model (ORM Entity)
// ============================================================================
//
// WHY THIS MODEL IS HERE:
//   The PaymentCommand needs to persist payment records. Rather than writing
//   raw INSERT SQL strings inside the command (which was the old commented-out
//   approach), we define a proper ORM-annotated entity and let DatabaseManager
//   handle the persistence via upsert().
//
//   This is also the pattern established by Enrollment, WaitlistEntry, and
//   Section — models annotated with @Table and @Column so the ORM can reflect
//   over them at runtime.
//
// DB TABLE: payments (defined in DatabaseManager.initializeDatabase(), Week 5-8 section)
//
// FIELDS MAP EXACTLY TO:
//   id, student_id, amount, payment_type, payment_method,
//   payment_date, status, transaction_id, reference_number, notes
//
// ============================================================================

import edu.advising.core.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "payments")
public class Payment {

    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;

    @Column(name = "student_id", foreignKey = true)
    private int studentId;          // FK → students(id)

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "payment_type")
    private String paymentType;     // TUITION, FEE, HOUSING, etc.

    @Column(name = "payment_method")
    private String paymentMethod;   // CREDIT_CARD, CHECK, CASH, etc.

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "status")
    private String status;          // PENDING, COMPLETED, FAILED, REFUNDED

    @Column(name = "transaction_id")
    private String transactionId;   // External gateway reference

    @Column(name = "reference_number")
    private String referenceNumber; // Internal reference for the student

    @Column(name = "notes")
    private String notes;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** No-arg constructor required by ORM reflective instantiation. */
    public Payment() {}

    /**
     * Minimal constructor used by PaymentCommand when processing a new payment.
     */
    public Payment(int studentId, BigDecimal amount, String paymentType,
                   String paymentMethod, String status) {
        this.studentId     = studentId;
        this.amount        = amount;
        this.paymentType   = paymentType;
        this.paymentMethod = paymentMethod;
        this.status        = status;
        this.paymentDate   = LocalDateTime.now();
        // Generate a human-readable reference number for the student's receipt.
        this.referenceNumber = generateReferenceNumber();
    }

    // -------------------------------------------------------------------------
    // Convenience Methods
    // -------------------------------------------------------------------------

    /** @return true when this payment record represents a completed transaction. */
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    /** @return true when this payment has been refunded (e.g. via undo). */
    public boolean isRefunded() {
        return "REFUNDED".equals(status);
    }

    /**
     * Generates a simple reference number for receipt display.
     * In a real system this would come from a payment gateway.
     * Format: PAY-<timestamp-millis>
     */
    private static String generateReferenceNumber() {
        return "PAY-" + System.currentTimeMillis();
    }

    // -------------------------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------------------------

    public int getId()                     { return id; }
    public void setId(int id)              { this.id = id; }

    public int getStudentId()              { return studentId; }
    public void setStudentId(int studentId){ this.studentId = studentId; }

    public BigDecimal getAmount()          { return amount; }
    public void setAmount(BigDecimal amount){ this.amount = amount; }

    public String getPaymentType()         { return paymentType; }
    public void setPaymentType(String t)   { this.paymentType = t; }

    public String getPaymentMethod()       { return paymentMethod; }
    public void setPaymentMethod(String m) { this.paymentMethod = m; }

    public LocalDateTime getPaymentDate()  { return paymentDate; }
    public void setPaymentDate(LocalDateTime d){ this.paymentDate = d; }

    public String getStatus()              { return status; }
    public void setStatus(String status)   { this.status = status; }

    public String getTransactionId()       { return transactionId; }
    public void setTransactionId(String t) { this.transactionId = t; }

    public String getReferenceNumber()     { return referenceNumber; }
    public void setReferenceNumber(String r){ this.referenceNumber = r; }

    public String getNotes()               { return notes; }
    public void setNotes(String notes)     { this.notes = notes; }

    @Override
    public String toString() {
        return String.format("Payment[id=%d, student=%d, amount=%s, type=%s, status=%s, ref=%s]",
                id, studentId, amount, paymentType, status, referenceNumber);
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\PaymentCommand.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - PaymentCommand (Concrete Command)
// ============================================================================
//
// FEATURE:  Financial Information → Make a Payment
//
// WHY COMMAND PATTERN HERE:
//   A payment is a transactional operation that:
//     1. Must be logged for auditing (every cent that moves needs a record).
//     2. May need to be reversed (refunds — the undo operation).
//     3. Should trigger Observer notifications (PaymentReceived → email receipt).
//     4. Could be part of a MacroCommand (e.g., enroll + pay tuition at once).
//
//   Without Command Pattern, all of this logic would be tangled into a button
//   handler or a service method. Command Pattern separates:
//     WHO triggers the action (GUI button / REST endpoint)
//     WHAT the action does (this class)
//     HOW it is undone (the undo() method)
//
// UNDO SEMANTICS:
//   Undoing a payment marks the Payment row as REFUNDED via ORM upsert().
//   In a real system this would also call a payment gateway refund API.
//
// GUI INTEGRATION:
//   PaymentCommand cmd = new PaymentCommand(student, amount, paymentType, paymentMethod);
//   executor.execute(cmd);
//   if (cmd.wasSuccessful()) showReceipt(cmd.getPaymentReferenceNumber());
//
// ============================================================================

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Student;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Table(name = "command_history", isSubTable = true)
public class PaymentCommand extends BaseCommand {
    private ObservableStudent student;
    private BigDecimal amount;
    private String paymentType;    // TUITION, FEE, HOUSING, etc.
    private String paymentMethod;  // CREDIT_CARD, CHECK, CASH, etc.

    // Populated after execute() completes — needed for undo and receipt display.
    private Payment paymentRecord;

    private NotificationManager notificationManager;
    private DatabaseManager     dbManager;

    // Constructors

    public PaymentCommand() {
        this(null, null, null, null);
    }

    /**
     * @param student       The student making the payment.
     * @param amount        Payment amount as BigDecimal (must be > 0).
     * @param paymentType   Category: TUITION, FEE, HOUSING, etc.
     * @param paymentMethod Method: CREDIT_CARD, CHECK, CASH, ONLINE, etc.
     */
    public PaymentCommand(ObservableStudent student, BigDecimal amount,
                          String paymentType, String paymentMethod) {
        super();
        this.commandType         = "PAYMENT";
        this.student             = student;
        this.amount              = amount;
        this.paymentType         = paymentType;
        this.paymentMethod       = paymentMethod;
        this.notificationManager = NotificationManager.getInstance();
        this.dbManager           = DatabaseManager.getInstance();
    }

    /** Backward-compatible convenience constructor for double amounts. */
    public PaymentCommand(ObservableStudent student, double amount, String paymentType) {
        this(student, BigDecimal.valueOf(amount), paymentType, "ONLINE");
    }

    public static PaymentCommand fromSuperType(BaseCommand base) {
        PaymentCommand cmd = new PaymentCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    // -------------------------------------------------------------------------
    // Command Interface — execute()
    // -------------------------------------------------------------------------

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        // Pre-condition: amount must be positive
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            successful = false;
            errorMessage = "Payment amount must be greater than zero.";
            System.out.println("✗ " + errorMessage);
            return;
        }

        // Build Payment ORM entity and persist it via upsert()
        paymentRecord = new Payment(
                student.getId(),
                amount,
                paymentType,
                paymentMethod,
                "COMPLETED"
        );
        paymentRecord.setNotes("Processed via " + paymentMethod);

        try {
            // upsert() reflects over Payment's @Table/@Column annotations and
            // builds the MERGE statement — no hand-written SQL needed here.
            dbManager.upsert(paymentRecord);

            if (paymentRecord.getId() <= 0) {
                // upsert() should set the generated id via setId() — something went wrong.
                throw new IllegalStateException("Payment was saved but no ID was returned.");
            }

            // Adjust the student's account balance atomically
            updateStudentAccountBalance(amount.negate()); // payment reduces balance owed

            executed  = true;
            successful = true;

            System.out.printf("✓ Payment processed: $%.2f (%s) via %s | Ref: %s%n",
                    amount, paymentType, paymentMethod, paymentRecord.getReferenceNumber());

            // ── Trigger Observer notification ─────────────────────────────────
            // This fires the NotificationManager which pushes to all attached
            // Observer channels (email receipt, push notification, etc.)
            notificationManager.notifyPaymentReceived(student, amount.doubleValue(), paymentType);

        } catch (SQLException | IllegalAccessException | IllegalStateException e) {
            successful   = false;
            errorMessage = "Payment processing failed: " + e.getMessage();
            System.err.println("✗ " + errorMessage);
        }
    }

    // -------------------------------------------------------------------------
    // Command Interface — undo()
    // -------------------------------------------------------------------------

    @Override
    public void undo() {
        if (!executed || !successful || paymentRecord == null) {
            System.out.println("Cannot undo — payment was not completed.");
            return;
        }

        try {
            // Mark the Payment entity REFUNDED and re-persist via ORM upsert()
            paymentRecord.setStatus("REFUNDED");
            dbManager.upsert(paymentRecord);

            // Reverse the balance adjustment
            updateStudentAccountBalance(amount); // adds the amount back to balance owed

            undoneAt = LocalDateTime.now();
            isUndone = true;

            System.out.printf("↶ Undone: Refund issued $%.2f (%s) | Ref: %s%n",
                    amount, paymentType, paymentRecord.getReferenceNumber());

            // Notify student of the refund.
            notificationManager.notifyPaymentReceived(
                    student, -amount.doubleValue(), "REFUND-" + paymentType);

        } catch (SQLException | IllegalAccessException e) {
            System.err.println("✗ Failed to process refund: " + e.getMessage());
        }
    }

    @Override
    public boolean isUndoable() {
        // Can only refund if the original payment was in this session and succeeded.
        // In production, you'd also enforce a refund window (e.g., same calendar day).
        return executed && successful && paymentRecord != null && paymentRecord.isCompleted();
    }

    @Override
    public String getDescription() {
        return String.format("Payment of $%.2f (%s via %s)", amount, paymentType, paymentMethod);
    }

    // -------------------------------------------------------------------------
    // Convenience Getter — used by the UI to show a receipt after execute()
    // -------------------------------------------------------------------------

    /**
     * Returns the reference number for receipt display after execute().
     *
     * GUI Usage:
     *   executor.execute(cmd);
     *   if (cmd.wasSuccessful()) receiptLabel.setText("Ref: " + cmd.getPaymentReferenceNumber());
     */
    public String getPaymentReferenceNumber() {
        return paymentRecord != null ? paymentRecord.getReferenceNumber() : null;
    }

    // -------------------------------------------------------------------------
    // Serialization — for CommandHistory persistence
    // -------------------------------------------------------------------------

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk",  student.getId());   // int PK
        data.put("studentId",     student.getStudentId());                               // int PK
        data.put("amount",        amount.toPlainString());                        // BigDecimal-safe
        data.put("paymentType",   paymentType);
        data.put("paymentMethod", paymentMethod);
         // Store the generated payment record id so we can retrieve it on undo/redo
        data.put("paymentId",     paymentRecord != null ? paymentRecord.getId() : 0);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("PaymentCommand: serialization failed", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);

            // Reconstruct the student by numeric pk (not the String student_id field)
            int studentPk = (int) data.get("studentPk");
            Student raw   = dbManager.fetchOne(Student.class, "id", studentPk);
            if (raw != null) {
                this.student = ObservableStudent.fromSuperType(raw);
            }

            this.amount        = new BigDecimal(data.get("amount").toString());
            this.paymentType   = (String) data.get("paymentType");
            this.paymentMethod = (String) data.get("paymentMethod");

            // Re-hydrate the Payment record so undo() can find the DB row.
            int paymentId = (int) data.get("paymentId");
            if (paymentId > 0) {
                this.paymentRecord = dbManager.fetchOne(Payment.class, "id", paymentId);
            }

        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("PaymentCommand: deserialization failed", e);
        }
    }

    // -------------------------------------------------------------------------
    // Private Helpers
    // -------------------------------------------------------------------------

    /**
     * Atomically adjusts the student's account balance.
     *
     * This intentionally uses a raw SQL UPDATE (via executeUpdate) rather than
     * an ORM upsert() because we need an atomic increment/decrement against
     * the existing row value. upsert() would overwrite the entire row with a
     * potentially stale in-memory value if two sessions ran concurrently.
     * This is the one place in PaymentCommand where direct SQL is the correct
     * and safer choice over the ORM without further ORM development.
     */
    private void updateStudentAccountBalance(BigDecimal delta) {
        String updateSql = "UPDATE student_accounts " +
                "SET current_balance = current_balance + ?, " +
                "    total_payments  = total_payments  + ?, " +
                "    last_updated    = CURRENT_TIMESTAMP " +
                "WHERE student_id = ?";
        try {
            int rows = dbManager.executeUpdate(updateSql, delta, delta.negate(), student.getId());
            if (rows == 0) {
                // Account row doesn't exist yet — create it.
                dbManager.executeInsert(
                        "INSERT INTO student_accounts " +
                                "(student_id, current_balance, total_charges, total_payments) " +
                                "VALUES (?, ?, 0.00, ?)",
                        student.getId(), delta, delta.negate()
                );
            }
        } catch (SQLException e) {
            System.err.println("PaymentCommand: could not update student account — " + e.getMessage());
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\RegisterCommand.java
```java
package edu.advising.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.users.Student;

import java.util.HashMap;
import java.util.Map;

/**
 * RegisterCommand - Register student for a course section
 */
@Table(name = "command_history", isSubTable = true)
public class RegisterCommand extends BaseCommand {
    private ObservableStudent student;
    private Section section;
    private NotificationManager notificationManager;
    private int enrollmentId;

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public RegisterCommand() {
        this(null, null);
    }

    public RegisterCommand(ObservableStudent student, Section section) {
        super();
        this.commandType = "REGISTER";
        this.student = student;
        this.section = section;
        this.notificationManager = NotificationManager.getInstance();
    }

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        if (!section.hasCapacity()) {
            successful = false;
            errorMessage = String.format("Registration failed for %s - section full", section.getCourseCode());
            System.out.println("✗ " + errorMessage);
            return;
        }

        if (hasScheduleConflict()) {
            successful = false;
            errorMessage = String.format("Registration failed for %s - schedule conflict", section.getCourseCode());
            System.out.println("✗ " + errorMessage);
            return;
        }

        if ((this.enrollmentId = section.enroll(student)) > 0) {
            executed    = true;
            successful  = true;
            System.out.printf("✓ Student %s registered for %s%n",
                    student.getStudentId(), section.getCourseCode());
            notificationManager.notifyRegistration(student, section.getCourseCode(), true);
        } else {
            successful   = false;
            errorMessage = "Already enrolled or duplicate registration prevented.";
        }
    }

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo - command not executed or failed");
            return;
        }

        // Remove from section
        if( section.drop(student) ) {
            System.out.printf("↶ Undone: Registration for %s%n", section.getCourseCode());
            this.undoneAt = LocalDateTime.now();
            this.isUndone = true;
            // Notify about drop
            notificationManager.notifyRegistration(student, section.getCourseCode(), false);
        }
    }

    @Override
    public boolean isUndoable() {
        return executed && successful;
    }

    @Override
    public String getDescription() {
        return String.format("Register for %s (%s)", section.getCourseCode(), section.getCourseName());
    }

    private boolean hasScheduleConflict() {
        // Simplified - in real implementation, check time conflicts in student.
        return false;
    }

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk", student.getId());    //TODO: I'm not sure this is needed since my ORM handles sub-classes.
        data.put("studentId", student.getStudentId());
        data.put("sectionId", section.getId()); // Assuming Section has an id
        data.put("enrollmentId", enrollmentId);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize RegisterCommand data", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            // TODO: Figure out if we have to really deal with studentPk because student is a subclass of  User.
            int studentPk = (int) data.get("studentPk");
            int sectionId = (int) data.get("sectionId");
            this.enrollmentId = (int) data.get("enrollmentId");

            // Fetch as Student (annotated), then promote to ObservableStudent
            Student raw = DatabaseManager.getInstance().fetchOne(Student.class, "id", studentPk);
            if (raw != null) {
                this.student = ObservableStudent.fromSuperType(raw);
                this.student = ObservableStudent.fromSuperType(raw);
                this.section = DatabaseManager.getInstance().fetchOne(Section.class, "id", sectionId);
            }
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("Failed to deserialize RegisterCommand data", e);
        }
    }
}


```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\Section.java
```java
package edu.advising.commands;

import edu.advising.core.*;
import edu.advising.users.Faculty;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Course Section - Represents a course section
 */
@Table(name = "sections")
public class Section {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "course_id", foreignKey = true)
    private int courseId;  // References courses
    @Id
    @Column(name = "section_number")
    private String sectionNumber;
    @Id
    @Column(name = "semester")
    private String semester;
    @Id
    @Column(name = "year")
    private int year;
    @Column(name = "capacity")
    private int capacity;
    @Column(name = "enrolled")
    private int enrolled;
    @Column(name = "faculty_id", nullableforeignKey = true)
    private int facultyId; // References faculty
    @Column(name = "room")
    private String room;
    @Column(name = "status")
    private String status;  //OPEN, CLOSED, CANCELLED
    @ManyToOne(targetEntity = Course.class, joinColumn = "course_id")
    private Course course; // Cached object representing this sections courses.
    @ManyToOne(targetEntity = Faculty.class, joinColumn = "faculty_id")
    private Faculty faculty; // Cached object representing this faculty that teaches this course.
    @ManyToMany(
            targetEntity = Student.class,
            joinTable = "enrollments",
            joinColumn = "section_id",
            inverseJoinColumn = "student_id"
    )
    private List<Student> enrolledStudents;
    @OneToMany(targetEntity = Enrollment.class, mappedBy = "section_id")
    private List<Enrollment> enrollments;
    @OneToMany(targetEntity = WaitlistEntry.class, mappedBy = "section_id")
    private List<WaitlistEntry> waitlist;

    public Section() {}

    public Section(int id, int courseId, String sectionNumber,
                   String semester, int year, int capacity, int enrolled, int facultyId, String room, String status) {
        this(id, courseId, sectionNumber, semester, year, capacity, enrolled, facultyId);
        this.room = room;
        this.status = status;
    }

    public Section(int id, int courseId, String sectionNumber,
                   String semester, int year, int capacity, int enrolled, int facultyId) {
        this(courseId, sectionNumber, semester, year, capacity, enrolled, facultyId);
        this.id = id;
    }

    public Section(int courseId, String sectionNumber,
                   String semester, int year, int capacity, int enrolled, int facultyId) {
        this(courseId, sectionNumber, semester, year, capacity);
        this.enrolled = enrolled;
        this.facultyId = facultyId;
    }

    public Section(int courseId, String sectionNumber, String semester, int year, int capacity) {
        this(sectionNumber, semester, year, capacity);
        this.courseId = courseId;
    }

    public Section(String sectionNumber, String semester, int year, int capacity) {
        this.sectionNumber = sectionNumber;
        this.semester = semester;
        this.year = year;
        this.capacity = capacity;
        this.enrolledStudents = new ArrayList<>();
        this.waitlist = new ArrayList<>();
    }

    public boolean hasCapacity() {
        return enrolled < capacity;
    }

    private boolean isAlreadyOnWaitlist(Student newStudent) {
        try {
            return getWaitlist().stream().anyMatch(we -> we.getStudentId() == newStudent.getId());
        } catch (SQLException se) {
            se.printStackTrace();
            return true;
        }
    }

    private boolean isAlreadyEnrolled(Student newStudent) {
        try {
            return getEnrolledStudents().stream().anyMatch(student -> student.getId() == newStudent.getId());
        } catch (SQLException se) {
            se.printStackTrace();
            return true;
        }
    }

    public int enroll(Student newStudent) {
        if (hasCapacity() && !isAlreadyEnrolled(newStudent)) {
            // TODO: Update DatabaseManager to handle generic composite object dependency updates.
            try {
                ensureId();
                Enrollment enrollment = new Enrollment(newStudent.getId(), this.getId());
                DatabaseManager.getInstance().upsert(enrollment);
                // Make sure enrollments has already been lazyloaded.
                if(this.enrollments == null) {
                    this.getEnrollments();
                }
                this.enrollments.add(enrollment);
                enrolledStudents.add(newStudent);
                enrolled++;
                // To make sure enrollment numbers get updated, could also make this a trigger in the database.
                DatabaseManager.getInstance().upsert(this);
                return enrollment.getId();
            } catch (SQLException | IllegalAccessException e) {
                return 0;
            }
        }
        return 0;
    }

    public boolean drop(Student dropStudent) {
        // First let's see if we can find an Enrollment for this student.
        try {
            Optional<Enrollment> optionalEnrollment = this.getEnrollments().stream()
                    .filter(enrollment -> enrollment.getStudentId() == dropStudent.getId()).findFirst();
            if(optionalEnrollment.isPresent()) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                // Update the Enrollment with the DROP status
                Enrollment enrollment = optionalEnrollment.get();
                enrollment.setStatus("DROPPED");
                enrollment.setDroppedAt(LocalDateTime.now());
                dbManager.upsert(enrollment);
                if( enrolledStudents.removeIf(student -> student.getId() == dropStudent.getId()) ) {
                    this.enrollments.remove(enrollment);
                    enrolled--;
                    // To make sure enrollment numbers get updated, could also make this a trigger in the database.
                    dbManager.upsert(this);
                    return true;
                }
            }
        } catch (SQLException | IllegalAccessException e) { e.printStackTrace(); }
        return false;
    }

    public int addToWaitlist(Student newStudent) {
        if (!isAlreadyOnWaitlist(newStudent) && !isAlreadyEnrolled(newStudent)) {
            try {
                ensureId();
                WaitlistEntry waitlist = new WaitlistEntry(newStudent.getId(), this.getId(), this.getNextWaitlistPosition());
                DatabaseManager.getInstance().upsert(waitlist);
                this.waitlist.add(waitlist);
                return waitlist.getId();
            } catch (SQLException | IllegalAccessException e) {
                //e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public boolean removeFromWaitlist(Student student) {
        try {
            // First let's see if we can find a WaitlistEntry for this student.
            Optional<WaitlistEntry> wle = getWaitlist().stream()
                    .filter(we -> we.getStudentId() == student.getId()).findFirst();
            if (wle.isPresent()) {
                DatabaseManager.getInstance().delete(wle.get());  // ← unwrap the Optional
                return waitlist.remove(wle.get());
            }
            return true;
        } catch (SQLException | IllegalAccessException e) { e.printStackTrace(); }
        return false;
    }

    public int getNextWaitlistPosition() throws SQLException {
        if(this.waitlist == null || this.waitlist.isEmpty()) {
            String sql = "SELECT count(*) FROM waitlist where section_id = ?;";
            return DatabaseManager.getInstance().executeQuery(sql, rs -> {
                if(rs.next()) {
                    return rs.getInt(1);
                }
                return 0;  // default return is 0
            }, this.getId()) + 1;
        }
        return waitlist.size() + 1; // 1-based
    }

    public int getWaitlistPosition(Student student) throws SQLException {
        if(this.waitlist == null || this.waitlist.isEmpty()) {
            String sql = "SELECT position FROM waitlist where section_id = ? and student_id = ?;";
            return DatabaseManager.getInstance().executeQuery(sql, rs -> {
                return rs.getInt(1);
            }, this.getId(), student.getId());
        }
        return waitlist.stream().filter(wl -> wl.getStudentId() == student.getId())
                .findFirst().map(WaitlistEntry::getPosition).orElse(0);
    }

    // Getters
    public int getId() { return id; }
    public String getSectionNumber() { return sectionNumber; }
    public String getSemester() { return semester; }
    public int getCapacity() { return capacity; }
    public int getEnrolled() { return enrolled; }
    public int getAvailableSeats() { return capacity - enrolled; }

    public List<WaitlistEntry> getWaitlist() throws SQLException {
        if (this.waitlist == null) {
            this.waitlist = DatabaseManager.getInstance().fetchMany(
                    WaitlistEntry.class, "section_id", this.getId());
        }
        return this.waitlist;
    }

    public String getCourseName() {
        try {
            Course c = this.getCourse();
            return (c != null) ? c.getName() : "UNKNOWN";
        } catch (SQLException se) {
            se.printStackTrace();
            return "UNKNOWN (Cause: DB ERROR)";
        }
    }

    public String getCourseCode() {
        try {
            Course course = this.getCourse();
            return course.getCode() + "-" + semester + year + "-" + sectionNumber; // CIS12-SP26-2
        } catch (SQLException e) { }
        return "UNKNOWN-" + semester + year + "-" + sectionNumber; // UNKNOWN-SP26-2
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s-%s: %s %s (%d/%d enrolled)",
                courseId, sectionNumber, semester, year, enrolled, capacity);
    }

    public Course getCourse() throws SQLException {
        if (this.course == null) {
            // Lazy Load: Use the generic fetchOne from DatabaseManager
            this.course = DatabaseManager.getInstance()
                    .fetchOne(Course.class, "id", this.courseId);
        }
        return (this.course != null) ? this.course : null;
    }

    public void setCourse(Course course) {
        this.courseId = course.getId();
        this.course = course;
    }

    public List<Student> getEnrolledStudents() throws SQLException {
        if (this.enrolledStudents == null) {
            this.enrolledStudents = DatabaseManager.getInstance().fetchManyToMany(
                    Student.class, "enrollments", "section_id", "student_id", this.getId()
            );
        }
        return this.enrolledStudents;
    }

    public void setEnrolledStudents(List<Student> students) {
        this.enrolledStudents = students;
    }

    public List<Enrollment> getEnrollments() throws SQLException {
        // TODO: Gotta find a way to modify the fetch calls to take additional filters since this will return
        //   Enrollments in ANY status (i.e. DROPPED, etc.).
        if (this.enrollments == null) {
            // Lazy Load: Use the generic fetchMany from DatabaseManager
            this.enrollments = DatabaseManager.getInstance()
                    .fetchMany(Enrollment.class, "section_id", this.id);
        }
        return this.enrollments;
    }

    protected void ensureId() throws SQLException, IllegalAccessException {
        if(this.getId() == 0) {
            // If the id is not set, we need to save this object to get an id to set on the list items.
            DatabaseManager.getInstance().upsert(this);
        }
    }

    public void setEnrollments(List<Enrollment> enrollments) throws SQLException, IllegalAccessException {
        // TODO: Make the DatabaseManager even MORE generic where it can build a dependency graph of objects
        //   and make upsert/upsertAll calls to satisfy and update ids in order, rather than coding setters like this.
        ensureId();
        // Now, let's add this object's id to the related list items foreign key id
        for(Enrollment e : enrollments) { e.setSectionId(this.getId()); }
        // Now let's upsertAll of these list items (i.e. a batch) and set as this object's related field.
        DatabaseManager.getInstance().upsertAll(enrollments);
        this.enrollments = enrollments;
    }

    public Faculty getFaculty() throws SQLException {
        if (this.faculty == null) {
            // Lazy Load: Use the generic fetchOne from DatabaseManager
            this.faculty = DatabaseManager.getInstance()
                    .fetchOne(Faculty.class, "id", this.facultyId);
        }
        return (this.faculty != null) ? this.faculty : null;
    }

    public void setFaculty(Faculty faculty) {
        this.facultyId = faculty.getId();
        this.faculty = faculty;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\UpdateContactCommand.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - UpdateContactCommand (Concrete Command)
// ============================================================================
//
// FEATURE:  Academic Profile → Contact Information Update
//
// WHY COMMAND PATTERN HERE:
//   At first glance, updating an email address looks too simple for a Command.
//   But consider:
//     1. UNDO: If a user accidentally changes their email to a typo, they need
//        a way to reverse it without contacting an administrator.
//     2. AUDIT: FERPA and institutional policy often require logging who changed
//        contact info and when — the command_history table provides this for free.
//     3. MACRO: A future "Import Contact Info from SSO" feature could batch
//        multiple UpdateContactCommands inside a MacroCommand.
//     4. VALIDATION: The command encapsulates all validation (email format,
//        duplicate check) in one place, reusable from CLI, web, or desktop GUI.
//
// UNDO SEMANTICS:
//   The old values are captured at construction time (before execute()).
//   Undo restores the previous values using the same ORM upsert path.
//   This guarantees the user record stays consistent regardless of how
//   many times they undo/redo the change.
//
// GUI INTEGRATION:
//   // "Save" button on the Contact Information Update form:
//   UpdateContactCommand cmd = new UpdateContactCommand(
//       student, emailField.getText(), phoneField.getText()
//   );
//   executor.execute(cmd);
//
//   if (cmd.wasSuccessful()) {
//       showSuccessToast("Contact information updated.");
//   } else {
//       showError(cmd.getErrorMessage());
//   }
//   undoButton.setEnabled(executor.canUndo()); // "Undo Contact Update" tooltip
//
// ============================================================================

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Student;
import edu.advising.users.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Table(name = "command_history", isSubTable = true)
public class UpdateContactCommand extends BaseCommand {

    // ── State needed for execute and undo ────────────────────────────────────

    private ObservableStudent student;
    private String newEmail;
    private String newPhone;
    private String oldEmail;    // Captured at construction for undo
    private String oldPhone;    // Captured at construction for undo

    private final DatabaseManager dbManager;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public UpdateContactCommand() {
        this(new ObservableStudent("", "", "", "", "", "S0000"),
                "", "");
    }

    /**
     * Capture old values at construction time, before anything is changed.
     * This is the "snapshot before" approach standard in Command Pattern undo.
     *
     * @param student  The currently logged-in, live user object.
     * @param newEmail New email address (null to leave unchanged).
     * @param newPhone New phone number (null to leave unchanged).
     */
    public UpdateContactCommand(ObservableStudent student, String newEmail, String newPhone) {
        super();
        this.commandType = "UPDATE_CONTACT";
        this.student     = student;
        this.newEmail    = newEmail;
        this.newPhone    = newPhone;

        // Snapshot old values NOW, before any changes are made.
        // This is what makes undo() reliable.
        this.oldEmail = student.getEmail();
        this.oldPhone = student.getPhone(); // Requires phone field on User — see User.java note

        this.dbManager = DatabaseManager.getInstance();
    }

    public static UpdateContactCommand fromSuperType(BaseCommand base) {
        UpdateContactCommand cmd = new UpdateContactCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    // -------------------------------------------------------------------------
    // Command Interface — execute()
    // -------------------------------------------------------------------------

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        // ── Validate inputs ──────────────────────────────────────────────────
        if (newEmail != null && !isValidEmail(newEmail)) {
            successful   = false;
            errorMessage = "Invalid email format: " + newEmail;
            System.out.println("✗ " + errorMessage);
            return;
        }

        if (newEmail != null && isDuplicateEmail(newEmail, student.getId())) {
            successful   = false;
            errorMessage = "Email address is already in use: " + newEmail;
            System.out.println("✗ " + errorMessage);
            return;
        }

        // ── Apply changes to the in-memory user object ───────────────────────
        if (newEmail != null) student.setEmail(newEmail);
        if (newPhone != null) student.setPhone(newPhone);
        student.setUpdatedAt(LocalDateTime.now());

        // ── Persist via ORM — upsert uses @Table/@Column annotations on User ─
        // upsert() generates:
        //   MERGE INTO users (id, email, phone, updated_at, ...) VALUES (...)
        // Only the columns that changed will differ; the rest stay as-is.
        try {
            Student copy = student.toSubType();  // Copying object so upsert hierarchy annotations work properly.
            dbManager.upsert(copy); // Updating the copied object, realizing fields like updatedAt won't be synced.
            //TODO: determine if other fields need to be synced as well after upsert.
            student.setUpdatedAt(copy.getUpdatedAt()); // Syncing update at manually.

            executed   = true;
            successful = true;
            System.out.printf("✓ Contact info updated for %s (ID %d)%n",
                    student.getFullName(), student.getId());

        } catch (SQLException | IllegalAccessException e) {
            // Rollback in-memory changes if the DB update fails.
            student.setEmail(oldEmail);
            student.setPhone(oldPhone);

            successful   = false;
            errorMessage = "Database update failed: " + e.getMessage();
            System.err.println("✗ " + errorMessage);
        }
    }

    // -------------------------------------------------------------------------
    // Command Interface — undo()
    // -------------------------------------------------------------------------

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo — contact update was not completed.");
            return;
        }

        // Restore old values on the in-memory object first.
        student.setEmail(oldEmail);
        student.setPhone(oldPhone);
        student.setUpdatedAt(LocalDateTime.now());

        // Then persist the restored state.
        try {
            DatabaseManager.getInstance().upsert(student.toSubType());
            System.out.println("↶ Undone: Contact info restored for " + student.getStudentId());
            this.undoneAt = LocalDateTime.now();
            this.isUndone = true;
        } catch (SQLException | IllegalAccessException e) {
            // Undo failed — re-apply new values to keep in-memory state consistent with DB.
            student.setEmail(newEmail);
            student.setPhone(newPhone);
            System.err.println("✗ Undo failed — could not restore contact info: " + e.getMessage());
        }
    }

    @Override
    public boolean isUndoable() {
        return executed && successful;
    }

    @Override
    public String getDescription() {
        return String.format("Update contact info for %s (email: %s → %s)",
                student.getFullName(), oldEmail, newEmail != null ? newEmail : oldEmail);
    }

    // -------------------------------------------------------------------------
    // Serialization — for CommandHistory persistence and session recovery
    // -------------------------------------------------------------------------

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk", student.getId()); // Numeric PK, not the String student_id
        data.put("newEmail",  newEmail);
        data.put("newPhone",  newPhone);
        data.put("oldEmail",  oldEmail);
        data.put("oldPhone",  oldPhone);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("UpdateContactCommand: serialization failed", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            int studentPk  = (int) data.get("studentPk");
            this.newEmail  = (String) data.get("newEmail");
            this.newPhone  = (String) data.get("newPhone");
            this.oldEmail  = (String) data.get("oldEmail");
            this.oldPhone  = (String) data.get("oldPhone");

            Student raw  = DatabaseManager.getInstance().fetchOne(Student.class, "id", studentPk);
            this.student = ObservableStudent.fromSuperType(raw);
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("Failed to deserialize UpdateContactCommand data", e);
        }
    }

    // -------------------------------------------------------------------------
    // Private Validation Helpers
    // -------------------------------------------------------------------------

    private boolean isValidEmail(String email) {
        // RFC 5322 simplified: local@domain.tld
        return email != null && email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Check whether another active user already owns this email address.
     * Excludes the current user so they can re-save their own email without conflict.
     */
    private boolean isDuplicateEmail(String email, int excludeUserId) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND id <> ? AND is_active = TRUE";
        try {
            return dbManager.executeQuery(sql, rs -> {
                rs.next();
                return rs.getInt(1) > 0;
            }, email, excludeUserId);
        } catch (SQLException e) {
            System.err.println("UpdateContactCommand: duplicate email check failed — " + e.getMessage());
            return false; // Fail open — let the DB UNIQUE constraint catch it
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\WaitlistCommand.java
```java
package edu.advising.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * WaitlistCommand - Add student to waitlist
 */
@Table(name = "command_history", isSubTable = true)
public class WaitlistCommand extends BaseCommand {
    private ObservableStudent student;
    private Section section;
    private int waitlistId;
    private NotificationManager notificationManager;

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public WaitlistCommand() {
        this(null, null);
    }

    public WaitlistCommand(ObservableStudent student, Section section) {
        super();
        this.commandType         = "WAITLIST";
        this.student             = student;
        this.section             = section;
        this.notificationManager = NotificationManager.getInstance();
    }

    public static WaitlistCommand fromSuperType(BaseCommand base) {
        WaitlistCommand cmd = new WaitlistCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        if ((this.waitlistId = section.addToWaitlist(student)) > 0) {
            executed = true;
            successful = true;
            try {
                int position = section.getWaitlistPosition(student);
                System.out.printf("✓ Student %s added to waitlist for %s (Position: #%d)%n",
                        student.getStudentId(), section.getCourseCode(), position);
                notificationManager.notifyWaitlistUpdate(student, section.getCourseCode(), position);
            } catch (SQLException e) {
                System.out.printf("✓ Student %s added to waitlist for %s but couldn't determine position.%n",
                        student.getStudentId(), section.getCourseCode());
                notificationManager.notifyWaitlistUpdate(student, section.getCourseCode(), -1);
            }
        } else {
            successful   = false;
            errorMessage = String.format("Waitlist add failed for %s — already on waitlist or other error",
                    section.getCourseCode());
            System.out.println("✗ " + errorMessage);
        }
    }

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo - command not executed or failed");
            return;
        }

        if (section.removeFromWaitlist(student)) {
            System.out.printf("↶ Undone: Waitlist for %s%n", section.getCourseCode());
            this.undoneAt = LocalDateTime.now();
            this.isUndone = true;
            // Notify about waitlist removal.
            notificationManager.notifyWaitlistUpdate(student, section.getCourseCode(), Integer.MAX_VALUE);
        }
    }

    @Override
    public boolean isUndoable() {
        return executed && successful;
    }

    @Override
    public String getDescription() {
        return String.format("Add to waitlist for %s", section.getCourseCode());
    }

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk",  student.getId());   // int PK
        data.put("studentId", student.getStudentId());
        data.put("sectionId", section.getId()); // Assuming Section has an id
        data.put("waitlistId", waitlistId);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize WaitlistCommand data", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            int studentPk = (int) data.get("studentPk");
            int sectionId = (int) data.get("sectionId");
            this.waitlistId = (int) data.get("waitlistId");

            Student raw  = DatabaseManager.getInstance().fetchOne(Student.class, "id", studentPk);
            this.student = ObservableStudent.fromSuperType(raw);
            this.section = DatabaseManager.getInstance().fetchOne(Section.class, "id", sectionId);
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("Failed to deserialize WaitlistCommand data", e);
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\WaitlistEntry.java
```java
package edu.advising.commands;

import edu.advising.core.*;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Table(name = "waitlist")
public class WaitlistEntry {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "student_id", foreignKey = true)
    private int studentId;
    @Id
    @Column(name = "section_id", foreignKey = true)
    private int sectionId;
    @Column(name = "position")
    private int position;
    @Column(name = "added_date")
    private LocalDateTime addedDate;
    @Column(name = "removed_date")
    private LocalDateTime removedDate;
    @Column(name = "status")
    private String status;
    @Column(name = "notification_sent")
    private boolean notificationSent;

    @ManyToOne(targetEntity = Section.class, joinColumn = "section_id")
    private Section section;
    @ManyToOne(targetEntity = Student.class, joinColumn = "student_id")
    private Student student;

    public WaitlistEntry() {}

    public WaitlistEntry(int studentId, int sectionId, int position) {
        this(studentId, sectionId);
        this.position = position;
    }
    public WaitlistEntry(int studentId, int sectionId) {
        this.studentId = studentId;
        this.sectionId = sectionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public LocalDateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public LocalDateTime getRemovedDate() {
        return removedDate;
    }

    public void setRemovedDate(LocalDateTime removedDate) {
        this.removedDate = removedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(boolean notificationSent) {
        this.notificationSent = notificationSent;
    }

    public Student getStudent() throws SQLException {
        if (this.student == null) {
            // Lazy Load: Use the generic fetchOne from DatabaseManager
            this.student = DatabaseManager.getInstance()
                    .fetchOne(Student.class, "id", this.studentId);
        }
        return (this.student != null) ? this.student : null;
    }

    public void setStudent(Student student) {
        this.studentId = student.getId();
        this.student = student;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\common\ValidationResult.java
```java
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
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\Column.java
```java
package edu.advising.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name();
    // Allows me to handle foreignKey columns in UPSERTS when they're null or 0
    boolean nullableforeignKey() default false;
    // Allows me to handle foreignKey columns in UPSERTS differently
    boolean foreignKey() default false;
    boolean upsertIgnore() default false; // Allows me to ignore Primary id fields for UPSERTS
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\DatabaseManager.java
```java
// Week 1: SINGLETON PATTERN
// Foundation: Database Connection Manager
// Features Implemented: Basic database connectivity
// Why First: Essential infrastructure that all other components will use

package edu.advising.core;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.reflect.Field;
import java.util.Optional;

// TODO: Make DatabaseManager an abstract class that implements a template methods for methods like upsertAll, which use
//  an abstract method called buildUpsertSql to implement Database specific upsert sql statements, then implement
//  concrete subclasses of DatabaseManager that override and implement buildUpsertSql for specific databases,
//  (H2, MySQL, PostgreSQL, etc.)

/**
 * DatabaseManager - Singleton Pattern
 * Ensures only one database connection pool exists throughout the application.
 * This prevents connection leaks and ensures efficient resource management.
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private final HikariDataSource dataSource;

    //private static final String URL = "jdbc:h2:mem:advising;DB_CLOSE_DELAY=-1";
    private static final String URL = "jdbc:h2:file:./advising";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    // Private constructor prevents instantiation from other classes
    private DatabaseManager() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setDriverClassName("org.h2.Driver");

        // Pool performance tuning
        config.setMaximumPoolSize(10);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");

        this.dataSource = new HikariDataSource(config);
        initializeDatabase();
        System.out.println("Database connection pool established");
    }

    // Thread-safe singleton instance retrieval
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // ======================================================================================
    // CORE LAMBDA METHODS - Allows ResultSet, Connection, and P-Statement to be managed here,
    //                       but still handle data with passed in Lambda function.
    // ======================================================================================

    /**
     * Executes a query and uses a lambda to process the ResultSet.
     * The connection is automatically returned to the pool after the lambda finishes.
     */
    public <T> T executeQuery(String sql, QueryHandler<T> handler, Object... params) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setParameters(pstmt, params);
            try (ResultSet rs = pstmt.executeQuery()) {
                return handler.handle(rs); // This is the Lambda that handles the data.
            }
        }
    }

    /**
     * Specialized helper to fetch a List of objects.
     */
    public <T> List<T> fetchList(String sql, QueryHandler<T> rowMapper, Object... params) throws SQLException {
        return executeQuery(sql, rs -> {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(rowMapper.handle(rs));
            }
            return results;
        }, params);
    }

    /**
     * Fetches a single object from the database.
     * Returns null if no record is found.
     */
    public <T> T fetch(String sql, QueryHandler<T> rowMapper, Object... params) throws SQLException {
        return executeQuery(sql, rs -> {
            if (rs.next()) {
                return rowMapper.handle(rs); // Use the same mapper logic as fetchList
            }
            return null; // Return null if the result set is empty
        }, params);
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Get class inheritance hierarchy for a class.
     */
    private List<Class<?>> getTableHierarchy(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<>();
        while (clazz != null && clazz.isAnnotationPresent(Table.class)) {
            hierarchy.add(0, clazz); // Add to the front to get [User, Student]
            clazz = clazz.getSuperclass();
        }
        return hierarchy;
    }

    /**
     * NOTE: ADD Observer Week
     * -
     * Get annotated fields Local to the clazz.
     *
     * @param clazz The class to inspect for annotated fields.
     */
    private List<Field> getAnnotatedFields(Class<?> clazz) {
        List<Field> columns = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Column.class)) columns.add(field);
        }
        return columns;
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Recursively get All annotated fields, even those inherited from Superclass(es)!
     * This is to support Superclass/Subclass hierarchies like User -> Student or User -> Faculty.
     *
     * @param clazz The class to inspect for annotated fields.
     */
    private List<Field> getAllAnnotatedFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        List<Class<?>> hierarchy = getTableHierarchy(clazz);
        for (Class<?> c : hierarchy) {
            for (Field field : c.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Column.class)) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    /**
     * NOTE: ADD Observer Week
     * -
     * Only get fields that are annoted with @Id. Useful for upsert merging on Natural Key/UNIQUE constraints.
     */
    private List<Field> getIdAnnotatedFields(List<Field> allFields) {
        return allFields.stream().filter(f -> f.isAnnotationPresent(Id.class)).toList();
    }

    /**
     * NOTE: ADD Observer or Command Week
     * -
     * Gets the PRIMARY annotated @Id field of a Class, which will primarily be used in ManyToMany object joins.
     */
    private <T> String getLocalIdColumnName(Class<T> targetClass) {
        // We get the target ID column name from the @Id field of the target class
        return getAnnotatedFields(targetClass).stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .map(f -> f.getAnnotation(Column.class).name())
                .findFirst().orElse("id");
    }

    /**
     * NOTE: ADD Observer or Command Week
     * -
     * Gets the PRIMARY annotated @Id field's name of a Class recursively to handle hierarchical classes.
     */
    private <T> String getPrimaryIdColumnName(Class<T> targetClass) {
        return getAllAnnotatedFields(targetClass).stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .filter(f -> f.getAnnotation(Id.class).isPrimary())
                .map(f -> f.getAnnotation(Column.class).name())
                .findFirst().orElse("id");
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Gets the PRIMARY annotated @Id field of a Class recursively to handle hierarchical classes.
     */
    private <T> Optional<Field> getPrimaryIdColumn(Class<T> targetClass) {
        // We get the target ID column name from the @Id field of the target class
        return getAllAnnotatedFields(targetClass).stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .filter(f -> f.getAnnotation(Id.class).isPrimary())
                .findFirst();
    }


    /**
     * NOTE: ADD Observer Week
     * -
     * Only get fields that aren't ignored for upserts. We ignore AUTO_INCREMENT id fields, for example.
     * To ignore AUTO_INC fields though, you'll still need a UNIQUE index on the Natural Key for upsert to work.
     */
    private List<Field> getUpsertFields(List<Field> allFields, Class<?> clazz) {
        List<Field> upsertFields = new ArrayList<>();
        for (Field f : allFields) {
            Column col = f.getAnnotation(Column.class);
            boolean isIgnored = col.upsertIgnore();
            if (!isIgnored) {
                upsertFields.add(f);
            }
        }
        // Need to make sure the parent's primary Id is in this list if this is a sub-class.
        if (clazz.getAnnotation(Table.class).isSubTable()) {
            Optional<Field> oFieldPId = getPrimaryIdColumn(clazz);
            oFieldPId.ifPresent(upsertFields::add);
        }
        return upsertFields;
    }

    /**
     * NOTE: ADD Observer Week
     * -
     * Propagate's Parent Ids/Primary keys to Subclass objects during hierarchical/related table updates.
     * This allows tables like Student to get its id from the related Superclass/User INSERT/UPDATE.
     * SQLException can arise due to database queries
     * IllegalAccessException can arise due to java.lang.reflect when using annotations.
     */
    private <T> void propagateGeneratedKeys(PreparedStatement pstmt, List<T> items, List<Field> localFields)
            throws SQLException, IllegalAccessException {
        // Find the auto-increment field in this class level
        // NOTE: localFields for say User will find an autoIncField and pass on to Student Items,
        //   but Student localFields will not, and therefore not go into the isPresent conditional.
        //   Thus, this only works for 2 levels, which is liekly good enough.
        Optional<Field> autoIncField = localFields.stream()
                .filter(f -> f.getAnnotation(Column.class).upsertIgnore())
                .findFirst();

        if (autoIncField.isPresent()) {
            Field field = autoIncField.get();
            field.setAccessible(true);

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                for (T item : items) {
                    // If the object didn't have an ID, set the one the DB just made
                    Object existingId = field.get(item);
                    if (existingId == null || (existingId instanceof Number && ((Number) existingId).longValue() == 0)) {
                        if (rs.next()) {
                            field.set(item, rs.getObject(1));
                        }
                    }
                }
            }
        }
    }

    /**
     * NOTE: ADD Observer Week
     * -
     * Upserts (Inserts or Updates) a list of objects into the database.
     * Uses H2's MERGE syntax and JDBC Batching for high performance.
     * SQLException can arise due to database queries
     * IllegalAccessException can arise due to java.lang.reflect when using annotations.
     */
    public <T> void upsertAll(List<T> items) throws SQLException, IllegalAccessException {
        if (items == null || items.isEmpty()) return;

        Connection conn = dataSource.getConnection();
        try {
            conn.setAutoCommit(false); // Start Transaction

            Class<?> leafClass = items.get(0).getClass();
            List<Class<?>> hierarchy = getTableHierarchy(leafClass);

            for (Class<?> clazz : hierarchy) {
                Table tableAnn = clazz.getAnnotation(Table.class);

                // Only get fields DECLARED in this specific class (User fields vs Student fields)
                List<Field> localFields = getAnnotatedFields(clazz);

                // If this subclass has no localFields, we can safely ignore it.
                // This is expected for concrete commands like RegisterCommand, which carry
                // no @Column fields of their own all persistence lives in BaseCommand.
                if (localFields.isEmpty()) {
                    continue;
                }

                List<Field> writeableFields = getUpsertFields(localFields, clazz);
                List<Field> keyFields = getIdAnnotatedFields(writeableFields);

                if (!keyFields.isEmpty()) {
                    // Strategy A: natural key MERGE
                    String sql = buildUpsertSql(tableAnn.name(), writeableFields, keyFields);

                    try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        for (T item : items) {
                            for (int i = 0; i < writeableFields.size(); i++) {
                                Field f = writeableFields.get(i);
                                Object value = f.get(item);
                                if (f.getAnnotation(Column.class).nullableforeignKey()
                                        && (value == null || (int) value == 0)) {
                                    pstmt.setObject(i + 1, null);
                                } else {
                                    pstmt.setObject(i + 1, value);
                                }
                            }
                            pstmt.addBatch();
                        }
                        pstmt.executeBatch();

                        // ID HAND OFF: capture auto generated ids for FK propagation to child tables
                        // (i.e. User.id -> Student.id).
                        propagateGeneratedKeys(pstmt, items, localFields);
                    }
                } else {
                    // Strategy B: PK only split batch
                    // keyFields is empty, the entity has no natural key. The only possible key is the AUTO_INCREMENT
                    // primary key, which was intentionally excluded from writeableFields by upsertIgnore=true. Route
                    // to the INSERT/UPDATE is a split path.
                    executePkOnlySplitBatch(conn, tableAnn.name(), localFields, writeableFields, items);
                }
            }
            conn.commit(); // Success!
        } catch (Exception e) {
            //e.printStackTrace();
            conn.rollback(); // Undo everything on failure
            throw new SQLException("Transaction failed. Changes rolled back.", e);
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    /**
     * Handles upserts for entities that have no natural key — only an AUTO_INCREMENT
     * primary key (flagged by @Id(isPrimary=true) + @Column(upsertIgnore=true)).
     *
     * Why a split is necessary:
     *   H2's MERGE INTO requires at least one KEY column. For AUTO_INCREMENT fields
     *   we cannot include id in KEY when id=0, because H2 would INSERT with id=0
     *   literally instead of letting AUTO_INCREMENT assign a value. So new rows and
     *   existing rows need fundamentally different SQL.
     *
     *   id == 0 → new row:
     *     INSERT INTO table (writeable_cols) VALUES (?)
     *     DB assigns the AUTO_INCREMENT id.
     *     propagateGeneratedKeys() reads the generated id and sets it back on the item.
     *
     *   id > 0 → existing row:
     *     UPDATE table SET col=?, col=?, ... WHERE id=?
     *     The id value is appended as the final parameter for the WHERE clause.
     *
     * @param conn           the active transactional connection (do not close it here)
     * @param tableName      the target table name
     * @param localFields    all @Column fields declared on this class level
     *                       (used by propagateGeneratedKeys to find the autoInc field)
     * @param writeableFields the subset of localFields that should appear in INSERT/SET
     *                       (already excludes the upsertIgnore=true id field)
     * @param items          the objects to persist
     */
    private <T> void executePkOnlySplitBatch(Connection conn,
                                             String tableName,
                                             List<Field> localFields,
                                             List<Field> writeableFields,
                                             List<T> items)
            throws SQLException, IllegalAccessException {

        // Find the AUTO_INCREMENT primary key field.
        // It is identified by having both @Id(isPrimary=true) AND upsertIgnore=true.
        Optional<Field> oPkField = localFields.stream()
                .filter(f -> f.isAnnotationPresent(Id.class)
                        && f.getAnnotation(Id.class).isPrimary()
                        && f.getAnnotation(Column.class).upsertIgnore())
                .findFirst();

        if (oPkField.isEmpty()) {
            // Safety net: keyFields was empty AND there's no auto increment PK.
            // This means the entity is genuinely un-keyable, a configuration error.
            throw new SQLException(
                    "upsertAll: no key fields and no auto-increment primary key found "
                            + "for table '" + tableName + "'. "
                            + "Add @Id to at least one non-upsertIgnore field (natural key), "
                            + "or add @Id(isPrimary=true) to the auto-increment id field.");
        }

        Field pkField = oPkField.get();
        pkField.setAccessible(true);

        // Split items into new (i.e. id=0) vs existing (i.e. id>0)
        List<T> newItems      = new ArrayList<>();
        List<T> existingItems = new ArrayList<>();

        for (T item : items) {
            Object pk   = pkField.get(item);
            boolean isNew = (pk == null)
                    || (pk instanceof Number && ((Number) pk).longValue() == 0);
            if (isNew) newItems.add(item);
            else       existingItems.add(item);
        }

        // INSERT new items
        if (!newItems.isEmpty()) {
            String insertSql = buildInsertSql(tableName, writeableFields);

            try (PreparedStatement pstmt = conn.prepareStatement(
                    insertSql, Statement.RETURN_GENERATED_KEYS)) {

                for (T item : newItems) {
                    for (int i = 0; i < writeableFields.size(); i++) {
                        Field f      = writeableFields.get(i);
                        Object value = f.get(item);
                        if (f.getAnnotation(Column.class).nullableforeignKey()
                                && (value == null || (int) value == 0)) {
                            pstmt.setObject(i + 1, null);
                        } else {
                            pstmt.setObject(i + 1, value);
                        }
                    }
                    pstmt.addBatch();
                }
                pstmt.executeBatch();

                // Capture generated ids and set them back on the Java objects.
                // propagateGeneratedKeys searches localFields for the upsertIgnore=true
                // field which IS the pkField so we pass localFields unchanged.
                propagateGeneratedKeys(pstmt, newItems, localFields);
            }
        }

        // UPDATE existing items
        if (!existingItems.isEmpty()) {
            String updateSql = buildUpdateByPkSql(tableName, writeableFields, pkField);

            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                for (T item : existingItems) {
                    // SET parameters: all writeable fields in order
                    for (int i = 0; i < writeableFields.size(); i++) {
                        Field f      = writeableFields.get(i);
                        Object value = f.get(item);
                        if (f.getAnnotation(Column.class).nullableforeignKey()
                                && (value == null || (int) value == 0)) {
                            pstmt.setObject(i + 1, null);
                        } else {
                            pstmt.setObject(i + 1, value);
                        }
                    }
                    // WHERE parameter: the primary key
                    pstmt.setObject(writeableFields.size() + 1, pkField.get(item));
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                // No generated keys to capture id was already set on these items.
            }
        }
    }

    /**
     * Builds a plain INSERT statement (no MERGE, no ON DUPLICATE KEY).
     * Used by executePkOnlySplitBatch for new items whose AUTO_INCREMENT id is 0.
     *
     * Output form:
     *   INSERT INTO `tableName` (`col1`, `col2`, ...) VALUES (?, ?, ...)
     *
     * The AUTO_INCREMENT id column is intentionally absent from columns — it was
     * excluded from writeableFields by getUpsertFields (upsertIgnore=true).
     *
     * @param tableName the target table
     * @param columns   the writeable fields (already excludes upsertIgnore fields)
     * @return parameterised INSERT SQL string
     */
    protected String buildInsertSql(String tableName, List<Field> columns) {
        StringBuilder cols = new StringBuilder();
        StringBuilder vals = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            cols.append(String.format("`%s`", columns.get(i).getAnnotation(Column.class).name()));
            vals.append("?");
            if (i < columns.size() - 1) {
                cols.append(", ");
                vals.append(", ");
            }
        }

        return "INSERT INTO `" + tableName + "` (" + cols + ") VALUES (" + vals + ")";
    }

    /**
     * Builds an UPDATE statement that matches on the primary key column.
     * Used by executePkOnlySplitBatch for existing items whose id > 0.
     *
     * Output form:
     *   UPDATE `tableName` SET `col1`=?, `col2`=?, ... WHERE `pkCol`=?
     *
     * The pk column value is supplied as the LAST parameter in the PreparedStatement
     * (after all the SET values), matching the order in executePkOnlySplitBatch.
     *
     * @param tableName  the target table
     * @param setColumns the fields to update (already excludes upsertIgnore fields)
     * @param pkField    the AUTO_INCREMENT primary key field (used in WHERE clause)
     * @return parameterised UPDATE SQL string
     */
    protected String buildUpdateByPkSql(String tableName, List<Field> setColumns, Field pkField) {
        StringBuilder sql = new StringBuilder("UPDATE `" + tableName + "` SET ");

        for (int i = 0; i < setColumns.size(); i++) {
            sql.append(String.format("`%s` = ?",
                    setColumns.get(i).getAnnotation(Column.class).name()));
            if (i < setColumns.size() - 1) sql.append(", ");
        }

        sql.append(String.format(" WHERE `%s` = ?",
                pkField.getAnnotation(Column.class).name()));

        return sql.toString();
    }

    protected String buildUpsertSql(String tableName, List<Field> allColumns, List<Field> keyColumns) {
        StringBuilder sql = new StringBuilder("MERGE INTO " + tableName + " (");
        StringBuilder values = new StringBuilder();
        StringBuilder keys = new StringBuilder();

        // 1. Build Column list and Value placeholders
        for (int i = 0; i < allColumns.size(); i++) {
            sql.append(String.format("`%s`", allColumns.get(i).getAnnotation(Column.class).name()));
            values.append("?");
            if (i < allColumns.size() - 1) {
                sql.append(", ");
                values.append(", ");
            }
        }

        // 2. Build the KEY clause (The columns to match on)
        for (int i = 0; i < keyColumns.size(); i++) {
            keys.append(String.format("`%s`", keyColumns.get(i).getAnnotation(Column.class).name()));
            if (i < keyColumns.size() - 1) keys.append(", ");
        }

        return sql.append(") KEY (").append(keys)
                .append(") VALUES (").append(values).append(")").toString();
    }

    /**
     * Builds the FROM clause with JOINs for the entire inheritance hierarchy.
     * Example: "students t JOIN users p ON t.id = p.id"
     */
    protected String buildJoinedFromClause(Class<?> clazz) {
        Table tableAnn = clazz.getAnnotation(Table.class);  // Child table
        String alias = "t"; // Child alias
        StringBuilder from = new StringBuilder(tableAnn.name() + " " + alias); // Initial SQL
        String childId = getPrimaryIdColumnName(clazz); // Initial primary id for joining.
        // Get the full hierarchy.
        List<Class<?>> hierarchy = getTableHierarchy(clazz);
        // Pseudo windowing function to consider 2 table hierarchies at a time, pseudo cause we ignore the Child table.
        for (int i = hierarchy.size() - 2; i >= 0; i--) {  // Minus 2 to ignore child, which is last in hierarchy.
            Class<?> parent = hierarchy.get(i);
            String parentTable = parent.getAnnotation(Table.class).name();
            String parentAlias = "p" + i;
            // Find Primary ID column for joining
            String parentId = getPrimaryIdColumnName(parent);
            // Process the pair (current, next)
            from.append(String.format(" JOIN %s %s ON %s.%s = %s.%s",
                    parentTable, parentAlias, alias, childId, parentAlias, parentId));
            // Update alias and childId so the next join uses previous parent as the new child.
            alias = parentAlias;
            childId = parentId;
        }
        return from.toString();
    }

    /**
     * NOTE: ADD Observer Week
     * -
     * Upserts a single object into the database.
     * Reuses the upsertAll logic for consistency.
     */
    public <T> void upsert(T item) throws SQLException, IllegalAccessException {
        if (item == null) return;
        upsertAll(Collections.singletonList(item));
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Generic Row Mapper that uses reflection to map ResultSet columns
     * to fields annotated with @Column.
     */
    private <T> QueryHandler<T> autoMapper(Class<T> clazz) {
        List<Field> allFields = getAllAnnotatedFields(clazz); // Use the recursive version we built
        return rs -> {
            try {
                T dto = clazz.getDeclaredConstructor().newInstance();
                for (Field field : allFields) {
                    String colName = field.getAnnotation(Column.class).name();
                    try {
                        // We use rs.getObject(colName) but catch if the column isn't in the SQL result
                        Object value = rs.getObject(colName);
                        if (value != null) {
                            field.setAccessible(true);
                            // Handle java.sql.Timestamp conversions which is a special case.
                            // If other cases arise, consider redesigning and refactoring, perhaps with a HashMap.
                            if (value instanceof Timestamp) {
                                field.set(dto, ((Timestamp) value).toLocalDateTime());  // This assumes LocalDateTime
                            } else if (value instanceof Date) {
                                field.set(dto, ((Date) value).toLocalDate());  // This assumes LocalDate
                            } else {
                                field.set(dto, value);
                            }
                        }
                    } catch (SQLException e) {
                        System.out.printf("~~~ Skipped %s as its not in SQL Result ~~~%n", colName);
                    }
                }
                return dto;
            } catch (Exception e) {
                throw new SQLException("Mapping failed for: " + clazz.getSimpleName(), e);
            }
        };
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Fetches a single record by a specific column value (e.g. id), and handles class hierarchies.
     * TODO: Modify fetchOne to take Optional Filter parameter allowing for additional SQL Filters.
     */
    public <T> T fetchOne(Class<T> clazz, String idColumn, Object idValue) throws SQLException {
        String joinedFrom = buildJoinedFromClause(clazz);
        // Note: We use "t." + idColumn to ensure we target the leaf table alias
        String sql = "SELECT * FROM " + joinedFrom + " WHERE t." + idColumn + " = ? LIMIT 1";
        return fetch(sql, autoMapper(clazz), idValue);
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Fetches a list of records by a specific column value (e.g., Foreign Key).
     * TODO: Modify fetchMany to take Optional Filter parameter allowing for additional SQL Filters.
     */
    public <T> List<T> fetchMany(Class<T> clazz, String fkColumn, Object value) throws SQLException {
        String joinedFrom = buildJoinedFromClause(clazz);
        String sql = "SELECT * FROM " + joinedFrom + " WHERE t." + fkColumn + " = ?";
        return fetchList(sql, autoMapper(clazz), value);
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Fetches a list of related objects across a Many-to-Many join table.
     * TODO: Modify fetchManyToMany to take Optional Filter parameter allowing for additional SQL Filters.
     */
    public <T> List<T> fetchManyToMany(Class<T> targetClass, String joinTable,
                                       String joinCol, String invJoinCol, Object sourceId) throws SQLException {
        String targetTable = targetClass.getAnnotation(Table.class).name();
        String targetIdCol = getPrimaryIdColumnName(targetClass);

        // INHERITANCE CHECK to handle Model Inheritance hierarchies.
        // If the parent has a @Table, we must JOIN it to handle cases like: User -> Student or User -> Faculty
        String fromClause = buildJoinedFromClause(targetClass);

        // Example: SELECT s.* FROM sections s JOIN enrollments e ON s.section_id = e.section_id WHERE e.student_id = ?
        String sql = String.format(
                "SELECT * FROM %s JOIN %s j ON t.%s = j.%s WHERE j.%s = ?",
                fromClause, joinTable, targetIdCol, invJoinCol, joinCol
        );

        return fetchList(sql, autoMapper(targetClass), sourceId);
    }

    /**
     * Deletes a single object from the database.
     * Reuses the deleteAll logic to ensure hierarchical integrity.
     */
    public <T> void delete(T item) throws SQLException, IllegalAccessException {
        if (item == null) return;
        deleteAll(Collections.singletonList(item));
    }

    /**
     * Deletes a list of objects from the database.
     * Handles class hierarchies by deleting from the most specific table (child)
     * up to the most general table (parent).
     */
    public <T> void deleteAll(List<T> items) throws SQLException, IllegalAccessException {
        if (items == null || items.isEmpty()) return;

        Connection conn = dataSource.getConnection();
        try {
            conn.setAutoCommit(false); // Start Transaction

            Class<?> leafClass = items.get(0).getClass();
            List<Class<?>> hierarchy = getTableHierarchy(leafClass);

            // IMPORTANT: We must delete in REVERSE order of insertion.
            // If hierarchy is [User, Student], we must delete from Student then User.
            List<Class<?>> reverseHierarchy = new ArrayList<>(hierarchy);
            Collections.reverse(reverseHierarchy);

            for (Class<?> clazz : reverseHierarchy) {
                Table tableAnn = clazz.getAnnotation(Table.class);
                if (tableAnn == null) continue;

                // We identify the row to delete using the Primary ID defined in the hierarchy
                String primaryKeyColName = getPrimaryIdColumnName(leafClass);
                Optional<Field> oPrimaryField = getPrimaryIdColumn(leafClass);

                if (oPrimaryField.isEmpty()) {
                    throw new SQLException("Delete failed: No primary key field found for " + leafClass.getSimpleName());
                }

                Field primaryField = oPrimaryField.get();
                primaryField.setAccessible(true);

                String sql = String.format("DELETE FROM %s WHERE %s = ?", tableAnn.name(), primaryKeyColName);

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    for (T item : items) {
                        Object idValue = primaryField.get(item);
                        if (idValue == null) continue; // Cannot delete a record without an ID

                        pstmt.setObject(1, idValue);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }
            conn.commit(); // Success!
        } catch (Exception e) {
            conn.rollback(); // Undo everything on failure
            throw new SQLException("Delete transaction failed. Changes rolled back.", e);
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    /**
     * Executes UPDATE, INSERT, or DELETE and returns affected rows.
     */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setParameters(pstmt, params);
            return pstmt.executeUpdate();
        }
    }

    /**
     * Executes INSERT and returns the auto-generated ID.
     */
    public int executeInsert(String sql, Object... params) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(pstmt, params);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    // ======================================================================================
    // UTILS & LIFECYCLE
    // ======================================================================================

    private void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }

    public void shutdown() {
        if (dataSource != null) dataSource.close();
    }

    /**
     * Initialize complete database schema for all 14 weeks - 2 Exam weeks.
     */
    private void initializeDatabase() {
        try {
            System.out.println("Initializing database schema...");

            // ================================================================
            // WEEK 1 & 2: Core User Tables (Singleton, Factory)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +  // Increased for hashed passwords
                    "user_type VARCHAR(20) NOT NULL, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "phone VARCHAR(20)," +
                    "email VARCHAR(100), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "last_login TIMESTAMP, " +
                    "is_active BOOLEAN DEFAULT TRUE," +
                    "UNIQUE(email))");

            executeUpdate("CREATE TABLE IF NOT EXISTS auth_sessions (" +
                    "auth_token VARCHAR(255) PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "state VARCHAR(50) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "expires_at TIMESTAMP NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
                    "id INT PRIMARY KEY, " +
                    "student_id VARCHAR(20) UNIQUE NOT NULL, " +
                    "gpa DECIMAL(3,2) DEFAULT 0.00, " +
                    "enrollment_status VARCHAR(20) DEFAULT 'ACTIVE', " +
                    "academic_standing VARCHAR(20) DEFAULT 'GOOD_STANDING', " +
                    "classification VARCHAR(20), " +  // FRESHMAN, SOPHOMORE, etc.
                    "major VARCHAR(100), " +
                    "minor VARCHAR(100), " +
                    "advisor_id INT, " +
                    "FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE)");

            executeUpdate("CREATE TABLE IF NOT EXISTS faculty (" +
                    "id INT PRIMARY KEY, " +
                    "employee_id VARCHAR(20) UNIQUE NOT NULL, " +
                    "department VARCHAR(50), " +
                    "title VARCHAR(50), " +  // Professor, Associate Professor, etc.
                    "office_location VARCHAR(100), " +
                    "office_hours TEXT, " +
                    "hire_date DATE, " +
                    "FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE)");

            // ================================================================
            // WEEK 3: Authentication & Security (Strategy Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS authentication_methods (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "method_type VARCHAR(50) NOT NULL, " +  // BASIC, SECURE, TWO_FACTOR, BIOMETRIC
                    "is_primary BOOLEAN DEFAULT FALSE, " +
                    "is_enabled BOOLEAN DEFAULT TRUE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");

            executeUpdate("CREATE TABLE IF NOT EXISTS two_factor_codes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "code VARCHAR(10) NOT NULL, " +
                    "code_type VARCHAR(20) NOT NULL, " +  // SMS, EMAIL, AUTHENTICATOR
                    "generated_at TIMESTAMP NOT NULL, " +
                    "expires_at TIMESTAMP NOT NULL, " +
                    "used_at TIMESTAMP, " +
                    "is_used BOOLEAN DEFAULT FALSE, " +
                    "attempts INT DEFAULT 0, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");

            executeUpdate("CREATE TABLE IF NOT EXISTS password_history (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "password_hash VARCHAR(255) NOT NULL, " +
                    "changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "changed_by INT, " +  // Admin override capability
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");

            executeUpdate("CREATE TABLE IF NOT EXISTS password_reset_tokens (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "token VARCHAR(255) UNIQUE NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "expires_at TIMESTAMP NOT NULL, " +
                    "used_at TIMESTAMP, " +
                    "is_used BOOLEAN DEFAULT FALSE, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");

            executeUpdate("CREATE TABLE IF NOT EXISTS login_attempts (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL, " +
                    "attempt_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "status VARCHAR(20) NOT NULL, " +
                    "ip_address VARCHAR(45), " +
                    "user_agent TEXT, " +
                    "failure_reason VARCHAR(100))");

            executeUpdate("CREATE TABLE IF NOT EXISTS password_policies (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "policy_name VARCHAR(50) UNIQUE NOT NULL, " +
                    "min_length INT DEFAULT 8, " +
                    "require_uppercase BOOLEAN DEFAULT TRUE, " +
                    "require_lowercase BOOLEAN DEFAULT TRUE, " +
                    "require_digit BOOLEAN DEFAULT TRUE, " +
                    "require_special BOOLEAN DEFAULT TRUE, " +
                    "max_age_days INT DEFAULT 90, " +
                    "history_count INT DEFAULT 5, " +  // Can't reuse last 5 passwords
                    "is_active BOOLEAN DEFAULT TRUE)");

            // ================================================================
            // WEEK 4: Notifications (Observer Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS notifications (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "`type` VARCHAR(50) NOT NULL, " +  // GRADE_CHANGE, REGISTRATION, PAYMENT, etc.
                    "message TEXT NOT NULL, " +
                    "priority VARCHAR(20) DEFAULT 'MEDIUM', " +  // HIGH, MEDIUM, LOW
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "read_at TIMESTAMP, " +
                    "read_status BOOLEAN DEFAULT FALSE, " +
                    "deleted_at TIMESTAMP, " +
                    "metadata TEXT, " +  // JSON for additional data
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");

            executeUpdate("CREATE TABLE IF NOT EXISTS notification_preferences (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "notification_type VARCHAR(50) NOT NULL, " +
                    "email_enabled BOOLEAN DEFAULT TRUE, " +
                    "sms_enabled BOOLEAN DEFAULT FALSE, " +
                    "push_enabled BOOLEAN DEFAULT TRUE, " +
                    "frequency VARCHAR(20) DEFAULT 'IMMEDIATE', " +  // IMMEDIATE, DIGEST, DISABLED
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                    "UNIQUE(user_id, notification_type))");

            // ================================================================
            // WEEK 5: Commands & Transactions (Command Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS command_history (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "command_type VARCHAR(50) NOT NULL, " +  // REGISTER, DROP, PAYMENT, etc.
                    "command_data TEXT, " +  // JSON serialized command
                    "executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "undone_at TIMESTAMP, " +
                    "is_undone BOOLEAN DEFAULT FALSE, " +
                    "success BOOLEAN NOT NULL, " +
                    "error_message TEXT, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");

            // ================================================================
            // WEEK 5-11: Course Management (Multiple Patterns)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS departments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "code VARCHAR(10) UNIQUE NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "chair_id INT, " +
                    "budget DECIMAL(12,2), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (chair_id) REFERENCES faculty(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS programs (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "code VARCHAR(20) UNIQUE NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "degree_type VARCHAR(20) NOT NULL, " +  // BS, BA, MS, MA, PhD
                    "department_id INT NOT NULL, " +
                    "total_credits_required INT DEFAULT 120, " +
                    "is_active BOOLEAN DEFAULT TRUE, " +
                    "FOREIGN KEY (department_id) REFERENCES departments(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS courses (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "code VARCHAR(20) UNIQUE NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "description TEXT, " +
                    "credits DOUBLE NOT NULL, " +
                    "department_id INT NOT NULL, " +
                    "level VARCHAR(20), " +  // UNDERGRADUATE, GRADUATE
                    "is_active BOOLEAN DEFAULT TRUE, " +
                    "FOREIGN KEY (department_id) REFERENCES departments(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS course_prerequisites (" +
                    "course_id INT NOT NULL, " +
                    "prerequisite_id INT NOT NULL, " +
                    "is_corequisite BOOLEAN DEFAULT FALSE, " +
                    "PRIMARY KEY (course_id, prerequisite_id), " +
                    "FOREIGN KEY (course_id) REFERENCES courses(id), " +
                    "FOREIGN KEY (prerequisite_id) REFERENCES courses(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS sections (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "course_id INT NOT NULL, " +
                    "section_number VARCHAR(10) NOT NULL, " +
                    "semester VARCHAR(20) NOT NULL, " +
                    "`year` INT NOT NULL, " +
                    "capacity INT NOT NULL, " +
                    "enrolled INT DEFAULT 0, " +
                    "faculty_id INT, " +
                    "room VARCHAR(50), " +
                    "status VARCHAR(20) DEFAULT 'OPEN', " +  // OPEN, CLOSED, CANCELLED
                    "FOREIGN KEY (course_id) REFERENCES courses(id), " +
                    "FOREIGN KEY (faculty_id) REFERENCES faculty(id), " +
                    "UNIQUE(course_id, section_number, semester, `year`))");

            executeUpdate("CREATE TABLE IF NOT EXISTS section_meeting_times (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "section_id INT NOT NULL, " +
                    "day_of_week VARCHAR(10) NOT NULL, " +
                    "start_time TIME NOT NULL, " +
                    "end_time TIME NOT NULL, " +
                    "room VARCHAR(50), " +
                    "FOREIGN KEY (section_id) REFERENCES sections(id) ON DELETE CASCADE)");

            // ================================================================
            // WEEK 6: Enrollment & State (State Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS enrollments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "section_id INT NOT NULL, " +
                    "enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "status VARCHAR(20) NOT NULL, " +  // ENROLLED, DROPPED, WITHDRAWN, COMPLETED
                    "grade VARCHAR(5), " +
                    "grade_points DECIMAL(3,2), " +
                    "midterm_grade VARCHAR(5), " +
                    "final_grade VARCHAR(5), " +
                    "graded_at TIMESTAMP, " +
                    "dropped_at TIMESTAMP, " +
                    "drop_reason TEXT, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id), " +
                    "FOREIGN KEY (section_id) REFERENCES sections(id), " +
                    "UNIQUE(student_id, section_id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS waitlist (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "section_id INT NOT NULL, " +
                    "position INT NOT NULL, " +
                    "added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "removed_date TIMESTAMP, " +
                    "status VARCHAR(20) DEFAULT 'ACTIVE', " +  // ACTIVE, ENROLLED, REMOVED
                    "notification_sent BOOLEAN DEFAULT FALSE, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id), " +
                    "FOREIGN KEY (section_id) REFERENCES sections(id), " +
                    "UNIQUE(student_id, section_id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS registration_periods (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "semester VARCHAR(20) NOT NULL, " +
                    "`year` INT NOT NULL, " +
                    "open_date TIMESTAMP NOT NULL, " +
                    "close_date TIMESTAMP NOT NULL, " +
                    "late_registration_end TIMESTAMP, " +
                    "current_state VARCHAR(20) NOT NULL, " +  // NOT_OPEN, OPEN, LATE, CLOSED
                    "UNIQUE(semester, `year`))");

            executeUpdate("CREATE TABLE IF NOT EXISTS transcript_requests (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "request_type VARCHAR(20) NOT NULL, " +  // OFFICIAL, UNOFFICIAL
                    "recipient_name VARCHAR(100), " +
                    "recipient_address TEXT, " +
                    "request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "status VARCHAR(20) NOT NULL, " +  // PENDING, PROCESSING, READY, SENT, CANCELLED, FAILED
                    "tracking_number VARCHAR(50), " +
                    "fee DECIMAL(6,2), " +
                    "is_rush BOOLEAN DEFAULT FALSE, " +
                    "completed_date TIMESTAMP, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");

            // ================================================================
            // WEEK 7: Permissions & Restrictions (Decorator Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS user_roles (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "role_name VARCHAR(50) NOT NULL, " +  // STUDENT, FACULTY, HONORS, ATHLETE, etc.
                    "granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "expires_at TIMESTAMP, " +
                    "granted_by INT, " +
                    "is_active BOOLEAN DEFAULT TRUE, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id), " +
                    "UNIQUE(user_id, role_name))");

            executeUpdate("CREATE TABLE IF NOT EXISTS permissions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "role_name VARCHAR(50) NOT NULL, " +
                    "feature_code VARCHAR(50) NOT NULL, " +
                    "can_access BOOLEAN DEFAULT TRUE, " +
                    "UNIQUE(role_name, feature_code))");

            executeUpdate("CREATE TABLE IF NOT EXISTS permission_grants (" +
                    "id  INT AUTO_INCREMENT PRIMARY KEY," +
            "faculty_id  INT NOT NULL," +
            "student_id  INT NOT NULL," +
            "section_id  INT NOT NULL," +
            "granted_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "expires_at  TIMESTAMP NOT NULL," +
            "is_used     BOOLEAN DEFAULT FALSE," +
            "used_at     TIMESTAMP," +
            "is_active   BOOLEAN DEFAULT TRUE," +
            "notes       TEXT," +
            "FOREIGN KEY (faculty_id) REFERENCES faculty(id)," +
            "FOREIGN KEY (student_id) REFERENCES students(id)," +
            "FOREIGN KEY (section_id) REFERENCES sections(id)," +
            "UNIQUE (student_id, section_id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS restrictions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "restriction_type VARCHAR(50) NOT NULL, " +  // FINANCIAL_HOLD, ACADEMIC_PROBATION, etc.
                    "description TEXT, " +
                    "start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "end_date TIMESTAMP, " +
                    "amount DECIMAL(10,2), " +  // For financial holds
                    "is_active BOOLEAN DEFAULT TRUE, " +
                    "created_by INT, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS restriction_impacts (" +
                    "restriction_type VARCHAR(50) NOT NULL, " +
                    "blocked_feature VARCHAR(50) NOT NULL, " +
                    "PRIMARY KEY(restriction_type, blocked_feature))");

            // ================================================================
            // WEEK 5 & 8: Financial Management (Command, Template Patterns)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS payments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "amount DECIMAL(10,2) NOT NULL, " +
                    "payment_type VARCHAR(50) NOT NULL, " +  // TUITION, FEE, HOUSING, etc.
                    "payment_method VARCHAR(50), " +  // CREDIT_CARD, CHECK, CASH, etc.
                    "payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "status VARCHAR(20) NOT NULL, " +  // COMPLETED, PENDING, FAILED, REFUNDED
                    "transaction_id VARCHAR(100), " +
                    "reference_number VARCHAR(100), " +
                    "processed_by INT, " +
                    "notes TEXT, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS payment_plans (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "total_amount DECIMAL(10,2) NOT NULL, " +
                    "installments INT NOT NULL, " +
                    "amount_per_installment DECIMAL(10,2) NOT NULL, " +
                    "start_date DATE NOT NULL, " +
                    "status VARCHAR(20) DEFAULT 'ACTIVE', " +  // ACTIVE, COMPLETED, DEFAULTED
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS payment_plan_installments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "plan_id INT NOT NULL, " +
                    "installment_number INT NOT NULL, " +
                    "due_date DATE NOT NULL, " +
                    "amount DECIMAL(10,2) NOT NULL, " +
                    "paid_date DATE, " +
                    "paid_amount DECIMAL(10,2), " +
                    "status VARCHAR(20) DEFAULT 'PENDING', " +  // PENDING, PAID, OVERDUE
                    "FOREIGN KEY (plan_id) REFERENCES payment_plans(id), " +
                    "UNIQUE(plan_id, installment_number))");

            executeUpdate("CREATE TABLE IF NOT EXISTS student_accounts (" +
                    "student_id INT PRIMARY KEY, " +
                    "current_balance DECIMAL(10,2) DEFAULT 0.00, " +
                    "total_charges DECIMAL(10,2) DEFAULT 0.00, " +
                    "total_payments DECIMAL(10,2) DEFAULT 0.00, " +
                    "total_aid DECIMAL(10,2) DEFAULT 0.00, " +
                    "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS financial_aid (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "aid_type VARCHAR(50) NOT NULL, " +  // GRANT, LOAN, SCHOLARSHIP, WORK_STUDY
                    "aid_name VARCHAR(100) NOT NULL, " +
                    "amount DECIMAL(10,2) NOT NULL, " +
                    "semester VARCHAR(20) NOT NULL, " +
                    "`year` INT NOT NULL, " +
                    "status VARCHAR(20) NOT NULL, " +  // PENDING, APPROVED, DISBURSED, DENIED
                    "application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "approval_date TIMESTAMP, " +
                    "disbursement_date TIMESTAMP, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");

            // ================================================================
            // WEEK 8: Reports (Template Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS report_generations (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "report_type VARCHAR(50) NOT NULL, " +  // TRANSCRIPT, FINANCIAL, TAX, ROSTER
                    "report_format VARCHAR(20), " +  // PDF, HTML, EXCEL, CSV
                    "generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "file_path VARCHAR(255), " +
                    "file_size INT, " +
                    "parameters TEXT, " +  // JSON
                    "FOREIGN KEY (user_id) REFERENCES users(id))");

            // ================================================================
            // WEEK 11: Budget Management (Composite Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS budgets (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "parent_budget_id INT, " +  // For hierarchical budgets
                    "budget_type VARCHAR(50) NOT NULL, " +  // DEPARTMENT, RESEARCH, TEACHING, etc.
                    "owner_id INT, " +  // Faculty ID
                    "fiscal_year VARCHAR(10) NOT NULL, " +
                    "allocated_amount DECIMAL(12,2) NOT NULL, " +
                    "spent_amount DECIMAL(12,2) DEFAULT 0.00, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (parent_budget_id) REFERENCES budgets(id), " +
                    "FOREIGN KEY (owner_id) REFERENCES faculty(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS budget_expenses (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "budget_id INT NOT NULL, " +
                    "description VARCHAR(255) NOT NULL, " +
                    "amount DECIMAL(10,2) NOT NULL, " +
                    "expense_date DATE NOT NULL, " +
                    "category VARCHAR(50), " +
                    "receipt_number VARCHAR(100), " +
                    "approved_by INT, " +
                    "FOREIGN KEY (budget_id) REFERENCES budgets(id), " +
                    "FOREIGN KEY (approved_by) REFERENCES faculty(id))");

            // ================================================================
            // WEEK 11: Program Requirements (Composite Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS program_requirements (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "program_id INT NOT NULL, " +
                    "requirement_group VARCHAR(100) NOT NULL, " +  // CORE, ELECTIVES, GEN_ED
                    "parent_group_id INT, " +  // For nested groups
                    "min_courses INT, " +
                    "min_credits DOUBLE, " +
                    "display_order INT DEFAULT 0, " +
                    "FOREIGN KEY (program_id) REFERENCES programs(id), " +
                    "FOREIGN KEY (parent_group_id) REFERENCES program_requirements(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS requirement_courses (" +
                    "requirement_id INT NOT NULL, " +
                    "course_id INT NOT NULL, " +
                    "is_required BOOLEAN DEFAULT TRUE, " +  // FALSE for elective choices
                    "PRIMARY KEY (requirement_id, course_id), " +
                    "FOREIGN KEY (requirement_id) REFERENCES program_requirements(id), " +
                    "FOREIGN KEY (course_id) REFERENCES courses(id))");

            // ================================================================
            // WEEK 10: External System Integration (Adapter Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS external_transactions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "transaction_type VARCHAR(50) NOT NULL, " +  // PAYMENT, TRANSCRIPT, etc.
                    "external_system VARCHAR(50) NOT NULL, " +  // NBS, NSC, etc.
                    "request_data TEXT, " +  // JSON
                    "response_data TEXT, " +  // JSON
                    "external_id VARCHAR(100), " +
                    "status VARCHAR(20) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "completed_at TIMESTAMP, " +
                    "error_message TEXT)");

            // ================================================================
            // WEEK 14: Audit Trail (Pipeline Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS validation_logs (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "request_id VARCHAR(100) NOT NULL, " +
                    "request_type VARCHAR(50) NOT NULL, " +
                    "user_id INT NOT NULL, " +
                    "handler_name VARCHAR(100) NOT NULL, " +
                    "handler_order INT NOT NULL, " +
                    "validation_result VARCHAR(20) NOT NULL, " +  // PASSED, FAILED, WARNING
                    "error_message TEXT, " +
                    "metadata TEXT, " +  // JSON
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS system_audit_log (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT, " +
                    "action VARCHAR(100) NOT NULL, " +
                    "entity_type VARCHAR(50), " +  // USER, COURSE, ENROLLMENT, etc.
                    "entity_id INT, " +
                    "old_value TEXT, " +  // JSON
                    "new_value TEXT, " +  // JSON
                    "ip_address VARCHAR(45), " +
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");

            // ================================================================
            // INDEXES for Performance
            // ================================================================

            executeUpdate("CREATE INDEX IF NOT EXISTS idx_users_username ON users(username)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_users_type ON users(user_type)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_students_student_id ON students(student_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_faculty_employee_id ON faculty(employee_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_enrollments_student ON enrollments(student_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_enrollments_section ON enrollments(section_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_enrollments_status ON enrollments(status)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_notifications_user ON notifications(user_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_notifications_read ON notifications(read_status)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_notifications_type ON notifications(`type`)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_payments_student ON payments(student_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_sections_semester ON sections(semester, `year`)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_waitlist_section ON waitlist(section_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_restrictions_student ON restrictions(student_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_restrictions_active ON restrictions(is_active)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_perm_grants_student ON permission_grants(student_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_perm_grants_section ON permission_grants(section_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_perm_grants_active ON permission_grants(is_active)");

            System.out.println("✓ Database schema initialized successfully");
            System.out.println("  Total tables created: 40+");

        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database schema", e);
        }
    }

    /**
     * Insert default/seed data for testing
     */
    public void seedDatabase() {
        try {
            System.out.println("Seeding database with default data...");

            // Default password policy
            executeInsert("INSERT INTO password_policies (policy_name, min_length, " +
                    "require_uppercase, require_lowercase, require_digit, require_special, " +
                    "max_age_days, history_count) VALUES " +
                    "('DEFAULT', 8, TRUE, TRUE, TRUE, TRUE, 90, 5)");

            // Default permissions for roles
            executeInsert("INSERT INTO permissions (role_name, feature_code) VALUES " +
                    "('STUDENT', 'REGISTER_COURSES'), " +
                    "('STUDENT', 'VIEW_GRADES'), " +
                    "('STUDENT', 'MAKE_PAYMENT'), " +
                    "('STUDENT', 'VIEW_TRANSCRIPT'), " +
                    "('FACULTY', 'VIEW_CLASS_ROSTER'), " +
                    "('FACULTY', 'ENTER_GRADES'), " +
                    "('FACULTY', 'DROP_STUDENTS'), " +
                    "('HONORS', 'PRIORITY_REGISTRATION'), " +
                    "('HONORS', 'OVERLOAD_CREDITS')");

            // Default restriction impacts. Using Update instead of Insert because this table doesn't have generated
            // keys
            executeUpdate("INSERT INTO restriction_impacts (restriction_type, blocked_feature) VALUES " +
                    "('FINANCIAL_HOLD', 'REGISTER_COURSES'), " +
                    "('FINANCIAL_HOLD', 'VIEW_TRANSCRIPT'), " +
                    "('FINANCIAL_HOLD', 'ORDER_TRANSCRIPT'), " +
                    "('ACADEMIC_PROBATION', 'HONORS_PROGRAMS'), " +
                    "('ACADEMIC_PROBATION', 'STUDY_ABROAD'), " +
                    "('ACADEMIC_PROBATION', 'OVERLOAD_CREDITS')");

            // Sample department
            executeInsert("INSERT INTO departments (code, name) VALUES " +
                    "('CS', 'Computer Science'), " +
                    "('MATH', 'Mathematics'), " +
                    "('ENG', 'English')");

            System.out.println("✓ Database seeded successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error seeding database: " + e.getMessage());
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\Id.java
```java
package edu.advising.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
    boolean isPrimary() default false;
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\ManyToMany.java
```java
package edu.advising.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToMany {
    Class<?> targetEntity();
    String joinTable();
    String joinColumn();        // Points to the "current" object's ID
    String inverseJoinColumn(); // Points to the "target" object's ID
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\ManyToOne.java
```java
package edu.advising.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToOne {
    Class<?> targetEntity();
    String joinColumn(); // The FK in the current table
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\OneToMany.java
```java
package edu.advising.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToMany {
    Class<?> targetEntity();
    String mappedBy(); // The FK in the remote table
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\QueryHandler.java
```java
package edu.advising.core;

import java.sql.ResultSet;
import java.sql.SQLException;

// Functional Interfaces allowing me to pass a Lambda handler into the DatabaseManager
// so the DatabaseManager will handle the connection open/close, and I can still handle
// the data/ResultSet without worrying about the connection pool or database boilerplate.
@FunctionalInterface
public interface QueryHandler<T> {
    T handle(ResultSet rs) throws SQLException;
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\Table.java
```java
package edu.advising.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String name();
    boolean isSubTable() default false;
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\EmailChannel.java
```java
package edu.advising.notifications;

import edu.advising.users.User;

class EmailChannel implements NotificationChannel {
    @Override
    public void send(Notification notification, User user) {
        // Simulate email sending
        System.out.printf("Email sent to %s: %s%n", user.getEmail(), notification.getMessage());
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\Notification.java
```java
package edu.advising.notifications;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Notification - Represents a notification message
 */
public class Notification {
    private int id;
    private String type;
    private String message;
    private String priority; // HIGH, MEDIUM, LOW
    private LocalDateTime timestamp;
    private int userId;
    private boolean read;
    private Map<String, String> metadata;

    public Notification(String type, String message, int userId) {
        this(type, message, userId, "MEDIUM");
    }

    public Notification(String type, String message, int userId, String priority) {
        this.type = type;
        this.message = message;
        this.userId = userId;
        this.priority = priority;
        this.timestamp = LocalDateTime.now();
        this.read = false;
        this.metadata = new HashMap<>();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getPriority() {
        return priority;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        this.read = true;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    @Override
    public String toString() {
        String icon = getIconForType();
        return String.format("[%s] %s - %s (Priority: %s)",
                icon, type, message, priority);
    }

    private String getIconForType() {
        switch (type) {
            case "GRADE_CHANGE":
                return "📝";
            case "REGISTRATION":
                return "📚";
            case "PAYMENT":
                return "💳";
            case "FINANCIAL_AID":
                return "💰";
            case "DOCUMENT":
                return "📄";
            case "RESTRICTION":
                return "⚠️";
            case "WAITLIST":
                return "⏳";
            default:
                return "🔔";
        }
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\NotificationChannel.java
```java
package edu.advising.notifications;

import edu.advising.users.User;

/**
 * NotificationChannel - Different delivery methods (Strategy-like)
 */
public interface NotificationChannel {
    void send(Notification notification, User user);
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\NotificationManager.java
```java
package edu.advising.notifications;

import edu.advising.core.DatabaseManager;
import edu.advising.users.Student;
import edu.advising.users.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationManager - Central notification hub (Subject implementation)
 */
public class NotificationManager implements Subject {
    private List<Observer> observers;
    private List<Notification> notificationHistory;
    private DatabaseManager dbManager;
    private static NotificationManager instance;

    private NotificationManager() {
        this.observers = new ArrayList<>();
        this.notificationHistory = new ArrayList<>();
        this.dbManager = DatabaseManager.getInstance();
    }

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("✓ Observer attached: User ID " + observer.getUserId());
        }
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
        System.out.println("✓ Observer detached: User ID " + observer.getUserId());
    }

    @Override
    public void notifyObservers(Notification notification) {
        // Save to database first
        persistNotification(notification);

        // Add to history
        notificationHistory.add(notification);

        // Notify specific observer(s)
        for (Observer observer : observers) {
            if (observer.getUserId() == notification.getUserId()) {
                observer.update(notification);
            }
        }
    }

    /**
     * Broadcast to all observers (system-wide announcements)
     */
    public void broadcast(String type, String message, String priority) {
        for (Observer observer : observers) {
            Notification notification = new Notification(type, message,
                    observer.getUserId(), priority);
            notifyObservers(notification);
        }
    }

    // Specific notification methods for different events

    public void notifyGradeChange(Student student, String courseCode, String grade) {
        Notification notification = new Notification(
                "GRADE_CHANGE",
                String.format("Grade posted for %s: %s", courseCode, grade),
                student.getId(),
                "HIGH"
        );
        notification.addMetadata("courseCode", courseCode);
        notification.addMetadata("grade", grade);
        notifyObservers(notification);
    }

    public void notifyRegistration(Student student, String courseCode, boolean success) {
        String message = success
                ? String.format("Successfully registered for %s", courseCode)
                : String.format("Registration failed for %s", courseCode);

        Notification notification = new Notification(
                "REGISTRATION",
                message,
                student.getId(),
                success ? "MEDIUM" : "HIGH"
        );
        notification.addMetadata("courseCode", courseCode);
        notification.addMetadata("success", String.valueOf(success));
        notifyObservers(notification);
    }

    public void notifyPaymentReceived(Student student, double amount, String paymentType) {
        Notification notification = new Notification(
                "PAYMENT",
                String.format("Payment of $%.2f received (%s)", amount, paymentType),
                student.getId(),
                "MEDIUM"
        );
        notification.addMetadata("amount", String.valueOf(amount));
        notification.addMetadata("paymentType", paymentType);
        notifyObservers(notification);
    }

    public void notifyFinancialAid(Student student, String aidType, String status, double amount) {
        Notification notification = new Notification(
                "FINANCIAL_AID",
                String.format("%s: %s - $%.2f", aidType, status, amount),
                student.getId(),
                "HIGH"
        );
        notification.addMetadata("aidType", aidType);
        notification.addMetadata("status", status);
        notification.addMetadata("amount", String.valueOf(amount));
        notifyObservers(notification);
    }

    public void notifyDocumentAvailable(User user, String documentName, String documentType) {
        Notification notification = new Notification(
                "DOCUMENT",
                String.format("New document available: %s", documentName),
                user.getId(),
                "MEDIUM"
        );
        notification.addMetadata("documentName", documentName);
        notification.addMetadata("documentType", documentType);
        notifyObservers(notification);
    }

    public void notifyRestriction(Student student, String restrictionType, String details) {
        Notification notification = new Notification(
                "RESTRICTION",
                String.format("Account restriction: %s - %s", restrictionType, details),
                student.getId(),
                "HIGH"
        );
        notification.addMetadata("restrictionType", restrictionType);
        notification.addMetadata("details", details);
        notifyObservers(notification);
    }

    public void notifyWaitlistUpdate(Student student, String courseCode, int position) {
        Notification notification = new Notification(
                "WAITLIST",
                String.format("WaitlistEntry update for %s: Position #%d", courseCode, position),
                student.getId(),
                "MEDIUM"
        );
        notification.addMetadata("courseCode", courseCode);
        notification.addMetadata("position", String.valueOf(position));
        notifyObservers(notification);
    }

    /**
     * Get unread notifications for a user
     */
    public List<Notification> getUnreadNotifications(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND read_status = FALSE " +
                "ORDER BY created_at DESC";

        try {
            return dbManager.fetchList(sql, rs -> {
                // This lambda runs ONCE per row found in the database
                Notification n = new Notification(
                        rs.getString("type"),
                        rs.getString("message"),
                        rs.getInt("user_id")
                );
                n.setId(rs.getInt("id"));
                return n;
            }, userId);
        } catch (SQLException e) {
            System.err.println("Error fetching unread notifications: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on failure
        }
    }

    /**
     * Mark notification as read
     */
    public void markAsRead(int notificationId) {
        try {
            String sql = "UPDATE notifications SET read_status = TRUE WHERE id = ?";
            dbManager.executeUpdate(sql, notificationId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get notification history for a user
     */
    public List<Notification> getNotificationHistory(int userId, int limit) {
        try {
            String sql = "SELECT * FROM notifications WHERE user_id = ? " +
                    "ORDER BY created_at DESC LIMIT ?";
            return dbManager.fetchList(sql, rs -> {
                Notification n = new Notification(
                        rs.getString("type"),
                        rs.getString("message"),
                        rs.getInt("user_id")
                );
                n.setId(rs.getInt("id"));
                if (rs.getBoolean("read_status")) {
                    n.markAsRead();
                }
                return n;
            }, userId, limit);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void persistNotification(Notification notification) {
        try {
            String sql = "INSERT INTO notifications (user_id, message, type, created_at, read_status) " +
                    "VALUES (?, ?, ?, ?, ?)";
            notification.setId(
                    dbManager.executeInsert(sql, notification.getUserId(), notification.getMessage(),
                            notification.getType(), Timestamp.valueOf(notification.getTimestamp()),
                            notification.isRead())
            );
        } catch (SQLException e) {
            System.err.println("Error persisting notification: " + e.getMessage());
        }
    }

    public int getObserverCount() {
        return observers.size();
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\NotificationPref.java
```java
package edu.advising.notifications;

import edu.advising.core.Column;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Id;
import edu.advising.core.Table;

import javax.xml.crypto.Data;
import java.sql.SQLException;

/**
 * NotificationPref - Represents a user's notification preference
 */
@Table(name = "notification_preferences")
public class NotificationPref {
    @Id
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "notification_type")
    private String notificationType;
    @Id
    @Column(name = "user_id")
    private int userId;
    @Column(name = "email_enabled")
    private boolean emailEnabled;
    @Column(name = "sms_enabled")
    private boolean smsEnabled;
    @Column(name = "push_enabled")
    private boolean pushEnabled;
    @Column(name = "frequency")
    private String frequency;  // IMMEDIATE, DIGEST, DISABLED
    private DatabaseManager dbManager;

    public NotificationPref(String type, int userId) {
        this(type, userId, true, true, true, "IMMEDIATE");
    }

    public NotificationPref(String type, int userId, boolean emailEnabled, boolean smsEnabled, boolean pushEnabled,
                            String frequency) {
        this.notificationType = type;
        this.userId = userId;
        this.emailEnabled = emailEnabled;
        this.smsEnabled = smsEnabled;
        this.pushEnabled = pushEnabled;
        // TODO: Should make restricted fields like this enums.
        this.frequency = frequency; // IMMEDIATE, DIGEST, DISABLED
        this.dbManager = DatabaseManager.getInstance();
    }

    public boolean shouldNotify() {
        return emailEnabled || smsEnabled || pushEnabled;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public boolean isSmsEnabled() {
        return smsEnabled;
    }

    public void setSmsEnabled(boolean smsEnabled) {
        this.smsEnabled = smsEnabled;
    }

    public boolean isPushEnabled() {
        return pushEnabled;
    }

    public void setPushEnabled(boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void saveNotificationPreference()
            throws SQLException {
        try {
            dbManager.upsert(this);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            System.out.println("Error upserting to database because model is not annotated.");
        }
        /*
        // LOOK HOW MUCH HARDER IT USED TO BE!!! //
        String sql = "INSERT INTO notification_preferences " +
                "(user_id, notification_type, email_enabled, sms_enabled, push_enabled) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "email_enabled = ?, sms_enabled = ?, push_enabled = ?";

        dbManager.executeUpdate(
                sql, userId, notificationType,
                emailEnabled, smsEnabled, pushEnabled,
                emailEnabled, smsEnabled, pushEnabled);
         */
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - Email: %s, SMS: %s, Push: %s (Frequency: %s)",
                userId, notificationType, emailEnabled, smsEnabled, pushEnabled, frequency);
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\NotificationPreferences.java
```java
package edu.advising.notifications;

import edu.advising.core.DatabaseManager;

import java.sql.SQLException;
import java.util.*;

/**
 * NotificationPreferences - User notification settings
 */
public class NotificationPreferences {
    private int userId;
    private DatabaseManager dbManager;
    private List<NotificationPref> collection;

    public NotificationPreferences(int userId) {
        // TODO: Store various channels in database too, maybe in EAV like structure, allowing channels to be dynamic.
        //   Or use a factory to load a NotificationChannel(s) per Notification.
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
        this.collection = loadNotificationPreferences(userId);
    }

    /*
     * Set user notification preferences.
     */
    public void saveNotificationPreferences()
            throws SQLException {
        try {
            dbManager.upsertAll(this.collection);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            System.out.println("Error upserting to database because model is not annotated.");
        }
    }

    /*
     * Add a new preference to NotificationPreferences
     */
    public void addNotificationPref(NotificationPref pref) {
        this.collection.add(pref);
    }

    /*
     * Check preferences before sending
     */
    private List<NotificationPref> loadNotificationPreferences(int userId) {
        // TODO: Add frequncy to this SQL, IMMEDIATE, DIGEST, DISABLED.
        String sql = "SELECT * FROM notification_preferences WHERE user_id = ?";

        try {
            return dbManager.fetchList(sql, rs -> {
                // This lambda runs ONCE per row found in the database
                NotificationPref n = new NotificationPref(
                        rs.getString("notification_type"),
                        rs.getInt("user_id"),
                        rs.getBoolean("email_enabled"),
                        rs.getBoolean("sms_enabled"),
                        rs.getBoolean("push_enabled"),
                        rs.getString("frequency")
                );
                n.setId(rs.getInt("id"));
                return n;
            }, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading notification preferences");
            return new ArrayList<NotificationPref>();
        }
    }

    public Optional<NotificationPref> getNotificationPref(String type) {
        return this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
    }

    public boolean shouldNotify(String type) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        return onp.map(np -> np.isEmailEnabled() || np.isSmsEnabled() || np.isPushEnabled())
                .orElse(false);
    }

    public void disableNotificationTypeChannel(String type, String channel) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        onp.ifPresent(np -> {
            switch (channel) {
                case "EMAIL":
                    np.setEmailEnabled(false);
                    break;
                case "SMS":
                    np.setSmsEnabled(false);
                    break;
                case "PUSH":
                    np.setPushEnabled(false);
                    break;
            }
        });
    }

    public void disableNotificationType(String type) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        onp.ifPresent(np -> {
                            np.setEmailEnabled(false); np.setSmsEnabled(false); np.setPushEnabled(false);
        });
    }

    public void enableNotificationTypeChannel(String type, String channel) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        onp.ifPresent(np -> {
            switch (channel) {
                case "EMAIL":
                    np.setEmailEnabled(true);
                    break;
                case "SMS":
                    np.setSmsEnabled(true);
                    break;
                case "PUSH":
                    np.setPushEnabled(true);
                    break;
            }
        });
    }

    public void enableNotificationType(String type) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        onp.ifPresent(np -> {
            np.setEmailEnabled(true); np.setSmsEnabled(true); np.setPushEnabled(true);
        });
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\ObservableFaculty.java
```java
package edu.advising.notifications;

import edu.advising.users.Faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Enhanced Faculty with Observer implementation
 */
class ObservableFaculty extends Faculty implements Observer {
    private List<Notification> notifications;
    private NotificationPreferences preferences;

    /*
     * Internal constructor allowing internal objects to set id during factory method copy.
     */
    private ObservableFaculty(int id, String username, String password, String email,
                             String firstName, String lastName, String employeeId, String department) {
        super(username, password, email, firstName, lastName, employeeId, department);
        this.setId(id);
        this.notifications = new ArrayList<>();
        this.preferences = new NotificationPreferences(this.getId());
    }

    public ObservableFaculty(String username, String password, String email,
                             String firstName, String lastName, String employeeId, String department) {
        super(username, password, email, firstName, lastName, employeeId, department);
        this.notifications = new ArrayList<>();
        this.preferences = new NotificationPreferences(this.getId());
    }

    /**
     * Factory Method to convert/copy Super student-Type Faculty into an ObservableFaculty.
     * @param superObj is the Super-Type Faculty that ObservableFaculty extends, and we want to convert.
     * @return ObservableFaculty with same fields as superObj but extended like the Sub-Type.
     */
    public static ObservableFaculty fromSuperType(Faculty superObj) {
        return new ObservableFaculty(superObj.getId(), superObj.getUsername(), superObj.getPassword(), superObj.getEmail(),
                superObj.getFirstName(), superObj.getLastName(), superObj.getEmployeeId(), superObj.getDepartment());
    }

    public void update(Notification notification) {
        // Check preferences
        Optional<NotificationPref> oPreference = preferences.getNotificationPref(notification.getType());
        if(oPreference.isEmpty()) { return; }
        NotificationPref preference = oPreference.get();
        if (!preference.shouldNotify()) { return; }

        notifications.add(notification);

        // Display notification with priority-based formatting
        String prefix = notification.getPriority().equals("HIGH") ? "❗" : "ℹ️";
        System.out.printf("%s New notification for %s %s: %s%n",
                prefix, getFirstName(), getLastName(), notification);

        // Simulate different delivery channels based on preferences
        if (preference.isEmailEnabled()) {
            new EmailChannel().send(notification, this);
        }
        if (preference.isSmsEnabled()) {
            new SMSChannel().send(notification, this);
        }
        if (preference.isPushEnabled()) {
            new PushChannel().send(notification, this);
        }
    }

    @Override
    public int getUserId() {
        return this.getId();
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public NotificationPreferences getPreferences() {
        return preferences;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\ObservableStudent.java
```java
package edu.advising.notifications;

import edu.advising.users.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Enhanced Student with Observer implementation
 *
 * ORM PERSISTENCE NOTE:
 *   ObservableStudent intentionally carries NO @Table annotation.  It is a
 *   runtime behavioural wrapper, not a DB entity.  Use toSubType() whenever
 *   you need to persist this object via DatabaseManager.upsert().
 *
 *   toSubType():     creates a plain Student whose getClass() == Student.class
 *   fromSuperType(): promotes a plain Student fetched from the DB into a fully initialised ObservableStudent
 *                    copying ALL fields.
 */
public class ObservableStudent extends Student implements Observer {
    //TODO: Should this notifications list be updated from the notifications or notifications_history table?
    private List<Notification> notifications;
    private NotificationPreferences preferences;

    /*
     * Internal constructor allowing internal objects to set id during factory method copy.
     */
    private ObservableStudent(int id, String username, String password, String email,
                             String firstName, String lastName, String studentId) {
        super(username, password, email, firstName, lastName, studentId);
        this.setId(id);
        this.notifications = new ArrayList<>();
        this.preferences = new NotificationPreferences(this.getId());
    }

    public ObservableStudent(String username, String password, String email,
                             String firstName, String lastName, String studentId) {
        super(username, password, email, firstName, lastName, studentId);
        this.notifications = new ArrayList<>();
        this.preferences = new NotificationPreferences(this.getId());
    }

    /**
     * Factory method: creates a plain Student just fetched from the DB via
     * DatabaseManager.fetchOne(Student.class, ...)) into an ObservableStudent.
     *
     * NOTE: I copy every field explicitly as I realized things were missing:
     *   The private constructor only accepts the subset of fields needed to
     *   reconstruct the identity of the object, but ALL mutable fields in
     *   the Student and User super classes must be transferred so that
     *   object memory reads are consistent with what is in the database. If I
     *   omitted a field here, that field would silently return null after conversion.
     */
    public static ObservableStudent fromSuperType(Student s) {
        // Use the private constructor to set the primary key
        ObservableStudent obs = new ObservableStudent(
                s.getId(),
                s.getUsername(),
                s.getPassword(),
                s.getEmail(),
                s.getFirstName(),
                s.getLastName(),
                s.getStudentId()
        );

        // Set User class fields
        obs.userType  = s.getUserType();
        obs.isActive  = s.isActive();  // Had to add getter to User to allow this.
        obs.phone     = s.getPhone();
        obs.lastLogin = s.getLastLogin();  // Had to add getter and change access modifier to allow this.

        // Set Student class fields
        obs.gpa              = s.getGpa();
        obs.enrollmentStatus = s.getEnrollmentStatus();
        obs.academicStanding = s.getAcademicStanding();
        obs.classification   = s.getClassification();
        obs.major            = s.getMajor();
        obs.minor            = s.getMinor();
        obs.advisorId        = s.getAdvisorId();

        return obs;
    }

    /**
     * Creates a plain Student instance populated with all fields from this ObservableStudent.
     *
     * WHY?
     *   Java's getClass() always returns the true underlying class type regardless of type cast.
     *   DatabaseManager.upsertAll() calls items.get(0).getClass() to call getTableHierarchy().
     *   If that returns ObservableStudent.class, which is intentionally not annotated, the
     *   hierarchy is empty and nothing is stored to the database. This method allows the ORM to
     *   receive a genuine Student object so getClass() is Student.class and the hierarchy is
     *   resolved correctly.
     *
     * USAGE:
     *   dbManager.upsert(student.toSubType());
     */
    public Student toSubType() {
        Student s = new Student(
                this.username,
                this.password,
                this.email,
                this.firstName,
                this.lastName,
                this.studentId
        );

        // Set User class fields
        s.setId(this.id);
        s.setUserType(this.userType); // Had to add setter to User to do this
        s.setActive(this.isActive); // Had to add setter to User to do this
        s.setPhone(this.phone);
        s.setLastLogin(this.lastLogin);  // Had to add setter to User to do this

        // Set Student class fields
        s.setGpa(this.gpa);
        s.setEnrollmentStatus(this.enrollmentStatus);
        s.setAcademicStanding(this.academicStanding);
        s.setClassification(this.classification);
        s.setMajor(this.major);
        s.setMinor(this.minor);
        s.setAdvisorId(this.advisorId);

        return s;
    }

    @Override
    public void update(Notification notification) {
        // Check preferences
        Optional<NotificationPref> oPreference = preferences.getNotificationPref(notification.getType());
        if(oPreference.isEmpty()) { return; }
        NotificationPref preference = oPreference.get();
        if (!preference.shouldNotify()) { return; }

        notifications.add(notification);

        // Display notification with priority-based formatting
        String prefix = notification.getPriority().equals("HIGH") ? "❗" : "ℹ️";
        System.out.printf("%s New notification for %s %s: %s%n",
                prefix, getFirstName(), getLastName(), notification);

        // Simulate different delivery channels based on preferences
        if (preference.isEmailEnabled()) {
            new EmailChannel().send(notification, this);
        }
        if (preference.isSmsEnabled()) {
            new SMSChannel().send(notification, this);
        }
        if (preference.isPushEnabled()) {
            new PushChannel().send(notification, this);
        }
    }

    @Override
    public int getUserId() {
        return this.getId();
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public List<Notification> getUnreadNotifications() {
        return notifications.stream()
                .filter(n -> !n.isRead())
                .collect(java.util.stream.Collectors.toList());
    }

    public void viewNotifications() {
        System.out.println("\n=== MY DOCUMENTS / NOTIFICATIONS ===");
        System.out.println("Total: " + notifications.size() +
                " | Unread: " + getUnreadNotifications().size());

        if (notifications.isEmpty()) {
            System.out.println("No notifications");
            return;
        }

        for (Notification n : notifications) {
            String status = n.isRead() ? "✓" : "○";
            System.out.printf("%s %s%n", status, n);
        }
    }

    public NotificationPreferences getPreferences() {
        return preferences;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\Observer.java
```java
// Week 4: OBSERVER PATTERN
// Features Implemented: Communication - My Documents, Grade Notifications, 
//                       Financial Aid alerts, Payment confirmations
// Why Now: Need event-driven notifications across the system

package edu.advising.notifications;

/**
 * Observer - Interface for objects that want to receive notifications
 */
public interface Observer {
    void update(Notification notification);
    int getUserId();
}


```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\PushChannel.java
```java
package edu.advising.notifications;

import edu.advising.users.User;

class PushChannel implements NotificationChannel {
    @Override
    public void send(Notification notification, User user) {
        // Simulate push notification
        System.out.printf("Push notification: %s%n",
                notification.getMessage());
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\SMSChannel.java
```java
package edu.advising.notifications;

import edu.advising.users.User;

class SMSChannel implements NotificationChannel {
    @Override
    public void send(Notification notification, User user) {
        // Simulate SMS sending
        System.out.printf("SMS sent: %s%n", notification.getMessage());
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\Subject.java
```java
package edu.advising.notifications;

/**
 * Subject - Interface for objects that send notifications
 */
public interface Subject {
    void attach(Observer observer);

    void detach(Observer observer);

    void notifyObservers(Notification notification);
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\users\Faculty.java
```java
package edu.advising.users;

import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;
import edu.advising.core.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Faculty - Concrete user type
 */
@Table(name = "faculty", isSubTable = true)
public class Faculty extends User {
    @Id
    @Column(name="employee_id")
    private String employeeId;
    @Column(name="department")
    private String department;
    @Column(name="title")
    private String title;  // Professor, Associate Professor, etc.
    @Column(name="office_location")
    private String officeLocation;
    @Column(name="office_hours")
    private String officeHours;
    @Column(name="hire_date")
    private LocalDate hireDate;

    @OneToMany(targetEntity = Section.class, mappedBy = "faculty_id")
    private List<Section> sections;

    public Faculty() {}

    public Faculty(String username, String password, String email,
                   String firstName, String lastName, String employeeId, String department) {
        super(username, password, email, firstName, lastName);
        this.userType = "FACULTY";
        this.employeeId = employeeId;
        this.department = department;
    }

    @Override
    public void showDashboard() {
        System.out.println("\n=== FACULTY DASHBOARD ===");
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Department: " + department);
        System.out.println("\nAvailable Features:");
        System.out.println("- View Class Roster");
        System.out.println("- Enter Grades");
        System.out.println("- View Schedule");
        System.out.println("- Drop Students");
    }

    // Getters
    public String getEmployeeId() {
        return employeeId;
    }

    public String getDepartment() {
        return department;
    }

    // Setters
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public List<Section> getSections() throws SQLException {
        if (this.sections == null) {
            // Lazy Load: Use the generic fetchMany from DatabaseManager
            this.sections = DatabaseManager.getInstance()
                    .fetchMany(Section.class, "faculty_id", this.id);
        }
        return this.sections;
    }

    public void setSections(List<Section> sections) throws SQLException, IllegalAccessException{
        if(this.getId() == 0) {
            // We need to save this object to get an id to set on the list items.
            DatabaseManager.getInstance().upsert(this);
        }
        // Now, let's add this object's id to the related list items foreign key id
        for(Section s : sections) { s.setFacultyId(this.getId()); }
        // Now let's upsertAll of these list items (i.e. a batch) and set as this object's related field.
        DatabaseManager.getInstance().upsertAll(sections);
        this.sections = sections;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\users\Student.java
```java
package edu.advising.users;

import edu.advising.commands.Section;
import edu.advising.commands.WaitlistEntry;
import edu.advising.core.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * ADD ANNOTATION STUFF ON Command Pattern Week
 * -
 * Student - Concrete user type
 */
@Table(name = "students", isSubTable = true)
public class Student extends User {
    @Id
    @Column(name = "student_id")
    protected String studentId;
    @Column(name = "gpa")
    protected BigDecimal gpa;
    @Column(name = "enrollment_status")
    protected String enrollmentStatus;
    @Column(name = "academic_standing")
    protected String academicStanding;
    @Column(name = "classification")
    protected String classification;
    @Column(name = "major")
    protected String major;
    @Column(name = "minor")
    protected String minor;
    @Column(name = "advisor_id")
    protected int advisorId;
    @ManyToMany(
            targetEntity = Section.class,
            joinTable = "enrollments",
            joinColumn = "student_id", // Linking table's FK for Student & User table's PK
            inverseJoinColumn = "section_id" // Linking table's FK for Section table's PK
    )
    private List<Section> sections;
    @OneToMany(targetEntity = WaitlistEntry.class, mappedBy = "student_id")
    private List<WaitlistEntry> waitlist;

    public Student() {}

    public Student(String username, String password, String email,
                   String firstName, String lastName, String studentId) {
        super(username, password, email, firstName, lastName);
        this.userType = "STUDENT";
        this.studentId = studentId;
        this.gpa = new BigDecimal("0.0");
    }

    @Override
    public void showDashboard() {
        System.out.println("\n=== STUDENT DASHBOARD ===");
        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("GPA: " + gpa.toPlainString());
        System.out.println("\nAvailable Features:");
        System.out.println("- Register for Classes");
        System.out.println("- View Schedule");
        System.out.println("- Check Grades");
        System.out.println("- Financial Aid");
        System.out.println("- Make Payment");
    }

    protected void ensureId() throws SQLException, IllegalAccessException {
        if(this.getId() == 0) {
            // If the id is not set, we need to save this object to get an id to set on the list items.
            DatabaseManager.getInstance().upsert(this);
        }
    }

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public BigDecimal getGpa() {
        return gpa;
    }

    public void setGpa(BigDecimal gpa) {
        this.gpa = gpa;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public String getAcademicStanding() {
        return academicStanding;
    }

    public void setAcademicStanding(String academicStanding) {
        this.academicStanding = academicStanding;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public int getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(int advisorId) {
        this.advisorId = advisorId;
    }

    public List<Section> getSections() throws SQLException {
        if (this.sections == null) {
            this.sections = DatabaseManager.getInstance().fetchManyToMany(
                    Section.class, "enrollments",
                    "student_id", // Linking table's FK for Student & User table's PK
                    "section_id", // Linking table's FK for Section table's PK
                    this.id
            );
        }
        return this.sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<WaitlistEntry> getWaitlist() throws SQLException {
        // TODO: Gotta find a way to modify the fetch calls to take additional filters since this will return
        //   WaitlistEntries of ANY age and in ANY status.
        if (this.waitlist == null) {
            // Lazy Load: Use the generic fetchMany from DatabaseManager
            this.waitlist = DatabaseManager.getInstance()
                    .fetchMany(WaitlistEntry.class, "student_id", this.id);
        }
        return this.waitlist;
    }

    public void setWaitlist(List<WaitlistEntry> waitlist) throws SQLException, IllegalAccessException {
        ensureId();
        // Now, let's add this object's id to the related list items foreign key id
        for(WaitlistEntry we : waitlist) { we.setStudentId(this.getId()); }
        // Now let's upsertAll of these list items (i.e. a batch) and set as this object's related field.
        DatabaseManager.getInstance().upsertAll(waitlist);
        this.waitlist = waitlist;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\users\User.java
```java
package edu.advising.users;

// ============================================================================
// WEEK 2: FACTORY PATTERN  (originally)
// WEEK 5: COMMAND PATTERN  (additions marked ★)
// ============================================================================
// WEEK 5 CHANGES:
//   ★ Added `phone` field with @Column annotation — required by UpdateContactCommand
//     for storing and restoring phone numbers during undo/redo.
//   ★ Added `updatedAt` field — needed for audit trail in contact update undo.
//   ★ Added setEmail(), setPhone(), setUpdatedAt() — mutators needed by command undo.
//   ★ Added getPhone(), getUpdatedAt() — accessors for serialization.
//
// NOTE: The `users` table in DatabaseManager must be migrated to add the `phone`
//       column. Add this line to initializeDatabase() or run as a migration:
//
//   ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20);
//   ALTER TABLE users ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
//
//   (H2 syntax: ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20))
//   updated_at is already in the CREATE TABLE definition — no migration needed for that.
// ============================================================================

import edu.advising.core.Column;
import edu.advising.core.Id;
import edu.advising.core.Table;

import java.time.LocalDateTime;

/**
 * User - Base class for all user types in the CRAdvisor system.
 *
 * Uses ORM annotations (@Table, @Column, @Id) so DatabaseManager.upsert()
 * can persist any User subclass without manual SQL strings.
 */
@Table(name = "users")
public class User {

    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    protected int id;

    @Id
    @Column(name = "username")
    protected String username;

    @Column(name = "password")
    protected String password;

    @Column(name = "user_type")
    protected String userType;

    @Column(name = "email")
    protected String email;

    @Column(name = "first_name")
    protected String firstName;

    @Column(name = "last_name")
    protected String lastName;

    @Column(name = "is_active")
    protected boolean isActive;

    @Column(name = "last_login")
    protected LocalDateTime lastLogin;

    // ★ WEEK 5 ADDITION — required by UpdateContactCommand
    // Requires: ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20);
    @Column(name = "phone")
    protected String phone;

    // ★ WEEK 5 ADDITION — for audit trail in command undo
    // Already exists in DB schema as: updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** No-arg constructor required by ORM reflective instantiation. */
    public User() {}

    public User(String username, String password, String email, String firstName, String lastName) {
        this.username  = username;
        this.password  = password;
        this.email     = email;
        this.firstName = firstName;
        this.lastName  = lastName;
    }

    // -------------------------------------------------------------------------
    // Business Methods
    // -------------------------------------------------------------------------

    /** Template for displaying user info (expanded further in Template Pattern week). */
    public void displayInfo() {
        System.out.println("User: " + username + " (" + userType + ")");
        System.out.println("Email: " + email);
        if (phone != null && !phone.isEmpty()) {
            System.out.println("Phone: " + phone);
        }
    }

    /** Hook method for subclass dashboards (Student / Faculty). */
    public void showDashboard() {}

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getId()           { return id; }
    public String getUsername()  { return username; }
    public String getEmail()     { return email; }
    public String getUserType()  { return userType; }
    public String getPassword()  { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public Boolean isActive() { return isActive; }
    public LocalDateTime getLastLogin()  { return lastLogin; }
    public String getPhone()     { return phone; }      // ★ WEEK 5
    public LocalDateTime getUpdatedAt() { return updatedAt; } // ★ WEEK 5

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    public void setId(int id)                { this.id = id; }
    public void setFirstName(String firstName){ this.firstName = firstName; }
    public void setLastName(String lastName)  { this.lastName = lastName; }

    // ★ WEEK 5 — needed by UpdateContactCommand.execute() and undo()
    public void setEmail(String email)            { this.email = email; }
    public void setUserType(String userType)      { this.userType = userType; }
    public void setActive(Boolean isActive)     { this.isActive = isActive; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public void setPhone(String phone)            { this.phone = phone; }
    public void setUpdatedAt(LocalDateTime ts)    { this.updatedAt = ts; }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\users\UserFactory.java
```java
package edu.advising.users;

import edu.advising.core.DatabaseManager;

import java.sql.*;

/**
 * UserFactory - Factory Pattern Implementation
 * Creates appropriate user objects and persists them to database
 */
public class UserFactory {
    private DatabaseManager dbManager;

    public UserFactory() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Factory method to create users based on type
     */
    public User createUser(String userType, String username, String password,
                           String email, String firstName, String lastName, String... additionalInfo) {
        User user = null;

        switch (userType.toUpperCase()) {
            case "STUDENT":
                if (additionalInfo.length >= 1) {
                    user = new Student(username, password, email, firstName, lastName,
                            additionalInfo[0]); // studentId
                }
                break;
            case "FACULTY":
                if (additionalInfo.length >= 2) {
                    user = new Faculty(username, password, email, firstName, lastName,
                            additionalInfo[0], // employeeId
                            additionalInfo[1]); // department
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown user type: " + userType);
        }

        if (user != null) {
            saveUserToDatabase(user, additionalInfo);
        }

        return user;
    }

    private void saveUserToDatabase(User user, String... additionalInfo) {
        try {
            String userSql = "INSERT INTO users (username, password, user_type, first_name, last_name, email) VALUES (?, ?, ?, ?, ?, ?)";

            // Retrieving the user id generated by the database, and setting it on this object.
            user.setId(dbManager.executeInsert(userSql,
                    user.getUsername(),
                    user.password,
                    user.getUserType(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail()));

            // Insert into specific user type table
            if (user instanceof Student) {
                Student student = (Student) user;
                String studentSql = "INSERT INTO students (id, student_id, gpa) VALUES (?, ?, ?)";
                dbManager.executeUpdate(studentSql, user.getId(), student.getStudentId(), student.getGpa());
            } else if (user instanceof Faculty) {
                Faculty faculty = (Faculty) user;
                String facultySql = "INSERT INTO faculty (id, employee_id, department) VALUES (?, ?, ?)";
                dbManager.executeUpdate(facultySql, user.getId(), faculty.getEmployeeId(), faculty.getDepartment());
            }

            System.out.println("User created successfully with ID: " + user.getId());
        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
            throw new RuntimeException("Error saving user to database", e);
        }
    }

    /**
     * Retrieve user from database by userId
     */
    private User getUserByParam(String sql, String param) {
        try {
            return dbManager.executeQuery(sql,rs ->
            {
                if (rs.next()) {
                    String userType = rs.getString("user_type");
                    User user = null;

                    if ("STUDENT".equals(userType)) {
                        user = new Student(
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email"),
                                rs.getString("s_fname"),
                                rs.getString("s_lname"),
                                rs.getString("student_id")
                        );
                        ((Student) user).setGpa(rs.getBigDecimal("gpa"));
                    } else if ("FACULTY".equals(userType)) {
                        user = new Faculty(
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email"),
                                rs.getString("f_fname"),
                                rs.getString("f_lname"),
                                rs.getString("employee_id"),
                                rs.getString("department")
                        );
                    }

                    if (user != null) {
                        user.setId(rs.getInt("id"));
                    }
                    return user;
                }
                return null;
            }, param);
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user", e);
        }
    }

    /**
     * Retrieve user from database by userId
     */
    public User getUserById(int userId) {
        String sql = "SELECT u.*, u.first_name as s_fname, u.last_name as s_lname, " +
                "s.student_id, s.gpa, u.first_name as f_fname, u.last_name as f_lname, " +
                "f.employee_id, f.department " +
                "FROM users u " +
                "LEFT JOIN students s ON u.id = s.id " +
                "LEFT JOIN faculty f ON u.id = f.id " +
                "WHERE u.id = ?";
        return this.getUserByParam(sql, String.valueOf(userId));
    }

    /**
     * Retrieve user from database by username
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT u.*, u.first_name as s_fname, u.last_name as s_lname, " +
                "s.student_id, s.gpa, u.first_name as f_fname, u.last_name as f_lname, " +
                "f.employee_id, f.department " +
                "FROM users u " +
                "LEFT JOIN students s ON u.id = s.id " +
                "LEFT JOIN faculty f ON u.id = f.id " +
                "WHERE u.username = ?";
        return this.getUserByParam(sql, username);
    }
}
```





File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\README.md
## Running Tests

```bash
mvn exec:java@run-week5-test
```




File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\AuthenticationContext.java
```java
package edu.advising.auth;

import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * AuthenticationContext - Context Class
 * Manages the current authentication strategy
 */
public class AuthenticationContext {
    private AuthenticationStrategy strategy;
    private DatabaseManager dbManager;
    private UserFactory userFactory;

    public AuthenticationContext(AuthenticationStrategy strategy) {
        this.strategy = strategy;
        this.dbManager = DatabaseManager.getInstance();
        this.userFactory = new UserFactory();
    }

    // Allow runtime strategy switching
    public void setStrategy(AuthenticationStrategy strategy) {
        this.strategy = strategy;
        System.out.println("Authentication strategy changed to: " +
                strategy.getClass().getSimpleName());
    }

    public AuthenticationStrategy getStrategy() {
        return strategy;
    }

    /**
     * Login with current strategy
     */
    public AuthenticationResult login(String username, String password, String ipAddress) {
        AuthenticationResult authResult =  strategy.authenticate(username, password);

        // Log attempt
        try {
            String sql = "INSERT INTO login_attempts (username, status, ip_address, failure_reason) " +
                    "VALUES (?, ?, ?, ?)";
            dbManager.executeInsert(sql, username, authResult.getState().name(), ipAddress, authResult.getMessage());
            if (authResult.isFullyAuthenticated()) {
                // Update last_login
                User user = authResult.getUser();
                String updateSql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
                dbManager.executeUpdate(updateSql, user.getId());
            }
        } catch (SQLException e) {
            System.err.println("Failed to log attempt: " + e.getMessage());
        }

        return authResult;
    }

    /**
     * Continue authentication with additional credential
     */
    public AuthenticationResult verify(String authToken, String credential) {
        return strategy.continueAuthentication(authToken, credential);
    }

    private boolean isPasswordInHistory(int userId, String newHash) throws SQLException {
        String sql = "SELECT password_hash FROM password_history " +
                "WHERE user_id = ? ORDER BY changed_at DESC LIMIT 5";

        return dbManager.executeQuery(sql, rs -> {
            while (rs.next()) {
                if (rs.getString("password_hash").equals(newHash)) {
                    return true; // Password was used recently
                }
            }
            return false;
        }, userId);
    }

    /**
     * Change password functionality
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) throws SQLException {
        // Verify old password
        AuthenticationResult authResult = strategy.authenticate(username, oldPassword);
        if (!authResult.isFullyAuthenticated()) {
            System.out.println("Old password is incorrect");
            return false;
        }
        // Now that the user is authenticated, get the user object to verify history.
        User user = authResult.getUser();
        // Use auth strategy to get our new password hash for old pass verification/update.
        String newHash = strategy.hashPassword(newPassword);
        // Verify that this is not an old password re-used.
        if (isPasswordInHistory(user.getId(), newHash)) {
            System.out.println("Cannot reuse recent passwords");
            return false;
        }

        // Validate new password strength
        if (!strategy.validatePasswordStrength(newPassword)) {
            System.out.println("New password does not meet strength requirements");
            return false;
        }

        // Update password in database
        try {
            String updateSql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            int updated = dbManager.executeUpdate(updateSql, newHash, user.getId());
            if (updated > 0) {
                System.out.println("Password changed successfully");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error changing password: " + e.getMessage());
        }
        return false;
    }

    /**
     * Password recovery - "What's My Password?" feature
     */
    public boolean initiatePasswordReset(String username, String email) {
        // Verify user exists and email matches
        User user = userFactory.getUserByUsername(username);
        if (user == null || !user.getEmail().equals(email)) {
            System.out.println("✗ User not found or email doesn't match");
            return false;
        }

        try {
            // Generate secure reset token
            String resetToken = generateResetToken();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiresAt = now.plusHours(24);

            // Invalidate any existing tokens for this user
            String invalidateSql = "UPDATE password_reset_tokens SET is_used = TRUE " +
                    "WHERE user_id = ? AND is_used = FALSE";
            dbManager.executeUpdate(invalidateSql, user.getId());

            // Store new reset token
            String insertSql = "INSERT INTO password_reset_tokens " +
                    "(user_id, token, expires_at) VALUES (?, ?, ?)";
            dbManager.executeUpdate(
                    insertSql, user.getId(), resetToken, Timestamp.valueOf(expiresAt));

            System.out.println("✓ Password reset link sent to: " + email);
            System.out.println("  Reset token: " + resetToken);
            System.out.println("  Expires: " + expiresAt);

            // In real system, send email with reset link:
            // emailService.sendPasswordResetEmail(email, resetToken);

            return true;

        } catch (SQLException e) {
            System.err.println("✗ Error creating reset token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify reset token and allow password reset
     * @param token Reset token from email link
     * @param newPassword New password to set
     * @return true if reset successful, false if token invalid/expired
     */
    public boolean resetPasswordWithToken(String token, String newPassword) {
        try {
            // Find valid token
            String sql = "SELECT user_id, expires_at FROM password_reset_tokens " +
                    "WHERE token = ? AND is_used = FALSE";
            return dbManager.executeQuery(sql, rs -> {
                if (!rs.next()) {
                    System.out.println("✗ Invalid or already used reset token");
                    return false;
                }

                int userId = rs.getInt("user_id");
                Timestamp expiresAt = rs.getTimestamp("expires_at");

                // Check if token expired
                if (expiresAt.before(Timestamp.valueOf(LocalDateTime.now()))) {
                    System.out.println("✗ Reset token has expired");
                    return false;
                }

                // Validate new password strength
                if (!strategy.validatePasswordStrength(newPassword)) {
                    System.out.println("✗ New password does not meet requirements");
                    return false;
                }

                if( isPasswordInHistory(userId, newPassword) ) {
                    System.out.println("✗ Cannot reuse recent passwords");
                    return false;
                }

                // Hash and update password
                String hashedPassword = strategy.hashPassword(newPassword);
                String updatePasswordSql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP " +
                        "WHERE id = ?";
                dbManager.executeUpdate(updatePasswordSql, hashedPassword, userId);

                // Mark token as used
                String markUsedSql = "UPDATE password_reset_tokens SET is_used = TRUE, " +
                        "used_at = CURRENT_TIMESTAMP WHERE token = ?";
                dbManager.executeUpdate(markUsedSql, token);

                // Add to password history
                String historySql = "INSERT INTO password_history (user_id, password_hash) VALUES (?, ?)";
                dbManager.executeUpdate(historySql, userId, hashedPassword);

                System.out.println("✓ Password reset successful");
                return true;

            },token);

        } catch (SQLException e) {
            System.err.println("✗ Error resetting password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clean up expired reset tokens (should be run periodically)
     */
    public void cleanupExpiredTokens() {
        try {
            String sql = "DELETE FROM password_reset_tokens " +
                    "WHERE expires_at < CURRENT_TIMESTAMP AND is_used = FALSE";
            int deleted = dbManager.executeUpdate(sql);
            System.out.println("✓ Cleaned up " + deleted + " expired reset tokens");
        } catch (SQLException e) {
            System.err.println("Error cleaning up tokens: " + e.getMessage());
        }
    }

    public void logout() {
        //TODO: Figure out what it means to logout.
        // It probably means to delegate to the strategy a logout, which will likely make sure auth_session tables are
        // updated properly to reflect dead stateless sessions.
    }

    private String generateResetToken() {
        // Generate random token
        byte[] token = new byte[32];
        new java.security.SecureRandom().nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }


    private boolean isAccountLocked(String username) throws SQLException {
        String sql = "SELECT COUNT(*) as failed_count FROM login_attempts " +
                "WHERE username = ? AND status = 'FAILED' " +
                "AND attempt_time > DATEADD('MINUTE', -15, CURRENT_TIMESTAMP)";

        return dbManager.executeQuery(sql, rs -> {
            if (rs.next()) {
                return rs.getInt("failed_count") >= 5; // Lock after 5 failures in 15 min
            }
            return false;
        }, username);
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\AuthenticationResult.java
```java
package edu.advising.auth;

import edu.advising.users.User;

/**
 * AuthenticationResult - returned by all authentication attempts
 * Contains state and token for stateless tracking
 */
public class AuthenticationResult {
    private AuthenticationState state;
    private String authToken; // JWT or session token for stateless tracking
    private String message;
    private User user;

    public AuthenticationResult(AuthenticationState state, String message) {
        this.state = state;
        this.message = message;
    }

    public static AuthenticationResult failed(String message) {
        return new AuthenticationResult(AuthenticationState.FAILED, message);
    }

    public static AuthenticationResult awaitingTwoFactor(String authToken) {
        AuthenticationResult result = new AuthenticationResult(
                AuthenticationState.AWAITING_TWO_FACTOR,
                "2FA code required");
        result.authToken = authToken;
        return result;
    }

    public static AuthenticationResult success(User user) {
        AuthenticationResult result = new AuthenticationResult(
                AuthenticationState.FULLY_AUTHENTICATED,
                "Authentication successful");
        result.user = user;
        return result;
    }

    // Getters
    public AuthenticationState getState() { return state; }
    public String getAuthToken() { return authToken; }
    public String getMessage() { return message; }
    public User getUser() { return user; }
    public boolean isFullyAuthenticated() {
        return state == AuthenticationState.FULLY_AUTHENTICATED;
    }
    public boolean requiresTwoFactor() {
        return state == AuthenticationState.AWAITING_TWO_FACTOR;
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\AuthenticationState.java
```java
package edu.advising.auth;

/**
 * Authentication State - represents where user is in auth flow
 */
enum AuthenticationState {
    UNAUTHENTICATED,
    PASSWORD_VERIFIED,
    AWAITING_TWO_FACTOR,
    FULLY_AUTHENTICATED,
    FAILED
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\AuthenticationStrategy.java
```java
// Week 3: STRATEGY PATTERN
// Features Implemented: Multiple Authentication Methods, Change Password
// Why Now: Need flexible authentication system that can swap algorithms at runtime

package edu.advising.auth;

/**
 * AuthenticationStrategy - Strategy Interface
 * Defines the contract for all authentication algorithms
 */
public interface AuthenticationStrategy {
    /**
     * Initiate authentication - may return partial success if 2FA like algorithms needed
     */
    AuthenticationResult authenticate(String username, String password);
    /**
     * Continue authentication with additional factor(s)
     */
    AuthenticationResult continueAuthentication(String authToken, String credential);
    /**
     * Utility methods
     */
    String hashPassword(String password);
    boolean validatePasswordStrength(String password);
}


```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\BasicAuthentication.java
```java
package edu.advising.auth;

import edu.advising.common.ValidationResult;
import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.sql.SQLException;

/**
 * BasicAuthentication - Concrete Strategy
 * Simple username/password authentication (for development/testing)
 */
public class BasicAuthentication implements AuthenticationStrategy {
    private DatabaseManager dbManager;
    private UserFactory userFactory = new UserFactory();

    public BasicAuthentication() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public AuthenticationResult authenticate(String username, String password) {
        try {
            String sql = "SELECT password FROM users WHERE username = ?";
            return dbManager.executeQuery(sql, rs -> {
                if (rs.next() && rs.getString("password").equals(password)) {
                    User user = userFactory.getUserByUsername(username);
                    return AuthenticationResult.success(user);
                }
                return AuthenticationResult.failed("Invalid credentials");
            }, username);
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            return AuthenticationResult.failed("Authentication error");
        }
    }

    @Override
    public AuthenticationResult continueAuthentication(String authToken, String credential) {
        return AuthenticationResult.failed("Basic auth doesn't support continuation");
    }


    @Override
    public String hashPassword(String password) {
        // Basic strategy: no hashing (not secure, for demo only)
        return password;
    }

    @Override
    public boolean validatePasswordStrength(String password) {
        // Strong validation: length, uppercase, lowercase, digit, special char
        if (password == null) {
            return false;
        }
        try {
            ValidationResult vr = PasswordPolicyValidator.validateAgainstPolicy(password);
            return vr.isValid();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\PasswordPolicyValidator.java
```java
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
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\SecureAuthentication.java
```java
package edu.advising.auth;

import edu.advising.common.ValidationResult;
import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;

/**
 * SecureAuthentication - Concrete Strategy
 * SHA-256 hashed password authentication (production-ready)
 */
public class SecureAuthentication implements AuthenticationStrategy {
    private DatabaseManager dbManager;
    private UserFactory userFactory = new UserFactory();

    public SecureAuthentication() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public AuthenticationResult authenticate(String username, String password) {
        try {
            String sql = "SELECT password FROM users WHERE username = ?";
            return dbManager.executeQuery(sql, rs -> {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String inputHash = hashPassword(password);
                    if (storedHash.equals(inputHash)) {
                        User user = userFactory.getUserByUsername(username);
                        return AuthenticationResult.success(user);
                    }
                }
                return AuthenticationResult.failed("Invalid credentials");
            }, username);
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            return AuthenticationResult.failed("Authentication error");
        }
    }

    @Override
    public AuthenticationResult continueAuthentication(String authToken, String credential) {
        return AuthenticationResult.failed("Secure auth doesn't support continuation");
    }

    @Override
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    @Override
    public boolean validatePasswordStrength(String password) {
        // Strong validation: length, uppercase, lowercase, digit, special char
        if (password == null) {
            return false;
        }
        try {
            ValidationResult vr = PasswordPolicyValidator.validateAgainstPolicy(password);
            return vr.isValid();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\auth\TwoFactorAuthentication.java
```java
package edu.advising.auth;

import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * TwoFactorAuthentication - Concrete Strategy
 * Two-factor authentication with temporary codes (simulated)
 */
public class TwoFactorAuthentication implements AuthenticationStrategy {
    private AuthenticationStrategy baseAuth;
    private DatabaseManager dbManager = DatabaseManager.getInstance();
    private UserFactory userFactory = new UserFactory();
    private static final int CODE_VALIDITY_MINUTES = 5; // 5 minutes

    public TwoFactorAuthentication(AuthenticationStrategy baseAuth) {
        this.baseAuth = baseAuth;
    }

    /**
     * Step 1: Authenticate with username/password, then send 2FA code
     */
    @Override
    public AuthenticationResult authenticate(String username, String password) {
        // First, validate with base authentication
        AuthenticationResult baseResult = baseAuth.authenticate(username, password);
        if (!baseResult.isFullyAuthenticated()) {
            return baseResult; // Password was wrong
        }
        // Let's get the user that just authenticated.
        User user = baseResult.getUser();

        try {
            // Generate auth token for stateless tracking
            String authToken = generateAuthToken();

            // Generate and store 2FA code
            String twoFactorCode = generateTwoFactorCode();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expires = now.plusMinutes(CODE_VALIDITY_MINUTES);
            // TODO: Update DB Manager to CREATE auth_sessions table, or use something like Redis
            // Store auth session in database
            String insertSession = "INSERT INTO auth_sessions (auth_token, user_id, state, expires_at) " +
                    "VALUES (?, ?, ?, ?)";
            //TODO: Get Enum label for AWAITING_TWO_FACTOR instead of this string.
            dbManager.executeUpdate(
                    insertSession, authToken, user.getId(), "AWAITING_TWO_FACTOR",
                    Timestamp.valueOf(expires));

            // Store 2FA code
            String sql = "INSERT INTO two_factor_codes (user_id, code, code_type, generated_at, expires_at) " +
                    "VALUES (?, ?, ?, ?, ?)";

            int codeId = dbManager.executeInsert(sql, user.getId(), twoFactorCode, "SMS",
                    Timestamp.valueOf(now), Timestamp.valueOf(expires));
            System.out.printf("Generated codeId: %d%n", codeId);

            // Notify the user of this code.
            sendTwoFactorCode(user, twoFactorCode); // SMS, email, etc.

            System.out.println("✓ 2FA code sent. Please verify with code");
            return AuthenticationResult.awaitingTwoFactor(authToken); // Not fully authenticated with this strategy.
        } catch (SQLException e) {
            System.err.println("✗ Error generating 2FA code: " + e.getMessage());
            return AuthenticationResult.failed("2FA setup error");
        }
    }

    /**
     * Step 2: Verify 2FA code using auth token
     */
    @Override
    public AuthenticationResult continueAuthentication(String authToken, String code) {
        try {
            // Retrieve session from database
            String sessionSql = "SELECT user_id, state FROM auth_sessions " +
                    "WHERE auth_token = ? AND expires_at > CURRENT_TIMESTAMP";
            return dbManager.executeQuery(sessionSql, sessionRs -> {
                if (!sessionRs.next()) {
                    return AuthenticationResult.failed("Invalid or expired auth token");
                }

                int userId = sessionRs.getInt("user_id");
                String state = sessionRs.getString("state");

                //TODO: Make this an Enum state comparison.
                if (!"AWAITING_TWO_FACTOR".equals(state)) {
                    return AuthenticationResult.failed("Invalid authentication state");
                }

                // Verify 2FA code
                String codeSql = "SELECT id FROM two_factor_codes " +
                        "WHERE user_id = ? AND code = ? AND is_used = FALSE " +
                        "AND expires_at > CURRENT_TIMESTAMP";
                return dbManager.executeQuery(codeSql, codeRs -> {
                    if (!codeRs.next()) {
                        // Increment failed attempts
                        incrementFailedAttempts(userId);
                        return AuthenticationResult.failed("Invalid or expired 2FA code");
                    }

                    int codeId = codeRs.getInt("id");

                    // Mark code as used
                    String markUsed = "UPDATE two_factor_codes SET is_used = TRUE, " +
                            "used_at = CURRENT_TIMESTAMP WHERE id = ?";
                    dbManager.executeUpdate(markUsed, codeId);

                    // Update session state
                    String updateSession = "UPDATE auth_sessions SET state = ? WHERE auth_token = ?";
                    dbManager.executeUpdate(updateSession, "FULLY_AUTHENTICATED", authToken);

                    // Get user and return success
                    User user = userFactory.getUserById(userId);
                    System.out.println("✓ 2FA verification successful");
                    return AuthenticationResult.success(user);

                }, userId, code);
            }, authToken);
        } catch (SQLException e) {
            System.err.println("Error verifying 2FA: " + e.getMessage());
            return AuthenticationResult.failed("2FA verification error");
        }
    }

    @Override
    public String hashPassword(String password) {
        return baseAuth.hashPassword(password);
    }

    @Override
    public boolean validatePasswordStrength(String password) {
        return baseAuth.validatePasswordStrength(password);
    }

    // Helper methods

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    private String generateTwoFactorCode() {
        return String.format("%06d", (int) (Math.random() * 900000) + 100000);
    }

    private void sendTwoFactorCode(User user, String code) {
        // In real system: twilioService.sendSMS(user.getPhone(), code);
        System.out.printf("📱 SMS sent to user %s with code: %s (valid for %d minutes)%n",
                user.getEmail(), code, CODE_VALIDITY_MINUTES);
    }

    private void incrementFailedAttempts(int userId) throws SQLException {
        String sql = "UPDATE two_factor_codes SET attempts = attempts + 1 " +
                "WHERE user_id = ? AND is_used = FALSE " +
                "ORDER BY generated_at DESC LIMIT 1";
        dbManager.executeUpdate(sql, userId);
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\BaseCommand.java
```java
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
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\Command.java
```java
package edu.advising.commands;

import java.time.LocalDateTime;

/**
 * Command - Interface for all command objects
 */
public interface Command {
    void execute();
    void undo();
    boolean isUndoable();
    String getDescription();
    LocalDateTime getExecutionTime();
    boolean wasSuccessful();
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\CommandExecutor.java
```java
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
// LIFECYCLE — One CommandExecutor per user session:
//
//   // When user logs in:
//   CommandExecutor executor = new CommandExecutor(loggedInUser.getId());
//   session.setCommandExecutor(executor);
//
//   // Store on the session so any screen can retrieve it:
//   session.getCommandExecutor().execute(new RegisterCommand(student, section));
//
// ─────────────────────────────────────────────────────────────────────────────
// GUI BUTTON WIRING (Swing example, works the same for JavaFX/Web):
//
//   // "Register" button
//   registerButton.addActionListener(e -> {
//       Section selected = sectionTable.getSelectedSection();
//       executor.execute(new RegisterCommand(student, selected));
//       undoButton.setEnabled(executor.canUndo());
//       redoButton.setEnabled(executor.canRedo());
//       refreshScheduleView();
//   });
//
//   // "Undo" button (always in the toolbar)
//   undoButton.addActionListener(e -> {
//       undoButton.setToolTipText("Undo: " + executor.peekUndoDescription());
//       executor.undo();
//       undoButton.setEnabled(executor.canUndo());
//       redoButton.setEnabled(executor.canRedo());
//       refreshScheduleView();
//   });
//
//   // "Redo" button
//   redoButton.addActionListener(e -> {
//       executor.redo();
//       undoButton.setEnabled(executor.canUndo());
//       redoButton.setEnabled(executor.canRedo());
//       refreshScheduleView();
//   });
//
// ─────────────────────────────────────────────────────────────────────────────
// OPEN/CLOSED PRINCIPLE:
//   Adding a new user action (e.g. Week 8's TranscriptRequestCommand) requires
//   ONLY creating a new BaseCommand subclass. CommandExecutor never changes.
//   This is the real power of the Command Pattern — the invoker is sealed.
//
// ============================================================================

import java.util.List;

public class CommandExecutor {

    private final CommandHistory history;

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------

    /**
     * Create an executor for a specific user session.
     * @param userId The logged-in user's numeric primary key.
     */
    public CommandExecutor(int userId) {
        this.history = new CommandHistory(userId);
    }

    /**
     * Convenience constructor when you already have a CommandHistory instance
     * (e.g. for testing with a mock history).
     */
    public CommandExecutor(CommandHistory history) {
        this.history = history;
    }

    // -------------------------------------------------------------------------
    // Command Execution — the primary API for the UI layer
    // -------------------------------------------------------------------------

    /**
     * Execute a command and record it in history.
     *
     * This is the ONLY method the UI/Service layer should call to trigger
     * business logic. The caller creates the appropriate Command object,
     * passes it here, and then queries wasSuccessful() on the command
     * (or canUndo() on the executor) to update the UI state.
     *
     * Example:
     *   RegisterCommand cmd = new RegisterCommand(student, section);
     *   executor.execute(cmd);
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

    /**
     * @return true if the Undo button should be enabled.
     *
     * GUI Usage:
     *   undoButton.setEnabled(executor.canUndo());
     *   undoButton.setToolTipText("Undo: " + executor.peekUndoDescription());
     */
    public boolean canUndo() {
        return history.canUndo();
    }

    /**
     * @return true if the Redo button should be enabled.
     */
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

    /**
     * Human-readable label for the next action that would be redone.
     */
    public String peekRedoDescription() {
        return history.peekRedoDescription();
    }

    // -------------------------------------------------------------------------
    // History Access
    // -------------------------------------------------------------------------

    /**
     * Returns the live in-session undo stack (most recent first).
     * Useful for a "Recent Actions" panel that lists what can currently be undone.
     *
     * GUI Usage:
     *   List<BaseCommand> recent = executor.getSessionHistory();
     *   recentActionsPanel.populate(recent);
     */
    public List<BaseCommand> getSessionHistory() {
        return history.getUndoStack();
    }

    /**
     * Load full audit history from the database for the current user.
     * Unlike getSessionHistory(), this survives session boundaries and
     * returns ALL historical records up to `limit`.
     *
     * GUI Usage (Transaction History screen):
     *   List<CommandRecord> records = executor.getAuditHistory(50);
     *   transactionTable.setModel(new CommandRecordTableModel(records));
     *
     * @param limit Maximum records to return (most recent first).
     */
    public List<CommandRecord> getAuditHistory(int limit) {
        return history.getAuditHistory(limit);
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\CommandHistory.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - Command History (Invoker's Memory)
// ============================================================================
//
// PATTERN ROLE: This class is the "Invoker's memory" in the Command Pattern.
//   The Invoker (CommandExecutor) delegates every execute/undo/redo call here.
//   CommandHistory owns the undo and redo stacks and knows how to persist
//   commands to the `command_history` table via the ORM.
//
// HOW UNDO/REDO STACKS WORK:
//
//   START:    undoStack=[]        redoStack=[]
//
//   User registers for CIS-12:
//             undoStack=[REG]     redoStack=[]
//
//   User registers for MATH-10:
//             undoStack=[MATH,REG] redoStack=[]
//
//   User clicks Undo (MATH-10):
//             undoStack=[REG]     redoStack=[MATH]
//
//   User clicks Redo (MATH-10):
//             undoStack=[MATH,REG] redoStack=[]
//
//   User takes a NEW action (drops CIS-12) — redo chain breaks:
//             undoStack=[DROP,MATH,REG] redoStack=[]   (MATH redo is gone)
//
// PERSISTENCE:
//   Each command is inserted into `command_history` on execute.
//   On undo the row is updated (is_undone=TRUE, undone_at=now).
//   This gives faculty/admins a full audit trail even if the user
//   navigates away, and lets analysts see exactly what happened.
//
// GUI INTEGRATION:
//   After any execute/undo/redo call, check canUndo()/canRedo() to decide
//   whether the toolbar Undo and Redo buttons should be enabled:
//
//     executor.execute(new RegisterCommand(student, section));
//     undoButton.setEnabled(executor.canUndo());   // Swing example
//     redoButton.setEnabled(executor.canRedo());
//
// ============================================================================

import edu.advising.core.DatabaseManager;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CommandHistory {

    // In-memory stacks scoped to the current user session.
    // ArrayDeque is used as a LIFO stack: push() adds to front, pop() removes from front.
    private final Deque<BaseCommand> undoStack;
    private final Deque<BaseCommand> redoStack;

    private final int userId;
    private final int maxStackSize;           // Keeps memory bounded
    private final DatabaseManager dbManager;

    /** Default: keep up to 20 actions in the live undo stack. */
    private static final int DEFAULT_MAX_SIZE = 20;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public CommandHistory(int userId) {
        this(userId, DEFAULT_MAX_SIZE);
    }

    public CommandHistory(int userId, int maxStackSize) {
        this.userId       = userId;
        this.maxStackSize = maxStackSize;
        this.undoStack    = new ArrayDeque<>();
        this.redoStack    = new ArrayDeque<>();
        this.dbManager    = DatabaseManager.getInstance();
    }

    // -------------------------------------------------------------------------
    // Core Execute / Undo / Redo
    // -------------------------------------------------------------------------

    /**
     * Execute a command and record it in history.
     *
     * Called by CommandExecutor — not directly by the UI.
     * After execution:
     *   - Successful commands go onto the undo stack and are persisted to DB.
     *   - Any pending redo stack is cleared (new action breaks redo chain).
     *   - Failed commands are persisted for audit purposes but NOT pushed
     *     onto the undo stack (nothing to undo if nothing happened).
     */
    public void executeCommand(BaseCommand command) {
        command.setUserId(userId);
        command.execute();

        // Always persist — even failures go into the audit log.
        persistNewCommand(command);

        if (command.wasSuccessful()) {
            // Enforce cap: evict the oldest entry before pushing new one.
            if (undoStack.size() >= maxStackSize) {
                undoStack.pollLast(); // remove oldest (back of deque)
            }
            undoStack.push(command);

            // A new action breaks the forward timeline — redo is no longer valid.
            redoStack.clear();
        }
    }

    /**
     * Undo the most recently executed undoable command.
     *
     * @return true if undo succeeded, false if nothing to undo or not undoable.
     */
    public boolean undo() {
        if (undoStack.isEmpty()) {
            System.out.println("↶ Nothing to undo.");
            return false;
        }

        BaseCommand command = undoStack.peek();

        if (!command.isUndoable()) {
            System.out.println("↶ Command cannot be undone: " + command.getDescription());
            return false;
        }

        undoStack.pop();
        command.undo();

        // Update the persisted record to mark it as reversed.
        markCommandUndone(command);

        // The undone command is pushed onto the redo stack so it can be re-applied.
        redoStack.push(command);
        return true;
    }

    /**
     * Redo the most recently undone command.
     *
     * @return true if redo succeeded, false if nothing to redo.
     */
    public boolean redo() {
        if (redoStack.isEmpty()) {
            System.out.println("↷ Nothing to redo.");
            return false;
        }

        BaseCommand command = redoStack.pop();

        // Re-run the command's execute logic from scratch.
        // The command's own execute() validates pre-conditions (capacity, conflicts, etc.)
        // so it's safe to call again — it won't blindly re-do something invalid.
        command.execute();

        if (command.wasSuccessful()) {
            command.setUndone(false);
            command.setUndoneAt(null);
            markCommandRedone(command);
            undoStack.push(command);
        } else {
            // Redo failed (e.g. section is now full); discard rather than loop.
            System.out.println("↷ Redo failed: " + command.getDescription());
        }

        return command.wasSuccessful();
    }

    // -------------------------------------------------------------------------
    // State Queries — used by GUI to enable/disable Undo/Redo buttons
    // -------------------------------------------------------------------------

    /** @return true if there is at least one undoable command in history. */
    public boolean canUndo() {
        return !undoStack.isEmpty() && undoStack.peek().isUndoable();
    }

    /** @return true if there is at least one redoable command in history. */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /** @return description of the next command that would be undone, or null. */
    public String peekUndoDescription() {
        BaseCommand top = undoStack.peek();
        return top != null ? top.getDescription() : null;
    }

    /** @return description of the next command that would be redone, or null. */
    public String peekRedoDescription() {
        BaseCommand top = redoStack.peek();
        return top != null ? top.getDescription() : null;
    }

    /**
     * Returns an ordered snapshot of the in-memory undo stack (most recent first).
     * Useful for showing a "Recent Actions" panel in the UI.
     */
    public List<BaseCommand> getUndoStack() {
        return new ArrayList<>(undoStack);
    }

    // -------------------------------------------------------------------------
    // Audit Trail — load historical records from DB
    // -------------------------------------------------------------------------

    /**
     * Loads a page of past commands from the `command_history` table.
     *
     * WHY THIS IS SEPARATE FROM getUndoStack():
     *   The in-memory stack only holds the current session's actions, bounded by
     *   maxStackSize. The database holds every action ever taken by this user.
     *   This method powers audit dashboards, "My Transaction History" screens,
     *   and admin review panels.
     *
     * GUI INTEGRATION:
     *   List<CommandRecord> history = commandHistory.getAuditHistory(50);
     *   // Bind history to a JTable model or a RecyclerView adapter.
     *
     * @param limit Max number of records to return (most recent first).
     */
    public List<CommandRecord> getAuditHistory(int limit) {
        String sql = "SELECT id, command_type, command_data, executed_at, " +
                "undone_at, is_undone, success, error_message " +
                "FROM command_history WHERE user_id = ? " +
                "ORDER BY executed_at DESC LIMIT ?";
        try {
            return dbManager.fetchList(sql, rs -> new CommandRecord(
                    rs.getInt("id"),
                    rs.getString("command_type"),
                    rs.getString("command_data"),
                    rs.getTimestamp("executed_at") != null
                            ? rs.getTimestamp("executed_at").toLocalDateTime() : null,
                    rs.getTimestamp("undone_at") != null
                            ? rs.getTimestamp("undone_at").toLocalDateTime() : null,
                    rs.getBoolean("is_undone"),
                    rs.getBoolean("success"),
                    rs.getString("error_message")
            ), userId, limit);
        } catch (SQLException e) {
            System.err.println("CommandHistory: failed to load audit history - " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // -------------------------------------------------------------------------
    // Private DB Helpers
    // -------------------------------------------------------------------------

    /**
     * Insert a new command record into command_history.
     * Uses ORM upsert via BaseCommand's @Table / @Column annotations.
     * Falls back to manual SQL if ORM fails (defensive coding).
     */
    private void persistNewCommand(BaseCommand command) {
        try {
            // prepareForStorage() calls serializeCommandData() on the concrete subclass,
            // storing the JSON payload into command.commandData before we persist.
            command.prepareForStorage();
            dbManager.upsert(command);
        } catch (SQLException | IllegalAccessException e) {
            System.err.println("CommandHistory: ORM upsert failed, trying fallback SQL — " + e.getMessage());
            persistCommandFallback(command);
        }
    }

    /**
     * Fallback insertion when ORM upsert cannot be used (e.g. subclass not directly annotated).
     */
    private void persistCommandFallback(BaseCommand command) {
        String sql = "INSERT INTO command_history " +
                "(user_id, command_type, command_data, executed_at, is_undone, success, error_message) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            int generatedId = dbManager.executeInsert(sql,
                    command.getUserId(),
                    command.getCommandType(),
                    command.getCommandData(),
                    command.getExecutionTime() != null
                            ? Timestamp.valueOf(command.getExecutionTime()) : null,
                    false,
                    command.wasSuccessful(),
                    command.getErrorMessage());

            // Write the generated DB id back onto the command so markCommandUndone()
            // can find the correct row by id later.
            if (generatedId > 0) command.setId(generatedId);

        } catch (SQLException ex) {
            System.err.println("CommandHistory: fallback persist also failed — " + ex.getMessage());
        }
    }

    /**
     * Update an existing command_history row to mark the command as undone.
     */
    private void markCommandUndone(BaseCommand command) {
        if (command.getId() <= 0) return;  // Row was never persisted — skip.
        String sql = "UPDATE command_history " +
                "SET is_undone = TRUE, undone_at = CURRENT_TIMESTAMP " +
                "WHERE id = ?";
        try {
            dbManager.executeUpdate(sql, command.getId());
            command.setUndone(true);
            command.setUndoneAt(LocalDateTime.now());
        } catch (SQLException e) {
            System.err.println("CommandHistory: failed to mark command undone — " + e.getMessage());
        }
    }

    /**
     * Update a command_history row to reflect that a previously-undone command was redone.
     */
    private void markCommandRedone(BaseCommand command) {
        if (command.getId() <= 0) return;
        String sql = "UPDATE command_history " +
                "SET is_undone = FALSE, undone_at = NULL " +
                "WHERE id = ?";
        try {
            dbManager.executeUpdate(sql, command.getId());
        } catch (SQLException e) {
            System.err.println("CommandHistory: failed to mark command redone — " + e.getMessage());
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\CommandRecord.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - Command Record (Audit/History DTO)
// ============================================================================
// WHY THIS CLASS EXISTS:
//   When we load command history from the database for display (audit trails,
//   "My Transaction History", admin dashboards), we only need the metadata
//   about each command — not a fully reconstructed, executable command object.
//
//   Trying to reconstruct a full RegisterCommand or PaymentCommand from the DB
//   just to display a list would require re-fetching Student, Section, and other
//   objects unnecessarily. Instead, we load this lightweight DTO.
//
// GUI INTEGRATION NOTE:
//   A "Transaction History" screen would call:
//       CommandExecutor executor = session.getCommandExecutor();
//       List<CommandRecord> history = executor.getHistory(20);
//   Then bind the list to a JTable or ListView. Each row shows:
//       - What was done (commandType)
//       - When it happened (executedAt)
//       - Whether it succeeded (success)
//       - Whether it was reversed (undone)
// ============================================================================

import java.time.LocalDateTime;

public class CommandRecord {
    private final int id;
    private final String commandType;
    private final String commandData;   // Raw JSON payload for debugging
    private final LocalDateTime executedAt;
    private final LocalDateTime undoneAt;
    private final boolean undone;
    private final boolean success;
    private final String errorMessage;

    public CommandRecord(int id, String commandType, String commandData,
                  LocalDateTime executedAt, LocalDateTime undoneAt,
                  boolean undone, boolean success, String errorMessage) {
        this.id           = id;
        this.commandType  = commandType;
        this.commandData  = commandData;
        this.executedAt   = executedAt;
        this.undoneAt     = undoneAt;
        this.undone       = undone;
        this.success      = success;
        this.errorMessage = errorMessage;
    }

    // -------------------------------------------------------------------------
    // Getters — read-only, this is a value object
    // -------------------------------------------------------------------------

    public int getId()                  { return id; }
    public String getCommandType()      { return commandType; }
    public String getCommandData()      { return commandData; }
    public LocalDateTime getExecutedAt(){ return executedAt; }
    public LocalDateTime getUndoneAt()  { return undoneAt; }
    public boolean isUndone()           { return undone; }
    public boolean isSuccess()          { return success; }
    public String getErrorMessage()     { return errorMessage; }

    /** Human-readable status badge for display in a UI table cell. */
    public String getStatusLabel() {
        if (!success)  return "✗ Failed";
        if (undone)    return "↶ Reversed";
        return "✓ Completed";
    }

    @Override
    public String toString() {
        return String.format("[%s] %-20s %s  %s",
                executedAt, commandType, getStatusLabel(),
                errorMessage != null ? "| " + errorMessage : "");
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\Course.java
```java
package edu.advising.commands;

import edu.advising.core.*;

import java.sql.SQLException;
import java.util.List;

/**
 * ADD ANNOTATIONS during Command Pattern Week
 * -
 * Course Section - Represents a course section
 */
@Table(name = "courses")
public class Course {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "credits")
    private double credits;
    @Column(name = "department_id", foreignKey = true)
    private int departmentId;
    @Column(name = "level")
    private String level;
    @Column(name = "is_active")
    private boolean isActive;
    @OneToMany(targetEntity = Section.class, mappedBy = "course_id")
    private List<Section> sections; // Cached list of available sections.
    @ManyToOne(targetEntity = Department.class, joinColumn = "department_id")
    private Department department;

    public Course() {}

    public Course(String code, String name, String description, int credits, int departmentId, String level) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.departmentId = departmentId;
        this.level = level;
        this.isActive = true;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Section> getSections() throws SQLException {
        if (this.sections == null) {
            // Lazy Load: Use the generic fetchMany from DatabaseManager
            this.sections = DatabaseManager.getInstance()
                    .fetchMany(Section.class, "course_id", this.id);
        }
        return this.sections;
    }

    protected void ensureId() throws SQLException, IllegalAccessException {
        if(this.getId() == 0) {
            // If the id is not set, we need to save this object to get an id to set on the list items.
            DatabaseManager.getInstance().upsert(this);
        }
    }

    public void setSections(List<Section> sections) throws SQLException, IllegalAccessException {
        ensureId();
        // Now, let's add this object's id to the related list items foreign key id
        for(Section s : sections) { s.setCourseId(this.getId()); }
        // Now let's upsertAll of these list items (i.e. a batch) and set as this object's related field.
        DatabaseManager.getInstance().upsertAll(sections);
        this.sections = sections;
    }

    public Department getDepartment() throws SQLException {
        if (this.department == null) {
            // Lazy Load: Use the generic fetchOne from DatabaseManager
            this.department = DatabaseManager.getInstance()
                    .fetchOne(Department.class, "id", this.departmentId);
        }
        return (this.department != null) ? this.department : null;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\Department.java
```java
package edu.advising.commands;

import edu.advising.core.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "departments")
public class Department {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "chair_id")  // References User/Faculty id
    private int chairId;
    @Column(name = "budget")
    private double budget;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(targetEntity = Course.class, mappedBy = "department_id")
    private List<Course> courses; // Cached list of available courses.

    public Department() {}

    private Department(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Department(String code, String name) {
        this(0, code, name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChairId() {
        return chairId;
    }

    public void setChairId(int chairId) {
        this.chairId = chairId;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Course> getCourses() throws SQLException {
        if (this.courses == null) {
            // Lazy Load: Use the generic fetchMany from DatabaseManager
            this.courses = DatabaseManager.getInstance()
                    .fetchMany(Course.class, "department_id", this.id);
        }
        return this.courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\DropCommand.java
```java
package edu.advising.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * DropCommand - Drop a course section
 */
@Table(name = "command_history", isSubTable = true)
public class DropCommand extends BaseCommand {
    private ObservableStudent student;
    private Section section;
    private int previousEnrollmentId;
    private DatabaseManager dbManager;

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public DropCommand() {
        this(null, null);
    }

    public DropCommand(ObservableStudent student, Section section) {
        super();
        this.commandType = "DROP";
        this.student = student;
        this.section = section;
        this.dbManager = DatabaseManager.getInstance();
    }

    public static DropCommand fromSuperType(BaseCommand base) {
        DropCommand cmd = new DropCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        if (section.drop(student)) {
            // Update database
            updateEnrollmentStatus("DROPPED");

            executed = true;
            successful = true;

            System.out.printf("✓ Student %s dropped %s%n",
                    student.getStudentId(), section.getCourseCode());

            // Check waitlist and promote next student
            try {
                promoteFromWaitlist();
            } catch (SQLException | IllegalAccessException e) {
                e.printStackTrace();
                System.out.println("Failed to promote from waitlist.");
            }
        } else {
            successful   = false;
            errorMessage = String.format("Drop failed — student not enrolled in %s",
                    section.getCourseCode());
            System.out.println("✗ " + errorMessage);
        }
    }

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo - command not executed or failed");
            return;
        }

        // Re-enroll
        if (section.enroll(student) > 0) {
            updateEnrollmentStatus("ENROLLED");
            System.out.printf("↶ Undone: Drop of %s - student re-enrolled%n",
                    section.getCourseCode());
            this.undoneAt = LocalDateTime.now();
            this.isUndone = true;
        }
    }

    @Override
    public boolean isUndoable() {
        return executed && successful && section.hasCapacity();
    }

    @Override
    public String getDescription() {
        return String.format("Drop %s (%s)", section.getCourseCode(), section.getCourseName());
    }

    private void updateEnrollmentStatus(String status) {
        // Section.drop() already updates the enrollment via ORM upsert.
        // This method exists as a safety net for direct DropCommand use outside Section.
        try {
            String sql = "UPDATE enrollments SET status = ? " +
                    "WHERE student_id = ? AND section_id = ? AND status = 'ENROLLED'";
            dbManager.executeUpdate(sql, status, student.getId(), section.getId());
        } catch (SQLException e) {
            System.err.println("DropCommand: enrollment status sync failed — " + e.getMessage());
        }
    }

    private void promoteFromWaitlist() throws SQLException, IllegalAccessException {
        if (!section.getWaitlist().isEmpty() && section.hasCapacity()) {
            // Get the next waitlist entry
            WaitlistEntry nextWaitlistEntry = section.getWaitlist().get(0);
            // Lookup the student for this entry
            Student student = nextWaitlistEntry.getStudent();
            // Remove that student from the waitlist
            section.removeFromWaitlist(student);
            section.enroll(student);
            System.out.println(String.format("↑ Student ID %s promoted from waitlist", student.getStudentId()));

            // In real implementation, notify the student with observer!!!
        }
    }

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk",            student.getId());   // int PK
        data.put("studentId", student.getStudentId());
        data.put("sectionId", section.getId()); // Assuming Section has an id
        data.put("previousEnrollmentId", previousEnrollmentId);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize DropCommand data", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            int studentPk = (int) data.get("studentPk");
            int sectionId = (int) data.get("sectionId");
            this.previousEnrollmentId = (int) data.get("previousEnrollmentId");

            Student raw  = DatabaseManager.getInstance().fetchOne(Student.class, "id", studentPk);
            this.student = ObservableStudent.fromSuperType(raw);
            this.section = DatabaseManager.getInstance().fetchOne(Section.class, "id", sectionId);
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("Failed to deserialize DropCommand data", e);
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\Enrollment.java
```java
package edu.advising.commands;

import edu.advising.core.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Course Section Enrollment - Represents an enrollment in a course section
 */
@Table(name = "enrollments")
public class Enrollment {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "student_id")
    private int studentId; // References sudents
    @Id
    @Column(name = "section_id") // References sections
    private int sectionId;
    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;
    @Column(name = "status")
    private String status; // ENROLLED, DROPPED, WITHDRAWN, COMPLETED
    @Column(name = "grade")
    private String grade;
    @Column(name = "grade_points")
    private BigDecimal gradePoints;
    @Column(name = "midterm_grade")
    private String midtermGrade;
    @Column(name = "final_grade")
    private String finalGrade;
    @Column(name = "graded_at")
    private LocalDateTime gradedAt;
    @Column(name = "dropped_at")
    private LocalDateTime droppedAt;
    @Column(name = "drop_reason")
    private String dropReason;
    @ManyToOne(targetEntity = Section.class, joinColumn = "section_id")
    private Section section; // Cached object representing this enrollment's course section.

    public Enrollment() {}

    public Enrollment(int studentId, int sectionId) {
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.status = "ENROLLED";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public BigDecimal getGradePoints() {
        return gradePoints;
    }

    public void setGradePoints(BigDecimal gradePoints) {
        this.gradePoints = gradePoints;
    }

    public String getMidtermGrade() {
        return midtermGrade;
    }

    public void setMidtermGrade(String midtermGrade) {
        this.midtermGrade = midtermGrade;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public LocalDateTime getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }

    public LocalDateTime getDroppedAt() {
        return droppedAt;
    }

    public void setDroppedAt(LocalDateTime droppedAt) {
        this.droppedAt = droppedAt;
    }

    public String getDropReason() {
        return dropReason;
    }

    public void setDropReason(String dropReason) {
        this.dropReason = dropReason;
    }

    public Section getSection() throws SQLException {
        if (this.section == null) {
            // Lazy Load: Use the generic fetchOne from DatabaseManager
            this.section = DatabaseManager.getInstance()
                    .fetchOne(Section.class, "section_id", this.sectionId);
        }
        return (this.section != null) ? this.section : null;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\FacultyDropCommand.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - FacultyDropCommand (Concrete Command)
// ============================================================================
//
// FEATURE:  Faculty Information → Faculty Drop/Census Roster
//           (Faculty administratively drops a student from their section)
//
// WHY THIS IS A SEPARATE COMMAND FROM DropCommand:
//   A student dropping themselves (DropCommand) and a faculty member
//   administratively dropping a student are conceptually different:
//     1. AUTHORIZATION: Faculty drops need a different permission check
//        (faculty must own the section). Week 7 Decorator will wrap this.
//     2. REASON CODE: Faculty drops require a documented reason
//        (no-show, census, academic) logged in the enrollment record.
//     3. NOTIFICATION: The student must be notified that they were dropped
//        by faculty — this is a different notification type and message.
//     4. AUDIT: Faculty drops are surfaced in admin reports separately from
//        student self-drops. The command_type "FACULTY_DROP" makes queries easy.
//     5. UNDO POLICY: Faculty may want to reinstate a student within the
//        census period — undo re-enrolls the student.
//
// GUI INTEGRATION:
//   // On faculty class roster → right-click → "Administrative Drop":
//   Student selectedStudent = rosterTable.getSelectedStudent();
//   String reason = reasonDialog.getSelectedReason(); // "NO_SHOW", "CENSUS", etc.
//   FacultyDropCommand cmd = new FacultyDropCommand(faculty, student, section, reason);
//   executor.execute(cmd);
//
//   if (cmd.wasSuccessful()) {
//       showConfirmation("Student dropped. They have been notified.");
//       rosterTable.removeStudent(selectedStudent);
//   }
//
// ============================================================================

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Faculty;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FacultyDropCommand extends BaseCommand {

    // ── State needed for execute and undo ────────────────────────────────────

    private final Faculty faculty;
    private ObservableStudent student;
    private Section section;
    private final String dropReason;  // NO_SHOW, CENSUS, ACADEMIC_INTEGRITY, OTHER

    // Captured during execute() for use in undo() and serialization
    private int droppedEnrollmentId;

    private final NotificationManager notificationManager;
    private final DatabaseManager     dbManager;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * @param faculty    The faculty member performing the drop.
     * @param student    The student being dropped.
     * @param section    The course section they are being dropped from.
     * @param dropReason Documented reason for the administrative drop.
     */
    public FacultyDropCommand(Faculty faculty, ObservableStudent student,
                              Section section, String dropReason) {
        super();
        this.commandType         = "FACULTY_DROP";
        this.faculty             = faculty;
        this.student             = student;
        this.section             = section;
        this.dropReason          = (dropReason != null) ? dropReason : "UNSPECIFIED";
        this.notificationManager = NotificationManager.getInstance();
        this.dbManager           = DatabaseManager.getInstance();
    }

    // -------------------------------------------------------------------------
    // Command Interface — execute()
    // -------------------------------------------------------------------------

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        // ── Authorization check: faculty must own this section ───────────────
        // NOTE: In Week 7 (Decorator Pattern), this check will be handled by
        // FacultyPermissions.canDrop(section). For now, we check inline.
        if (section.getFacultyId() != faculty.getId()) {
            successful   = false;
            errorMessage = String.format("Faculty %s does not own section %s.",
                    faculty.getFullName(), section.getCourseCode());
            System.out.println("✗ " + errorMessage);
            return;
        }

        // ── Capture the enrollment ID before dropping, for undo purposes ─────
        try {
            Optional<Enrollment> enrollment = section.getEnrollments().stream()
                    .filter(e -> e.getStudentId() == student.getId()
                            && "ENROLLED".equals(e.getStatus()))
                    .findFirst();

            if (enrollment.isEmpty()) {
                successful   = false;
                errorMessage = String.format("Student %s is not enrolled in %s.",
                        student.getStudentId(), section.getCourseCode());
                System.out.println("✗ " + errorMessage);
                return;
            }

            droppedEnrollmentId = enrollment.get().getId();

            // ── Update enrollment record with drop details ───────────────────
            Enrollment e = enrollment.get();
            e.setStatus("DROPPED");
            e.setDroppedAt(LocalDateTime.now());
            e.setDropReason(dropReason + " (Faculty: " + faculty.getFullName() + ")");
            dbManager.upsert(e);

            // ── Update section's enrolled count ──────────────────────────────
            // section.drop() handles the in-memory list and upserts the section.
            section.drop(student);

            executed   = true;
            successful = true;

            System.out.printf("✓ Faculty drop: %s dropped %s from %s (Reason: %s)%n",
                    faculty.getFullName(), student.getFullName(),
                    section.getCourseCode(), dropReason);

            // ── Notify the student they were administratively dropped ─────────
            // This is a high-priority notification — student needs to know ASAP.
            notificationManager.notifyRegistration(student, section.getCourseCode(), false);
            // TODO Week 4 enhancement: add a faculty-drop-specific notification type
            //   that includes the reason code, so the student can respond if needed.

            // ── Check if anyone on the waitlist should be promoted ────────────
            promoteFromWaitlistIfAvailable();

        } catch (SQLException | IllegalAccessException e) {
            successful   = false;
            errorMessage = "Faculty drop failed: " + e.getMessage();
            System.err.println("✗ " + errorMessage);
        }
    }

    // -------------------------------------------------------------------------
    // Command Interface — undo()
    // -------------------------------------------------------------------------

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo — drop was not completed.");
            return;
        }

        // Re-enroll the student in the section (reverses the drop).
        if (section.hasCapacity()) {
            int newEnrollmentId = section.enroll(student);
            if (newEnrollmentId > 0) {
                undoneAt = LocalDateTime.now();
                isUndone = true;
                System.out.printf("↶ Undone: %s re-enrolled in %s%n",
                        student.getFullName(), section.getCourseCode());
                notificationManager.notifyRegistration(student, section.getCourseCode(), true);
            } else {
                System.out.println("✗ Undo failed — could not re-enroll student.");
            }
        } else {
            System.out.printf("✗ Cannot undo — %s is now full.%n", section.getCourseCode());
        }
    }

    @Override
    public boolean isUndoable() {
        // Can only reinstate if the section still has capacity.
        return executed && successful && section.hasCapacity();
    }

    @Override
    public String getDescription() {
        return String.format("Faculty drop: %s from %s (Reason: %s)",
                student.getFullName(), section.getCourseCode(), dropReason);
    }

    // -------------------------------------------------------------------------
    // Serialization
    // -------------------------------------------------------------------------

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("facultyId",           faculty.getId());
        data.put("studentPk",           student.getId());
        data.put("sectionId",           section.getId());
        data.put("dropReason",          dropReason);
        data.put("droppedEnrollmentId", droppedEnrollmentId);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("FacultyDropCommand: serialization failed", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            int studentPk = (int) data.get("studentPk");
            int sectionId = (int) data.get("sectionId");

            Student raw = dbManager.fetchOne(Student.class, "id", studentPk);
            if (raw != null) this.student = ObservableStudent.fromSuperType(raw);
            this.section             = dbManager.fetchOne(Section.class, "id", sectionId);
            this.droppedEnrollmentId = (int) data.get("droppedEnrollmentId");

        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("FacultyDropCommand: deserialization failed", e);
        }
    }

    // -------------------------------------------------------------------------
    // Private Helper
    // -------------------------------------------------------------------------

    /**
     * After a drop frees a seat, promote the next eligible waitlist student.
     * Mirrors the logic in DropCommand.promoteFromWaitlist() but also notifies
     * the promoted student via the Observer system.
     */
    private void promoteFromWaitlistIfAvailable() {
        try {
            if (!section.getWaitlist().isEmpty() && section.hasCapacity()) {
                WaitlistEntry next = section.getWaitlist().get(0);
                Student waitlisted = next.getStudent();
                section.removeFromWaitlist(waitlisted);
                int newEnrollmentId = section.enroll(waitlisted);
                if (newEnrollmentId > 0) {
                    System.out.printf("↑ %s promoted from waitlist into %s%n",
                            waitlisted.getFullName(), section.getCourseCode());
                    // Notify the promoted student via the Observer chain.
                    // TODO: wrap waitlisted student in ObservableStudent before notifying
                    notificationManager.notifyRegistration(
                            ObservableStudent.fromSuperType((Student) waitlisted),
                            section.getCourseCode(), true);
                }
            }
        } catch (SQLException e) {
            System.err.println("FacultyDropCommand: waitlist promotion failed — " + e.getMessage());
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\GrantWaitlistPermissionCommand.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - GrantWaitlistPermissionCommand (Concrete Command)
// ============================================================================
//
// FEATURE:  Faculty Information → Permission to Add Waitlisted Students
//           (Faculty explicitly grants a waitlisted student permission to enroll
//            in a section that is at or over capacity)
//
// REAL-WORLD CONTEXT (WebAdvisor):
//   Some sections have "permission-required" flags or a faculty override flow.
//   A student on the waitlist can see they are position #1 and contact the
//   instructor. The instructor reviews their situation and grants permission.
//   That permission shows up for the student as a one-time-use enrollment token.
//
// WHY COMMAND PATTERN HERE:
//   1. REVERSIBLE: Revoking a permission grant before the student acts on it
//      should be an undo, not a separate "revoke" flow.
//   2. LOGGED: Faculty overrides must be audited ("Prof. Smith over-enrolled CS101").
//   3. TIME-BOUNDED: The permission expires if unused — the command record
//      stores the expiry and can be queried by the registration flow.
//   4. FUTURE PIPELINE HOOK: Week 14's registration pipeline will check for
//      a valid PermissionGrant before allowing enrollment in a full section.
//
// GUI INTEGRATION:
//   // Faculty roster → select waitlisted student → "Grant Permission" button:
//   GrantWaitlistPermissionCommand cmd =
//       new GrantWaitlistPermissionCommand(faculty, student, section, "Student has prerequisite waiver");
//   executor.execute(cmd);
//
//   if (cmd.wasSuccessful()) {
//       showInfo(student.getFullName() + " can now enroll within 48 hours.");
//   }
//
//   // Student portal checks permissions when attempting to register:
//   boolean canOverride = PermissionGrant.hasActiveGrant(student.getId(), section.getId());
//
// ============================================================================

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.Column;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Id;
import edu.advising.core.Table;
import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Faculty;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class GrantWaitlistPermissionCommand extends BaseCommand {

    // ── State ────────────────────────────────────────────────────────────────

    private final Faculty faculty;
    private ObservableStudent student;
    private Section section;
    private final String notes;         // Optional faculty note ("prerequisite waived")
    private final int validForHours;    // How long the permission is active

    // Populated during execute() — needed for undo and student-facing display
    private int grantId;                // PK of the permission_grants row

    private final NotificationManager notificationManager;
    private final DatabaseManager     dbManager;

    // Default permission window: 48 hours before it expires unused
    private static final int DEFAULT_VALID_HOURS = 48;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public GrantWaitlistPermissionCommand(Faculty faculty, ObservableStudent student,
                                          Section section, String notes) {
        this(faculty, student, section, notes, DEFAULT_VALID_HOURS);
    }

    public GrantWaitlistPermissionCommand(Faculty faculty, ObservableStudent student,
                                          Section section, String notes, int validForHours) {
        super();
        this.commandType      = "GRANT_WAITLIST_PERMISSION";
        this.faculty          = faculty;
        this.student          = student;
        this.section          = section;
        this.notes            = notes;
        this.validForHours    = validForHours;
        this.notificationManager = NotificationManager.getInstance();
        this.dbManager           = DatabaseManager.getInstance();
    }

    // -------------------------------------------------------------------------
    // Command Interface — execute()
    // -------------------------------------------------------------------------

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        // ── Verify faculty owns the section ──────────────────────────────────
        if (section.getFacultyId() != faculty.getId()) {
            successful   = false;
            errorMessage = "Only the section instructor can grant enrollment permission.";
            System.out.println("✗ " + errorMessage);
            return;
        }

        // ── Check student is actually on the waitlist ─────────────────────────
        try {
            boolean onWaitlist = section.getWaitlist().stream()
                    .anyMatch(we -> we.getStudentId() == student.getId());
            if (!onWaitlist) {
                successful   = false;
                errorMessage = String.format("%s is not on the waitlist for %s.",
                        student.getFullName(), section.getCourseCode());
                System.out.println("✗ " + errorMessage);
                return;
            }
        } catch (SQLException e) {
            successful   = false;
            errorMessage = "Could not verify waitlist status: " + e.getMessage();
            System.err.println("✗ " + errorMessage);
            return;
        }

        // ── Persist a permission_grants record ───────────────────────────────
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(validForHours);
        String sql = "INSERT INTO permission_grants " +
                "(faculty_id, student_id, section_id, granted_at, expires_at, notes, is_used, is_active) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, FALSE, TRUE)";
        try {
            grantId = dbManager.executeInsert(sql,
                    faculty.getId(),
                    student.getId(),
                    section.getId(),
                    Timestamp.valueOf(expiresAt),
                    notes);

            if (grantId <= 0) {
                successful   = false;
                errorMessage = "Permission grant could not be saved.";
                return;
            }

            executed   = true;
            successful = true;

            System.out.printf("✓ Permission granted: %s may enroll in %s within %d hours.%n",
                    student.getFullName(), section.getCourseCode(), validForHours);

            // ── Notify the student that they may now register ─────────────────
            // This fires the Observer chain → email / push notification to student.
            notificationManager.notifyWaitlistUpdate(
                    student, section.getCourseCode(),
                    0); // Position 0 signals "you have a permission override"

        } catch (SQLException e) {
            successful   = false;
            errorMessage = "Failed to persist permission grant: " + e.getMessage();
            System.err.println("✗ " + errorMessage);
        }
    }

    // -------------------------------------------------------------------------
    // Command Interface — undo()
    // -------------------------------------------------------------------------

    @Override
    public void undo() {
        if (!executed || !successful || grantId <= 0) {
            System.out.println("Cannot undo — permission was not granted.");
            return;
        }

        // Deactivate the permission grant so the student can no longer use it.
        String sql = "UPDATE permission_grants SET is_active = FALSE WHERE id = ? AND is_used = FALSE";
        try {
            int updated = dbManager.executeUpdate(sql, grantId);
            if (updated > 0) {
                undoneAt = LocalDateTime.now();
                isUndone = true;
                System.out.printf("↶ Undone: Permission revoked for %s in %s.%n",
                        student.getFullName(), section.getCourseCode());
                // Notify student the permission was revoked.
                notificationManager.notifyWaitlistUpdate(
                        student, section.getCourseCode(),
                        section.getEnrolled()); // Show actual position again
            } else {
                System.out.println("✗ Permission could not be revoked — student may have already used it.");
            }
        } catch (SQLException e) {
            System.err.println("✗ Undo failed: " + e.getMessage());
        }
    }

    @Override
    public boolean isUndoable() {
        // Can only revoke if the permission hasn't been used by the student yet.
        if (!executed || !successful || grantId <= 0) return false;
        try {
            return dbManager.executeQuery(
                    "SELECT is_used FROM permission_grants WHERE id = ?",
                    rs -> rs.next() && !rs.getBoolean("is_used"),
                    grantId);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return String.format("Grant waitlist permission: %s → %s (valid %dh)",
                student.getFullName(), section.getCourseCode(), validForHours);
    }

    // -------------------------------------------------------------------------
    // Serialization
    // -------------------------------------------------------------------------

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("facultyId",    faculty.getId());
        data.put("studentPk",    student.getId());
        data.put("sectionId",    section.getId());
        data.put("notes",        notes);
        data.put("validForHours",validForHours);
        data.put("grantId",      grantId);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("GrantWaitlistPermissionCommand: serialization failed", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            int studentPk = (int) data.get("studentPk");
            int sectionId = (int) data.get("sectionId");

            Student raw = dbManager.fetchOne(Student.class, "id", studentPk);
            if (raw != null) this.student = ObservableStudent.fromSuperType(raw);
            this.section  = dbManager.fetchOne(Section.class, "id", sectionId);
            this.grantId  = (int) data.get("grantId");
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("GrantWaitlistPermissionCommand: deserialization failed", e);
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\MacroCommand.java
```java
package edu.advising.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MacroCommand - Executes multiple commands as one transaction
 */
@Table(name = "command_history", isSubTable = true)
public class MacroCommand extends BaseCommand {
    private List<BaseCommand> commands;
    private String description;

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public MacroCommand() {
        this("Initialized Macro");
    }

    public MacroCommand(String description) {
        super();
        this.commandType = "MACRO";
        this.description = description;
        this.commands    = new ArrayList<>();
    }

    public void addCommand(BaseCommand command) {
        commands.add(command);
    }

    public static MacroCommand fromSuperType(BaseCommand base) {
        MacroCommand cmd = new MacroCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();
        System.out.printf("▶ Executing macro: %s (%d commands)%n", description, commands.size());

        for (BaseCommand command : commands) {
            command.execute();
            if (!command.wasSuccessful()) {
                System.out.println("  ✗ Sub-command failed: " + command.getDescription());
                successful = false;
                executed   = true;
                System.out.println("✗ Macro failed — rolling back completed sub-commands");
                undo();
                return;
            }
        }

        executed   = true;
        successful = true;
        System.out.println("✓ Macro completed successfully");
    }

    @Override
    public void undo() {
        if (!executed) return;
        System.out.printf("↶ Undoing macro: %s%n", description);
        // Undo in reverse order (i.e. only commands that actually succeeded)
        for (int i = commands.size() - 1; i >= 0; i--) {
            BaseCommand cmd = commands.get(i);
            if (cmd.wasSuccessful()) {
                cmd.undo();
            }
        }
        this.undoneAt = LocalDateTime.now();
        this.isUndone = true;
    }

    @Override
    public boolean isUndoable() {
        return executed && commands.stream().allMatch(BaseCommand::isUndoable);
    }

    @Override
    public String getDescription() {
        return String.format("%s (Macro: %d commands)", description, commands.size());
    }

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < commands.size(); i++) {
            BaseCommand bc = commands.get(i);
            Map<String, Object> entry = new HashMap<>();
            entry.put("type",  bc.getClass().getName());  // fully-qualified, no "class " prefix
            entry.put("index", i);
            entry.put("data",  bc.serializeCommandData());
            list.add(entry);
        }
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize MacroCommand data", e);
        }
    }

    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Map<String, Object>> list =
                    mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
            this.commands = new ArrayList<>(list.size());

            for (Map<String, Object> entry : list) {
                String className    = (String) entry.get("type");
                String subData      = mapper.writeValueAsString(entry.get("data"));
                BaseCommand subCmd  = instantiateCommand(className);
                subCmd.setCommandData(subData);
                subCmd.initAfterLoad();
                this.commands.add(subCmd);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize MacroCommand data", e);
        }
    }

    private BaseCommand instantiateCommand(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return (BaseCommand) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate command class: " + className, e);
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\Payment.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - Payment Model (ORM Entity)
// ============================================================================
//
// WHY THIS MODEL IS HERE:
//   The PaymentCommand needs to persist payment records. Rather than writing
//   raw INSERT SQL strings inside the command (which was the old commented-out
//   approach), we define a proper ORM-annotated entity and let DatabaseManager
//   handle the persistence via upsert().
//
//   This is also the pattern established by Enrollment, WaitlistEntry, and
//   Section — models annotated with @Table and @Column so the ORM can reflect
//   over them at runtime.
//
// DB TABLE: payments (defined in DatabaseManager.initializeDatabase(), Week 5-8 section)
//
// FIELDS MAP EXACTLY TO:
//   id, student_id, amount, payment_type, payment_method,
//   payment_date, status, transaction_id, reference_number, notes
//
// ============================================================================

import edu.advising.core.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "payments")
public class Payment {

    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;

    @Column(name = "student_id", foreignKey = true)
    private int studentId;          // FK → students(id)

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "payment_type")
    private String paymentType;     // TUITION, FEE, HOUSING, etc.

    @Column(name = "payment_method")
    private String paymentMethod;   // CREDIT_CARD, CHECK, CASH, etc.

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "status")
    private String status;          // PENDING, COMPLETED, FAILED, REFUNDED

    @Column(name = "transaction_id")
    private String transactionId;   // External gateway reference

    @Column(name = "reference_number")
    private String referenceNumber; // Internal reference for the student

    @Column(name = "notes")
    private String notes;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** No-arg constructor required by ORM reflective instantiation. */
    public Payment() {}

    /**
     * Minimal constructor used by PaymentCommand when processing a new payment.
     */
    public Payment(int studentId, BigDecimal amount, String paymentType,
                   String paymentMethod, String status) {
        this.studentId     = studentId;
        this.amount        = amount;
        this.paymentType   = paymentType;
        this.paymentMethod = paymentMethod;
        this.status        = status;
        this.paymentDate   = LocalDateTime.now();
        // Generate a human-readable reference number for the student's receipt.
        this.referenceNumber = generateReferenceNumber();
    }

    // -------------------------------------------------------------------------
    // Convenience Methods
    // -------------------------------------------------------------------------

    /** @return true when this payment record represents a completed transaction. */
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    /** @return true when this payment has been refunded (e.g. via undo). */
    public boolean isRefunded() {
        return "REFUNDED".equals(status);
    }

    /**
     * Generates a simple reference number for receipt display.
     * In a real system this would come from a payment gateway.
     * Format: PAY-<timestamp-millis>
     */
    private static String generateReferenceNumber() {
        return "PAY-" + System.currentTimeMillis();
    }

    // -------------------------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------------------------

    public int getId()                     { return id; }
    public void setId(int id)              { this.id = id; }

    public int getStudentId()              { return studentId; }
    public void setStudentId(int studentId){ this.studentId = studentId; }

    public BigDecimal getAmount()          { return amount; }
    public void setAmount(BigDecimal amount){ this.amount = amount; }

    public String getPaymentType()         { return paymentType; }
    public void setPaymentType(String t)   { this.paymentType = t; }

    public String getPaymentMethod()       { return paymentMethod; }
    public void setPaymentMethod(String m) { this.paymentMethod = m; }

    public LocalDateTime getPaymentDate()  { return paymentDate; }
    public void setPaymentDate(LocalDateTime d){ this.paymentDate = d; }

    public String getStatus()              { return status; }
    public void setStatus(String status)   { this.status = status; }

    public String getTransactionId()       { return transactionId; }
    public void setTransactionId(String t) { this.transactionId = t; }

    public String getReferenceNumber()     { return referenceNumber; }
    public void setReferenceNumber(String r){ this.referenceNumber = r; }

    public String getNotes()               { return notes; }
    public void setNotes(String notes)     { this.notes = notes; }

    @Override
    public String toString() {
        return String.format("Payment[id=%d, student=%d, amount=%s, type=%s, status=%s, ref=%s]",
                id, studentId, amount, paymentType, status, referenceNumber);
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\PaymentCommand.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - PaymentCommand (Concrete Command)
// ============================================================================
//
// FEATURE:  Financial Information → Make a Payment
//
// WHY COMMAND PATTERN HERE:
//   A payment is a transactional operation that:
//     1. Must be logged for auditing (every cent that moves needs a record).
//     2. May need to be reversed (refunds — the undo operation).
//     3. Should trigger Observer notifications (PaymentReceived → email receipt).
//     4. Could be part of a MacroCommand (e.g., enroll + pay tuition at once).
//
//   Without Command Pattern, all of this logic would be tangled into a button
//   handler or a service method. Command Pattern separates:
//     WHO triggers the action (GUI button / REST endpoint)
//     WHAT the action does (this class)
//     HOW it is undone (the undo() method)
//
// UNDO SEMANTICS:
//   Undoing a payment marks the Payment row as REFUNDED via ORM upsert().
//   In a real system this would also call a payment gateway refund API.
//
// GUI INTEGRATION:
//   PaymentCommand cmd = new PaymentCommand(student, amount, paymentType, paymentMethod);
//   executor.execute(cmd);
//   if (cmd.wasSuccessful()) showReceipt(cmd.getPaymentReferenceNumber());
//
// ============================================================================

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Student;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Table(name = "command_history", isSubTable = true)
public class PaymentCommand extends BaseCommand {
    private ObservableStudent student;
    private BigDecimal amount;
    private String paymentType;    // TUITION, FEE, HOUSING, etc.
    private String paymentMethod;  // CREDIT_CARD, CHECK, CASH, etc.

    // Populated after execute() completes — needed for undo and receipt display.
    private Payment paymentRecord;

    private NotificationManager notificationManager;
    private DatabaseManager     dbManager;

    // Constructors

    public PaymentCommand() {
        this(null, null, null, null);
    }

    /**
     * @param student       The student making the payment.
     * @param amount        Payment amount as BigDecimal (must be > 0).
     * @param paymentType   Category: TUITION, FEE, HOUSING, etc.
     * @param paymentMethod Method: CREDIT_CARD, CHECK, CASH, ONLINE, etc.
     */
    public PaymentCommand(ObservableStudent student, BigDecimal amount,
                          String paymentType, String paymentMethod) {
        super();
        this.commandType         = "PAYMENT";
        this.student             = student;
        this.amount              = amount;
        this.paymentType         = paymentType;
        this.paymentMethod       = paymentMethod;
        this.notificationManager = NotificationManager.getInstance();
        this.dbManager           = DatabaseManager.getInstance();
    }

    /** Backward-compatible convenience constructor for double amounts. */
    public PaymentCommand(ObservableStudent student, double amount, String paymentType) {
        this(student, BigDecimal.valueOf(amount), paymentType, "ONLINE");
    }

    public static PaymentCommand fromSuperType(BaseCommand base) {
        PaymentCommand cmd = new PaymentCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    // -------------------------------------------------------------------------
    // Command Interface — execute()
    // -------------------------------------------------------------------------

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        // Pre-condition: amount must be positive
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            successful = false;
            errorMessage = "Payment amount must be greater than zero.";
            System.out.println("✗ " + errorMessage);
            return;
        }

        // Build Payment ORM entity and persist it via upsert()
        paymentRecord = new Payment(
                student.getId(),
                amount,
                paymentType,
                paymentMethod,
                "COMPLETED"
        );
        paymentRecord.setNotes("Processed via " + paymentMethod);

        try {
            // upsert() reflects over Payment's @Table/@Column annotations and
            // builds the MERGE statement — no hand-written SQL needed here.
            dbManager.upsert(paymentRecord);

            if (paymentRecord.getId() <= 0) {
                // upsert() should set the generated id via setId() — something went wrong.
                throw new IllegalStateException("Payment was saved but no ID was returned.");
            }

            // Adjust the student's account balance atomically
            updateStudentAccountBalance(amount.negate()); // payment reduces balance owed

            executed  = true;
            successful = true;

            System.out.printf("✓ Payment processed: $%.2f (%s) via %s | Ref: %s%n",
                    amount, paymentType, paymentMethod, paymentRecord.getReferenceNumber());

            // ── Trigger Observer notification ─────────────────────────────────
            // This fires the NotificationManager which pushes to all attached
            // Observer channels (email receipt, push notification, etc.)
            notificationManager.notifyPaymentReceived(student, amount.doubleValue(), paymentType);

        } catch (SQLException | IllegalAccessException | IllegalStateException e) {
            successful   = false;
            errorMessage = "Payment processing failed: " + e.getMessage();
            System.err.println("✗ " + errorMessage);
        }
    }

    // -------------------------------------------------------------------------
    // Command Interface — undo()
    // -------------------------------------------------------------------------

    @Override
    public void undo() {
        if (!executed || !successful || paymentRecord == null) {
            System.out.println("Cannot undo — payment was not completed.");
            return;
        }

        try {
            // Mark the Payment entity REFUNDED and re-persist via ORM upsert()
            paymentRecord.setStatus("REFUNDED");
            dbManager.upsert(paymentRecord);

            // Reverse the balance adjustment
            updateStudentAccountBalance(amount); // adds the amount back to balance owed

            undoneAt = LocalDateTime.now();
            isUndone = true;

            System.out.printf("↶ Undone: Refund issued $%.2f (%s) | Ref: %s%n",
                    amount, paymentType, paymentRecord.getReferenceNumber());

            // Notify student of the refund.
            notificationManager.notifyPaymentReceived(
                    student, -amount.doubleValue(), "REFUND-" + paymentType);

        } catch (SQLException | IllegalAccessException e) {
            System.err.println("✗ Failed to process refund: " + e.getMessage());
        }
    }

    @Override
    public boolean isUndoable() {
        // Can only refund if the original payment was in this session and succeeded.
        // In production, you'd also enforce a refund window (e.g., same calendar day).
        return executed && successful && paymentRecord != null && paymentRecord.isCompleted();
    }

    @Override
    public String getDescription() {
        return String.format("Payment of $%.2f (%s via %s)", amount, paymentType, paymentMethod);
    }

    // -------------------------------------------------------------------------
    // Convenience Getter — used by the UI to show a receipt after execute()
    // -------------------------------------------------------------------------

    /**
     * Returns the reference number for receipt display after execute().
     *
     * GUI Usage:
     *   executor.execute(cmd);
     *   if (cmd.wasSuccessful()) receiptLabel.setText("Ref: " + cmd.getPaymentReferenceNumber());
     */
    public String getPaymentReferenceNumber() {
        return paymentRecord != null ? paymentRecord.getReferenceNumber() : null;
    }

    // -------------------------------------------------------------------------
    // Serialization — for CommandHistory persistence
    // -------------------------------------------------------------------------

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk",  student.getId());   // int PK
        data.put("studentId",     student.getStudentId());                               // int PK
        data.put("amount",        amount.toPlainString());                        // BigDecimal-safe
        data.put("paymentType",   paymentType);
        data.put("paymentMethod", paymentMethod);
         // Store the generated payment record id so we can retrieve it on undo/redo
        data.put("paymentId",     paymentRecord != null ? paymentRecord.getId() : 0);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("PaymentCommand: serialization failed", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);

            // Reconstruct the student by numeric pk (not the String student_id field)
            int studentPk = (int) data.get("studentPk");
            Student raw   = dbManager.fetchOne(Student.class, "id", studentPk);
            if (raw != null) {
                this.student = ObservableStudent.fromSuperType(raw);
            }

            this.amount        = new BigDecimal(data.get("amount").toString());
            this.paymentType   = (String) data.get("paymentType");
            this.paymentMethod = (String) data.get("paymentMethod");

            // Re-hydrate the Payment record so undo() can find the DB row.
            int paymentId = (int) data.get("paymentId");
            if (paymentId > 0) {
                this.paymentRecord = dbManager.fetchOne(Payment.class, "id", paymentId);
            }

        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("PaymentCommand: deserialization failed", e);
        }
    }

    // -------------------------------------------------------------------------
    // Private Helpers
    // -------------------------------------------------------------------------

    /**
     * Atomically adjusts the student's account balance.
     *
     * This intentionally uses a raw SQL UPDATE (via executeUpdate) rather than
     * an ORM upsert() because we need an atomic increment/decrement against
     * the existing row value. upsert() would overwrite the entire row with a
     * potentially stale in-memory value if two sessions ran concurrently.
     * This is the one place in PaymentCommand where direct SQL is the correct
     * and safer choice over the ORM without further ORM development.
     */
    private void updateStudentAccountBalance(BigDecimal delta) {
        String updateSql = "UPDATE student_accounts " +
                "SET current_balance = current_balance + ?, " +
                "    total_payments  = total_payments  + ?, " +
                "    last_updated    = CURRENT_TIMESTAMP " +
                "WHERE student_id = ?";
        try {
            int rows = dbManager.executeUpdate(updateSql, delta, delta.negate(), student.getId());
            if (rows == 0) {
                // Account row doesn't exist yet — create it.
                dbManager.executeInsert(
                        "INSERT INTO student_accounts " +
                                "(student_id, current_balance, total_charges, total_payments) " +
                                "VALUES (?, ?, 0.00, ?)",
                        student.getId(), delta, delta.negate()
                );
            }
        } catch (SQLException e) {
            System.err.println("PaymentCommand: could not update student account — " + e.getMessage());
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\RegisterCommand.java
```java
package edu.advising.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.users.Student;

import java.util.HashMap;
import java.util.Map;

/**
 * RegisterCommand - Register student for a course section
 */
@Table(name = "command_history", isSubTable = true)
public class RegisterCommand extends BaseCommand {
    private ObservableStudent student;
    private Section section;
    private NotificationManager notificationManager;
    private int enrollmentId;

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public RegisterCommand() {
        this(null, null);
    }

    public RegisterCommand(ObservableStudent student, Section section) {
        super();
        this.commandType = "REGISTER";
        this.student = student;
        this.section = section;
        this.notificationManager = NotificationManager.getInstance();
    }

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        if (!section.hasCapacity()) {
            successful = false;
            errorMessage = String.format("Registration failed for %s - section full", section.getCourseCode());
            System.out.println("✗ " + errorMessage);
            return;
        }

        if (hasScheduleConflict()) {
            successful = false;
            errorMessage = String.format("Registration failed for %s - schedule conflict", section.getCourseCode());
            System.out.println("✗ " + errorMessage);
            return;
        }

        if ((this.enrollmentId = section.enroll(student)) > 0) {
            executed    = true;
            successful  = true;
            System.out.printf("✓ Student %s registered for %s%n",
                    student.getStudentId(), section.getCourseCode());
            notificationManager.notifyRegistration(student, section.getCourseCode(), true);
        } else {
            successful   = false;
            errorMessage = "Already enrolled or duplicate registration prevented.";
        }
    }

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo - command not executed or failed");
            return;
        }

        // Remove from section
        if( section.drop(student) ) {
            System.out.printf("↶ Undone: Registration for %s%n", section.getCourseCode());
            this.undoneAt = LocalDateTime.now();
            this.isUndone = true;
            // Notify about drop
            notificationManager.notifyRegistration(student, section.getCourseCode(), false);
        }
    }

    @Override
    public boolean isUndoable() {
        return executed && successful;
    }

    @Override
    public String getDescription() {
        return String.format("Register for %s (%s)", section.getCourseCode(), section.getCourseName());
    }

    private boolean hasScheduleConflict() {
        // Simplified - in real implementation, check time conflicts in student.
        return false;
    }

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk", student.getId());    //TODO: I'm not sure this is needed since my ORM handles sub-classes.
        data.put("studentId", student.getStudentId());
        data.put("sectionId", section.getId()); // Assuming Section has an id
        data.put("enrollmentId", enrollmentId);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize RegisterCommand data", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            // TODO: Figure out if we have to really deal with studentPk because student is a subclass of  User.
            int studentPk = (int) data.get("studentPk");
            int sectionId = (int) data.get("sectionId");
            this.enrollmentId = (int) data.get("enrollmentId");

            // Fetch as Student (annotated), then promote to ObservableStudent
            Student raw = DatabaseManager.getInstance().fetchOne(Student.class, "id", studentPk);
            if (raw != null) {
                this.student = ObservableStudent.fromSuperType(raw);
                this.student = ObservableStudent.fromSuperType(raw);
                this.section = DatabaseManager.getInstance().fetchOne(Section.class, "id", sectionId);
            }
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("Failed to deserialize RegisterCommand data", e);
        }
    }
}


```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\Section.java
```java
package edu.advising.commands;

import edu.advising.core.*;
import edu.advising.users.Faculty;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Course Section - Represents a course section
 */
@Table(name = "sections")
public class Section {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "course_id", foreignKey = true)
    private int courseId;  // References courses
    @Id
    @Column(name = "section_number")
    private String sectionNumber;
    @Id
    @Column(name = "semester")
    private String semester;
    @Id
    @Column(name = "year")
    private int year;
    @Column(name = "capacity")
    private int capacity;
    @Column(name = "enrolled")
    private int enrolled;
    @Column(name = "faculty_id", nullableforeignKey = true)
    private int facultyId; // References faculty
    @Column(name = "room")
    private String room;
    @Column(name = "status")
    private String status;  //OPEN, CLOSED, CANCELLED
    @ManyToOne(targetEntity = Course.class, joinColumn = "course_id")
    private Course course; // Cached object representing this sections courses.
    @ManyToOne(targetEntity = Faculty.class, joinColumn = "faculty_id")
    private Faculty faculty; // Cached object representing this faculty that teaches this course.
    @ManyToMany(
            targetEntity = Student.class,
            joinTable = "enrollments",
            joinColumn = "section_id",
            inverseJoinColumn = "student_id"
    )
    private List<Student> enrolledStudents;
    @OneToMany(targetEntity = Enrollment.class, mappedBy = "section_id")
    private List<Enrollment> enrollments;
    @OneToMany(targetEntity = WaitlistEntry.class, mappedBy = "section_id")
    private List<WaitlistEntry> waitlist;

    public Section() {}

    public Section(int id, int courseId, String sectionNumber,
                   String semester, int year, int capacity, int enrolled, int facultyId, String room, String status) {
        this(id, courseId, sectionNumber, semester, year, capacity, enrolled, facultyId);
        this.room = room;
        this.status = status;
    }

    public Section(int id, int courseId, String sectionNumber,
                   String semester, int year, int capacity, int enrolled, int facultyId) {
        this(courseId, sectionNumber, semester, year, capacity, enrolled, facultyId);
        this.id = id;
    }

    public Section(int courseId, String sectionNumber,
                   String semester, int year, int capacity, int enrolled, int facultyId) {
        this(courseId, sectionNumber, semester, year, capacity);
        this.enrolled = enrolled;
        this.facultyId = facultyId;
    }

    public Section(int courseId, String sectionNumber, String semester, int year, int capacity) {
        this(sectionNumber, semester, year, capacity);
        this.courseId = courseId;
    }

    public Section(String sectionNumber, String semester, int year, int capacity) {
        this.sectionNumber = sectionNumber;
        this.semester = semester;
        this.year = year;
        this.capacity = capacity;
        this.enrolledStudents = new ArrayList<>();
        this.waitlist = new ArrayList<>();
    }

    public boolean hasCapacity() {
        return enrolled < capacity;
    }

    private boolean isAlreadyOnWaitlist(Student newStudent) {
        try {
            return getWaitlist().stream().anyMatch(we -> we.getStudentId() == newStudent.getId());
        } catch (SQLException se) {
            se.printStackTrace();
            return true;
        }
    }

    private boolean isAlreadyEnrolled(Student newStudent) {
        try {
            return getEnrolledStudents().stream().anyMatch(student -> student.getId() == newStudent.getId());
        } catch (SQLException se) {
            se.printStackTrace();
            return true;
        }
    }

    public int enroll(Student newStudent) {
        if (hasCapacity() && !isAlreadyEnrolled(newStudent)) {
            // TODO: Update DatabaseManager to handle generic composite object dependency updates.
            try {
                ensureId();
                Enrollment enrollment = new Enrollment(newStudent.getId(), this.getId());
                DatabaseManager.getInstance().upsert(enrollment);
                // Make sure enrollments has already been lazyloaded.
                if(this.enrollments == null) {
                    this.getEnrollments();
                }
                this.enrollments.add(enrollment);
                enrolledStudents.add(newStudent);
                enrolled++;
                // To make sure enrollment numbers get updated, could also make this a trigger in the database.
                DatabaseManager.getInstance().upsert(this);
                return enrollment.getId();
            } catch (SQLException | IllegalAccessException e) {
                return 0;
            }
        }
        return 0;
    }

    public boolean drop(Student dropStudent) {
        // First let's see if we can find an Enrollment for this student.
        try {
            Optional<Enrollment> optionalEnrollment = this.getEnrollments().stream()
                    .filter(enrollment -> enrollment.getStudentId() == dropStudent.getId()).findFirst();
            if(optionalEnrollment.isPresent()) {
                DatabaseManager dbManager = DatabaseManager.getInstance();
                // Update the Enrollment with the DROP status
                Enrollment enrollment = optionalEnrollment.get();
                enrollment.setStatus("DROPPED");
                enrollment.setDroppedAt(LocalDateTime.now());
                dbManager.upsert(enrollment);
                if( enrolledStudents.removeIf(student -> student.getId() == dropStudent.getId()) ) {
                    this.enrollments.remove(enrollment);
                    enrolled--;
                    // To make sure enrollment numbers get updated, could also make this a trigger in the database.
                    dbManager.upsert(this);
                    return true;
                }
            }
        } catch (SQLException | IllegalAccessException e) { e.printStackTrace(); }
        return false;
    }

    public int addToWaitlist(Student newStudent) {
        if (!isAlreadyOnWaitlist(newStudent) && !isAlreadyEnrolled(newStudent)) {
            try {
                ensureId();
                WaitlistEntry waitlist = new WaitlistEntry(newStudent.getId(), this.getId(), this.getNextWaitlistPosition());
                DatabaseManager.getInstance().upsert(waitlist);
                this.waitlist.add(waitlist);
                return waitlist.getId();
            } catch (SQLException | IllegalAccessException e) {
                //e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    public boolean removeFromWaitlist(Student student) {
        try {
            // First let's see if we can find a WaitlistEntry for this student.
            Optional<WaitlistEntry> wle = getWaitlist().stream()
                    .filter(we -> we.getStudentId() == student.getId()).findFirst();
            if (wle.isPresent()) {
                DatabaseManager.getInstance().delete(wle.get());  // ← unwrap the Optional
                return waitlist.remove(wle.get());
            }
            return true;
        } catch (SQLException | IllegalAccessException e) { e.printStackTrace(); }
        return false;
    }

    public int getNextWaitlistPosition() throws SQLException {
        if(this.waitlist == null || this.waitlist.isEmpty()) {
            String sql = "SELECT count(*) FROM waitlist where section_id = ?;";
            return DatabaseManager.getInstance().executeQuery(sql, rs -> {
                if(rs.next()) {
                    return rs.getInt(1);
                }
                return 0;  // default return is 0
            }, this.getId()) + 1;
        }
        return waitlist.size() + 1; // 1-based
    }

    public int getWaitlistPosition(Student student) throws SQLException {
        if(this.waitlist == null || this.waitlist.isEmpty()) {
            String sql = "SELECT position FROM waitlist where section_id = ? and student_id = ?;";
            return DatabaseManager.getInstance().executeQuery(sql, rs -> {
                return rs.getInt(1);
            }, this.getId(), student.getId());
        }
        return waitlist.stream().filter(wl -> wl.getStudentId() == student.getId())
                .findFirst().map(WaitlistEntry::getPosition).orElse(0);
    }

    // Getters
    public int getId() { return id; }
    public String getSectionNumber() { return sectionNumber; }
    public String getSemester() { return semester; }
    public int getCapacity() { return capacity; }
    public int getEnrolled() { return enrolled; }
    public int getAvailableSeats() { return capacity - enrolled; }

    public List<WaitlistEntry> getWaitlist() throws SQLException {
        if (this.waitlist == null) {
            this.waitlist = DatabaseManager.getInstance().fetchMany(
                    WaitlistEntry.class, "section_id", this.getId());
        }
        return this.waitlist;
    }

    public String getCourseName() {
        try {
            Course c = this.getCourse();
            return (c != null) ? c.getName() : "UNKNOWN";
        } catch (SQLException se) {
            se.printStackTrace();
            return "UNKNOWN (Cause: DB ERROR)";
        }
    }

    public String getCourseCode() {
        try {
            Course course = this.getCourse();
            return course.getCode() + "-" + semester + year + "-" + sectionNumber; // CIS12-SP26-2
        } catch (SQLException e) { }
        return "UNKNOWN-" + semester + year + "-" + sectionNumber; // UNKNOWN-SP26-2
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s-%s: %s %s (%d/%d enrolled)",
                courseId, sectionNumber, semester, year, enrolled, capacity);
    }

    public Course getCourse() throws SQLException {
        if (this.course == null) {
            // Lazy Load: Use the generic fetchOne from DatabaseManager
            this.course = DatabaseManager.getInstance()
                    .fetchOne(Course.class, "id", this.courseId);
        }
        return (this.course != null) ? this.course : null;
    }

    public void setCourse(Course course) {
        this.courseId = course.getId();
        this.course = course;
    }

    public List<Student> getEnrolledStudents() throws SQLException {
        if (this.enrolledStudents == null) {
            this.enrolledStudents = DatabaseManager.getInstance().fetchManyToMany(
                    Student.class, "enrollments", "section_id", "student_id", this.getId()
            );
        }
        return this.enrolledStudents;
    }

    public void setEnrolledStudents(List<Student> students) {
        this.enrolledStudents = students;
    }

    public List<Enrollment> getEnrollments() throws SQLException {
        // TODO: Gotta find a way to modify the fetch calls to take additional filters since this will return
        //   Enrollments in ANY status (i.e. DROPPED, etc.).
        if (this.enrollments == null) {
            // Lazy Load: Use the generic fetchMany from DatabaseManager
            this.enrollments = DatabaseManager.getInstance()
                    .fetchMany(Enrollment.class, "section_id", this.id);
        }
        return this.enrollments;
    }

    protected void ensureId() throws SQLException, IllegalAccessException {
        if(this.getId() == 0) {
            // If the id is not set, we need to save this object to get an id to set on the list items.
            DatabaseManager.getInstance().upsert(this);
        }
    }

    public void setEnrollments(List<Enrollment> enrollments) throws SQLException, IllegalAccessException {
        // TODO: Make the DatabaseManager even MORE generic where it can build a dependency graph of objects
        //   and make upsert/upsertAll calls to satisfy and update ids in order, rather than coding setters like this.
        ensureId();
        // Now, let's add this object's id to the related list items foreign key id
        for(Enrollment e : enrollments) { e.setSectionId(this.getId()); }
        // Now let's upsertAll of these list items (i.e. a batch) and set as this object's related field.
        DatabaseManager.getInstance().upsertAll(enrollments);
        this.enrollments = enrollments;
    }

    public Faculty getFaculty() throws SQLException {
        if (this.faculty == null) {
            // Lazy Load: Use the generic fetchOne from DatabaseManager
            this.faculty = DatabaseManager.getInstance()
                    .fetchOne(Faculty.class, "id", this.facultyId);
        }
        return (this.faculty != null) ? this.faculty : null;
    }

    public void setFaculty(Faculty faculty) {
        this.facultyId = faculty.getId();
        this.faculty = faculty;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\UpdateContactCommand.java
```java
package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - UpdateContactCommand (Concrete Command)
// ============================================================================
//
// FEATURE:  Academic Profile → Contact Information Update
//
// WHY COMMAND PATTERN HERE:
//   At first glance, updating an email address looks too simple for a Command.
//   But consider:
//     1. UNDO: If a user accidentally changes their email to a typo, they need
//        a way to reverse it without contacting an administrator.
//     2. AUDIT: FERPA and institutional policy often require logging who changed
//        contact info and when — the command_history table provides this for free.
//     3. MACRO: A future "Import Contact Info from SSO" feature could batch
//        multiple UpdateContactCommands inside a MacroCommand.
//     4. VALIDATION: The command encapsulates all validation (email format,
//        duplicate check) in one place, reusable from CLI, web, or desktop GUI.
//
// UNDO SEMANTICS:
//   The old values are captured at construction time (before execute()).
//   Undo restores the previous values using the same ORM upsert path.
//   This guarantees the user record stays consistent regardless of how
//   many times they undo/redo the change.
//
// GUI INTEGRATION:
//   // "Save" button on the Contact Information Update form:
//   UpdateContactCommand cmd = new UpdateContactCommand(
//       student, emailField.getText(), phoneField.getText()
//   );
//   executor.execute(cmd);
//
//   if (cmd.wasSuccessful()) {
//       showSuccessToast("Contact information updated.");
//   } else {
//       showError(cmd.getErrorMessage());
//   }
//   undoButton.setEnabled(executor.canUndo()); // "Undo Contact Update" tooltip
//
// ============================================================================

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Student;
import edu.advising.users.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Table(name = "command_history", isSubTable = true)
public class UpdateContactCommand extends BaseCommand {

    // ── State needed for execute and undo ────────────────────────────────────

    private ObservableStudent student;
    private String newEmail;
    private String newPhone;
    private String oldEmail;    // Captured at construction for undo
    private String oldPhone;    // Captured at construction for undo

    private final DatabaseManager dbManager;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public UpdateContactCommand() {
        this(new ObservableStudent("", "", "", "", "", "S0000"),
                "", "");
    }

    /**
     * Capture old values at construction time, before anything is changed.
     * This is the "snapshot before" approach standard in Command Pattern undo.
     *
     * @param student  The currently logged-in, live user object.
     * @param newEmail New email address (null to leave unchanged).
     * @param newPhone New phone number (null to leave unchanged).
     */
    public UpdateContactCommand(ObservableStudent student, String newEmail, String newPhone) {
        super();
        this.commandType = "UPDATE_CONTACT";
        this.student     = student;
        this.newEmail    = newEmail;
        this.newPhone    = newPhone;

        // Snapshot old values NOW, before any changes are made.
        // This is what makes undo() reliable.
        this.oldEmail = student.getEmail();
        this.oldPhone = student.getPhone(); // Requires phone field on User — see User.java note

        this.dbManager = DatabaseManager.getInstance();
    }

    public static UpdateContactCommand fromSuperType(BaseCommand base) {
        UpdateContactCommand cmd = new UpdateContactCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    // -------------------------------------------------------------------------
    // Command Interface — execute()
    // -------------------------------------------------------------------------

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        // ── Validate inputs ──────────────────────────────────────────────────
        if (newEmail != null && !isValidEmail(newEmail)) {
            successful   = false;
            errorMessage = "Invalid email format: " + newEmail;
            System.out.println("✗ " + errorMessage);
            return;
        }

        if (newEmail != null && isDuplicateEmail(newEmail, student.getId())) {
            successful   = false;
            errorMessage = "Email address is already in use: " + newEmail;
            System.out.println("✗ " + errorMessage);
            return;
        }

        // ── Apply changes to the in-memory user object ───────────────────────
        if (newEmail != null) student.setEmail(newEmail);
        if (newPhone != null) student.setPhone(newPhone);
        student.setUpdatedAt(LocalDateTime.now());

        // ── Persist via ORM — upsert uses @Table/@Column annotations on User ─
        // upsert() generates:
        //   MERGE INTO users (id, email, phone, updated_at, ...) VALUES (...)
        // Only the columns that changed will differ; the rest stay as-is.
        try {
            Student copy = student.toSubType();  // Copying object so upsert hierarchy annotations work properly.
            dbManager.upsert(copy); // Updating the copied object, realizing fields like updatedAt won't be synced.
            //TODO: determine if other fields need to be synced as well after upsert.
            student.setUpdatedAt(copy.getUpdatedAt()); // Syncing update at manually.

            executed   = true;
            successful = true;
            System.out.printf("✓ Contact info updated for %s (ID %d)%n",
                    student.getFullName(), student.getId());

        } catch (SQLException | IllegalAccessException e) {
            // Rollback in-memory changes if the DB update fails.
            student.setEmail(oldEmail);
            student.setPhone(oldPhone);

            successful   = false;
            errorMessage = "Database update failed: " + e.getMessage();
            System.err.println("✗ " + errorMessage);
        }
    }

    // -------------------------------------------------------------------------
    // Command Interface — undo()
    // -------------------------------------------------------------------------

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo — contact update was not completed.");
            return;
        }

        // Restore old values on the in-memory object first.
        student.setEmail(oldEmail);
        student.setPhone(oldPhone);
        student.setUpdatedAt(LocalDateTime.now());

        // Then persist the restored state.
        try {
            DatabaseManager.getInstance().upsert(student.toSubType());
            System.out.println("↶ Undone: Contact info restored for " + student.getStudentId());
            this.undoneAt = LocalDateTime.now();
            this.isUndone = true;
        } catch (SQLException | IllegalAccessException e) {
            // Undo failed — re-apply new values to keep in-memory state consistent with DB.
            student.setEmail(newEmail);
            student.setPhone(newPhone);
            System.err.println("✗ Undo failed — could not restore contact info: " + e.getMessage());
        }
    }

    @Override
    public boolean isUndoable() {
        return executed && successful;
    }

    @Override
    public String getDescription() {
        return String.format("Update contact info for %s (email: %s → %s)",
                student.getFullName(), oldEmail, newEmail != null ? newEmail : oldEmail);
    }

    // -------------------------------------------------------------------------
    // Serialization — for CommandHistory persistence and session recovery
    // -------------------------------------------------------------------------

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk", student.getId()); // Numeric PK, not the String student_id
        data.put("newEmail",  newEmail);
        data.put("newPhone",  newPhone);
        data.put("oldEmail",  oldEmail);
        data.put("oldPhone",  oldPhone);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("UpdateContactCommand: serialization failed", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            int studentPk  = (int) data.get("studentPk");
            this.newEmail  = (String) data.get("newEmail");
            this.newPhone  = (String) data.get("newPhone");
            this.oldEmail  = (String) data.get("oldEmail");
            this.oldPhone  = (String) data.get("oldPhone");

            Student raw  = DatabaseManager.getInstance().fetchOne(Student.class, "id", studentPk);
            this.student = ObservableStudent.fromSuperType(raw);
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("Failed to deserialize UpdateContactCommand data", e);
        }
    }

    // -------------------------------------------------------------------------
    // Private Validation Helpers
    // -------------------------------------------------------------------------

    private boolean isValidEmail(String email) {
        // RFC 5322 simplified: local@domain.tld
        return email != null && email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Check whether another active user already owns this email address.
     * Excludes the current user so they can re-save their own email without conflict.
     */
    private boolean isDuplicateEmail(String email, int excludeUserId) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND id <> ? AND is_active = TRUE";
        try {
            return dbManager.executeQuery(sql, rs -> {
                rs.next();
                return rs.getInt(1) > 0;
            }, email, excludeUserId);
        } catch (SQLException e) {
            System.err.println("UpdateContactCommand: duplicate email check failed — " + e.getMessage());
            return false; // Fail open — let the DB UNIQUE constraint catch it
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\WaitlistCommand.java
```java
package edu.advising.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * WaitlistCommand - Add student to waitlist
 */
@Table(name = "command_history", isSubTable = true)
public class WaitlistCommand extends BaseCommand {
    private ObservableStudent student;
    private Section section;
    private int waitlistId;
    private NotificationManager notificationManager;

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public WaitlistCommand() {
        this(null, null);
    }

    public WaitlistCommand(ObservableStudent student, Section section) {
        super();
        this.commandType         = "WAITLIST";
        this.student             = student;
        this.section             = section;
        this.notificationManager = NotificationManager.getInstance();
    }

    public static WaitlistCommand fromSuperType(BaseCommand base) {
        WaitlistCommand cmd = new WaitlistCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        if ((this.waitlistId = section.addToWaitlist(student)) > 0) {
            executed = true;
            successful = true;
            try {
                int position = section.getWaitlistPosition(student);
                System.out.printf("✓ Student %s added to waitlist for %s (Position: #%d)%n",
                        student.getStudentId(), section.getCourseCode(), position);
                notificationManager.notifyWaitlistUpdate(student, section.getCourseCode(), position);
            } catch (SQLException e) {
                System.out.printf("✓ Student %s added to waitlist for %s but couldn't determine position.%n",
                        student.getStudentId(), section.getCourseCode());
                notificationManager.notifyWaitlistUpdate(student, section.getCourseCode(), -1);
            }
        } else {
            successful   = false;
            errorMessage = String.format("Waitlist add failed for %s — already on waitlist or other error",
                    section.getCourseCode());
            System.out.println("✗ " + errorMessage);
        }
    }

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo - command not executed or failed");
            return;
        }

        if (section.removeFromWaitlist(student)) {
            System.out.printf("↶ Undone: Waitlist for %s%n", section.getCourseCode());
            this.undoneAt = LocalDateTime.now();
            this.isUndone = true;
            // Notify about waitlist removal.
            notificationManager.notifyWaitlistUpdate(student, section.getCourseCode(), Integer.MAX_VALUE);
        }
    }

    @Override
    public boolean isUndoable() {
        return executed && successful;
    }

    @Override
    public String getDescription() {
        return String.format("Add to waitlist for %s", section.getCourseCode());
    }

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk",  student.getId());   // int PK
        data.put("studentId", student.getStudentId());
        data.put("sectionId", section.getId()); // Assuming Section has an id
        data.put("waitlistId", waitlistId);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize WaitlistCommand data", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            int studentPk = (int) data.get("studentPk");
            int sectionId = (int) data.get("sectionId");
            this.waitlistId = (int) data.get("waitlistId");

            Student raw  = DatabaseManager.getInstance().fetchOne(Student.class, "id", studentPk);
            this.student = ObservableStudent.fromSuperType(raw);
            this.section = DatabaseManager.getInstance().fetchOne(Section.class, "id", sectionId);
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("Failed to deserialize WaitlistCommand data", e);
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\commands\WaitlistEntry.java
```java
package edu.advising.commands;

import edu.advising.core.*;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Table(name = "waitlist")
public class WaitlistEntry {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "student_id", foreignKey = true)
    private int studentId;
    @Id
    @Column(name = "section_id", foreignKey = true)
    private int sectionId;
    @Column(name = "position")
    private int position;
    @Column(name = "added_date")
    private LocalDateTime addedDate;
    @Column(name = "removed_date")
    private LocalDateTime removedDate;
    @Column(name = "status")
    private String status;
    @Column(name = "notification_sent")
    private boolean notificationSent;

    @ManyToOne(targetEntity = Section.class, joinColumn = "section_id")
    private Section section;
    @ManyToOne(targetEntity = Student.class, joinColumn = "student_id")
    private Student student;

    public WaitlistEntry() {}

    public WaitlistEntry(int studentId, int sectionId, int position) {
        this(studentId, sectionId);
        this.position = position;
    }
    public WaitlistEntry(int studentId, int sectionId) {
        this.studentId = studentId;
        this.sectionId = sectionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public LocalDateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public LocalDateTime getRemovedDate() {
        return removedDate;
    }

    public void setRemovedDate(LocalDateTime removedDate) {
        this.removedDate = removedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(boolean notificationSent) {
        this.notificationSent = notificationSent;
    }

    public Student getStudent() throws SQLException {
        if (this.student == null) {
            // Lazy Load: Use the generic fetchOne from DatabaseManager
            this.student = DatabaseManager.getInstance()
                    .fetchOne(Student.class, "id", this.studentId);
        }
        return (this.student != null) ? this.student : null;
    }

    public void setStudent(Student student) {
        this.studentId = student.getId();
        this.student = student;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\common\ValidationResult.java
```java
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
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\Column.java
```java
package edu.advising.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name();
    // Allows me to handle foreignKey columns in UPSERTS when they're null or 0
    boolean nullableforeignKey() default false;
    // Allows me to handle foreignKey columns in UPSERTS differently
    boolean foreignKey() default false;
    boolean upsertIgnore() default false; // Allows me to ignore Primary id fields for UPSERTS
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\DatabaseManager.java
```java
// Week 1: SINGLETON PATTERN
// Foundation: Database Connection Manager
// Features Implemented: Basic database connectivity
// Why First: Essential infrastructure that all other components will use

package edu.advising.core;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.reflect.Field;
import java.util.Optional;

// TODO: Make DatabaseManager an abstract class that implements a template methods for methods like upsertAll, which use
//  an abstract method called buildUpsertSql to implement Database specific upsert sql statements, then implement
//  concrete subclasses of DatabaseManager that override and implement buildUpsertSql for specific databases,
//  (H2, MySQL, PostgreSQL, etc.)

/**
 * DatabaseManager - Singleton Pattern
 * Ensures only one database connection pool exists throughout the application.
 * This prevents connection leaks and ensures efficient resource management.
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private final HikariDataSource dataSource;

    //private static final String URL = "jdbc:h2:mem:advising;DB_CLOSE_DELAY=-1";
    private static final String URL = "jdbc:h2:file:./advising";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    // Private constructor prevents instantiation from other classes
    private DatabaseManager() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setDriverClassName("org.h2.Driver");

        // Pool performance tuning
        config.setMaximumPoolSize(10);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");

        this.dataSource = new HikariDataSource(config);
        initializeDatabase();
        System.out.println("Database connection pool established");
    }

    // Thread-safe singleton instance retrieval
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // ======================================================================================
    // CORE LAMBDA METHODS - Allows ResultSet, Connection, and P-Statement to be managed here,
    //                       but still handle data with passed in Lambda function.
    // ======================================================================================

    /**
     * Executes a query and uses a lambda to process the ResultSet.
     * The connection is automatically returned to the pool after the lambda finishes.
     */
    public <T> T executeQuery(String sql, QueryHandler<T> handler, Object... params) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setParameters(pstmt, params);
            try (ResultSet rs = pstmt.executeQuery()) {
                return handler.handle(rs); // This is the Lambda that handles the data.
            }
        }
    }

    /**
     * Specialized helper to fetch a List of objects.
     */
    public <T> List<T> fetchList(String sql, QueryHandler<T> rowMapper, Object... params) throws SQLException {
        return executeQuery(sql, rs -> {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(rowMapper.handle(rs));
            }
            return results;
        }, params);
    }

    /**
     * Fetches a single object from the database.
     * Returns null if no record is found.
     */
    public <T> T fetch(String sql, QueryHandler<T> rowMapper, Object... params) throws SQLException {
        return executeQuery(sql, rs -> {
            if (rs.next()) {
                return rowMapper.handle(rs); // Use the same mapper logic as fetchList
            }
            return null; // Return null if the result set is empty
        }, params);
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Get class inheritance hierarchy for a class.
     */
    private List<Class<?>> getTableHierarchy(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<>();
        while (clazz != null && clazz.isAnnotationPresent(Table.class)) {
            hierarchy.add(0, clazz); // Add to the front to get [User, Student]
            clazz = clazz.getSuperclass();
        }
        return hierarchy;
    }

    /**
     * NOTE: ADD Observer Week
     * -
     * Get annotated fields Local to the clazz.
     *
     * @param clazz The class to inspect for annotated fields.
     */
    private List<Field> getAnnotatedFields(Class<?> clazz) {
        List<Field> columns = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Column.class)) columns.add(field);
        }
        return columns;
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Recursively get All annotated fields, even those inherited from Superclass(es)!
     * This is to support Superclass/Subclass hierarchies like User -> Student or User -> Faculty.
     *
     * @param clazz The class to inspect for annotated fields.
     */
    private List<Field> getAllAnnotatedFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        List<Class<?>> hierarchy = getTableHierarchy(clazz);
        for (Class<?> c : hierarchy) {
            for (Field field : c.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Column.class)) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    /**
     * NOTE: ADD Observer Week
     * -
     * Only get fields that are annoted with @Id. Useful for upsert merging on Natural Key/UNIQUE constraints.
     */
    private List<Field> getIdAnnotatedFields(List<Field> allFields) {
        return allFields.stream().filter(f -> f.isAnnotationPresent(Id.class)).toList();
    }

    /**
     * NOTE: ADD Observer or Command Week
     * -
     * Gets the PRIMARY annotated @Id field of a Class, which will primarily be used in ManyToMany object joins.
     */
    private <T> String getLocalIdColumnName(Class<T> targetClass) {
        // We get the target ID column name from the @Id field of the target class
        return getAnnotatedFields(targetClass).stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .map(f -> f.getAnnotation(Column.class).name())
                .findFirst().orElse("id");
    }

    /**
     * NOTE: ADD Observer or Command Week
     * -
     * Gets the PRIMARY annotated @Id field's name of a Class recursively to handle hierarchical classes.
     */
    private <T> String getPrimaryIdColumnName(Class<T> targetClass) {
        return getAllAnnotatedFields(targetClass).stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .filter(f -> f.getAnnotation(Id.class).isPrimary())
                .map(f -> f.getAnnotation(Column.class).name())
                .findFirst().orElse("id");
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Gets the PRIMARY annotated @Id field of a Class recursively to handle hierarchical classes.
     */
    private <T> Optional<Field> getPrimaryIdColumn(Class<T> targetClass) {
        // We get the target ID column name from the @Id field of the target class
        return getAllAnnotatedFields(targetClass).stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .filter(f -> f.getAnnotation(Id.class).isPrimary())
                .findFirst();
    }


    /**
     * NOTE: ADD Observer Week
     * -
     * Only get fields that aren't ignored for upserts. We ignore AUTO_INCREMENT id fields, for example.
     * To ignore AUTO_INC fields though, you'll still need a UNIQUE index on the Natural Key for upsert to work.
     */
    private List<Field> getUpsertFields(List<Field> allFields, Class<?> clazz) {
        List<Field> upsertFields = new ArrayList<>();
        for (Field f : allFields) {
            Column col = f.getAnnotation(Column.class);
            boolean isIgnored = col.upsertIgnore();
            if (!isIgnored) {
                upsertFields.add(f);
            }
        }
        // Need to make sure the parent's primary Id is in this list if this is a sub-class.
        if (clazz.getAnnotation(Table.class).isSubTable()) {
            Optional<Field> oFieldPId = getPrimaryIdColumn(clazz);
            oFieldPId.ifPresent(upsertFields::add);
        }
        return upsertFields;
    }

    /**
     * NOTE: ADD Observer Week
     * -
     * Propagate's Parent Ids/Primary keys to Subclass objects during hierarchical/related table updates.
     * This allows tables like Student to get its id from the related Superclass/User INSERT/UPDATE.
     * SQLException can arise due to database queries
     * IllegalAccessException can arise due to java.lang.reflect when using annotations.
     */
    private <T> void propagateGeneratedKeys(PreparedStatement pstmt, List<T> items, List<Field> localFields)
            throws SQLException, IllegalAccessException {
        // Find the auto-increment field in this class level
        // NOTE: localFields for say User will find an autoIncField and pass on to Student Items,
        //   but Student localFields will not, and therefore not go into the isPresent conditional.
        //   Thus, this only works for 2 levels, which is liekly good enough.
        Optional<Field> autoIncField = localFields.stream()
                .filter(f -> f.getAnnotation(Column.class).upsertIgnore())
                .findFirst();

        if (autoIncField.isPresent()) {
            Field field = autoIncField.get();
            field.setAccessible(true);

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                for (T item : items) {
                    // If the object didn't have an ID, set the one the DB just made
                    Object existingId = field.get(item);
                    if (existingId == null || (existingId instanceof Number && ((Number) existingId).longValue() == 0)) {
                        if (rs.next()) {
                            field.set(item, rs.getObject(1));
                        }
                    }
                }
            }
        }
    }

    /**
     * NOTE: ADD Observer Week
     * -
     * Upserts (Inserts or Updates) a list of objects into the database.
     * Uses H2's MERGE syntax and JDBC Batching for high performance.
     * SQLException can arise due to database queries
     * IllegalAccessException can arise due to java.lang.reflect when using annotations.
     */
    public <T> void upsertAll(List<T> items) throws SQLException, IllegalAccessException {
        if (items == null || items.isEmpty()) return;

        Connection conn = dataSource.getConnection();
        try {
            conn.setAutoCommit(false); // Start Transaction

            Class<?> leafClass = items.get(0).getClass();
            List<Class<?>> hierarchy = getTableHierarchy(leafClass);

            for (Class<?> clazz : hierarchy) {
                Table tableAnn = clazz.getAnnotation(Table.class);

                // Only get fields DECLARED in this specific class (User fields vs Student fields)
                List<Field> localFields = getAnnotatedFields(clazz);

                // If this subclass has no localFields, we can safely ignore it.
                // This is expected for concrete commands like RegisterCommand, which carry
                // no @Column fields of their own all persistence lives in BaseCommand.
                if (localFields.isEmpty()) {
                    continue;
                }

                List<Field> writeableFields = getUpsertFields(localFields, clazz);
                List<Field> keyFields = getIdAnnotatedFields(writeableFields);

                if (!keyFields.isEmpty()) {
                    // Strategy A: natural key MERGE
                    String sql = buildUpsertSql(tableAnn.name(), writeableFields, keyFields);

                    try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        for (T item : items) {
                            for (int i = 0; i < writeableFields.size(); i++) {
                                Field f = writeableFields.get(i);
                                Object value = f.get(item);
                                if (f.getAnnotation(Column.class).nullableforeignKey()
                                        && (value == null || (int) value == 0)) {
                                    pstmt.setObject(i + 1, null);
                                } else {
                                    pstmt.setObject(i + 1, value);
                                }
                            }
                            pstmt.addBatch();
                        }
                        pstmt.executeBatch();

                        // ID HAND OFF: capture auto generated ids for FK propagation to child tables
                        // (i.e. User.id -> Student.id).
                        propagateGeneratedKeys(pstmt, items, localFields);
                    }
                } else {
                    // Strategy B: PK only split batch
                    // keyFields is empty, the entity has no natural key. The only possible key is the AUTO_INCREMENT
                    // primary key, which was intentionally excluded from writeableFields by upsertIgnore=true. Route
                    // to the INSERT/UPDATE is a split path.
                    executePkOnlySplitBatch(conn, tableAnn.name(), localFields, writeableFields, items);
                }
            }
            conn.commit(); // Success!
        } catch (Exception e) {
            //e.printStackTrace();
            conn.rollback(); // Undo everything on failure
            throw new SQLException("Transaction failed. Changes rolled back.", e);
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    /**
     * Handles upserts for entities that have no natural key — only an AUTO_INCREMENT
     * primary key (flagged by @Id(isPrimary=true) + @Column(upsertIgnore=true)).
     *
     * Why a split is necessary:
     *   H2's MERGE INTO requires at least one KEY column. For AUTO_INCREMENT fields
     *   we cannot include id in KEY when id=0, because H2 would INSERT with id=0
     *   literally instead of letting AUTO_INCREMENT assign a value. So new rows and
     *   existing rows need fundamentally different SQL.
     *
     *   id == 0 → new row:
     *     INSERT INTO table (writeable_cols) VALUES (?)
     *     DB assigns the AUTO_INCREMENT id.
     *     propagateGeneratedKeys() reads the generated id and sets it back on the item.
     *
     *   id > 0 → existing row:
     *     UPDATE table SET col=?, col=?, ... WHERE id=?
     *     The id value is appended as the final parameter for the WHERE clause.
     *
     * @param conn           the active transactional connection (do not close it here)
     * @param tableName      the target table name
     * @param localFields    all @Column fields declared on this class level
     *                       (used by propagateGeneratedKeys to find the autoInc field)
     * @param writeableFields the subset of localFields that should appear in INSERT/SET
     *                       (already excludes the upsertIgnore=true id field)
     * @param items          the objects to persist
     */
    private <T> void executePkOnlySplitBatch(Connection conn,
                                             String tableName,
                                             List<Field> localFields,
                                             List<Field> writeableFields,
                                             List<T> items)
            throws SQLException, IllegalAccessException {

        // Find the AUTO_INCREMENT primary key field.
        // It is identified by having both @Id(isPrimary=true) AND upsertIgnore=true.
        Optional<Field> oPkField = localFields.stream()
                .filter(f -> f.isAnnotationPresent(Id.class)
                        && f.getAnnotation(Id.class).isPrimary()
                        && f.getAnnotation(Column.class).upsertIgnore())
                .findFirst();

        if (oPkField.isEmpty()) {
            // Safety net: keyFields was empty AND there's no auto increment PK.
            // This means the entity is genuinely un-keyable, a configuration error.
            throw new SQLException(
                    "upsertAll: no key fields and no auto-increment primary key found "
                            + "for table '" + tableName + "'. "
                            + "Add @Id to at least one non-upsertIgnore field (natural key), "
                            + "or add @Id(isPrimary=true) to the auto-increment id field.");
        }

        Field pkField = oPkField.get();
        pkField.setAccessible(true);

        // Split items into new (i.e. id=0) vs existing (i.e. id>0)
        List<T> newItems      = new ArrayList<>();
        List<T> existingItems = new ArrayList<>();

        for (T item : items) {
            Object pk   = pkField.get(item);
            boolean isNew = (pk == null)
                    || (pk instanceof Number && ((Number) pk).longValue() == 0);
            if (isNew) newItems.add(item);
            else       existingItems.add(item);
        }

        // INSERT new items
        if (!newItems.isEmpty()) {
            String insertSql = buildInsertSql(tableName, writeableFields);

            try (PreparedStatement pstmt = conn.prepareStatement(
                    insertSql, Statement.RETURN_GENERATED_KEYS)) {

                for (T item : newItems) {
                    for (int i = 0; i < writeableFields.size(); i++) {
                        Field f      = writeableFields.get(i);
                        Object value = f.get(item);
                        if (f.getAnnotation(Column.class).nullableforeignKey()
                                && (value == null || (int) value == 0)) {
                            pstmt.setObject(i + 1, null);
                        } else {
                            pstmt.setObject(i + 1, value);
                        }
                    }
                    pstmt.addBatch();
                }
                pstmt.executeBatch();

                // Capture generated ids and set them back on the Java objects.
                // propagateGeneratedKeys searches localFields for the upsertIgnore=true
                // field which IS the pkField so we pass localFields unchanged.
                propagateGeneratedKeys(pstmt, newItems, localFields);
            }
        }

        // UPDATE existing items
        if (!existingItems.isEmpty()) {
            String updateSql = buildUpdateByPkSql(tableName, writeableFields, pkField);

            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                for (T item : existingItems) {
                    // SET parameters: all writeable fields in order
                    for (int i = 0; i < writeableFields.size(); i++) {
                        Field f      = writeableFields.get(i);
                        Object value = f.get(item);
                        if (f.getAnnotation(Column.class).nullableforeignKey()
                                && (value == null || (int) value == 0)) {
                            pstmt.setObject(i + 1, null);
                        } else {
                            pstmt.setObject(i + 1, value);
                        }
                    }
                    // WHERE parameter: the primary key
                    pstmt.setObject(writeableFields.size() + 1, pkField.get(item));
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                // No generated keys to capture id was already set on these items.
            }
        }
    }

    /**
     * Builds a plain INSERT statement (no MERGE, no ON DUPLICATE KEY).
     * Used by executePkOnlySplitBatch for new items whose AUTO_INCREMENT id is 0.
     *
     * Output form:
     *   INSERT INTO `tableName` (`col1`, `col2`, ...) VALUES (?, ?, ...)
     *
     * The AUTO_INCREMENT id column is intentionally absent from columns — it was
     * excluded from writeableFields by getUpsertFields (upsertIgnore=true).
     *
     * @param tableName the target table
     * @param columns   the writeable fields (already excludes upsertIgnore fields)
     * @return parameterised INSERT SQL string
     */
    protected String buildInsertSql(String tableName, List<Field> columns) {
        StringBuilder cols = new StringBuilder();
        StringBuilder vals = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            cols.append(String.format("`%s`", columns.get(i).getAnnotation(Column.class).name()));
            vals.append("?");
            if (i < columns.size() - 1) {
                cols.append(", ");
                vals.append(", ");
            }
        }

        return "INSERT INTO `" + tableName + "` (" + cols + ") VALUES (" + vals + ")";
    }

    /**
     * Builds an UPDATE statement that matches on the primary key column.
     * Used by executePkOnlySplitBatch for existing items whose id > 0.
     *
     * Output form:
     *   UPDATE `tableName` SET `col1`=?, `col2`=?, ... WHERE `pkCol`=?
     *
     * The pk column value is supplied as the LAST parameter in the PreparedStatement
     * (after all the SET values), matching the order in executePkOnlySplitBatch.
     *
     * @param tableName  the target table
     * @param setColumns the fields to update (already excludes upsertIgnore fields)
     * @param pkField    the AUTO_INCREMENT primary key field (used in WHERE clause)
     * @return parameterised UPDATE SQL string
     */
    protected String buildUpdateByPkSql(String tableName, List<Field> setColumns, Field pkField) {
        StringBuilder sql = new StringBuilder("UPDATE `" + tableName + "` SET ");

        for (int i = 0; i < setColumns.size(); i++) {
            sql.append(String.format("`%s` = ?",
                    setColumns.get(i).getAnnotation(Column.class).name()));
            if (i < setColumns.size() - 1) sql.append(", ");
        }

        sql.append(String.format(" WHERE `%s` = ?",
                pkField.getAnnotation(Column.class).name()));

        return sql.toString();
    }

    protected String buildUpsertSql(String tableName, List<Field> allColumns, List<Field> keyColumns) {
        StringBuilder sql = new StringBuilder("MERGE INTO " + tableName + " (");
        StringBuilder values = new StringBuilder();
        StringBuilder keys = new StringBuilder();

        // 1. Build Column list and Value placeholders
        for (int i = 0; i < allColumns.size(); i++) {
            sql.append(String.format("`%s`", allColumns.get(i).getAnnotation(Column.class).name()));
            values.append("?");
            if (i < allColumns.size() - 1) {
                sql.append(", ");
                values.append(", ");
            }
        }

        // 2. Build the KEY clause (The columns to match on)
        for (int i = 0; i < keyColumns.size(); i++) {
            keys.append(String.format("`%s`", keyColumns.get(i).getAnnotation(Column.class).name()));
            if (i < keyColumns.size() - 1) keys.append(", ");
        }

        return sql.append(") KEY (").append(keys)
                .append(") VALUES (").append(values).append(")").toString();
    }

    /**
     * Builds the FROM clause with JOINs for the entire inheritance hierarchy.
     * Example: "students t JOIN users p ON t.id = p.id"
     */
    protected String buildJoinedFromClause(Class<?> clazz) {
        Table tableAnn = clazz.getAnnotation(Table.class);  // Child table
        String alias = "t"; // Child alias
        StringBuilder from = new StringBuilder(tableAnn.name() + " " + alias); // Initial SQL
        String childId = getPrimaryIdColumnName(clazz); // Initial primary id for joining.
        // Get the full hierarchy.
        List<Class<?>> hierarchy = getTableHierarchy(clazz);
        // Pseudo windowing function to consider 2 table hierarchies at a time, pseudo cause we ignore the Child table.
        for (int i = hierarchy.size() - 2; i >= 0; i--) {  // Minus 2 to ignore child, which is last in hierarchy.
            Class<?> parent = hierarchy.get(i);
            String parentTable = parent.getAnnotation(Table.class).name();
            String parentAlias = "p" + i;
            // Find Primary ID column for joining
            String parentId = getPrimaryIdColumnName(parent);
            // Process the pair (current, next)
            from.append(String.format(" JOIN %s %s ON %s.%s = %s.%s",
                    parentTable, parentAlias, alias, childId, parentAlias, parentId));
            // Update alias and childId so the next join uses previous parent as the new child.
            alias = parentAlias;
            childId = parentId;
        }
        return from.toString();
    }

    /**
     * NOTE: ADD Observer Week
     * -
     * Upserts a single object into the database.
     * Reuses the upsertAll logic for consistency.
     */
    public <T> void upsert(T item) throws SQLException, IllegalAccessException {
        if (item == null) return;
        upsertAll(Collections.singletonList(item));
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Generic Row Mapper that uses reflection to map ResultSet columns
     * to fields annotated with @Column.
     */
    private <T> QueryHandler<T> autoMapper(Class<T> clazz) {
        List<Field> allFields = getAllAnnotatedFields(clazz); // Use the recursive version we built
        return rs -> {
            try {
                T dto = clazz.getDeclaredConstructor().newInstance();
                for (Field field : allFields) {
                    String colName = field.getAnnotation(Column.class).name();
                    try {
                        // We use rs.getObject(colName) but catch if the column isn't in the SQL result
                        Object value = rs.getObject(colName);
                        if (value != null) {
                            field.setAccessible(true);
                            // Handle java.sql.Timestamp conversions which is a special case.
                            // If other cases arise, consider redesigning and refactoring, perhaps with a HashMap.
                            if (value instanceof Timestamp) {
                                field.set(dto, ((Timestamp) value).toLocalDateTime());  // This assumes LocalDateTime
                            } else if (value instanceof Date) {
                                field.set(dto, ((Date) value).toLocalDate());  // This assumes LocalDate
                            } else {
                                field.set(dto, value);
                            }
                        }
                    } catch (SQLException e) {
                        System.out.printf("~~~ Skipped %s as its not in SQL Result ~~~%n", colName);
                    }
                }
                return dto;
            } catch (Exception e) {
                throw new SQLException("Mapping failed for: " + clazz.getSimpleName(), e);
            }
        };
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Fetches a single record by a specific column value (e.g. id), and handles class hierarchies.
     * TODO: Modify fetchOne to take Optional Filter parameter allowing for additional SQL Filters.
     */
    public <T> T fetchOne(Class<T> clazz, String idColumn, Object idValue) throws SQLException {
        String joinedFrom = buildJoinedFromClause(clazz);
        // Note: We use "t." + idColumn to ensure we target the leaf table alias
        String sql = "SELECT * FROM " + joinedFrom + " WHERE t." + idColumn + " = ? LIMIT 1";
        return fetch(sql, autoMapper(clazz), idValue);
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Fetches a list of records by a specific column value (e.g., Foreign Key).
     * TODO: Modify fetchMany to take Optional Filter parameter allowing for additional SQL Filters.
     */
    public <T> List<T> fetchMany(Class<T> clazz, String fkColumn, Object value) throws SQLException {
        String joinedFrom = buildJoinedFromClause(clazz);
        String sql = "SELECT * FROM " + joinedFrom + " WHERE t." + fkColumn + " = ?";
        return fetchList(sql, autoMapper(clazz), value);
    }

    /**
     * NOTE: ADD Command Week
     * -
     * Fetches a list of related objects across a Many-to-Many join table.
     * TODO: Modify fetchManyToMany to take Optional Filter parameter allowing for additional SQL Filters.
     */
    public <T> List<T> fetchManyToMany(Class<T> targetClass, String joinTable,
                                       String joinCol, String invJoinCol, Object sourceId) throws SQLException {
        String targetTable = targetClass.getAnnotation(Table.class).name();
        String targetIdCol = getPrimaryIdColumnName(targetClass);

        // INHERITANCE CHECK to handle Model Inheritance hierarchies.
        // If the parent has a @Table, we must JOIN it to handle cases like: User -> Student or User -> Faculty
        String fromClause = buildJoinedFromClause(targetClass);

        // Example: SELECT s.* FROM sections s JOIN enrollments e ON s.section_id = e.section_id WHERE e.student_id = ?
        String sql = String.format(
                "SELECT * FROM %s JOIN %s j ON t.%s = j.%s WHERE j.%s = ?",
                fromClause, joinTable, targetIdCol, invJoinCol, joinCol
        );

        return fetchList(sql, autoMapper(targetClass), sourceId);
    }

    /**
     * Deletes a single object from the database.
     * Reuses the deleteAll logic to ensure hierarchical integrity.
     */
    public <T> void delete(T item) throws SQLException, IllegalAccessException {
        if (item == null) return;
        deleteAll(Collections.singletonList(item));
    }

    /**
     * Deletes a list of objects from the database.
     * Handles class hierarchies by deleting from the most specific table (child)
     * up to the most general table (parent).
     */
    public <T> void deleteAll(List<T> items) throws SQLException, IllegalAccessException {
        if (items == null || items.isEmpty()) return;

        Connection conn = dataSource.getConnection();
        try {
            conn.setAutoCommit(false); // Start Transaction

            Class<?> leafClass = items.get(0).getClass();
            List<Class<?>> hierarchy = getTableHierarchy(leafClass);

            // IMPORTANT: We must delete in REVERSE order of insertion.
            // If hierarchy is [User, Student], we must delete from Student then User.
            List<Class<?>> reverseHierarchy = new ArrayList<>(hierarchy);
            Collections.reverse(reverseHierarchy);

            for (Class<?> clazz : reverseHierarchy) {
                Table tableAnn = clazz.getAnnotation(Table.class);
                if (tableAnn == null) continue;

                // We identify the row to delete using the Primary ID defined in the hierarchy
                String primaryKeyColName = getPrimaryIdColumnName(leafClass);
                Optional<Field> oPrimaryField = getPrimaryIdColumn(leafClass);

                if (oPrimaryField.isEmpty()) {
                    throw new SQLException("Delete failed: No primary key field found for " + leafClass.getSimpleName());
                }

                Field primaryField = oPrimaryField.get();
                primaryField.setAccessible(true);

                String sql = String.format("DELETE FROM %s WHERE %s = ?", tableAnn.name(), primaryKeyColName);

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    for (T item : items) {
                        Object idValue = primaryField.get(item);
                        if (idValue == null) continue; // Cannot delete a record without an ID

                        pstmt.setObject(1, idValue);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }
            conn.commit(); // Success!
        } catch (Exception e) {
            conn.rollback(); // Undo everything on failure
            throw new SQLException("Delete transaction failed. Changes rolled back.", e);
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    /**
     * Executes UPDATE, INSERT, or DELETE and returns affected rows.
     */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setParameters(pstmt, params);
            return pstmt.executeUpdate();
        }
    }

    /**
     * Executes INSERT and returns the auto-generated ID.
     */
    public int executeInsert(String sql, Object... params) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(pstmt, params);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    // ======================================================================================
    // UTILS & LIFECYCLE
    // ======================================================================================

    private void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }

    public void shutdown() {
        if (dataSource != null) dataSource.close();
    }

    /**
     * Initialize complete database schema for all 14 weeks - 2 Exam weeks.
     */
    private void initializeDatabase() {
        try {
            System.out.println("Initializing database schema...");

            // ================================================================
            // WEEK 1 & 2: Core User Tables (Singleton, Factory)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +  // Increased for hashed passwords
                    "user_type VARCHAR(20) NOT NULL, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "phone VARCHAR(20)," +
                    "email VARCHAR(100), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "last_login TIMESTAMP, " +
                    "is_active BOOLEAN DEFAULT TRUE," +
                    "UNIQUE(email))");

            executeUpdate("CREATE TABLE IF NOT EXISTS auth_sessions (" +
                    "auth_token VARCHAR(255) PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "state VARCHAR(50) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "expires_at TIMESTAMP NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
                    "id INT PRIMARY KEY, " +
                    "student_id VARCHAR(20) UNIQUE NOT NULL, " +
                    "gpa DECIMAL(3,2) DEFAULT 0.00, " +
                    "enrollment_status VARCHAR(20) DEFAULT 'ACTIVE', " +
                    "academic_standing VARCHAR(20) DEFAULT 'GOOD_STANDING', " +
                    "classification VARCHAR(20), " +  // FRESHMAN, SOPHOMORE, etc.
                    "major VARCHAR(100), " +
                    "minor VARCHAR(100), " +
                    "advisor_id INT, " +
                    "FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE)");

            executeUpdate("CREATE TABLE IF NOT EXISTS faculty (" +
                    "id INT PRIMARY KEY, " +
                    "employee_id VARCHAR(20) UNIQUE NOT NULL, " +
                    "department VARCHAR(50), " +
                    "title VARCHAR(50), " +  // Professor, Associate Professor, etc.
                    "office_location VARCHAR(100), " +
                    "office_hours TEXT, " +
                    "hire_date DATE, " +
                    "FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE)");

            // ================================================================
            // WEEK 3: Authentication & Security (Strategy Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS authentication_methods (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "method_type VARCHAR(50) NOT NULL, " +  // BASIC, SECURE, TWO_FACTOR, BIOMETRIC
                    "is_primary BOOLEAN DEFAULT FALSE, " +
                    "is_enabled BOOLEAN DEFAULT TRUE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");

            executeUpdate("CREATE TABLE IF NOT EXISTS two_factor_codes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "code VARCHAR(10) NOT NULL, " +
                    "code_type VARCHAR(20) NOT NULL, " +  // SMS, EMAIL, AUTHENTICATOR
                    "generated_at TIMESTAMP NOT NULL, " +
                    "expires_at TIMESTAMP NOT NULL, " +
                    "used_at TIMESTAMP, " +
                    "is_used BOOLEAN DEFAULT FALSE, " +
                    "attempts INT DEFAULT 0, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");

            executeUpdate("CREATE TABLE IF NOT EXISTS password_history (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "password_hash VARCHAR(255) NOT NULL, " +
                    "changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "changed_by INT, " +  // Admin override capability
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");

            executeUpdate("CREATE TABLE IF NOT EXISTS password_reset_tokens (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "token VARCHAR(255) UNIQUE NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "expires_at TIMESTAMP NOT NULL, " +
                    "used_at TIMESTAMP, " +
                    "is_used BOOLEAN DEFAULT FALSE, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");

            executeUpdate("CREATE TABLE IF NOT EXISTS login_attempts (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL, " +
                    "attempt_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "status VARCHAR(20) NOT NULL, " +
                    "ip_address VARCHAR(45), " +
                    "user_agent TEXT, " +
                    "failure_reason VARCHAR(100))");

            executeUpdate("CREATE TABLE IF NOT EXISTS password_policies (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "policy_name VARCHAR(50) UNIQUE NOT NULL, " +
                    "min_length INT DEFAULT 8, " +
                    "require_uppercase BOOLEAN DEFAULT TRUE, " +
                    "require_lowercase BOOLEAN DEFAULT TRUE, " +
                    "require_digit BOOLEAN DEFAULT TRUE, " +
                    "require_special BOOLEAN DEFAULT TRUE, " +
                    "max_age_days INT DEFAULT 90, " +
                    "history_count INT DEFAULT 5, " +  // Can't reuse last 5 passwords
                    "is_active BOOLEAN DEFAULT TRUE)");

            // ================================================================
            // WEEK 4: Notifications (Observer Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS notifications (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "`type` VARCHAR(50) NOT NULL, " +  // GRADE_CHANGE, REGISTRATION, PAYMENT, etc.
                    "message TEXT NOT NULL, " +
                    "priority VARCHAR(20) DEFAULT 'MEDIUM', " +  // HIGH, MEDIUM, LOW
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "read_at TIMESTAMP, " +
                    "read_status BOOLEAN DEFAULT FALSE, " +
                    "deleted_at TIMESTAMP, " +
                    "metadata TEXT, " +  // JSON for additional data
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");

            executeUpdate("CREATE TABLE IF NOT EXISTS notification_preferences (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "notification_type VARCHAR(50) NOT NULL, " +
                    "email_enabled BOOLEAN DEFAULT TRUE, " +
                    "sms_enabled BOOLEAN DEFAULT FALSE, " +
                    "push_enabled BOOLEAN DEFAULT TRUE, " +
                    "frequency VARCHAR(20) DEFAULT 'IMMEDIATE', " +  // IMMEDIATE, DIGEST, DISABLED
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, " +
                    "UNIQUE(user_id, notification_type))");

            // ================================================================
            // WEEK 5: Commands & Transactions (Command Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS command_history (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "command_type VARCHAR(50) NOT NULL, " +  // REGISTER, DROP, PAYMENT, etc.
                    "command_data TEXT, " +  // JSON serialized command
                    "executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "undone_at TIMESTAMP, " +
                    "is_undone BOOLEAN DEFAULT FALSE, " +
                    "success BOOLEAN NOT NULL, " +
                    "error_message TEXT, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");

            // ================================================================
            // WEEK 5-11: Course Management (Multiple Patterns)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS departments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "code VARCHAR(10) UNIQUE NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "chair_id INT, " +
                    "budget DECIMAL(12,2), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (chair_id) REFERENCES faculty(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS programs (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "code VARCHAR(20) UNIQUE NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "degree_type VARCHAR(20) NOT NULL, " +  // BS, BA, MS, MA, PhD
                    "department_id INT NOT NULL, " +
                    "total_credits_required INT DEFAULT 120, " +
                    "is_active BOOLEAN DEFAULT TRUE, " +
                    "FOREIGN KEY (department_id) REFERENCES departments(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS courses (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "code VARCHAR(20) UNIQUE NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "description TEXT, " +
                    "credits DOUBLE NOT NULL, " +
                    "department_id INT NOT NULL, " +
                    "level VARCHAR(20), " +  // UNDERGRADUATE, GRADUATE
                    "is_active BOOLEAN DEFAULT TRUE, " +
                    "FOREIGN KEY (department_id) REFERENCES departments(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS course_prerequisites (" +
                    "course_id INT NOT NULL, " +
                    "prerequisite_id INT NOT NULL, " +
                    "is_corequisite BOOLEAN DEFAULT FALSE, " +
                    "PRIMARY KEY (course_id, prerequisite_id), " +
                    "FOREIGN KEY (course_id) REFERENCES courses(id), " +
                    "FOREIGN KEY (prerequisite_id) REFERENCES courses(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS sections (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "course_id INT NOT NULL, " +
                    "section_number VARCHAR(10) NOT NULL, " +
                    "semester VARCHAR(20) NOT NULL, " +
                    "`year` INT NOT NULL, " +
                    "capacity INT NOT NULL, " +
                    "enrolled INT DEFAULT 0, " +
                    "faculty_id INT, " +
                    "room VARCHAR(50), " +
                    "status VARCHAR(20) DEFAULT 'OPEN', " +  // OPEN, CLOSED, CANCELLED
                    "FOREIGN KEY (course_id) REFERENCES courses(id), " +
                    "FOREIGN KEY (faculty_id) REFERENCES faculty(id), " +
                    "UNIQUE(course_id, section_number, semester, `year`))");

            executeUpdate("CREATE TABLE IF NOT EXISTS section_meeting_times (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "section_id INT NOT NULL, " +
                    "day_of_week VARCHAR(10) NOT NULL, " +
                    "start_time TIME NOT NULL, " +
                    "end_time TIME NOT NULL, " +
                    "room VARCHAR(50), " +
                    "FOREIGN KEY (section_id) REFERENCES sections(id) ON DELETE CASCADE)");

            // ================================================================
            // WEEK 6: Enrollment & State (State Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS enrollments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "section_id INT NOT NULL, " +
                    "enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "status VARCHAR(20) NOT NULL, " +  // ENROLLED, DROPPED, WITHDRAWN, COMPLETED
                    "grade VARCHAR(5), " +
                    "grade_points DECIMAL(3,2), " +
                    "midterm_grade VARCHAR(5), " +
                    "final_grade VARCHAR(5), " +
                    "graded_at TIMESTAMP, " +
                    "dropped_at TIMESTAMP, " +
                    "drop_reason TEXT, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id), " +
                    "FOREIGN KEY (section_id) REFERENCES sections(id), " +
                    "UNIQUE(student_id, section_id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS waitlist (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "section_id INT NOT NULL, " +
                    "position INT NOT NULL, " +
                    "added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "removed_date TIMESTAMP, " +
                    "status VARCHAR(20) DEFAULT 'ACTIVE', " +  // ACTIVE, ENROLLED, REMOVED
                    "notification_sent BOOLEAN DEFAULT FALSE, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id), " +
                    "FOREIGN KEY (section_id) REFERENCES sections(id), " +
                    "UNIQUE(student_id, section_id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS registration_periods (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "semester VARCHAR(20) NOT NULL, " +
                    "`year` INT NOT NULL, " +
                    "open_date TIMESTAMP NOT NULL, " +
                    "close_date TIMESTAMP NOT NULL, " +
                    "late_registration_end TIMESTAMP, " +
                    "current_state VARCHAR(20) NOT NULL, " +  // NOT_OPEN, OPEN, LATE, CLOSED
                    "UNIQUE(semester, `year`))");

            executeUpdate("CREATE TABLE IF NOT EXISTS transcript_requests (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "request_type VARCHAR(20) NOT NULL, " +  // OFFICIAL, UNOFFICIAL
                    "recipient_name VARCHAR(100), " +
                    "recipient_address TEXT, " +
                    "request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "status VARCHAR(20) NOT NULL, " +  // PENDING, PROCESSING, READY, SENT, CANCELLED, FAILED
                    "tracking_number VARCHAR(50), " +
                    "fee DECIMAL(6,2), " +
                    "is_rush BOOLEAN DEFAULT FALSE, " +
                    "completed_date TIMESTAMP, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");

            // ================================================================
            // WEEK 7: Permissions & Restrictions (Decorator Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS user_roles (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "role_name VARCHAR(50) NOT NULL, " +  // STUDENT, FACULTY, HONORS, ATHLETE, etc.
                    "granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "expires_at TIMESTAMP, " +
                    "granted_by INT, " +
                    "is_active BOOLEAN DEFAULT TRUE, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id), " +
                    "UNIQUE(user_id, role_name))");

            executeUpdate("CREATE TABLE IF NOT EXISTS permissions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "role_name VARCHAR(50) NOT NULL, " +
                    "feature_code VARCHAR(50) NOT NULL, " +
                    "can_access BOOLEAN DEFAULT TRUE, " +
                    "UNIQUE(role_name, feature_code))");

            executeUpdate("CREATE TABLE IF NOT EXISTS permission_grants (" +
                    "id  INT AUTO_INCREMENT PRIMARY KEY," +
            "faculty_id  INT NOT NULL," +
            "student_id  INT NOT NULL," +
            "section_id  INT NOT NULL," +
            "granted_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "expires_at  TIMESTAMP NOT NULL," +
            "is_used     BOOLEAN DEFAULT FALSE," +
            "used_at     TIMESTAMP," +
            "is_active   BOOLEAN DEFAULT TRUE," +
            "notes       TEXT," +
            "FOREIGN KEY (faculty_id) REFERENCES faculty(id)," +
            "FOREIGN KEY (student_id) REFERENCES students(id)," +
            "FOREIGN KEY (section_id) REFERENCES sections(id)," +
            "UNIQUE (student_id, section_id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS restrictions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "restriction_type VARCHAR(50) NOT NULL, " +  // FINANCIAL_HOLD, ACADEMIC_PROBATION, etc.
                    "description TEXT, " +
                    "start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "end_date TIMESTAMP, " +
                    "amount DECIMAL(10,2), " +  // For financial holds
                    "is_active BOOLEAN DEFAULT TRUE, " +
                    "created_by INT, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS restriction_impacts (" +
                    "restriction_type VARCHAR(50) NOT NULL, " +
                    "blocked_feature VARCHAR(50) NOT NULL, " +
                    "PRIMARY KEY(restriction_type, blocked_feature))");

            // ================================================================
            // WEEK 5 & 8: Financial Management (Command, Template Patterns)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS payments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "amount DECIMAL(10,2) NOT NULL, " +
                    "payment_type VARCHAR(50) NOT NULL, " +  // TUITION, FEE, HOUSING, etc.
                    "payment_method VARCHAR(50), " +  // CREDIT_CARD, CHECK, CASH, etc.
                    "payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "status VARCHAR(20) NOT NULL, " +  // COMPLETED, PENDING, FAILED, REFUNDED
                    "transaction_id VARCHAR(100), " +
                    "reference_number VARCHAR(100), " +
                    "processed_by INT, " +
                    "notes TEXT, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS payment_plans (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "total_amount DECIMAL(10,2) NOT NULL, " +
                    "installments INT NOT NULL, " +
                    "amount_per_installment DECIMAL(10,2) NOT NULL, " +
                    "start_date DATE NOT NULL, " +
                    "status VARCHAR(20) DEFAULT 'ACTIVE', " +  // ACTIVE, COMPLETED, DEFAULTED
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS payment_plan_installments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "plan_id INT NOT NULL, " +
                    "installment_number INT NOT NULL, " +
                    "due_date DATE NOT NULL, " +
                    "amount DECIMAL(10,2) NOT NULL, " +
                    "paid_date DATE, " +
                    "paid_amount DECIMAL(10,2), " +
                    "status VARCHAR(20) DEFAULT 'PENDING', " +  // PENDING, PAID, OVERDUE
                    "FOREIGN KEY (plan_id) REFERENCES payment_plans(id), " +
                    "UNIQUE(plan_id, installment_number))");

            executeUpdate("CREATE TABLE IF NOT EXISTS student_accounts (" +
                    "student_id INT PRIMARY KEY, " +
                    "current_balance DECIMAL(10,2) DEFAULT 0.00, " +
                    "total_charges DECIMAL(10,2) DEFAULT 0.00, " +
                    "total_payments DECIMAL(10,2) DEFAULT 0.00, " +
                    "total_aid DECIMAL(10,2) DEFAULT 0.00, " +
                    "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS financial_aid (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "aid_type VARCHAR(50) NOT NULL, " +  // GRANT, LOAN, SCHOLARSHIP, WORK_STUDY
                    "aid_name VARCHAR(100) NOT NULL, " +
                    "amount DECIMAL(10,2) NOT NULL, " +
                    "semester VARCHAR(20) NOT NULL, " +
                    "`year` INT NOT NULL, " +
                    "status VARCHAR(20) NOT NULL, " +  // PENDING, APPROVED, DISBURSED, DENIED
                    "application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "approval_date TIMESTAMP, " +
                    "disbursement_date TIMESTAMP, " +
                    "FOREIGN KEY (student_id) REFERENCES students(id))");

            // ================================================================
            // WEEK 8: Reports (Template Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS report_generations (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "report_type VARCHAR(50) NOT NULL, " +  // TRANSCRIPT, FINANCIAL, TAX, ROSTER
                    "report_format VARCHAR(20), " +  // PDF, HTML, EXCEL, CSV
                    "generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "file_path VARCHAR(255), " +
                    "file_size INT, " +
                    "parameters TEXT, " +  // JSON
                    "FOREIGN KEY (user_id) REFERENCES users(id))");

            // ================================================================
            // WEEK 11: Budget Management (Composite Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS budgets (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "parent_budget_id INT, " +  // For hierarchical budgets
                    "budget_type VARCHAR(50) NOT NULL, " +  // DEPARTMENT, RESEARCH, TEACHING, etc.
                    "owner_id INT, " +  // Faculty ID
                    "fiscal_year VARCHAR(10) NOT NULL, " +
                    "allocated_amount DECIMAL(12,2) NOT NULL, " +
                    "spent_amount DECIMAL(12,2) DEFAULT 0.00, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (parent_budget_id) REFERENCES budgets(id), " +
                    "FOREIGN KEY (owner_id) REFERENCES faculty(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS budget_expenses (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "budget_id INT NOT NULL, " +
                    "description VARCHAR(255) NOT NULL, " +
                    "amount DECIMAL(10,2) NOT NULL, " +
                    "expense_date DATE NOT NULL, " +
                    "category VARCHAR(50), " +
                    "receipt_number VARCHAR(100), " +
                    "approved_by INT, " +
                    "FOREIGN KEY (budget_id) REFERENCES budgets(id), " +
                    "FOREIGN KEY (approved_by) REFERENCES faculty(id))");

            // ================================================================
            // WEEK 11: Program Requirements (Composite Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS program_requirements (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "program_id INT NOT NULL, " +
                    "requirement_group VARCHAR(100) NOT NULL, " +  // CORE, ELECTIVES, GEN_ED
                    "parent_group_id INT, " +  // For nested groups
                    "min_courses INT, " +
                    "min_credits DOUBLE, " +
                    "display_order INT DEFAULT 0, " +
                    "FOREIGN KEY (program_id) REFERENCES programs(id), " +
                    "FOREIGN KEY (parent_group_id) REFERENCES program_requirements(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS requirement_courses (" +
                    "requirement_id INT NOT NULL, " +
                    "course_id INT NOT NULL, " +
                    "is_required BOOLEAN DEFAULT TRUE, " +  // FALSE for elective choices
                    "PRIMARY KEY (requirement_id, course_id), " +
                    "FOREIGN KEY (requirement_id) REFERENCES program_requirements(id), " +
                    "FOREIGN KEY (course_id) REFERENCES courses(id))");

            // ================================================================
            // WEEK 10: External System Integration (Adapter Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS external_transactions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "transaction_type VARCHAR(50) NOT NULL, " +  // PAYMENT, TRANSCRIPT, etc.
                    "external_system VARCHAR(50) NOT NULL, " +  // NBS, NSC, etc.
                    "request_data TEXT, " +  // JSON
                    "response_data TEXT, " +  // JSON
                    "external_id VARCHAR(100), " +
                    "status VARCHAR(20) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "completed_at TIMESTAMP, " +
                    "error_message TEXT)");

            // ================================================================
            // WEEK 14: Audit Trail (Pipeline Pattern)
            // ================================================================

            executeUpdate("CREATE TABLE IF NOT EXISTS validation_logs (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "request_id VARCHAR(100) NOT NULL, " +
                    "request_type VARCHAR(50) NOT NULL, " +
                    "user_id INT NOT NULL, " +
                    "handler_name VARCHAR(100) NOT NULL, " +
                    "handler_order INT NOT NULL, " +
                    "validation_result VARCHAR(20) NOT NULL, " +  // PASSED, FAILED, WARNING
                    "error_message TEXT, " +
                    "metadata TEXT, " +  // JSON
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");

            executeUpdate("CREATE TABLE IF NOT EXISTS system_audit_log (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT, " +
                    "action VARCHAR(100) NOT NULL, " +
                    "entity_type VARCHAR(50), " +  // USER, COURSE, ENROLLMENT, etc.
                    "entity_id INT, " +
                    "old_value TEXT, " +  // JSON
                    "new_value TEXT, " +  // JSON
                    "ip_address VARCHAR(45), " +
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");

            // ================================================================
            // INDEXES for Performance
            // ================================================================

            executeUpdate("CREATE INDEX IF NOT EXISTS idx_users_username ON users(username)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_users_type ON users(user_type)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_students_student_id ON students(student_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_faculty_employee_id ON faculty(employee_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_enrollments_student ON enrollments(student_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_enrollments_section ON enrollments(section_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_enrollments_status ON enrollments(status)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_notifications_user ON notifications(user_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_notifications_read ON notifications(read_status)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_notifications_type ON notifications(`type`)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_payments_student ON payments(student_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_sections_semester ON sections(semester, `year`)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_waitlist_section ON waitlist(section_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_restrictions_student ON restrictions(student_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_restrictions_active ON restrictions(is_active)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_perm_grants_student ON permission_grants(student_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_perm_grants_section ON permission_grants(section_id)");
            executeUpdate("CREATE INDEX IF NOT EXISTS idx_perm_grants_active ON permission_grants(is_active)");

            System.out.println("✓ Database schema initialized successfully");
            System.out.println("  Total tables created: 40+");

        } catch (SQLException e) {
            throw new RuntimeException("Error initializing database schema", e);
        }
    }

    /**
     * Insert default/seed data for testing
     */
    public void seedDatabase() {
        try {
            System.out.println("Seeding database with default data...");

            // Default password policy
            executeInsert("INSERT INTO password_policies (policy_name, min_length, " +
                    "require_uppercase, require_lowercase, require_digit, require_special, " +
                    "max_age_days, history_count) VALUES " +
                    "('DEFAULT', 8, TRUE, TRUE, TRUE, TRUE, 90, 5)");

            // Default permissions for roles
            executeInsert("INSERT INTO permissions (role_name, feature_code) VALUES " +
                    "('STUDENT', 'REGISTER_COURSES'), " +
                    "('STUDENT', 'VIEW_GRADES'), " +
                    "('STUDENT', 'MAKE_PAYMENT'), " +
                    "('STUDENT', 'VIEW_TRANSCRIPT'), " +
                    "('FACULTY', 'VIEW_CLASS_ROSTER'), " +
                    "('FACULTY', 'ENTER_GRADES'), " +
                    "('FACULTY', 'DROP_STUDENTS'), " +
                    "('HONORS', 'PRIORITY_REGISTRATION'), " +
                    "('HONORS', 'OVERLOAD_CREDITS')");

            // Default restriction impacts. Using Update instead of Insert because this table doesn't have generated
            // keys
            executeUpdate("INSERT INTO restriction_impacts (restriction_type, blocked_feature) VALUES " +
                    "('FINANCIAL_HOLD', 'REGISTER_COURSES'), " +
                    "('FINANCIAL_HOLD', 'VIEW_TRANSCRIPT'), " +
                    "('FINANCIAL_HOLD', 'ORDER_TRANSCRIPT'), " +
                    "('ACADEMIC_PROBATION', 'HONORS_PROGRAMS'), " +
                    "('ACADEMIC_PROBATION', 'STUDY_ABROAD'), " +
                    "('ACADEMIC_PROBATION', 'OVERLOAD_CREDITS')");

            // Sample department
            executeInsert("INSERT INTO departments (code, name) VALUES " +
                    "('CS', 'Computer Science'), " +
                    "('MATH', 'Mathematics'), " +
                    "('ENG', 'English')");

            System.out.println("✓ Database seeded successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error seeding database: " + e.getMessage());
        }
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\Id.java
```java
package edu.advising.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
    boolean isPrimary() default false;
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\ManyToMany.java
```java
package edu.advising.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToMany {
    Class<?> targetEntity();
    String joinTable();
    String joinColumn();        // Points to the "current" object's ID
    String inverseJoinColumn(); // Points to the "target" object's ID
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\ManyToOne.java
```java
package edu.advising.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToOne {
    Class<?> targetEntity();
    String joinColumn(); // The FK in the current table
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\OneToMany.java
```java
package edu.advising.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToMany {
    Class<?> targetEntity();
    String mappedBy(); // The FK in the remote table
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\QueryHandler.java
```java
package edu.advising.core;

import java.sql.ResultSet;
import java.sql.SQLException;

// Functional Interfaces allowing me to pass a Lambda handler into the DatabaseManager
// so the DatabaseManager will handle the connection open/close, and I can still handle
// the data/ResultSet without worrying about the connection pool or database boilerplate.
@FunctionalInterface
public interface QueryHandler<T> {
    T handle(ResultSet rs) throws SQLException;
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\core\Table.java
```java
package edu.advising.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String name();
    boolean isSubTable() default false;
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\EmailChannel.java
```java
package edu.advising.notifications;

import edu.advising.users.User;

class EmailChannel implements NotificationChannel {
    @Override
    public void send(Notification notification, User user) {
        // Simulate email sending
        System.out.printf("Email sent to %s: %s%n", user.getEmail(), notification.getMessage());
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\Notification.java
```java
package edu.advising.notifications;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Notification - Represents a notification message
 */
public class Notification {
    private int id;
    private String type;
    private String message;
    private String priority; // HIGH, MEDIUM, LOW
    private LocalDateTime timestamp;
    private int userId;
    private boolean read;
    private Map<String, String> metadata;

    public Notification(String type, String message, int userId) {
        this(type, message, userId, "MEDIUM");
    }

    public Notification(String type, String message, int userId, String priority) {
        this.type = type;
        this.message = message;
        this.userId = userId;
        this.priority = priority;
        this.timestamp = LocalDateTime.now();
        this.read = false;
        this.metadata = new HashMap<>();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getPriority() {
        return priority;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        this.read = true;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    @Override
    public String toString() {
        String icon = getIconForType();
        return String.format("[%s] %s - %s (Priority: %s)",
                icon, type, message, priority);
    }

    private String getIconForType() {
        switch (type) {
            case "GRADE_CHANGE":
                return "📝";
            case "REGISTRATION":
                return "📚";
            case "PAYMENT":
                return "💳";
            case "FINANCIAL_AID":
                return "💰";
            case "DOCUMENT":
                return "📄";
            case "RESTRICTION":
                return "⚠️";
            case "WAITLIST":
                return "⏳";
            default:
                return "🔔";
        }
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\NotificationChannel.java
```java
package edu.advising.notifications;

import edu.advising.users.User;

/**
 * NotificationChannel - Different delivery methods (Strategy-like)
 */
public interface NotificationChannel {
    void send(Notification notification, User user);
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\NotificationManager.java
```java
package edu.advising.notifications;

import edu.advising.core.DatabaseManager;
import edu.advising.users.Student;
import edu.advising.users.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationManager - Central notification hub (Subject implementation)
 */
public class NotificationManager implements Subject {
    private List<Observer> observers;
    private List<Notification> notificationHistory;
    private DatabaseManager dbManager;
    private static NotificationManager instance;

    private NotificationManager() {
        this.observers = new ArrayList<>();
        this.notificationHistory = new ArrayList<>();
        this.dbManager = DatabaseManager.getInstance();
    }

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("✓ Observer attached: User ID " + observer.getUserId());
        }
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
        System.out.println("✓ Observer detached: User ID " + observer.getUserId());
    }

    @Override
    public void notifyObservers(Notification notification) {
        // Save to database first
        persistNotification(notification);

        // Add to history
        notificationHistory.add(notification);

        // Notify specific observer(s)
        for (Observer observer : observers) {
            if (observer.getUserId() == notification.getUserId()) {
                observer.update(notification);
            }
        }
    }

    /**
     * Broadcast to all observers (system-wide announcements)
     */
    public void broadcast(String type, String message, String priority) {
        for (Observer observer : observers) {
            Notification notification = new Notification(type, message,
                    observer.getUserId(), priority);
            notifyObservers(notification);
        }
    }

    // Specific notification methods for different events

    public void notifyGradeChange(Student student, String courseCode, String grade) {
        Notification notification = new Notification(
                "GRADE_CHANGE",
                String.format("Grade posted for %s: %s", courseCode, grade),
                student.getId(),
                "HIGH"
        );
        notification.addMetadata("courseCode", courseCode);
        notification.addMetadata("grade", grade);
        notifyObservers(notification);
    }

    public void notifyRegistration(Student student, String courseCode, boolean success) {
        String message = success
                ? String.format("Successfully registered for %s", courseCode)
                : String.format("Registration failed for %s", courseCode);

        Notification notification = new Notification(
                "REGISTRATION",
                message,
                student.getId(),
                success ? "MEDIUM" : "HIGH"
        );
        notification.addMetadata("courseCode", courseCode);
        notification.addMetadata("success", String.valueOf(success));
        notifyObservers(notification);
    }

    public void notifyPaymentReceived(Student student, double amount, String paymentType) {
        Notification notification = new Notification(
                "PAYMENT",
                String.format("Payment of $%.2f received (%s)", amount, paymentType),
                student.getId(),
                "MEDIUM"
        );
        notification.addMetadata("amount", String.valueOf(amount));
        notification.addMetadata("paymentType", paymentType);
        notifyObservers(notification);
    }

    public void notifyFinancialAid(Student student, String aidType, String status, double amount) {
        Notification notification = new Notification(
                "FINANCIAL_AID",
                String.format("%s: %s - $%.2f", aidType, status, amount),
                student.getId(),
                "HIGH"
        );
        notification.addMetadata("aidType", aidType);
        notification.addMetadata("status", status);
        notification.addMetadata("amount", String.valueOf(amount));
        notifyObservers(notification);
    }

    public void notifyDocumentAvailable(User user, String documentName, String documentType) {
        Notification notification = new Notification(
                "DOCUMENT",
                String.format("New document available: %s", documentName),
                user.getId(),
                "MEDIUM"
        );
        notification.addMetadata("documentName", documentName);
        notification.addMetadata("documentType", documentType);
        notifyObservers(notification);
    }

    public void notifyRestriction(Student student, String restrictionType, String details) {
        Notification notification = new Notification(
                "RESTRICTION",
                String.format("Account restriction: %s - %s", restrictionType, details),
                student.getId(),
                "HIGH"
        );
        notification.addMetadata("restrictionType", restrictionType);
        notification.addMetadata("details", details);
        notifyObservers(notification);
    }

    public void notifyWaitlistUpdate(Student student, String courseCode, int position) {
        Notification notification = new Notification(
                "WAITLIST",
                String.format("WaitlistEntry update for %s: Position #%d", courseCode, position),
                student.getId(),
                "MEDIUM"
        );
        notification.addMetadata("courseCode", courseCode);
        notification.addMetadata("position", String.valueOf(position));
        notifyObservers(notification);
    }

    /**
     * Get unread notifications for a user
     */
    public List<Notification> getUnreadNotifications(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND read_status = FALSE " +
                "ORDER BY created_at DESC";

        try {
            return dbManager.fetchList(sql, rs -> {
                // This lambda runs ONCE per row found in the database
                Notification n = new Notification(
                        rs.getString("type"),
                        rs.getString("message"),
                        rs.getInt("user_id")
                );
                n.setId(rs.getInt("id"));
                return n;
            }, userId);
        } catch (SQLException e) {
            System.err.println("Error fetching unread notifications: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on failure
        }
    }

    /**
     * Mark notification as read
     */
    public void markAsRead(int notificationId) {
        try {
            String sql = "UPDATE notifications SET read_status = TRUE WHERE id = ?";
            dbManager.executeUpdate(sql, notificationId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get notification history for a user
     */
    public List<Notification> getNotificationHistory(int userId, int limit) {
        try {
            String sql = "SELECT * FROM notifications WHERE user_id = ? " +
                    "ORDER BY created_at DESC LIMIT ?";
            return dbManager.fetchList(sql, rs -> {
                Notification n = new Notification(
                        rs.getString("type"),
                        rs.getString("message"),
                        rs.getInt("user_id")
                );
                n.setId(rs.getInt("id"));
                if (rs.getBoolean("read_status")) {
                    n.markAsRead();
                }
                return n;
            }, userId, limit);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void persistNotification(Notification notification) {
        try {
            String sql = "INSERT INTO notifications (user_id, message, type, created_at, read_status) " +
                    "VALUES (?, ?, ?, ?, ?)";
            notification.setId(
                    dbManager.executeInsert(sql, notification.getUserId(), notification.getMessage(),
                            notification.getType(), Timestamp.valueOf(notification.getTimestamp()),
                            notification.isRead())
            );
        } catch (SQLException e) {
            System.err.println("Error persisting notification: " + e.getMessage());
        }
    }

    public int getObserverCount() {
        return observers.size();
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\NotificationPref.java
```java
package edu.advising.notifications;

import edu.advising.core.Column;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Id;
import edu.advising.core.Table;

import javax.xml.crypto.Data;
import java.sql.SQLException;

/**
 * NotificationPref - Represents a user's notification preference
 */
@Table(name = "notification_preferences")
public class NotificationPref {
    @Id
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "notification_type")
    private String notificationType;
    @Id
    @Column(name = "user_id")
    private int userId;
    @Column(name = "email_enabled")
    private boolean emailEnabled;
    @Column(name = "sms_enabled")
    private boolean smsEnabled;
    @Column(name = "push_enabled")
    private boolean pushEnabled;
    @Column(name = "frequency")
    private String frequency;  // IMMEDIATE, DIGEST, DISABLED
    private DatabaseManager dbManager;

    public NotificationPref(String type, int userId) {
        this(type, userId, true, true, true, "IMMEDIATE");
    }

    public NotificationPref(String type, int userId, boolean emailEnabled, boolean smsEnabled, boolean pushEnabled,
                            String frequency) {
        this.notificationType = type;
        this.userId = userId;
        this.emailEnabled = emailEnabled;
        this.smsEnabled = smsEnabled;
        this.pushEnabled = pushEnabled;
        // TODO: Should make restricted fields like this enums.
        this.frequency = frequency; // IMMEDIATE, DIGEST, DISABLED
        this.dbManager = DatabaseManager.getInstance();
    }

    public boolean shouldNotify() {
        return emailEnabled || smsEnabled || pushEnabled;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public boolean isSmsEnabled() {
        return smsEnabled;
    }

    public void setSmsEnabled(boolean smsEnabled) {
        this.smsEnabled = smsEnabled;
    }

    public boolean isPushEnabled() {
        return pushEnabled;
    }

    public void setPushEnabled(boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void saveNotificationPreference()
            throws SQLException {
        try {
            dbManager.upsert(this);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            System.out.println("Error upserting to database because model is not annotated.");
        }
        /*
        // LOOK HOW MUCH HARDER IT USED TO BE!!! //
        String sql = "INSERT INTO notification_preferences " +
                "(user_id, notification_type, email_enabled, sms_enabled, push_enabled) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "email_enabled = ?, sms_enabled = ?, push_enabled = ?";

        dbManager.executeUpdate(
                sql, userId, notificationType,
                emailEnabled, smsEnabled, pushEnabled,
                emailEnabled, smsEnabled, pushEnabled);
         */
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - Email: %s, SMS: %s, Push: %s (Frequency: %s)",
                userId, notificationType, emailEnabled, smsEnabled, pushEnabled, frequency);
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\NotificationPreferences.java
```java
package edu.advising.notifications;

import edu.advising.core.DatabaseManager;

import java.sql.SQLException;
import java.util.*;

/**
 * NotificationPreferences - User notification settings
 */
public class NotificationPreferences {
    private int userId;
    private DatabaseManager dbManager;
    private List<NotificationPref> collection;

    public NotificationPreferences(int userId) {
        // TODO: Store various channels in database too, maybe in EAV like structure, allowing channels to be dynamic.
        //   Or use a factory to load a NotificationChannel(s) per Notification.
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
        this.collection = loadNotificationPreferences(userId);
    }

    /*
     * Set user notification preferences.
     */
    public void saveNotificationPreferences()
            throws SQLException {
        try {
            dbManager.upsertAll(this.collection);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            System.out.println("Error upserting to database because model is not annotated.");
        }
    }

    /*
     * Add a new preference to NotificationPreferences
     */
    public void addNotificationPref(NotificationPref pref) {
        this.collection.add(pref);
    }

    /*
     * Check preferences before sending
     */
    private List<NotificationPref> loadNotificationPreferences(int userId) {
        // TODO: Add frequncy to this SQL, IMMEDIATE, DIGEST, DISABLED.
        String sql = "SELECT * FROM notification_preferences WHERE user_id = ?";

        try {
            return dbManager.fetchList(sql, rs -> {
                // This lambda runs ONCE per row found in the database
                NotificationPref n = new NotificationPref(
                        rs.getString("notification_type"),
                        rs.getInt("user_id"),
                        rs.getBoolean("email_enabled"),
                        rs.getBoolean("sms_enabled"),
                        rs.getBoolean("push_enabled"),
                        rs.getString("frequency")
                );
                n.setId(rs.getInt("id"));
                return n;
            }, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading notification preferences");
            return new ArrayList<NotificationPref>();
        }
    }

    public Optional<NotificationPref> getNotificationPref(String type) {
        return this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
    }

    public boolean shouldNotify(String type) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        return onp.map(np -> np.isEmailEnabled() || np.isSmsEnabled() || np.isPushEnabled())
                .orElse(false);
    }

    public void disableNotificationTypeChannel(String type, String channel) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        onp.ifPresent(np -> {
            switch (channel) {
                case "EMAIL":
                    np.setEmailEnabled(false);
                    break;
                case "SMS":
                    np.setSmsEnabled(false);
                    break;
                case "PUSH":
                    np.setPushEnabled(false);
                    break;
            }
        });
    }

    public void disableNotificationType(String type) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        onp.ifPresent(np -> {
                            np.setEmailEnabled(false); np.setSmsEnabled(false); np.setPushEnabled(false);
        });
    }

    public void enableNotificationTypeChannel(String type, String channel) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        onp.ifPresent(np -> {
            switch (channel) {
                case "EMAIL":
                    np.setEmailEnabled(true);
                    break;
                case "SMS":
                    np.setSmsEnabled(true);
                    break;
                case "PUSH":
                    np.setPushEnabled(true);
                    break;
            }
        });
    }

    public void enableNotificationType(String type) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        onp.ifPresent(np -> {
            np.setEmailEnabled(true); np.setSmsEnabled(true); np.setPushEnabled(true);
        });
    }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\ObservableFaculty.java
```java
package edu.advising.notifications;

import edu.advising.users.Faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Enhanced Faculty with Observer implementation
 */
class ObservableFaculty extends Faculty implements Observer {
    private List<Notification> notifications;
    private NotificationPreferences preferences;

    /*
     * Internal constructor allowing internal objects to set id during factory method copy.
     */
    private ObservableFaculty(int id, String username, String password, String email,
                             String firstName, String lastName, String employeeId, String department) {
        super(username, password, email, firstName, lastName, employeeId, department);
        this.setId(id);
        this.notifications = new ArrayList<>();
        this.preferences = new NotificationPreferences(this.getId());
    }

    public ObservableFaculty(String username, String password, String email,
                             String firstName, String lastName, String employeeId, String department) {
        super(username, password, email, firstName, lastName, employeeId, department);
        this.notifications = new ArrayList<>();
        this.preferences = new NotificationPreferences(this.getId());
    }

    /**
     * Factory Method to convert/copy Super student-Type Faculty into an ObservableFaculty.
     * @param superObj is the Super-Type Faculty that ObservableFaculty extends, and we want to convert.
     * @return ObservableFaculty with same fields as superObj but extended like the Sub-Type.
     */
    public static ObservableFaculty fromSuperType(Faculty superObj) {
        return new ObservableFaculty(superObj.getId(), superObj.getUsername(), superObj.getPassword(), superObj.getEmail(),
                superObj.getFirstName(), superObj.getLastName(), superObj.getEmployeeId(), superObj.getDepartment());
    }

    public void update(Notification notification) {
        // Check preferences
        Optional<NotificationPref> oPreference = preferences.getNotificationPref(notification.getType());
        if(oPreference.isEmpty()) { return; }
        NotificationPref preference = oPreference.get();
        if (!preference.shouldNotify()) { return; }

        notifications.add(notification);

        // Display notification with priority-based formatting
        String prefix = notification.getPriority().equals("HIGH") ? "❗" : "ℹ️";
        System.out.printf("%s New notification for %s %s: %s%n",
                prefix, getFirstName(), getLastName(), notification);

        // Simulate different delivery channels based on preferences
        if (preference.isEmailEnabled()) {
            new EmailChannel().send(notification, this);
        }
        if (preference.isSmsEnabled()) {
            new SMSChannel().send(notification, this);
        }
        if (preference.isPushEnabled()) {
            new PushChannel().send(notification, this);
        }
    }

    @Override
    public int getUserId() {
        return this.getId();
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public NotificationPreferences getPreferences() {
        return preferences;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\ObservableStudent.java
```java
package edu.advising.notifications;

import edu.advising.users.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Enhanced Student with Observer implementation
 *
 * ORM PERSISTENCE NOTE:
 *   ObservableStudent intentionally carries NO @Table annotation.  It is a
 *   runtime behavioural wrapper, not a DB entity.  Use toSubType() whenever
 *   you need to persist this object via DatabaseManager.upsert().
 *
 *   toSubType():     creates a plain Student whose getClass() == Student.class
 *   fromSuperType(): promotes a plain Student fetched from the DB into a fully initialised ObservableStudent
 *                    copying ALL fields.
 */
public class ObservableStudent extends Student implements Observer {
    //TODO: Should this notifications list be updated from the notifications or notifications_history table?
    private List<Notification> notifications;
    private NotificationPreferences preferences;

    /*
     * Internal constructor allowing internal objects to set id during factory method copy.
     */
    private ObservableStudent(int id, String username, String password, String email,
                             String firstName, String lastName, String studentId) {
        super(username, password, email, firstName, lastName, studentId);
        this.setId(id);
        this.notifications = new ArrayList<>();
        this.preferences = new NotificationPreferences(this.getId());
    }

    public ObservableStudent(String username, String password, String email,
                             String firstName, String lastName, String studentId) {
        super(username, password, email, firstName, lastName, studentId);
        this.notifications = new ArrayList<>();
        this.preferences = new NotificationPreferences(this.getId());
    }

    /**
     * Factory method: creates a plain Student just fetched from the DB via
     * DatabaseManager.fetchOne(Student.class, ...)) into an ObservableStudent.
     *
     * NOTE: I copy every field explicitly as I realized things were missing:
     *   The private constructor only accepts the subset of fields needed to
     *   reconstruct the identity of the object, but ALL mutable fields in
     *   the Student and User super classes must be transferred so that
     *   object memory reads are consistent with what is in the database. If I
     *   omitted a field here, that field would silently return null after conversion.
     */
    public static ObservableStudent fromSuperType(Student s) {
        // Use the private constructor to set the primary key
        ObservableStudent obs = new ObservableStudent(
                s.getId(),
                s.getUsername(),
                s.getPassword(),
                s.getEmail(),
                s.getFirstName(),
                s.getLastName(),
                s.getStudentId()
        );

        // Set User class fields
        obs.userType  = s.getUserType();
        obs.isActive  = s.isActive();  // Had to add getter to User to allow this.
        obs.phone     = s.getPhone();
        obs.lastLogin = s.getLastLogin();  // Had to add getter and change access modifier to allow this.

        // Set Student class fields
        obs.gpa              = s.getGpa();
        obs.enrollmentStatus = s.getEnrollmentStatus();
        obs.academicStanding = s.getAcademicStanding();
        obs.classification   = s.getClassification();
        obs.major            = s.getMajor();
        obs.minor            = s.getMinor();
        obs.advisorId        = s.getAdvisorId();

        return obs;
    }

    /**
     * Creates a plain Student instance populated with all fields from this ObservableStudent.
     *
     * WHY?
     *   Java's getClass() always returns the true underlying class type regardless of type cast.
     *   DatabaseManager.upsertAll() calls items.get(0).getClass() to call getTableHierarchy().
     *   If that returns ObservableStudent.class, which is intentionally not annotated, the
     *   hierarchy is empty and nothing is stored to the database. This method allows the ORM to
     *   receive a genuine Student object so getClass() is Student.class and the hierarchy is
     *   resolved correctly.
     *
     * USAGE:
     *   dbManager.upsert(student.toSubType());
     */
    public Student toSubType() {
        Student s = new Student(
                this.username,
                this.password,
                this.email,
                this.firstName,
                this.lastName,
                this.studentId
        );

        // Set User class fields
        s.setId(this.id);
        s.setUserType(this.userType); // Had to add setter to User to do this
        s.setActive(this.isActive); // Had to add setter to User to do this
        s.setPhone(this.phone);
        s.setLastLogin(this.lastLogin);  // Had to add setter to User to do this

        // Set Student class fields
        s.setGpa(this.gpa);
        s.setEnrollmentStatus(this.enrollmentStatus);
        s.setAcademicStanding(this.academicStanding);
        s.setClassification(this.classification);
        s.setMajor(this.major);
        s.setMinor(this.minor);
        s.setAdvisorId(this.advisorId);

        return s;
    }

    @Override
    public void update(Notification notification) {
        // Check preferences
        Optional<NotificationPref> oPreference = preferences.getNotificationPref(notification.getType());
        if(oPreference.isEmpty()) { return; }
        NotificationPref preference = oPreference.get();
        if (!preference.shouldNotify()) { return; }

        notifications.add(notification);

        // Display notification with priority-based formatting
        String prefix = notification.getPriority().equals("HIGH") ? "❗" : "ℹ️";
        System.out.printf("%s New notification for %s %s: %s%n",
                prefix, getFirstName(), getLastName(), notification);

        // Simulate different delivery channels based on preferences
        if (preference.isEmailEnabled()) {
            new EmailChannel().send(notification, this);
        }
        if (preference.isSmsEnabled()) {
            new SMSChannel().send(notification, this);
        }
        if (preference.isPushEnabled()) {
            new PushChannel().send(notification, this);
        }
    }

    @Override
    public int getUserId() {
        return this.getId();
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public List<Notification> getUnreadNotifications() {
        return notifications.stream()
                .filter(n -> !n.isRead())
                .collect(java.util.stream.Collectors.toList());
    }

    public void viewNotifications() {
        System.out.println("\n=== MY DOCUMENTS / NOTIFICATIONS ===");
        System.out.println("Total: " + notifications.size() +
                " | Unread: " + getUnreadNotifications().size());

        if (notifications.isEmpty()) {
            System.out.println("No notifications");
            return;
        }

        for (Notification n : notifications) {
            String status = n.isRead() ? "✓" : "○";
            System.out.printf("%s %s%n", status, n);
        }
    }

    public NotificationPreferences getPreferences() {
        return preferences;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\Observer.java
```java
// Week 4: OBSERVER PATTERN
// Features Implemented: Communication - My Documents, Grade Notifications, 
//                       Financial Aid alerts, Payment confirmations
// Why Now: Need event-driven notifications across the system

package edu.advising.notifications;

/**
 * Observer - Interface for objects that want to receive notifications
 */
public interface Observer {
    void update(Notification notification);
    int getUserId();
}


```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\PushChannel.java
```java
package edu.advising.notifications;

import edu.advising.users.User;

class PushChannel implements NotificationChannel {
    @Override
    public void send(Notification notification, User user) {
        // Simulate push notification
        System.out.printf("Push notification: %s%n",
                notification.getMessage());
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\SMSChannel.java
```java
package edu.advising.notifications;

import edu.advising.users.User;

class SMSChannel implements NotificationChannel {
    @Override
    public void send(Notification notification, User user) {
        // Simulate SMS sending
        System.out.printf("SMS sent: %s%n", notification.getMessage());
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\notifications\Subject.java
```java
package edu.advising.notifications;

/**
 * Subject - Interface for objects that send notifications
 */
public interface Subject {
    void attach(Observer observer);

    void detach(Observer observer);

    void notifyObservers(Notification notification);
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\users\Faculty.java
```java
package edu.advising.users;

import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;
import edu.advising.core.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Faculty - Concrete user type
 */
@Table(name = "faculty", isSubTable = true)
public class Faculty extends User {
    @Id
    @Column(name="employee_id")
    private String employeeId;
    @Column(name="department")
    private String department;
    @Column(name="title")
    private String title;  // Professor, Associate Professor, etc.
    @Column(name="office_location")
    private String officeLocation;
    @Column(name="office_hours")
    private String officeHours;
    @Column(name="hire_date")
    private LocalDate hireDate;

    @OneToMany(targetEntity = Section.class, mappedBy = "faculty_id")
    private List<Section> sections;

    public Faculty() {}

    public Faculty(String username, String password, String email,
                   String firstName, String lastName, String employeeId, String department) {
        super(username, password, email, firstName, lastName);
        this.userType = "FACULTY";
        this.employeeId = employeeId;
        this.department = department;
    }

    @Override
    public void showDashboard() {
        System.out.println("\n=== FACULTY DASHBOARD ===");
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Department: " + department);
        System.out.println("\nAvailable Features:");
        System.out.println("- View Class Roster");
        System.out.println("- Enter Grades");
        System.out.println("- View Schedule");
        System.out.println("- Drop Students");
    }

    // Getters
    public String getEmployeeId() {
        return employeeId;
    }

    public String getDepartment() {
        return department;
    }

    // Setters
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public List<Section> getSections() throws SQLException {
        if (this.sections == null) {
            // Lazy Load: Use the generic fetchMany from DatabaseManager
            this.sections = DatabaseManager.getInstance()
                    .fetchMany(Section.class, "faculty_id", this.id);
        }
        return this.sections;
    }

    public void setSections(List<Section> sections) throws SQLException, IllegalAccessException{
        if(this.getId() == 0) {
            // We need to save this object to get an id to set on the list items.
            DatabaseManager.getInstance().upsert(this);
        }
        // Now, let's add this object's id to the related list items foreign key id
        for(Section s : sections) { s.setFacultyId(this.getId()); }
        // Now let's upsertAll of these list items (i.e. a batch) and set as this object's related field.
        DatabaseManager.getInstance().upsertAll(sections);
        this.sections = sections;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\users\Student.java
```java
package edu.advising.users;

import edu.advising.commands.Section;
import edu.advising.commands.WaitlistEntry;
import edu.advising.core.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * ADD ANNOTATION STUFF ON Command Pattern Week
 * -
 * Student - Concrete user type
 */
@Table(name = "students", isSubTable = true)
public class Student extends User {
    @Id
    @Column(name = "student_id")
    protected String studentId;
    @Column(name = "gpa")
    protected BigDecimal gpa;
    @Column(name = "enrollment_status")
    protected String enrollmentStatus;
    @Column(name = "academic_standing")
    protected String academicStanding;
    @Column(name = "classification")
    protected String classification;
    @Column(name = "major")
    protected String major;
    @Column(name = "minor")
    protected String minor;
    @Column(name = "advisor_id")
    protected int advisorId;
    @ManyToMany(
            targetEntity = Section.class,
            joinTable = "enrollments",
            joinColumn = "student_id", // Linking table's FK for Student & User table's PK
            inverseJoinColumn = "section_id" // Linking table's FK for Section table's PK
    )
    private List<Section> sections;
    @OneToMany(targetEntity = WaitlistEntry.class, mappedBy = "student_id")
    private List<WaitlistEntry> waitlist;

    public Student() {}

    public Student(String username, String password, String email,
                   String firstName, String lastName, String studentId) {
        super(username, password, email, firstName, lastName);
        this.userType = "STUDENT";
        this.studentId = studentId;
        this.gpa = new BigDecimal("0.0");
    }

    @Override
    public void showDashboard() {
        System.out.println("\n=== STUDENT DASHBOARD ===");
        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("GPA: " + gpa.toPlainString());
        System.out.println("\nAvailable Features:");
        System.out.println("- Register for Classes");
        System.out.println("- View Schedule");
        System.out.println("- Check Grades");
        System.out.println("- Financial Aid");
        System.out.println("- Make Payment");
    }

    protected void ensureId() throws SQLException, IllegalAccessException {
        if(this.getId() == 0) {
            // If the id is not set, we need to save this object to get an id to set on the list items.
            DatabaseManager.getInstance().upsert(this);
        }
    }

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public BigDecimal getGpa() {
        return gpa;
    }

    public void setGpa(BigDecimal gpa) {
        this.gpa = gpa;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public String getAcademicStanding() {
        return academicStanding;
    }

    public void setAcademicStanding(String academicStanding) {
        this.academicStanding = academicStanding;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public int getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(int advisorId) {
        this.advisorId = advisorId;
    }

    public List<Section> getSections() throws SQLException {
        if (this.sections == null) {
            this.sections = DatabaseManager.getInstance().fetchManyToMany(
                    Section.class, "enrollments",
                    "student_id", // Linking table's FK for Student & User table's PK
                    "section_id", // Linking table's FK for Section table's PK
                    this.id
            );
        }
        return this.sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<WaitlistEntry> getWaitlist() throws SQLException {
        // TODO: Gotta find a way to modify the fetch calls to take additional filters since this will return
        //   WaitlistEntries of ANY age and in ANY status.
        if (this.waitlist == null) {
            // Lazy Load: Use the generic fetchMany from DatabaseManager
            this.waitlist = DatabaseManager.getInstance()
                    .fetchMany(WaitlistEntry.class, "student_id", this.id);
        }
        return this.waitlist;
    }

    public void setWaitlist(List<WaitlistEntry> waitlist) throws SQLException, IllegalAccessException {
        ensureId();
        // Now, let's add this object's id to the related list items foreign key id
        for(WaitlistEntry we : waitlist) { we.setStudentId(this.getId()); }
        // Now let's upsertAll of these list items (i.e. a batch) and set as this object's related field.
        DatabaseManager.getInstance().upsertAll(waitlist);
        this.waitlist = waitlist;
    }
}

```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\users\User.java
```java
package edu.advising.users;

// ============================================================================
// WEEK 2: FACTORY PATTERN  (originally)
// WEEK 5: COMMAND PATTERN  (additions marked ★)
// ============================================================================
// WEEK 5 CHANGES:
//   ★ Added `phone` field with @Column annotation — required by UpdateContactCommand
//     for storing and restoring phone numbers during undo/redo.
//   ★ Added `updatedAt` field — needed for audit trail in contact update undo.
//   ★ Added setEmail(), setPhone(), setUpdatedAt() — mutators needed by command undo.
//   ★ Added getPhone(), getUpdatedAt() — accessors for serialization.
//
// NOTE: The `users` table in DatabaseManager must be migrated to add the `phone`
//       column. Add this line to initializeDatabase() or run as a migration:
//
//   ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20);
//   ALTER TABLE users ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
//
//   (H2 syntax: ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20))
//   updated_at is already in the CREATE TABLE definition — no migration needed for that.
// ============================================================================

import edu.advising.core.Column;
import edu.advising.core.Id;
import edu.advising.core.Table;

import java.time.LocalDateTime;

/**
 * User - Base class for all user types in the CRAdvisor system.
 *
 * Uses ORM annotations (@Table, @Column, @Id) so DatabaseManager.upsert()
 * can persist any User subclass without manual SQL strings.
 */
@Table(name = "users")
public class User {

    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    protected int id;

    @Id
    @Column(name = "username")
    protected String username;

    @Column(name = "password")
    protected String password;

    @Column(name = "user_type")
    protected String userType;

    @Column(name = "email")
    protected String email;

    @Column(name = "first_name")
    protected String firstName;

    @Column(name = "last_name")
    protected String lastName;

    @Column(name = "is_active")
    protected boolean isActive;

    @Column(name = "last_login")
    protected LocalDateTime lastLogin;

    // ★ WEEK 5 ADDITION — required by UpdateContactCommand
    // Requires: ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20);
    @Column(name = "phone")
    protected String phone;

    // ★ WEEK 5 ADDITION — for audit trail in command undo
    // Already exists in DB schema as: updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** No-arg constructor required by ORM reflective instantiation. */
    public User() {}

    public User(String username, String password, String email, String firstName, String lastName) {
        this.username  = username;
        this.password  = password;
        this.email     = email;
        this.firstName = firstName;
        this.lastName  = lastName;
    }

    // -------------------------------------------------------------------------
    // Business Methods
    // -------------------------------------------------------------------------

    /** Template for displaying user info (expanded further in Template Pattern week). */
    public void displayInfo() {
        System.out.println("User: " + username + " (" + userType + ")");
        System.out.println("Email: " + email);
        if (phone != null && !phone.isEmpty()) {
            System.out.println("Phone: " + phone);
        }
    }

    /** Hook method for subclass dashboards (Student / Faculty). */
    public void showDashboard() {}

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getId()           { return id; }
    public String getUsername()  { return username; }
    public String getEmail()     { return email; }
    public String getUserType()  { return userType; }
    public String getPassword()  { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public Boolean isActive() { return isActive; }
    public LocalDateTime getLastLogin()  { return lastLogin; }
    public String getPhone()     { return phone; }      // ★ WEEK 5
    public LocalDateTime getUpdatedAt() { return updatedAt; } // ★ WEEK 5

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    public void setId(int id)                { this.id = id; }
    public void setFirstName(String firstName){ this.firstName = firstName; }
    public void setLastName(String lastName)  { this.lastName = lastName; }

    // ★ WEEK 5 — needed by UpdateContactCommand.execute() and undo()
    public void setEmail(String email)            { this.email = email; }
    public void setUserType(String userType)      { this.userType = userType; }
    public void setActive(Boolean isActive)     { this.isActive = isActive; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public void setPhone(String phone)            { this.phone = phone; }
    public void setUpdatedAt(LocalDateTime ts)    { this.updatedAt = ts; }
}
```



File: C:\Users\Humbo\IdeaProjects\BetterAdvisor\src\edu\advising\users\UserFactory.java
```java
package edu.advising.users;

import edu.advising.core.DatabaseManager;

import java.sql.*;

/**
 * UserFactory - Factory Pattern Implementation
 * Creates appropriate user objects and persists them to database
 */
public class UserFactory {
    private DatabaseManager dbManager;

    public UserFactory() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Factory method to create users based on type
     */
    public User createUser(String userType, String username, String password,
                           String email, String firstName, String lastName, String... additionalInfo) {
        User user = null;

        switch (userType.toUpperCase()) {
            case "STUDENT":
                if (additionalInfo.length >= 1) {
                    user = new Student(username, password, email, firstName, lastName,
                            additionalInfo[0]); // studentId
                }
                break;
            case "FACULTY":
                if (additionalInfo.length >= 2) {
                    user = new Faculty(username, password, email, firstName, lastName,
                            additionalInfo[0], // employeeId
                            additionalInfo[1]); // department
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown user type: " + userType);
        }

        if (user != null) {
            saveUserToDatabase(user, additionalInfo);
        }

        return user;
    }

    private void saveUserToDatabase(User user, String... additionalInfo) {
        try {
            String userSql = "INSERT INTO users (username, password, user_type, first_name, last_name, email) VALUES (?, ?, ?, ?, ?, ?)";

            // Retrieving the user id generated by the database, and setting it on this object.
            user.setId(dbManager.executeInsert(userSql,
                    user.getUsername(),
                    user.password,
                    user.getUserType(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail()));

            // Insert into specific user type table
            if (user instanceof Student) {
                Student student = (Student) user;
                String studentSql = "INSERT INTO students (id, student_id, gpa) VALUES (?, ?, ?)";
                dbManager.executeUpdate(studentSql, user.getId(), student.getStudentId(), student.getGpa());
            } else if (user instanceof Faculty) {
                Faculty faculty = (Faculty) user;
                String facultySql = "INSERT INTO faculty (id, employee_id, department) VALUES (?, ?, ?)";
                dbManager.executeUpdate(facultySql, user.getId(), faculty.getEmployeeId(), faculty.getDepartment());
            }

            System.out.println("User created successfully with ID: " + user.getId());
        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
            throw new RuntimeException("Error saving user to database", e);
        }
    }

    /**
     * Retrieve user from database by userId
     */
    private User getUserByParam(String sql, String param) {
        try {
            return dbManager.executeQuery(sql,rs ->
            {
                if (rs.next()) {
                    String userType = rs.getString("user_type");
                    User user = null;

                    if ("STUDENT".equals(userType)) {
                        user = new Student(
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email"),
                                rs.getString("s_fname"),
                                rs.getString("s_lname"),
                                rs.getString("student_id")
                        );
                        ((Student) user).setGpa(rs.getBigDecimal("gpa"));
                    } else if ("FACULTY".equals(userType)) {
                        user = new Faculty(
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email"),
                                rs.getString("f_fname"),
                                rs.getString("f_lname"),
                                rs.getString("employee_id"),
                                rs.getString("department")
                        );
                    }

                    if (user != null) {
                        user.setId(rs.getInt("id"));
                    }
                    return user;
                }
                return null;
            }, param);
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user", e);
        }
    }

    /**
     * Retrieve user from database by userId
     */
    public User getUserById(int userId) {
        String sql = "SELECT u.*, u.first_name as s_fname, u.last_name as s_lname, " +
                "s.student_id, s.gpa, u.first_name as f_fname, u.last_name as f_lname, " +
                "f.employee_id, f.department " +
                "FROM users u " +
                "LEFT JOIN students s ON u.id = s.id " +
                "LEFT JOIN faculty f ON u.id = f.id " +
                "WHERE u.id = ?";
        return this.getUserByParam(sql, String.valueOf(userId));
    }

    /**
     * Retrieve user from database by username
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT u.*, u.first_name as s_fname, u.last_name as s_lname, " +
                "s.student_id, s.gpa, u.first_name as f_fname, u.last_name as f_lname, " +
                "f.employee_id, f.department " +
                "FROM users u " +
                "LEFT JOIN students s ON u.id = s.id " +
                "LEFT JOIN faculty f ON u.id = f.id " +
                "WHERE u.username = ?";
        return this.getUserByParam(sql, username);
    }
}
```

