package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ByDeliveryModeIterator implements ScheduleIterator {

    private List<Enrollment> enrollments;
    private String mode;
    private int position;

    public ByDeliveryModeIterator(List<Enrollment> enrollments, String mode) {
        this.mode = mode;
        this.enrollments = new ArrayList<>();
        for (Enrollment e : enrollments) {
            try {
                Section s = e.getSection();
                if (s != null && mode != null
                        && mode.equalsIgnoreCase(s.getDeliveryMode())) {
                    this.enrollments.add(e);
                }
            } catch (SQLException ex) {
                // skip enrollments whose section cannot be loaded
            }
        }
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