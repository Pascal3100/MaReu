package fr.plopez.mareu.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fr.plopez.mareu.utils.TimeGen;
import fr.plopez.mareu.view.model.MeetingTimeItem;

public class TimeFilterRepository {
    private static List<MeetingTimeItem> meetingTimeItemsList;
    private static final MutableLiveData<List<MeetingTimeItem>> meetingTimeItemListMutableLiveData = new MutableLiveData<>();

    public TimeFilterRepository() {
        initRepo();
    }

    private void initRepo() {
        meetingTimeItemsList = TimeGen.getAvailableTimes(8, 18);
        meetingTimeItemListMutableLiveData.setValue(meetingTimeItemsList);
    }

    public LiveData<List<MeetingTimeItem>> getMeetingTimeItemListLiveData() {
        return meetingTimeItemListMutableLiveData;
    }

    public void updateMeetingTimeItemList(List<MeetingTimeItem> modifiedMeetingTimeItemsList) {
        meetingTimeItemsList = modifiedMeetingTimeItemsList;
        meetingTimeItemListMutableLiveData.setValue(meetingTimeItemsList);
    }
}

