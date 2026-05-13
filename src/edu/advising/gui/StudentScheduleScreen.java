package edu.advising.gui;

import edu.advising.BetterAdvisorApp;
import edu.advising.iterator.*;
import edu.advising.users.Student;
import edu.advising.users.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.SQLException;

public class StudentScheduleScreen {

    public StudentScheduleScreen() throws SQLException {
    }

    public static Scene getScene(User user) {

        // Header
        Label titleLabel = new Label("Class Schedule");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("View a schedule of your registered classes");
        subtitleLabel.setFont(Font.font("Arial", 13));
        subtitleLabel.setTextFill(Color.LIGHTGRAY);

        //
        Button semesterBtn = new Button("Sort by Semester");
        semesterBtn.setStyle(
                "-fx-background-color: #4A90D9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;"
        );
        Button weeklyBtn = new Button("Sort by Day of the Week");
        weeklyBtn.setStyle(
                "-fx-background-color: #4A90D9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;"
        );

        SplitMenuButton statusBtn = new SplitMenuButton();
        statusBtn.setText("Sort by Status");
        MenuItem open = new MenuItem("OPEN");
        MenuItem closed = new MenuItem("CLOSED");
        statusBtn.getItems().addAll(open, closed);
        statusBtn.setStyle(
                "-fx-background-color: #4A90D9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;"
        );

        SplitMenuButton deliveryModeBtn = new SplitMenuButton();
        deliveryModeBtn.setText("Sort by Delivery Mode");
        MenuItem online = new MenuItem("ONLINE");
        MenuItem inPerson = new MenuItem("IN_PERSON");
        MenuItem hybrid = new MenuItem("HYBRID");
        deliveryModeBtn.getItems().addAll(online, inPerson, hybrid);
        deliveryModeBtn.setStyle(
                "-fx-background-color: #4A90D9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;"
        );

        // Button actions
        semesterBtn.setOnAction(e -> {
            Stage owner = (Stage) semesterBtn.getScene().getWindow();
            try {
                ScheduleDialog.show(owner, new StudentSchedule(user.getSections()), new BySemesterIterator(user.getSections()), (Student)user);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        weeklyBtn.setOnAction(e -> {
            Stage owner = (Stage) weeklyBtn.getScene().getWindow();
            try {
                ScheduleDialog.show(owner, new StudentSchedule(user.getSections()), new WeeklyScheduleIterator(user.getSections()), (Student)user);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        open.setOnAction(e -> {
            Stage owner = (Stage) statusBtn.getScene().getWindow();
            try {
                ScheduleDialog.show(owner, new StudentSchedule(user.getSections()), new ByStatusIterator(user.getSections(), open.getText()), (Student)user);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        closed.setOnAction(e -> {
            Stage owner = (Stage) statusBtn.getScene().getWindow();
            try {
                ScheduleDialog.show(owner, new StudentSchedule(user.getSections()), new ByStatusIterator(user.getSections(), closed.getText()), (Student)user);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        online.setOnAction(e -> {
            Stage owner = (Stage) deliveryModeBtn.getScene().getWindow();
            try {
                ScheduleDialog.show(owner, new StudentSchedule(user.getSections()), new ByDeliveryModeIterator(user.getSections(), online.getText()), (Student)user);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        inPerson.setOnAction(e -> {
            Stage owner = (Stage) deliveryModeBtn.getScene().getWindow();
            try {
                ScheduleDialog.show(owner, new StudentSchedule(user.getSections()), new ByDeliveryModeIterator(user.getSections(), inPerson.getText()), (Student)user);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        hybrid.setOnAction(e -> {
            Stage owner = (Stage) deliveryModeBtn.getScene().getWindow();
            try {
                ScheduleDialog.show(owner, new StudentSchedule(user.getSections()), new ByDeliveryModeIterator(user.getSections(), hybrid.getText()), (Student)user);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
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
        buttonRow.getChildren().addAll(semesterBtn, weeklyBtn, statusBtn, deliveryModeBtn);

        // Content area
        VBox contentArea = new VBox(20);
        contentArea.setPadding(new Insets(30));
        contentArea.setStyle("-fx-background-color: #1E1E2E;");
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        contentArea.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                buttonRow
        );

        // Full layout
        VBox layout = new VBox();
        layout.getChildren().addAll(navBar, contentArea);

        return new Scene(layout, 900, 650);
    }
}
