package edu.advising.iterator;

import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;

import java.sql.SQLException;

public class ScheduleGenerator {

    StudentSchedule studentSchedule;

    public ScheduleGenerator(StudentSchedule studentSchedule){
        this.studentSchedule = studentSchedule;
    }

    public String printSchedule() throws SQLException {
        ScheduleIterator semesterIterator = studentSchedule.createBySemesterIterator();
        String schedule;
        schedule = "no information available";

        while (semesterIterator.hasNext()){
            Section s = semesterIterator.next();
            schedule = ("Semester: "+s.getSemester()+" Course: "+s.getCourseCode()+" "+s.getCourseName()+" Status: "+s.getStatus()+"\n"
                    +s.getDeliveryMode()+" "+s.getDayOfWeek()+" "+s.getStartTime() +" Room: "+s.getRoom());
        }
        semesterIterator.reset();
        return schedule;
    }

}
