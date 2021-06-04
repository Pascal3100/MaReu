package fr.plopez.mareu.view.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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

    // LiveData for text pattern filter
    private final MutableLiveData<String> textFilterPatternMutableLiveData = new MutableLiveData<>();

    // LiveData for filtered rooms
    private final MediatorLiveData<List<MeetingRoomItem>> filteredMeetingRoomItemsMediatorLiveData =
            new MediatorLiveData<>();

    // Meeting hosted data
    private String subject = "";
    private String startTime = "";
    private String meetingRoom = "";
    private final List<String> emailsList = new ArrayList<>();

    public AddMeetingViewModel(MeetingsRepository meetingsRepository,
                               RoomFilterRepository roomFilterRepository) {
        this.meetingsRepository = meetingsRepository;
        this.roomFilterRepository = roomFilterRepository;

        LiveData<List<MeetingRoomItem>> meetingRoomItemLiveData =
                roomFilterRepository.getMeetingRoomItemListLiveData();

        // Add meetings item list to mediatorLiveData
        filteredMeetingRoomItemsMediatorLiveData.addSource(meetingRoomItemLiveData,
                meetingRoomItemList -> combine(meetingRoomItemList, textFilterPatternMutableLiveData.getValue()));

        // Add text filter pattern to mediatorLiveData
        filteredMeetingRoomItemsMediatorLiveData.addSource(textFilterPatternMutableLiveData,
                filterTextPattern -> combine(meetingRoomItemLiveData.getValue(), filterTextPattern));
    }

    // Filter utility for exposed drop down menu
    private void combine(List<MeetingRoomItem> meetingRoomItemList, String filterTextPattern) {

        if (filterTextPattern == null ||
            filterTextPattern.length() == 0) {
            // No filter pattern applied, all the rooms will be available
            filteredMeetingRoomItemsMediatorLiveData.setValue(meetingRoomItemList);
        } else {
            List<MeetingRoomItem> filteredMeetingRoomItemList = new ArrayList<>();
            // Making some formatting operations on the filter pattern
            for (MeetingRoomItem item : meetingRoomItemList) {
                if (item.getRoomName().contains(filterTextPattern)) {
                    filteredMeetingRoomItemList.add(item);
                }
            }
            filteredMeetingRoomItemsMediatorLiveData.setValue(filteredMeetingRoomItemList);
        }
    }

    public LiveData<List<MeetingRoomItem>> getFilteredMeetingRoomItemLiveData() {
        return filteredMeetingRoomItemsMediatorLiveData;
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

    // Emails LiveData getter
    public LiveData<List<String>> getEmailsListLiveData() {
        return emailsListMutableLiveData;
    }

    public void updateTextFilterPattern(String textFilterPattern){
        textFilterPatternMutableLiveData.setValue(textFilterPattern);
    }

    // Empty Strings checker util
    private boolean checkIfStringIsEmpty(String inputString) {
        return inputString == null || inputString.isEmpty();
    }

}
