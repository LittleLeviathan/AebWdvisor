package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class WeeklyScheduleIterator implements ScheduleIterator {

    private List<Section> sections;
    private int position;

    private static final List<String> DAY_ORDER = Arrays.asList(
            "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"
    );

    public WeeklyScheduleIterator(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
        this.sections.sort(Comparator
                .comparingInt((Section s) -> {
                    if (s == null || s.getDayOfWeek() == null) return Integer.MAX_VALUE;
                    int idx = DAY_ORDER.indexOf(s.getDayOfWeek().toUpperCase());
                    return idx == -1 ? Integer.MAX_VALUE : idx;
                })
                .thenComparing(s -> {
                    if (s == null || s.getStartTime() == null) return "";
                    return s.getStartTime();
                })
        );
        this.position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < sections.size();
    }

    @Override
    public Section next() {
        Section s = sections.get(position);
        position++;
        return s;
    }

    @Override
    public void reset() {
        position = 0;
    }
}