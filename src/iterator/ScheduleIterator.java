package iterator;

import edu.advising.commands.Enrollment;

public interface ScheduleIterator {

    public boolean hasNext();

    public Enrollment next();
    public void reset();
}
