package fr.plopez.mareu.view;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.RoomsRepository;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.data.model.Room;
import fr.plopez.mareu.utils.SingleLiveEvent;
import fr.plopez.mareu.view.model.MeetingRoomItem;
import fr.plopez.mareu.view.model.MeetingViewState;

import static android.content.ContentValues.TAG;

public class MainActivityViewModel extends ViewModel {

    private final String RESUME_SEPARATOR = " - ";
    private final String EMAIL_SEPARATOR = ", ";

    private final LiveData<List<Meeting>> meetingsLiveData;
    private SingleLiveEvent<String> meetingValidationMessage = new SingleLiveEvent<>();
    private MeetingsRepository meetingsRepository;
    private RoomsRepository roomsRepository;

    public MainActivityViewModel(MeetingsRepository meetingsRepository, RoomsRepository roomsRepository){
        this.meetingsRepository = meetingsRepository;
        this.roomsRepository = roomsRepository;
        meetingsLiveData = meetingsRepository.getMeetings();
    }

    public SingleLiveEvent<String> getMeetingValidationMessage(){
        return meetingValidationMessage;
    }

    public LiveData<List<MeetingViewState>> getMainActivityViewStatesLiveData() {
        return Transformations.map(meetingsLiveData, new Function<List<Meeting>, List<MeetingViewState>>() {
            @Override
            public List<MeetingViewState> apply(List<Meeting> input) {
                return mapMeetingsToListOfMeetingViewState(input);
            }
        });
    }

    private List<MeetingViewState> mapMeetingsToListOfMeetingViewState(List<Meeting> meetings){
        List<MeetingViewState> meetingViewStates = new ArrayList<>();

        for (Meeting meeting : meetings) {
            meetingViewStates.add(new MeetingViewState(getResume(meeting),
                                                       getEmails(meeting),
                                                       meeting.getRoom().getRoomId(),
                                                       meeting));
        }
        Log.d(TAG, "------ mapMeetingsToListOfMeetingViewState: " + meetingViewStates);
        return meetingViewStates;
    }

    // Add a new meeting
    public void addMeeting(@Nullable String subject,
                           @NonNull String time,
                           @Nullable String room,
                           @Nullable List<String> emails,
                           int nbEmails){

        // Check if all the fields are correctly filled
        if (subject.isEmpty()) {
            meetingValidationMessage.setValue("Enter a correct subject");
        } else if (room.isEmpty() || !roomsRepository.getRoomsNames().contains(room)) {
            meetingValidationMessage.setValue("Enter a correct room");
        } else if (nbEmails == 0) {
            meetingValidationMessage.setValue("Enter at least one email");
        } else {
            meetingValidationMessage.setValue(null);
            meetingsRepository.addMeeting(new Meeting(subject, time, roomsRepository.getRoomByName(room), emails));
        }
    }

    // Delete selected meeting
    public void deleteMeeting(@NonNull MeetingViewState meeting){
        meetingsRepository.deleteMeeting(meeting.getMeetingObjectRef());
    }

    // Calculate resume text to display in view holders
    public String getResume(Meeting meeting) {
        String resume = meeting.getSubject() + RESUME_SEPARATOR + meeting.getStartHour();
        return resume;
    }

    // Calculate email text to display in view holders
    public String getEmails(Meeting meeting) {
        String emails = "";
        for (String email:meeting.getParticipantsEmailList()) {
            emails = emails.concat(email + EMAIL_SEPARATOR);
        }
        return emails.substring(0, emails.length() - EMAIL_SEPARATOR.length());
    }

    // Provides rooms names
    public List<String> getRoomsNames() {
        return roomsRepository.getRoomsNames();
    }

    // Provides rooms items list
    public List<MeetingRoomItem> getRoomsItems() {
        ArrayList<MeetingRoomItem> roomsItemsList = new ArrayList<>();

        for (String roomName:roomsRepository.getRoomsNames()){
            roomsItemsList.add(new MeetingRoomItem(roomName, roomsRepository.getRoomByName(roomName).getRoomId()));
        }

        return roomsItemsList;
    }
}
