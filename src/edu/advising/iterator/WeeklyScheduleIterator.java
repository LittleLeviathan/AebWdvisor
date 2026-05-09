package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class WeeklyScheduleIterator implements ScheduleIterator {

    private List<Enrollment> enrollments;
    private int position;

    private static final List<String> DAY_ORDER = Arrays.asList(
            "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"
    );

    public WeeklyScheduleIterator(List<Enrollment> enrollments) {
        this.enrollments = new ArrayList<>(enrollments);
        this.enrollments.sort(Comparator
                .comparingInt((Enrollment e) -> {
                    try {
                        Section s = e.getSection();
                        if (s == null || s.getDayOfWeek() == null) return Integer.MAX_VALUE;
                        int idx = DAY_ORDER.indexOf(s.getDayOfWeek().toUpperCase());
                        return idx == -1 ? Integer.MAX_VALUE : idx;
                    } catch (SQLException ex) {
                        return Integer.MAX_VALUE;
                    }
                })
                .thenComparing(e -> {
                    try {
                        Section s = e.getSection();
                        if (s == null || s.getStartTime() == null) return "";
                        return s.getStartTime();
                    } catch (SQLException ex) {
                        return "";
                    }
                })
        );
        this.position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < enrollments.size();
    }

    @Override
    public Enrollment next() {
        Enrollment e = enrollments.get(position);
        position++;
        return e;
    }

    @Override
    public void reset() {
        position = 0;
    }
}