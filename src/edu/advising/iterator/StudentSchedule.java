package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import java.util.ArrayList;
import java.util.List;

public class StudentSchedule implements ScheduleCollection {

    private List<Enrollment> enrollments;

    public StudentSchedule(List<Enrollment> enrollments) {
        this.enrollments = new ArrayList<>(enrollments);
    }

    @Override
    public ScheduleIterator createWeeklyIterator() {
        return new WeeklyScheduleIterator(enrollments);
    }

    @Override
    public ScheduleIterator createBySemesterIterator() {
        return new BySemesterIterator(enrollments);
    }

    @Override
    public ScheduleIterator createByDeliveryModeIterator(String mode) {
        return new ByDeliveryModeIterator(enrollments, mode);
    }

    @Override
    public ScheduleIterator createByStatusIterator(String status) {
        return new ByStatusIterator(enrollments, status);
    }
}