package fr.plopez.mareu.view.main;

import java.util.List;

import fr.plopez.mareu.view.model.MeetingRoomItem;
import fr.plopez.mareu.view.model.MeetingTimeItem;

public interface OnModifyFilters {
    void onRoomFilterModify(List<MeetingRoomItem> meetingRoomItemList);
    void onTimeFilterModify(List<MeetingTimeItem> meetingTimeItemList);
}
