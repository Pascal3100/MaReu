package fr.plopez.mareu.data.model;

import java.util.Calendar;

public class Time {

    private Calendar calendar = Calendar.getInstance();

    public int getCurrentHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    public int getCurrentMin() {
        return calendar.get(Calendar.MINUTE);
    }
}
