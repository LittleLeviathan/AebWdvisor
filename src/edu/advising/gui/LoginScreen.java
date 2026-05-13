package edu.advising.gui;

import edu.advising.BetterAdvisorApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.SQLException;

public class LoginScreen {

    public static Scene getScene() {

        // Title label
        Label titleLabel = new Label("BetterAdvisor");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);

        // Subtitle
        Label subtitleLabel = new Label("Student & Faculty Portal");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.LIGHTGRAY);

        // Username field
        Label usernameLabel = new Label("Username");
        usernameLabel.setTextFill(Color.WHITE);
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(300);

        // Password field
        Label passwordLabel = new Label("Password");
        passwordLabel.setTextFill(Color.WHITE);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(300);

        // Error label (hidden until needed)
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setMinWidth(300);
        loginButton.setStyle(
                "-fx-background-color: #4A90D9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10px;" +
                        "-fx-cursor: hand;"
        );

        // What happens when login button is clicked
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter both username and password.");
                return;
            }

            // Fire the LOGIN action through your existing ViewContext
            try {
                BetterAdvisorApp.viewContext.handleAction("LOGIN", username, password, "127.0.0.1");
            } catch (SQLException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Stack everything vertically
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #1E1E2E;");
        layout.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                usernameLabel,
                usernameField,
                passwordLabel,
                passwordField,
                errorLabel,
                loginButton
        );

        return new Scene(layout, 900, 650);
    }
}