package iterator;

import edu.advising.commands.Enrollment;

import java.util.List;

public class BySemesterIterator implements ScheduleIterator{

    private List<Enrollment> enrollments;
    private int currentIndex;

    public BySemesterIterator(List<Enrollment> enrollments){
        this.enrollments = enrollments;
    }

    @Override
    public boolean hasNext() {
        if (currentIndex >= enrollments.size()) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public Enrollment next() {
        Enrollment enrollment = enrollments.get(currentIndex);
        currentIndex += 1;
        return enrollment;
    }

    @Override
    public void reset() {

    }
}
