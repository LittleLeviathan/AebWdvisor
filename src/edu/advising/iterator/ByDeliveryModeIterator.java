package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import java.util.List;

public class ByDeliveryModeIterator implements ScheduleIterator {

    private List<Enrollment> enrollments;
    private String mode;
    private int position;

    public ByDeliveryModeIterator(List<Enrollment> enrollments, String mode) {
        // Filter the enrollments list to only include sections matching the given mode
        // (e.g. ONLINE, IN_PERSON, HYBRID)
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