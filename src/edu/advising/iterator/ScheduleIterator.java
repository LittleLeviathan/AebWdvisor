package edu.advising.iterator;

import edu.advising.commands.Section;

public interface ScheduleIterator {
    boolean hasNext();
    Section next();
    void reset();
}