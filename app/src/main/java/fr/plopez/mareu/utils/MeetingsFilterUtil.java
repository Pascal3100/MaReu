package fr.plopez.mareu.utils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.view.model.MeetingRoomItem;
import fr.plopez.mareu.view.model.MeetingTimeItem;

public class MeetingsFilterUtil {

    public static List<Meeting> meetingsFilter(@Nullable List<Meeting> meetingsList,
                                        @Nullable List<MeetingRoomItem> meetingRoomItemList,
                                        @Nullable List<MeetingTimeItem> meetingTimeItemList){
        List<Meeting> filteredMeetingsList;

        // Exclusion management
        assert meetingsList != null;
        if (meetingsList.size() < 2 || meetingRoomItemList == null || meetingTimeItemList == null) {
            return meetingsList;
        } else {
            filteredMeetingsList = new ArrayList<>(meetingsList);
        }

        List<Meeting> meetingsToNotKeep = new ArrayList<>();

        // Filter by room name
        List<String> roomsFilter = new ArrayList<>();
        for (MeetingRoomItem meetingRoomItem : meetingRoomItemList) {
            if (meetingRoomItem.isChecked()) {
                roomsFilter.add(meetingRoomItem.getRoomName());
            }
        }
        if (roomsFilter.size() > 0) {
            for (Meeting meeting : filteredMeetingsList) {
                if (!roomsFilter.contains(meeting.getRoom().getName())) {
                    meetingsToNotKeep.add(meeting);
                }
            }
        }
        // Filter by time hour
        List<String> timeFilter = new ArrayList<>();
        for (MeetingTimeItem meetingTimeItem : meetingTimeItemList) {
            if (meetingTimeItem.isChecked()) {
                timeFilter.add(meetingTimeItem.getHour());
            }
        }
        if (timeFilter.size() > 0) {
            for (Meeting meeting : filteredMeetingsList) {
                if (!timeFilter.contains(meeting.getStartHour().split(":")[0])) {
                    meetingsToNotKeep.add(meeting);
                }
            }
        }
        // Delete all filtered meetings
        for (Meeting meeting : meetingsToNotKeep) {
            filteredMeetingsList.remove(meeting);
        }

        return filteredMeetingsList;
    }
}
