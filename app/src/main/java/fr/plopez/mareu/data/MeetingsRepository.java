package fr.plopez.mareu.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.plopez.mareu.data.model.Meeting;

public class MeetingsRepository {

    private static MeetingsRepository MeetingsRepositoryInstance;
    private static RoomsRepository roomsRepositoryInstance;

    private final MutableLiveData<List<Meeting>> meetingListMutableLiveData = new MutableLiveData<>();
    private final List<Meeting> meetingList = new ArrayList<>();

    private int lastGeneratedId = 0;

    // Singleton
    public static MeetingsRepository getMeetingsRepositoryInstance() {
        if (MeetingsRepositoryInstance == null) {
            MeetingsRepositoryInstance = new MeetingsRepository();
            MeetingsRepositoryInstance.initRepo();
        }
        return MeetingsRepositoryInstance;
    }

    private void updateMeetingListMutableLiveData() {
        meetingListMutableLiveData.setValue(meetingList);
    }

    //Initialize meetings repo
    private void setInitialMeetings() {
        meetingList.add(new Meeting(
            // TODO commenter gérer un id auto incrémental ?,
            0,
            "Mentorat with Nino",
            "12:30",
            roomsRepositoryInstance.getRoomByName("Flower"),
            Arrays.asList(new String[]{"pascal.lopez@expleogroup.com", "anthony.delcey.fr@gmail.com"})));
        meetingList.add(new Meeting(
            1,
            "Code review",
            "9:30",
            roomsRepositoryInstance.getRoomByName("Leaf"),
            Arrays.asList(new String[]{"pascal.lopez@expleogroup.com", "jojoLaFrite@gmail.com", "toto@gmail.com"})));
        meetingList.add(new Meeting(
            2,
            "JCVD Interview",
            "11:00",
            roomsRepositoryInstance.getRoomByName("Mushroom"),
            Arrays.asList(new String[]{"jcvd@gmail.com", "anthony.delcey.fr@gmail.com"})));

        lastGeneratedId += 3;

        updateMeetingListMutableLiveData();
    }

    public LiveData<List<Meeting>> getMeetings() {
        return meetingListMutableLiveData;
    }

    private void initRepo() {
        roomsRepositoryInstance = RoomsRepository.getRoomsRepositoryInstance();

        //Dummy meetings generation for dev purpose only
        setInitialMeetings();
    }

    // Add a new meeting
    public void addMeeting(@NonNull Meeting meeting) {
        meetingList.add(meeting);
        updateMeetingListMutableLiveData();
    }

    // delete selected meeting
    // TODO use meeting id instead
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
