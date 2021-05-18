package fr.plopez.mareu.view.add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.RoomFilterRepository;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.utils.SingleLiveEvent;
import fr.plopez.mareu.view.model.MeetingRoomItem;

public class AddMeetingViewModel extends ViewModel {

    private final MeetingsRepository meetingsRepository;
    private final RoomFilterRepository roomFilterRepository;

    private final SingleLiveEvent<AddMeetingViewAction> addMeetingViewActionSingleLiveEvent = new SingleLiveEvent<>();

    public AddMeetingViewModel(MeetingsRepository meetingsRepository,
                               RoomFilterRepository roomFilterRepository){
        this.meetingsRepository = meetingsRepository;
        this.roomFilterRepository = roomFilterRepository;
    }

    public SingleLiveEvent<AddMeetingViewAction> getAddMeetingViewActionSingleLiveEvent(){
        return addMeetingViewActionSingleLiveEvent;
    }

    // Add a new meeting
    public void addMeeting(@Nullable String subject,
                           @NonNull String time,
                           @Nullable String room,
                           @Nullable List<String> emails,
                           int nbEmails){

        // Check if all the fields are correctly filled
        if (subject.isEmpty()) {
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.DISPLAY_INCORRECT_SUBJECT_MESSAGE);
        } else if (room.isEmpty() || !roomFilterRepository.getRoomsNames().contains(room)) {
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.DISPLAY_INCORRECT_ROOM_MESSAGE);
        } else if (nbEmails == 0) {
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.DISPLAY_INCORRECT_EMAIL_MESSAGE);
        } else {
            meetingsRepository.addMeeting(new Meeting(meetingsRepository.generateId(), subject, time, roomFilterRepository.getRoomByName(room), emails));
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.FINISH_ACTIVITY);
        }
    }

    // Provides rooms names
    public List<String> getRoomsNames() {
        return roomFilterRepository.getRoomsNames();
    }

    // Provides rooms items list
    public LiveData<List<MeetingRoomItem>> getMeetingRoomItemList(){
        return roomFilterRepository.getMeetingRoomItemListLiveData();
    }
}
