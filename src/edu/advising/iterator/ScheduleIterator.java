package edu.advising.iterator;

import edu.advising.commands.Enrollment;

public interface ScheduleIterator {
    boolean hasNext();
    Enrollment next();
    void reset();
}