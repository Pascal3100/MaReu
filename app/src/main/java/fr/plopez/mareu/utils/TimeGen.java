package fr.plopez.mareu.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.Throws;

public class TimeGen {

    public static List<String> getAvailableTimes(@NonNull int startTime, @NonNull int endTime) {

        List<String> availableTimes = new ArrayList<>();

        for (int i = startTime; i < endTime; i++) {
            availableTimes.add(i+":00");
        }

        return availableTimes;
    }
}
