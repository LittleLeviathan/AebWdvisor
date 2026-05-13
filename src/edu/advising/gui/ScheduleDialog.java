package edu.advising.gui;

import edu.advising.commands.Course;
import edu.advising.commands.Section;
import edu.advising.iterator.BySemesterIterator;
import edu.advising.iterator.ScheduleGenerator;
import edu.advising.iterator.ScheduleIterator;
import edu.advising.iterator.StudentSchedule;
import edu.advising.users.Faculty;
import edu.advising.users.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ScheduleDialog {

    User user;

    public ScheduleDialog(User user){
        this.user = user;
    }

    public static void show(Stage owner, StudentSchedule studentSchedule) throws SQLException {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);

        // ── Pull data ──────────────────────────────────────────
        String courseName = "Unknown Course";
        String courseCode = "";
        String description = "";
        String level = "";
        double credits = 0;
        String instructorName = "TBA";
        String instructorEmail = "";
        String instructorDept = "";



        // ── Header ─────────────────────────────────────────────
        Label title = new Label("My Class Schedule");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.WHITE);
        title.setWrapText(true);

        VBox header = new VBox(6);
        header.getChildren().addAll(title);
        ///clean up

        // ── Close button ───────────────────────────────────────
        Button closeBtn = new Button("Close");
        closeBtn.setStyle(
                "-fx-background-color: #4A90D9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 8px 24px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 4px;"
        );
        closeBtn.setOnAction(e -> dialog.close());

        HBox buttonRow = new HBox();
        buttonRow.setAlignment(Pos.CENTER_RIGHT);
        buttonRow.getChildren().add(closeBtn);

        // ── Assemble content ───────────────────────────────────
        ScheduleIterator semesterIterator = studentSchedule.createBySemesterIterator();
        VBox content = new VBox(16);
        content.setPadding(new Insets(28));
        content.setStyle("-fx-background-color: #1E1E2E;");
        content.getChildren().addAll(header, new Separator());
        while (semesterIterator.hasNext()){
            Section s = semesterIterator.next();
            content.getChildren().add(sectionHeading(s.getCourseCode()+" "+s.getCourseName()));
            content.getChildren().addAll(detailRow("Semester:", s.getSemester()), detailRow("Status:", s.getStatus()),
                    detailRow("Meeting Information:", s.getDeliveryMode()+" "+s.getDayOfWeek()+" "+s.getStartTime() +" Room: "+s.getRoom()));
            content.getChildren().add(new Separator());
        }
        semesterIterator.reset();

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #1E1E2E; -fx-background: #1E1E2E;");

        dialog.setTitle("View My Schedule");
        dialog.setScene(new Scene(scrollPane, 520, 580));
        dialog.showAndWait();
    }

    // ── Helpers ────────────────────────────────────────────────

    private static Label sectionHeading(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        label.setTextFill(Color.web("#4A90D9"));
        return label;
    }

    private static HBox detailRow(String key, String value) {
        Label keyLabel = new Label(key);
        keyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        keyLabel.setTextFill(Color.LIGHTGRAY);
        keyLabel.setMinWidth(100);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", 13));
        valueLabel.setTextFill(Color.WHITE);
        valueLabel.setWrapText(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(12);
        row.getChildren().addAll(keyLabel, valueLabel);
        return row;
    }
}
