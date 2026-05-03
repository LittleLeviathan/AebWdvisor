package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import java.util.List;

public class BySemesterIterator implements ScheduleIterator {

    private List<Enrollment> enrollments;
    private int position;

    public BySemesterIterator(List<Enrollment> enrollments) {
        // Sort the enrollments by semester and year chronologically
        // Store the sorted list and set position to 0
    }

    @Override
    public boolean hasNext() {
        // Return true if position is less than the size of the enrollments list
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