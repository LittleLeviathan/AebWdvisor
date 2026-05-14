package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;

import java.util.ArrayList;
import java.util.List;

public class ByStatusIterator implements ScheduleIterator {

    private List<Section> sections;
    private String status;
    private int position;

    public ByStatusIterator(List<Section> enrollments, String status) {
        this.status = status;
        this.sections = new ArrayList<>();
        for (Section s : sections) {
            if (s.getStatus() != null && status != null
                    && status.equalsIgnoreCase(s.getStatus())) {
                this.sections.add(s);
            }
        }
        this.position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < sections.size();
    }

    @Override
    public Section next() {
        Section s = sections.get(position);
        position++;
        return s;
    }

    @Override
    public void reset() {
        position = 0;
    }
}