package edu.advising.gui;

import edu.advising.BetterAdvisorApp;
import edu.advising.users.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TranscriptScreen {

    public static Scene getScene(User user) {

        // Header
        Label titleLabel = new Label("Transcript");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("View your academic history");
        subtitleLabel.setFont(Font.font("Arial", 13));
        subtitleLabel.setTextFill(Color.LIGHTGRAY);

        // Unofficial transcript button
        Button unofficialBtn = new Button("View Unofficial Transcript");
        unofficialBtn.setStyle(
                "-fx-background-color: #4A90D9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;"
        );

        // Official transcript request button
        Button officialBtn = new Button("Request Official Transcript");
        officialBtn.setStyle(
                "-fx-background-color: #2E2E3E;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;"
        );

        // Status label for feedback
        Label statusLabel = new Label("");
        statusLabel.setTextFill(Color.LIGHTGREEN);
        statusLabel.setFont(Font.font("Arial", 13));

        // Placeholder for transcript content
        Label placeholderLabel = new Label("Your transcript will appear here.");
        placeholderLabel.setTextFill(Color.GRAY);
        placeholderLabel.setFont(Font.font("Arial", 13));

        // Button actions
        unofficialBtn.setOnAction(e -> {
            statusLabel.setText("Loading unofficial transcript...");
            placeholderLabel.setText(
                    "UNOFFICIAL TRANSCRIPT\n" +
                            "Student: " + user.getFirstName() + " " + user.getLastName() + "\n" +
                            "ID: " + user.getId() + "\n\n" +
                            "Transcript data will be populated from database in a future update."
            );
        });

        officialBtn.setOnAction(e -> {
            statusLabel.setText("Official transcript request submitted.");
        });

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

        // Button row
        HBox buttonRow = new HBox(15);
        buttonRow.setAlignment(Pos.CENTER_LEFT);
        buttonRow.getChildren().addAll(unofficialBtn, officialBtn);

        // Content area
        VBox contentArea = new VBox(20);
        contentArea.setPadding(new Insets(30));
        contentArea.setStyle("-fx-background-color: #1E1E2E;");
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        contentArea.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                buttonRow,
                statusLabel,
                placeholderLabel
        );

        // Full layout
        VBox layout = new VBox();
        layout.getChildren().addAll(navBar, contentArea);

        return new Scene(layout, 900, 650);
    }
}