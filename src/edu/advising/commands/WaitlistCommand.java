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