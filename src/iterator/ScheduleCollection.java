package iterator;

public interface ScheduleCollection {

    public ScheduleIterator createWeeklyIterator();
    public ScheduleIterator createBySemesterIterator();
    public ScheduleIterator createByDeliveryModeIterator(String mode);
    public ScheduleIterator createByStatusIterator(String status);
}
