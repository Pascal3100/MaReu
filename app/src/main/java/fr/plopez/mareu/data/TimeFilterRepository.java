package fr.plopez.mareu.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fr.plopez.mareu.utils.TimeGen;
import fr.plopez.mareu.view.model.MeetingTimeItem;

public class TimeFilterRepository {
    private static TimeFilterRepository timeFilterRepositoryInstance;
    private static List<MeetingTimeItem> meetingTimeItemsList;
    private static MutableLiveData<List<MeetingTimeItem>> meetingTimeItemListMutableLiveData = new MutableLiveData<>();

    // Singleton
    public static TimeFilterRepository getTimeFilterRepositoryInstance() {
        if (timeFilterRepositoryInstance == null) {
            timeFilterRepositoryInstance = new TimeFilterRepository();
            timeFilterRepositoryInstance.initRepo();
        }
        return timeFilterRepositoryInstance;
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

