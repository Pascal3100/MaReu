package fr.plopez.mareu.view;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.data.GlobalRepository;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.data.model.Room;
import fr.plopez.mareu.data.model.Time;
import fr.plopez.mareu.view.model.MeetingViewState;

import static android.content.ContentValues.TAG;

public class MainActivityViewModel extends ViewModel {

    private final String RESUME_SEPARATOR = " - ";
    private final String EMAIL_SEPARATOR = ", ";

    private MutableLiveData<List<Meeting>> meetings;
    private GlobalRepository globalRepository;

    public MainActivityViewModel(GlobalRepository globalRepository){
        this.globalRepository = globalRepository;
        meetings = globalRepository.getMeetings();
    }

    public LiveData<List<MeetingViewState>> getMainActivityViewStatesLiveData() {
        return Transformations.map(meetings, new Function<List<Meeting>, List<MeetingViewState>>() {
            @Override
            public List<MeetingViewState> apply(List<Meeting> input) {
                return mapMeetingsToListOfMeetingViewState(meetings.getValue());
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

    private void updateLiveData(){
        Log.d(TAG, "------ updateLiveData: before setValue: " + meetings.getValue());
        meetings.setValue(globalRepository.getMeetings().getValue());
        Log.d(TAG, "------ updateLiveData: after setValue: " + meetings.getValue());
    }

    // Add a new meeting
    public void addMeeting(@Nullable String subject,
                           @NonNull String time,
                           @Nullable String room,
                           @Nullable List<String> emails,
                           @NonNull int nbEmails){

        //Check if all the fields are correctly filled
        if (subject.isEmpty()) {
            // CustomToasts.showErrorToast(getContext(), "Enter a correct subject");
            return;
        } else if (room.isEmpty() || !getRoomsNames().contains(room)) {
            // CustomToasts.showErrorToast(getContext(), "Enter a correct room");
            return;
        } else if (nbEmails == 0) {
            // CustomToasts.showErrorToast(getContext(), "Enter at least one email");
            return;
        }

        Log.d(TAG, "------ addMeeting: before adding: " + globalRepository.getMeetings().getValue());
        globalRepository.addMeeting(new Meeting(subject, time, getRoomByName(room), emails));
        Log.d(TAG, "------ addMeeting: after adding: " + globalRepository.getMeetings().getValue());

        updateLiveData();
    }

    // Delete selected meeting
    public void deleteMeeting(@NonNull MeetingViewState meeting){
        globalRepository.deleteMeeting(meeting.getMeetingObjectRef());
        updateLiveData();
    }

    // Retrieve Rooms names
    public List<String> getRoomsNames() {
        return globalRepository.getRooms();
    }

    // Retrieve room object by its name
    public Room getRoomByName(String roomName) {
        return globalRepository.getRoom(roomName);
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
}
