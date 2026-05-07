package edu.advising.gui;

import edu.advising.BetterAdvisorApp;
import edu.advising.commands.Section;
import edu.advising.repository.SectionRepository;
import edu.advising.users.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class RegistrationScreen {

    public static Scene getScene(User user) {

        // Header
        Label titleLabel = new Label("Course Registration");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("Search and register for available courses");
        subtitleLabel.setFont(Font.font("Arial", 13));
        subtitleLabel.setTextFill(Color.LIGHTGRAY);

        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search by course code or name...");
        searchField.setMaxWidth(400);

        Button searchBtn = new Button("Search");
        searchBtn.setStyle(
                "-fx-background-color: #4A90D9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 8px 20px;" +
                        "-fx-cursor: hand;"
        );

        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.getChildren().addAll(searchField, searchBtn);

        // Status label
        Label statusLabel = new Label("Registration is currently open.");
        statusLabel.setTextFill(Color.LIGHTGREEN);
        statusLabel.setFont(Font.font("Arial", 13));

        // Replace the placeholder with a scrollable list
        VBox courseList = new VBox(10);
        courseList.setPadding(new Insets(4, 0, 0, 0));

        // Temporary hardcoded test — swap this for the service call later
        try {
            List<Section> sections = SectionRepository.findAll();
            System.out.println("Loaded " + sections.size() + " sections");
            for (Section s : sections) {
                courseList.getChildren().add(new CourseCardComponent(s).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Could not load courses.");
            errorLabel.setTextFill(Color.SALMON);
            courseList.getChildren().add(errorLabel);
        }

        ScrollPane scrollPane = new ScrollPane(courseList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #1E1E2E; -fx-background: #1E1E2E;");
        scrollPane.getStyleClass().add("edge-to-edge");

        courseList.setStyle("-fx-background-color: #1E1E2E;");

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
        VBox contentArea = new VBox(20);
        contentArea.setPadding(new Insets(30));
        contentArea.setStyle("-fx-background-color: #1E1E2E;");
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        contentArea.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                statusLabel,
                searchBar,
                scrollPane
        );

        // Full layout
        VBox layout = new VBox();
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        layout.getChildren().addAll(navBar, contentArea);

        return new Scene(layout, 900, 650);
    }
}