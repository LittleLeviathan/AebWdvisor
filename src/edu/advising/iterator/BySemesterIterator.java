package edu.advising.iterator;

import edu.advising.commands.Section;

import java.util.*;

public class BySemesterIterator implements ScheduleIterator {

    private List<Section> sections;
    private int position;

    private static final List<String> SEMESTER_ORDER = Arrays.asList(
            "SP", "SU", "FA"
    );

    public BySemesterIterator(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
        this.sections.sort(Comparator
                .comparingInt((Section s) -> {
                    if (s == null) return Integer.MAX_VALUE;
                    return s.getYear();
                })
                .thenComparingInt(s -> {
                    if (s == null || s.getSemester() == null) return Integer.MAX_VALUE;
                    String sem = s.getSemester().toUpperCase();
                    int idx = SEMESTER_ORDER.indexOf(sem);
                    return idx == -1 ? Integer.MAX_VALUE : idx;
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