package fr.plopez.mareu.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.view.model.MeetingTimeItem;
import kotlin.jvm.Throws;

public class TimeGen {

    public static List<MeetingTimeItem> getAvailableTimes(int startTime, int endTime) {

        List<MeetingTimeItem> availableTimes = new ArrayList<>();

        for (int i = startTime; i < endTime+1; i++) {
            availableTimes.add(new MeetingTimeItem(i+":00"));
        }

        return availableTimes;
    }
}
