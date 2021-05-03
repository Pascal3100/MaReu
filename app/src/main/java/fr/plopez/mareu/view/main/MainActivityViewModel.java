package fr.plopez.mareu.view.main;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.view.model.MeetingViewState;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = "MainActivityFragment";

    private static final String RESUME_SEPARATOR = " - ";
    private static final String EMAIL_SEPARATOR = ", ";

    private final MeetingsRepository meetingsRepository;

    private final LiveData<List<Meeting>> meetingsLiveData;

    public MainActivityViewModel(MeetingsRepository meetingsRepository){
        this.meetingsRepository = meetingsRepository;
        meetingsLiveData = meetingsRepository.getMeetings();
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
                                                       meeting.getId()));
        }
        return meetingViewStates;
    }

    // Delete selected meeting
    public void deleteMeeting(@NonNull MeetingViewState meeting){
        meetingsRepository.deleteMeeting(meeting.getMeetingObjectId());
    }

    // Calculate resume text to display in view holders
    public String getResume(Meeting meeting) {
        return meeting.getSubject() + RESUME_SEPARATOR + meeting.getStartHour();
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
