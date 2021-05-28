package fr.plopez.mareu.view.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.RoomFilterRepository;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.utils.SingleLiveEvent;
import fr.plopez.mareu.view.model.MeetingRoomItem;

public class AddMeetingViewModel extends ViewModel {

    private final MeetingsRepository meetingsRepository;
    private final RoomFilterRepository roomFilterRepository;

    // SingleLiveEvent for notification management
    private final SingleLiveEvent<AddMeetingViewAction> addMeetingViewActionSingleLiveEvent = new SingleLiveEvent<>();

    // LiveData for emails management
    private final MutableLiveData<List<String>> emailsListMutableLiveData = new MutableLiveData<>();

    // Meeting hosted data
    private String subject = "";
    private String startTime = "";
    private String meetingRoom = "";
    private final List<String> emailsList = new ArrayList<>();

    public AddMeetingViewModel(MeetingsRepository meetingsRepository,
                               RoomFilterRepository roomFilterRepository) {
        this.meetingsRepository = meetingsRepository;
        this.roomFilterRepository = roomFilterRepository;
    }

    public SingleLiveEvent<AddMeetingViewAction> getAddMeetingViewActionSingleLiveEvent() {
        return addMeetingViewActionSingleLiveEvent;
    }

    // Add a new meeting
    public void addMeeting() {

        // Check if all the fields are correctly filled
        if (checkIfStringIsEmpty(subject)) {
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.DISPLAY_INCORRECT_SUBJECT_MESSAGE);
        } else if (checkIfStringIsEmpty(meetingRoom)) {
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.DISPLAY_INCORRECT_ROOM_MESSAGE);
        } else if (emailsList.size() == 0) {
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.DISPLAY_INCORRECT_EMAIL_MESSAGE);
        } else {
            meetingsRepository.addMeeting(new Meeting(
                    meetingsRepository.generateId(),
                    subject, startTime,
                    roomFilterRepository.getRoomByName(meetingRoom),
                    emailsList));
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.FINISH_ACTIVITY);
        }
    }

    // Add Subject
    public void addSubject(String inputFieldText) {
        if (checkIfStringIsEmpty(inputFieldText)) {
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.DISPLAY_INCORRECT_SUBJECT_MESSAGE);
        } else {
            subject = inputFieldText;
        }
    }

    // Add Room
    public void addRoom(String inputFieldText) {

        if (checkIfStringIsEmpty(inputFieldText) || !roomFilterRepository.getRoomsNames().contains(inputFieldText)) {
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.DISPLAY_INCORRECT_ROOM_MESSAGE);
        } else {
            meetingRoom = inputFieldText;
        }
    }

    // Add start time
    public void addStartTime(String startTime) {
        this.startTime = startTime;
    }

    // Add Email
    public void addEmail(String email) {

        // Email pattern to check
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (checkIfStringIsEmpty(email)) {
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.DISPLAY_EMPTY_EMAIL_MESSAGE);
        } else if (!email.trim().matches(emailPattern)) {
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.DISPLAY_INCORRECT_EMAIL_MESSAGE);
        } else {
            emailsList.add(email);
            emailsListMutableLiveData.setValue(emailsList);
        }
    }

    // Remove Email
    public void removeEmail(String email) {
        emailsList.remove(email);
        emailsListMutableLiveData.setValue(emailsList);
    }


    // Provides rooms items list
    public LiveData<List<MeetingRoomItem>> getMeetingRoomItemList() {
        return roomFilterRepository.getMeetingRoomItemListLiveData();
    }

    // Emails LiveData getter
    public LiveData<List<String>> getEmailsListLiveData() {
        return emailsListMutableLiveData;
    }

    // Empty Strings checker util
    private boolean checkIfStringIsEmpty(String inputString) {
        return inputString == null || inputString.isEmpty() || inputString == "";
    }
}
