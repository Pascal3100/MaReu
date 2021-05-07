package fr.plopez.mareu.view.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.RoomsRepository;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.utils.TimeGen;
import fr.plopez.mareu.view.model.MeetingRoomItem;
import fr.plopez.mareu.view.model.MeetingTimeItem;
import fr.plopez.mareu.view.model.MeetingViewState;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = "MainActivityViewModel";

    private static final String RESUME_SEPARATOR = " - ";
    private static final String EMAIL_SEPARATOR = ", ";

    private final MeetingsRepository meetingsRepository;
    private final RoomsRepository roomsRepository;

    private final LiveData<List<Meeting>> meetingsLiveData;

    private final MutableLiveData<List<MeetingRoomItem>> roomFilterLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MeetingTimeItem>> timeFilterLiveData = new MutableLiveData<>();

    private final MediatorLiveData<List<Meeting>> filteredMeetingListMediatorLiveData = new MediatorLiveData<>();

    public MainActivityViewModel(MeetingsRepository meetingsRepository, RoomsRepository roomsRepository) {
        this.meetingsRepository = meetingsRepository;
        this.roomsRepository = roomsRepository;
        meetingsLiveData = meetingsRepository.getMeetings();

        // Add meeting list to mediatorLiveData
        filteredMeetingListMediatorLiveData.addSource(meetingsLiveData, new Observer<List<Meeting>>() {
            @Override
            public void onChanged(List<Meeting> meetingsLiveData) {
                combine(meetingsLiveData, roomFilterLiveData.getValue(), timeFilterLiveData.getValue());
            }
        });

        // Add room filter to mediatorLiveData
        filteredMeetingListMediatorLiveData.addSource(roomFilterLiveData, new Observer<List<MeetingRoomItem>>() {
            @Override
            public void onChanged(List<MeetingRoomItem> meetingRoomItemList) {
                combine(meetingsLiveData.getValue(), meetingRoomItemList, timeFilterLiveData.getValue());
            }
        });

        // Add time filter to mediatorLiveData
        filteredMeetingListMediatorLiveData.addSource(timeFilterLiveData, new Observer<List<MeetingTimeItem>>() {
            @Override
            public void onChanged(List<MeetingTimeItem> meetingTimeItemList) {
                combine(meetingsLiveData.getValue(), roomFilterLiveData.getValue(), meetingTimeItemList);
            }
        });
    }

    private void combine(List<Meeting> meetingsList,
                         List<MeetingRoomItem> meetingRoomItemList,
                         List<MeetingTimeItem> meetingTimeItemList) {

        List<Meeting> filteredMeetingsList = new ArrayList<>(meetingsList);

        // Exclusion management
        if (meetingsList.size() < 2 || meetingRoomItemList == null || meetingTimeItemList == null) {
            filteredMeetingListMediatorLiveData.setValue(filteredMeetingsList);
            Log.d(TAG, " -------   -------  combine: nop");
            return;
        }

        // Filter by room name
        List<String> roomsFilter = new ArrayList<>();
        for (MeetingRoomItem meetingRoomItem : meetingRoomItemList) {
            if (meetingRoomItem.isChecked()) {
                roomsFilter.add(meetingRoomItem.getRoomName());
            }
        }
        Log.d(TAG, "combine: room filter = " + roomsFilter.toString());
        for (Meeting meeting : filteredMeetingsList) {
            if (!roomsFilter.contains(meeting.getRoom().getName())) {
                filteredMeetingsList.remove(meeting);
            }
        }
        // Filter by time hour
        List<String> timeFilter = new ArrayList<>();
        for (MeetingTimeItem meetingTimeItem : meetingTimeItemList) {
            if (meetingTimeItem.isChecked()) {
                timeFilter.add(meetingTimeItem.getHour());
            }
        }
        for (Meeting meeting : filteredMeetingsList) {
            if (!timeFilter.contains(meeting.getStartHour().split(":")[0])) {
                filteredMeetingsList.remove(meeting);
            }
        }

        filteredMeetingListMediatorLiveData.setValue(filteredMeetingsList);
    }

    public LiveData<List<MeetingViewState>> getMainActivityViewStatesLiveData() {
        return Transformations.map(filteredMeetingListMediatorLiveData, new Function<List<Meeting>, List<MeetingViewState>>() {
            @Override
            public List<MeetingViewState> apply(List<Meeting> input) {
                return mapMeetingsToListOfMeetingViewState(input);
            }
        });
    }

    private List<MeetingViewState> mapMeetingsToListOfMeetingViewState(List<Meeting> meetings) {
        List<MeetingViewState> meetingViewStates = new ArrayList<>();

        for (Meeting meeting : meetings) {
            meetingViewStates.add(new MeetingViewState(getResume(meeting),
                    getEmails(meeting),
                    meeting.getRoom().getRoomId(),
                    meeting.getId()));
        }
        return meetingViewStates;
    }

    // Delete selected meeting
    public void deleteMeeting(@NonNull MeetingViewState meeting) {
        meetingsRepository.deleteMeeting(meeting.getMeetingObjectId());
    }

    // Calculate resume text to display in view holders
    public String getResume(Meeting meeting) {
        return meeting.getSubject() + RESUME_SEPARATOR + meeting.getStartHour();
    }

    // Calculate email text to display in view holders
    public String getEmails(Meeting meeting) {
        String emails = "";
        for (String email : meeting.getParticipantsEmailList()) {
            emails = emails.concat(email + EMAIL_SEPARATOR);
        }
        return emails.substring(0, emails.length() - EMAIL_SEPARATOR.length());
    }

    // Provides rooms items list
    public List<MeetingRoomItem> getRoomsItems() {
        ArrayList<MeetingRoomItem> roomsItemsList = new ArrayList<>();

        for (String roomName : roomsRepository.getRoomsNames()) {
            roomsItemsList.add(new MeetingRoomItem(roomName, roomsRepository.getRoomByName(roomName).getRoomId()));
        }

        return roomsItemsList;
    }

    public void updateRoomFilter(List<MeetingRoomItem> meetingRoomItemList) {
        roomFilterLiveData.setValue(meetingRoomItemList);
    }

    public void updateTimeFilter(List<MeetingTimeItem> meetingTimeItemList) {
        timeFilterLiveData.setValue(meetingTimeItemList);
    }
}
