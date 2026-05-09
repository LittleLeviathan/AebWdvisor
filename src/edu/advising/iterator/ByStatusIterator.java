package edu.advising.iterator;

import edu.advising.commands.Enrollment;

import java.util.ArrayList;
import java.util.List;

public class ByStatusIterator implements ScheduleIterator {

    private List<Enrollment> enrollments;
    private String status;
    private int position;

    public ByStatusIterator(List<Enrollment> enrollments, String status) {
        this.status = status;
        this.enrollments = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getStatus() != null && status != null
                    && status.equalsIgnoreCase(e.getStatus())) {
                this.enrollments.add(e);
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