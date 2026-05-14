package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ByDeliveryModeIterator implements ScheduleIterator {

    private List<Section> sections;
    private String mode;
    private int position;

    public ByDeliveryModeIterator(List<Section> sections, String mode) {
        this.mode = mode;
        this.sections = new ArrayList<>();
        for (Section s : sections) {
            if (s != null && mode != null
                    && mode.equalsIgnoreCase(s.getDeliveryMode())) {
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