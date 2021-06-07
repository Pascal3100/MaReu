package fr.plopez.mareu.utils;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.view.model.MeetingTimeItem;

public class TimeGen {

    public static List<MeetingTimeItem> getAvailableTimes(int startTime, int endTime) {

        List<MeetingTimeItem> availableTimes = new ArrayList<>();

        String time = "";

        for (int i = startTime; i < endTime+1; i++) {

            if (i < 10) {
                time = "0"+i+":00";
            } else {
                time = i+":00";
            }
            availableTimes.add(new MeetingTimeItem(time));
        }

        return availableTimes;
    }
}
