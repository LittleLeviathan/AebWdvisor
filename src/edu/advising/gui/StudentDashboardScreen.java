package edu.advising.gui;

import edu.advising.BetterAdvisorApp;
import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;
import edu.advising.core.DatabaseManager;
import edu.advising.state.EnrollmentContext;
import edu.advising.users.Student;
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

import java.sql.SQLException;

public class StudentDashboardScreen {

    public StudentDashboardScreen() throws SQLException {
    }

    public static Scene getScene(User user) {

        // Welcome header
        Label welcomeLabel = new Label("Welcome, " + user.getFirstName() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        welcomeLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("Student ID: " + user.getId());
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.LIGHTGRAY);

        // Navigation buttons
        Button registrationBtn = new Button("Registration");
        Button transcriptBtn = new Button("Transcript");
        Button logoutBtn = new Button("Logout");
        Button scheduleBtn = new Button("My Class Schedule");

        // Style the nav buttons
        String navButtonStyle =
                "-fx-background-color: #2E2E3E;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;";

        registrationBtn.setStyle(navButtonStyle);
        transcriptBtn.setStyle(navButtonStyle);
        scheduleBtn.setStyle(navButtonStyle);

        logoutBtn.setStyle(
                "-fx-background-color: #8B0000;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;"
        );

        // Button actions - wire into existing ViewContext
        registrationBtn.setOnAction(e -> {
            BetterAdvisorApp.viewContext.handleAction("NAVIGATE", "REGISTRATION");
        });

        transcriptBtn.setOnAction(e -> {
            BetterAdvisorApp.viewContext.handleAction("NAVIGATE", "TRANSCRIPT");
        });

        scheduleBtn.setOnAction(e ->{

            BetterAdvisorApp.viewContext.handleAction("NAVIGATE", "SCHEDULE");
        });

        logoutBtn.setOnAction(e -> {
            BetterAdvisorApp.viewContext.logout();
        });

        // Nav bar across the top
        HBox navBar = new HBox(10);
        navBar.setPadding(new Insets(15));
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setStyle("-fx-background-color: #12121E;");
        navBar.getChildren().addAll(registrationBtn, transcriptBtn, scheduleBtn, logoutBtn);

        // Main content area
        Label contentLabel = new Label("Student Dashboard");
        contentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        contentLabel.setTextFill(Color.WHITE);

        Label infoLabel = new Label("Use the navigation buttons above to get started.");
        infoLabel.setTextFill(Color.LIGHTGRAY);

        VBox contentArea = new VBox(15);
        contentArea.setPadding(new Insets(30));
        contentArea.setStyle("-fx-background-color: #1E1E2E;");
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        contentArea.getChildren().addAll(welcomeLabel, subtitleLabel, contentLabel, infoLabel);

        // Full layout
        VBox layout = new VBox();
        layout.getChildren().addAll(navBar, contentArea);

        return new Scene(layout, 900, 650);
    }



    private static void testEnrollments(User user) throws SQLException, IllegalAccessException {
        Enrollment fa2025 = buildEnrollment(1, "FA", 2025, "ONLINE",    "MONDAY", "09:00", "ENROLLED", user);
        Enrollment sp2025 = buildEnrollment(2, "SP", 2025, "IN_PERSON", "TUESDAY", "13:00", "ENROLLED", user);
        Enrollment su2024 = buildEnrollment(3, "SU", 2024, "HYBRID",    "WEDNESDAY", "10:00", "DROPPED", user);
        Enrollment sp2024 = buildEnrollment(4, "SP", 2024, "ONLINE",    "THURSDAY", "08:00", "ENROLLED", user);
        DatabaseManager.getInstance().upsert(fa2025);
        DatabaseManager.getInstance().upsert(sp2025);
        DatabaseManager.getInstance().upsert(su2024);
        DatabaseManager.getInstance().upsert(sp2024);
    }

    private static Enrollment buildEnrollment(int id, String semester, int year,
                                              String deliveryMode, String dayOfWeek,
                                              String startTime, String status, User user) {
        Section section = new Section("S" + id, semester, year, 30);
        section.setDeliveryMode(deliveryMode);
        section.setDayOfWeek(dayOfWeek);
        section.setStartTime(startTime);

        Enrollment e = new Enrollment(user.getId(), section.getId());
        e.setId(id);
        e.setStatus(status);
        e.setSection(section);
        return e;
    }
}