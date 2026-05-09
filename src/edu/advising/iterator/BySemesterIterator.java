package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BySemesterIterator implements ScheduleIterator {

    private List<Enrollment> enrollments;
    private int position;

    private static final List<String> SEMESTER_ORDER = Arrays.asList(
            "SP", "SU", "FA"
    );

    public BySemesterIterator(List<Enrollment> enrollments) {
        this.enrollments = new ArrayList<>(enrollments);
        this.enrollments.sort(Comparator
                .comparingInt((Enrollment e) -> {
                    try {
                        Section s = e.getSection();
                        if (s == null) return Integer.MAX_VALUE;
                        return s.getYear();
                    } catch (SQLException ex) {
                        return Integer.MAX_VALUE;
                    }
                })
                .thenComparingInt(e -> {
                    try {
                        Section s = e.getSection();
                        if (s == null || s.getSemester() == null) return Integer.MAX_VALUE;
                        String sem = s.getSemester().toUpperCase();
                        int idx = SEMESTER_ORDER.indexOf(sem);
                        return idx == -1 ? Integer.MAX_VALUE : idx;
                    } catch (SQLException ex) {
                        return Integer.MAX_VALUE;
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