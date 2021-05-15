package fr.plopez.mareu.view.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import fr.plopez.mareu.data.RoomFilterRepository;
import fr.plopez.mareu.data.RoomsRepository;
import fr.plopez.mareu.data.TimeFilterRepository;
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
    private final RoomFilterRepository roomFilterRepository;
    private final TimeFilterRepository timeFilterRepository;

    private final MediatorLiveData<List<Meeting>> filteredMeetingListMediatorLiveData = new MediatorLiveData<>();

    public MainActivityViewModel(MeetingsRepository meetingsRepository,
                                 RoomFilterRepository roomFilterRepository,
                                 TimeFilterRepository timeFilterRepository) {
        this.meetingsRepository = meetingsRepository;
        this.roomFilterRepository = roomFilterRepository;
        this.timeFilterRepository = timeFilterRepository;

        LiveData<List<Meeting>> meetingsLiveData = meetingsRepository.getMeetings();
        LiveData<List<MeetingRoomItem>> roomFilterLiveData = roomFilterRepository.getMeetingRoomItemListLiveData();
        LiveData<List<MeetingTimeItem>> timeFilterLiveData = timeFilterRepository.getMeetingTimeItemListLiveData();

        // Add meeting list to mediatorLiveData
        filteredMeetingListMediatorLiveData.addSource(meetingsLiveData, new Observer<List<Meeting>>() {
            @Override
            public void onChanged(List<Meeting> meetingsLiveData) {
                Log.d(TAG, "---------- onChanged: meetingsLiveData has changed");
                combine(meetingsLiveData, roomFilterLiveData.getValue(), timeFilterLiveData.getValue());
            }
        });

        // Add room filter to mediatorLiveData
        filteredMeetingListMediatorLiveData.addSource(roomFilterLiveData, new Observer<List<MeetingRoomItem>>() {
            @Override
            public void onChanged(List<MeetingRoomItem> meetingRoomItemList) {
                Log.d(TAG, "---------- onChanged: roomFilterLiveData has changed");
                combine(meetingsLiveData.getValue(), meetingRoomItemList, timeFilterLiveData.getValue());
            }
        });

        // Add time filter to mediatorLiveData
        filteredMeetingListMediatorLiveData.addSource(timeFilterLiveData, new Observer<List<MeetingTimeItem>>() {
            @Override
            public void onChanged(List<MeetingTimeItem> meetingTimeItemList) {
                Log.d(TAG, "---------- onChanged: timeFilterLiveData has changed");
                combine(meetingsLiveData.getValue(), roomFilterLiveData.getValue(), meetingTimeItemList);
            }
        });
    }

    private void combine(@Nullable List<Meeting> meetingsList,
                         @Nullable List<MeetingRoomItem> meetingRoomItemList,
                         @Nullable List<MeetingTimeItem> meetingTimeItemList) {

        List<Meeting> filteredMeetingsList;

        // Exclusion management
        if (meetingsList.size() < 2 || meetingRoomItemList == null || meetingTimeItemList == null) {
            filteredMeetingListMediatorLiveData.setValue(meetingsList);
            return;
        } else {
            filteredMeetingsList = new ArrayList<>(meetingsList);
        }

        List<Meeting> meetingsToNotKeep = new ArrayList<>();

        // Filter by room name
        List<String> roomsFilter = new ArrayList<>();
        for (MeetingRoomItem meetingRoomItem : meetingRoomItemList) {
            if (meetingRoomItem.isChecked()) {
                roomsFilter.add(meetingRoomItem.getRoomName().toLowerCase());
            }
        }
        if (roomsFilter.size() > 0) {
            for (Meeting meeting : filteredMeetingsList) {
                if (!roomsFilter.contains(meeting.getRoom().getName().toLowerCase())) {
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


    public void updateRoomFilter(List<MeetingRoomItem> meetingRoomItemList) {
/*        Log.d(TAG, "--------- updateRoomFilter: --------- ");
        for (MeetingRoomItem m:meetingRoomItemList){
            Log.d(TAG, "        --> "+m.getRoomName()+" -- "+m.isChecked());
        }
*/
        roomFilterRepository.updateMeetingRoomItemList(meetingRoomItemList);
    }

    public void updateTimeFilter(List<MeetingTimeItem> meetingTimeItemList) {
/*
        Log.d(TAG, "--------- updateTimeFilter: --------- ");
        for (MeetingTimeItem m:meetingTimeItemList){
            Log.d(TAG, "        --> "+m.getTime()+" -- "+m.isChecked());
        }
*/
        timeFilterRepository.updateMeetingTimeItemList(meetingTimeItemList);
    }

    public List<MeetingRoomItem> getMeetingRoomItemList() {
        return roomFilterRepository.getMeetingRoomItemListLiveData().getValue();
    }

    public List<MeetingTimeItem> getMeetingTimeItemList() {
        return timeFilterRepository.getMeetingTimeItemListLiveData().getValue();
    }
}
