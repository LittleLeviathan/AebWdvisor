package edu.advising.gui;

import edu.advising.BetterAdvisorApp;
import edu.advising.users.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PermissionManagementScreen {

    public static Scene getScene(User user) {

        // Header
        Label titleLabel = new Label("Permission Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("Manage waitlist permission requests for your sections");
        subtitleLabel.setFont(Font.font("Arial", 13));
        subtitleLabel.setTextFill(Color.LIGHTGRAY);

        // Student ID input
        Label studentIdLabel = new Label("Student ID");
        studentIdLabel.setTextFill(Color.WHITE);
        studentIdLabel.setFont(Font.font("Arial", 13));

        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Enter student ID...");
        studentIdField.setMaxWidth(300);

        // Section ID input
        Label sectionIdLabel = new Label("Section ID");
        sectionIdLabel.setTextFill(Color.WHITE);
        sectionIdLabel.setFont(Font.font("Arial", 13));

        TextField sectionIdField = new TextField();
        sectionIdField.setPromptText("Enter section ID...");
        sectionIdField.setMaxWidth(300);

        // Action buttons
        Button approveBtn = new Button("Approve Permission");
        approveBtn.setStyle(
                "-fx-background-color: #2E7D32;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;"
        );

        Button denyBtn = new Button("Deny Permission");
        denyBtn.setStyle(
                "-fx-background-color: #8B0000;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;"
        );

        // Status label
        Label statusLabel = new Label("");
        statusLabel.setFont(Font.font("Arial", 13));
        statusLabel.setTextFill(Color.LIGHTGREEN);

        // Button actions
        approveBtn.setOnAction(e -> {
            String studentId = studentIdField.getText().trim();
            String sectionId = sectionIdField.getText().trim();

            if (studentId.isEmpty() || sectionId.isEmpty()) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("Please enter both a student ID and section ID.");
                return;
            }

            BetterAdvisorApp.viewContext.handleAction(
                    "APPROVE_PERMISSION", studentId, sectionId
            );
            statusLabel.setTextFill(Color.LIGHTGREEN);
            statusLabel.setText("Permission approved for student " + studentId +
                    " in section " + sectionId + ".");
            studentIdField.clear();
            sectionIdField.clear();
        });

        denyBtn.setOnAction(e -> {
            String studentId = studentIdField.getText().trim();
            String sectionId = sectionIdField.getText().trim();

            if (studentId.isEmpty() || sectionId.isEmpty()) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("Please enter both a student ID and section ID.");
                return;
            }

            BetterAdvisorApp.viewContext.handleAction(
                    "DENY_PERMISSION", studentId, sectionId
            );
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Permission denied for student " + studentId +
                    " in section " + sectionId + ".");
            studentIdField.clear();
            sectionIdField.clear();
        });

        // Button row
        HBox buttonRow = new HBox(15);
        buttonRow.setAlignment(Pos.CENTER_LEFT);
        buttonRow.getChildren().addAll(approveBtn, denyBtn);

        // Back button
        Button backBtn = new Button("Back to Dashboard");
        backBtn.setStyle(
                "-fx-background-color: #2E2E3E;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;"
        );

        backBtn.setOnAction(e -> {
            BetterAdvisorApp.viewContext.back();
        });

        // Nav bar
        HBox navBar = new HBox(10);
        navBar.setPadding(new Insets(15));
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setStyle("-fx-background-color: #12121E;");
        navBar.getChildren().addAll(backBtn);

        // Content area
        VBox contentArea = new VBox(15);
        contentArea.setPadding(new Insets(30));
        contentArea.setStyle("-fx-background-color: #1E1E2E;");
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        contentArea.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                studentIdLabel,
                studentIdField,
                sectionIdLabel,
                sectionIdField,
                buttonRow,
                statusLabel
        );

        // Full layout
        VBox layout = new VBox();
        layout.getChildren().addAll(navBar, contentArea);

        return new Scene(layout, 900, 650);
    }
}