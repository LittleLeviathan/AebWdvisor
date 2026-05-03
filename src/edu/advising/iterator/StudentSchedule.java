package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import java.util.List;

public class StudentSchedule implements ScheduleCollection {

    private List<Enrollment> enrollments;

    public StudentSchedule(List<Enrollment> enrollments) {
        // Store the list of enrollments passed in
    }

    @Override
    public ScheduleIterator createWeeklyIterator() {
        // Create and return a new WeeklyScheduleIterator with the enrollments list
    }

    @Override
    public ScheduleIterator createBySemesterIterator() {
        // Create and return a new BySemesterIterator with the enrollments list
    }

    @Override
    public ScheduleIterator createByDeliveryModeIterator(String mode) {
        // Create and return a new ByDeliveryModeIterator with the enrollments list and mode
    }

    @Override
    public ScheduleIterator createByStatusIterator(String status) {
        // Create and return a new ByStatusIterator with the enrollments list and status
    }
}