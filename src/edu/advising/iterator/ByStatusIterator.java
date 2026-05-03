package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import java.util.List;

public class ByStatusIterator implements ScheduleIterator {

    private List<Enrollment> enrollments;
    private String status;
    private int position;

    public ByStatusIterator(List<Enrollment> enrollments, String status) {
        // Filter the enrollments list to only include enrollments matching the given status
        // (e.g. ENROLLED, DROPPED, WITHDRAWN)
        // Store the filtered list and set position to 0
    }

    @Override
    public boolean hasNext() {
        // Return true if position is less than the size of the filtered enrollments list
    }

    @Override
    public Enrollment next() {
        // Get the enrollment at the current position
        // Advance position by one
        // Return the enrollment
    }

    @Override
    public void reset() {
        // Set position back to 0
    }
}