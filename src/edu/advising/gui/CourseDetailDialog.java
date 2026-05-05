package edu.advising.gui;

import edu.advising.commands.Course;
import edu.advising.commands.Section;
import edu.advising.users.Faculty;
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

public class CourseDetailDialog {

    public static void show(Stage owner, Section section) {
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

        try {
            Course course = section.getCourse();
            if (course != null) {
                courseName = course.getName();
                courseCode = course.getCode();
                description = course.getDescription();
                level = course.getLevel();
                credits = course.getCredits();
            }
            Faculty faculty = section.getFaculty();
            if (faculty != null) {
                instructorName = faculty.getFirstName() + " " + faculty.getLastName();
                instructorEmail = faculty.getEmail();
                instructorDept = faculty.getDepartment();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ── Header ─────────────────────────────────────────────
        Label courseCodeLabel = new Label(courseCode + " — " + courseName);
        courseCodeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        courseCodeLabel.setTextFill(Color.WHITE);
        courseCodeLabel.setWrapText(true);

        String statusText = section.getStatus();
        Label statusBadge = new Label(statusText);
        statusBadge.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        boolean isOpen = "OPEN".equals(statusText);
        statusBadge.setTextFill(isOpen ? Color.web("#1E3A2A") : Color.web("#3A1E1E"));
        statusBadge.setStyle(
                "-fx-background-color: " + (isOpen ? "#2ECC71" : "#E05555") + ";" +
                        "-fx-background-radius: 4px;" +
                        "-fx-padding: 3px 10px;"
        );

        Label sectionLabel = new Label(
                "Section " + section.getSectionNumber() +
                        "  ·  " + section.getSemester() + " " + section.getYear()
        );
        sectionLabel.setFont(Font.font("Arial", 13));
        sectionLabel.setTextFill(Color.LIGHTGRAY);

        HBox statusRow = new HBox(10);
        statusRow.setAlignment(Pos.CENTER_LEFT);
        statusRow.getChildren().addAll(sectionLabel, statusBadge);

        VBox header = new VBox(6);
        header.getChildren().addAll(courseCodeLabel, statusRow);

        // ── Section builder helper ─────────────────────────────
        // We'll build each section manually since we can't use lambdas for helpers easily

        // ── Instructor section ─────────────────────────────────
        Label instructorHeading = sectionHeading("Instructor");

        VBox instructorContent = new VBox(6);
        instructorContent.getChildren().addAll(
                detailRow("Name", instructorName),
                detailRow("Email", instructorEmail.isEmpty() ? "—" : instructorEmail),
                detailRow("Department", instructorDept.isEmpty() ? "—" : instructorDept)
        );

        VBox instructorSection = new VBox(8);
        instructorSection.getChildren().addAll(instructorHeading, instructorContent);

        // ── Course details section ─────────────────────────────
        Label detailsHeading = sectionHeading("Course Details");

        int seatsLeft = section.getCapacity() - section.getEnrolled();
        String seatsText = seatsLeft + " / " + section.getCapacity() + " seats available";

        VBox detailsContent = new VBox(6);
        detailsContent.getChildren().addAll(
                detailRow("Credits", String.valueOf(credits)),
                detailRow("Level", level + "0-level"),
                detailRow("Room", section.getRoom() == null ? "—" : section.getRoom()),
                detailRow("Seats", seatsText),
                detailRow("Description", description.isEmpty() ? "—" : description)
        );

        VBox detailsSection = new VBox(8);
        detailsSection.getChildren().addAll(detailsHeading, detailsContent);

        // ── Syllabus section (placeholder) ────────────────────
        Label syllabusHeading = sectionHeading("Syllabus");
        Label syllabusPlaceholder = new Label("Syllabus not yet available.");
        syllabusPlaceholder.setFont(Font.font("Arial", 13));
        syllabusPlaceholder.setTextFill(Color.GRAY);

        VBox syllabusSection = new VBox(8);
        syllabusSection.getChildren().addAll(syllabusHeading, syllabusPlaceholder);

        // ── Prerequisites section (placeholder) ───────────────
        Label prereqHeading = sectionHeading("Prerequisites");
        Label prereqPlaceholder = new Label("No prerequisites listed.");
        prereqPlaceholder.setFont(Font.font("Arial", 13));
        prereqPlaceholder.setTextFill(Color.GRAY);

        VBox prereqSection = new VBox(8);
        prereqSection.getChildren().addAll(prereqHeading, prereqPlaceholder);

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
        VBox content = new VBox(16);
        content.setPadding(new Insets(28));
        content.setStyle("-fx-background-color: #1E1E2E;");
        content.getChildren().addAll(
                header,
                new Separator(),
                instructorSection,
                new Separator(),
                detailsSection,
                new Separator(),
                syllabusSection,
                new Separator(),
                prereqSection,
                buttonRow
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #1E1E2E; -fx-background: #1E1E2E;");

        dialog.setTitle(courseCode + " — " + courseName);
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