package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;

import java.util.ArrayList;
import java.util.List;

public class StudentSchedule implements ScheduleCollection {

    private List<Section> sections;

    public StudentSchedule(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    @Override
    public ScheduleIterator createWeeklyIterator() {
        return new WeeklyScheduleIterator(sections);
    }

    @Override
    public ScheduleIterator createBySemesterIterator() {
        return new BySemesterIterator(sections);
    }

    @Override
    public ScheduleIterator createByDeliveryModeIterator(String mode) {
        return new ByDeliveryModeIterator(sections, mode);
    }

    @Override
    public ScheduleIterator createByStatusIterator(String status) {
        return new ByStatusIterator(sections, status);
    }
}