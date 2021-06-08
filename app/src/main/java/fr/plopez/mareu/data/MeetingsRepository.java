package fr.plopez.mareu.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.utils.FakeMeetingsGen;

public class MeetingsRepository {

    private final MutableLiveData<List<Meeting>> meetingListMutableLiveData = new MutableLiveData<>();
    private List<Meeting> meetingList = new ArrayList<>();
    private final RoomsRepository roomsRepositoryInstance;


    private int lastGeneratedId = 0;

    public MeetingsRepository(RoomsRepository roomsRepositoryInstance) {
        this.roomsRepositoryInstance = roomsRepositoryInstance;
        initRepo();
    }

    private void updateMeetingListMutableLiveData() {
        meetingListMutableLiveData.setValue(meetingList);
    }


    public LiveData<List<Meeting>> getMeetings() {
        return meetingListMutableLiveData;
    }

    private void initRepo() {
        lastGeneratedId += meetingList.size();
        updateMeetingListMutableLiveData();
    }

    // Add a new meeting
    public void addMeeting(@NonNull Meeting meeting) {
        meetingList.add(meeting);
        updateMeetingListMutableLiveData();
    }

    // delete selected meeting
    public void deleteMeeting(int meetingId) {
        Meeting meetingToDelete = null;

        for (Meeting meeting : meetingList) {
            if (meeting.getId() == meetingId) {
                meetingToDelete = meeting;
                break;
            }
        }

        meetingList.remove(meetingToDelete);
        updateMeetingListMutableLiveData();
    }

    public int generateId() {
        return lastGeneratedId++;
    }
}
