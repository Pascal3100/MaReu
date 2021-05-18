package fr.plopez.mareu.view.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.RoomFilterRepository;
import fr.plopez.mareu.data.TimeFilterRepository;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.utils.MeetingsFilterUtil;
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
        filteredMeetingListMediatorLiveData.addSource(meetingsLiveData, meetingsLiveData1 -> {
            combine(meetingsLiveData1, roomFilterLiveData.getValue(), timeFilterLiveData.getValue());
        });

        // Add room filter to mediatorLiveData
        filteredMeetingListMediatorLiveData.addSource(roomFilterLiveData, meetingRoomItemList -> {
            combine(meetingsLiveData.getValue(), meetingRoomItemList, timeFilterLiveData.getValue());
        });

        // Add time filter to mediatorLiveData
        filteredMeetingListMediatorLiveData.addSource(timeFilterLiveData, meetingTimeItemList -> {
            combine(meetingsLiveData.getValue(), roomFilterLiveData.getValue(), meetingTimeItemList);
        });
    }

    private void combine(@Nullable List<Meeting> meetingsList,
                         @Nullable List<MeetingRoomItem> meetingRoomItemList,
                         @Nullable List<MeetingTimeItem> meetingTimeItemList) {

        filteredMeetingListMediatorLiveData.setValue(MeetingsFilterUtil.meetingsFilter(
                meetingsList,
                meetingRoomItemList,
                meetingTimeItemList)
        );

    }

    public LiveData<List<MeetingViewState>> getMainActivityViewStatesLiveData() {
        return Transformations.map(filteredMeetingListMediatorLiveData, this::mapMeetingsToListOfMeetingViewState);
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
}
