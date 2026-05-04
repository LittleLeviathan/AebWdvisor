package edu.advising.gui;

import edu.advising.commands.Course;
import edu.advising.commands.Section;
import edu.advising.users.Faculty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CourseCardComponent {
    private final Section section;

    CourseCardComponent(Section section) {
        this.section = section;
    }

    public VBox build() {

        // Grab related data (lazy-loaded from DB via existing getters)
        String courseName = "Unknown Course";
        String courseCode = "";
        String instructorName = "TBA";
        String credits = "";

        try {
            Course course = section.getCourse();
            if (course != null) {
                courseName = course.getName();
                courseCode = course.getCode();
                credits = course.getCredits() + " credits";
            }
            Faculty faculty = section.getFaculty();
            if (faculty != null) {
                instructorName = faculty.getFirstName() + " " + faculty.getLastName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Course title row
        Label nameLabel = new Label(courseCode + " — " + courseName);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        nameLabel.setTextFill(Color.WHITE);

        // Credits badge
        Label creditsLabel = new Label(credits);
        creditsLabel.setFont(Font.font("Arial", 12));
        creditsLabel.setTextFill(Color.web("#4A90D9"));
        creditsLabel.setStyle(
                "-fx-background-color: #1A2A3E;" +
                        "-fx-padding: 3px 8px;" +
                        "-fx-background-radius: 4px;"
        );

        // Spacer pushes badge to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox titleRow = new HBox(8);
        titleRow.getChildren().addAll(nameLabel, spacer, creditsLabel);

        // Instructor + section info
        Label detailsLabel = new Label(
                "Instructor: " + instructorName +
                        "   |   Section: " + section.getSectionNumber() +
                        "   |   Room: " + section.getRoom()
        );
        detailsLabel.setFont(Font.font("Arial", 13));
        detailsLabel.setTextFill(Color.LIGHTGRAY);

        // Seats remaining
        int seatsLeft = section.getCapacity() - section.getEnrolled();
        String seatsText = seatsLeft > 0 ? seatsLeft + " seats open" : "Full";
        Color seatsColor = seatsLeft > 0 ? Color.LIGHTGREEN : Color.web("#E05555");

        Label seatsLabel = new Label(seatsText);
        seatsLabel.setFont(Font.font("Arial", 12));
        seatsLabel.setTextFill(seatsColor);

        // "View Details" button — wired up to nothing yet, that's the next user story
        Button detailsBtn = new Button("View Details");
        detailsBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #4A90D9;" +
                        "-fx-font-size: 12px;" +
                        "-fx-border-color: #4A90D9;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-padding: 4px 12px;" +
                        "-fx-cursor: hand;"
        );

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        HBox bottomRow = new HBox(8);
        bottomRow.getChildren().addAll(seatsLabel, bottomSpacer, detailsBtn);

        // Outer card
        VBox card = new VBox(8);
        card.setPadding(new Insets(16));
        card.setStyle(
                "-fx-background-color: #2A2A3E;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #3A3A5E;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 8px;"
        );
        card.getChildren().addAll(titleRow, detailsLabel, bottomRow);

        return card;
    }
}
