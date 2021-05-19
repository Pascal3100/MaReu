package fr.plopez.mareu.view.main.filter_fragment_dialog;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.plopez.mareu.R;
import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.RoomFilterRepository;
import fr.plopez.mareu.data.TimeFilterRepository;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.utils.MeetingsFilterUtil;
import fr.plopez.mareu.view.model.MeetingRoomItem;
import fr.plopez.mareu.view.model.MeetingTimeItem;


public class MainActivityFilterDialogFragmentViewModel extends ViewModel {

    private static final String TAG = "MainActivityFilterDialogFragmentViewModel";

    private final MeetingsRepository meetingsRepository;
    private final RoomFilterRepository roomFilterRepository;
    private final TimeFilterRepository timeFilterRepository;
    private final Application app;

    private final MediatorLiveData<String> numberOfFilteredMeetingMediatorLiveData = new MediatorLiveData<>();

    public MainActivityFilterDialogFragmentViewModel(MeetingsRepository meetingsRepository,
                                                     RoomFilterRepository roomFilterRepository,
                                                     TimeFilterRepository timeFilterRepository,
                                                     Application app) {
        this.meetingsRepository = meetingsRepository;
        this.roomFilterRepository = roomFilterRepository;
        this.timeFilterRepository = timeFilterRepository;
        this.app = app;

        LiveData<List<Meeting>> meetingsLiveData = meetingsRepository.getMeetings();
        LiveData<List<MeetingRoomItem>> roomFilterLiveData = roomFilterRepository.getMeetingRoomItemListLiveData();
        LiveData<List<MeetingTimeItem>> timeFilterLiveData = timeFilterRepository.getMeetingTimeItemListLiveData();

        // Add meeting list to mediatorLiveData
        numberOfFilteredMeetingMediatorLiveData.addSource(meetingsLiveData, meetingsLiveData1 -> {
            combine(meetingsLiveData1, roomFilterLiveData.getValue(), timeFilterLiveData.getValue());
        });

        // Add room filter to mediatorLiveData
        numberOfFilteredMeetingMediatorLiveData.addSource(roomFilterLiveData, meetingRoomItemList -> {
            combine(meetingsLiveData.getValue(), meetingRoomItemList, timeFilterLiveData.getValue());
        });

        // Add time filter to mediatorLiveData
        numberOfFilteredMeetingMediatorLiveData.addSource(timeFilterLiveData, meetingTimeItemList -> {
            combine(meetingsLiveData.getValue(), roomFilterLiveData.getValue(), meetingTimeItemList);
        });
    }

    private void combine(@Nullable List<Meeting> meetingsList,
                         @Nullable List<MeetingRoomItem> meetingRoomItemList,
                         @Nullable List<MeetingTimeItem> meetingTimeItemList) {

        numberOfFilteredMeetingMediatorLiveData.setValue(mapNumberOfFilteredMeetingToString(MeetingsFilterUtil.meetingsFilter(
                meetingsList,
                meetingRoomItemList,
                meetingTimeItemList).size())
        );
    }

    public LiveData<String> getFilterDialogDynamicTextLiveData() {
        return numberOfFilteredMeetingMediatorLiveData;
    }

    private String mapNumberOfFilteredMeetingToString(Integer numberOfFilteredMeeting) {
        String toConcat = "";

        if (numberOfFilteredMeeting > 1) {
            toConcat = app.getString(R.string.filter_text_message_singular);
        } else {
            toConcat = app.getString(R.string.filter_text_message_plural);
        }
        return numberOfFilteredMeeting.toString() + " " + toConcat;
    }

    public void updateRoomFilter(List<MeetingRoomItem> meetingRoomItemList) {
        roomFilterRepository.updateMeetingRoomItemList(meetingRoomItemList);
    }

    public void updateTimeFilter(List<MeetingTimeItem> meetingTimeItemList) {
        timeFilterRepository.updateMeetingTimeItemList(meetingTimeItemList);
    }

    public LiveData<List<MeetingRoomItem>> getMeetingRoomItemList() {
        return roomFilterRepository.getMeetingRoomItemListLiveData();
    }

    public LiveData<List<MeetingTimeItem>> getMeetingTimeItemList() {
        return timeFilterRepository.getMeetingTimeItemListLiveData();
    }
}
