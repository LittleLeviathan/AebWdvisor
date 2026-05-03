package edu.advising.iterator;

public interface ScheduleCollection {
    ScheduleIterator createWeeklyIterator();
    ScheduleIterator createBySemesterIterator();
    ScheduleIterator createByDeliveryModeIterator(String mode);
    ScheduleIterator createByStatusIterator(String status);
}