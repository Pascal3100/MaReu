package fr.plopez.mareu.view.add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.data.MeetingsRepository;
import fr.plopez.mareu.data.RoomsRepository;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.utils.SingleLiveEvent;
import fr.plopez.mareu.view.model.MeetingRoomItem;

public class AddMeetingViewModel extends ViewModel {

    private final MeetingsRepository meetingsRepository;
    private final RoomsRepository roomsRepository;

    private final SingleLiveEvent<AddMeetingViewAction> addMeetingViewActionSingleLiveEvent = new SingleLiveEvent<>();

    public AddMeetingViewModel(MeetingsRepository meetingsRepository, RoomsRepository roomsRepository){
        this.meetingsRepository = meetingsRepository;
        this.roomsRepository = roomsRepository;
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
        } else if (room.isEmpty() || !roomsRepository.getRoomsNames().contains(room)) {
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.DISPLAY_INCORRECT_ROOM_MESSAGE);
        } else if (nbEmails == 0) {
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.DISPLAY_INCORRECT_EMAIL_MESSAGE);
        } else {
            meetingsRepository.addMeeting(new Meeting(meetingsRepository.generateId(), subject, time, roomsRepository.getRoomByName(room), emails));
            addMeetingViewActionSingleLiveEvent.setValue(AddMeetingViewAction.FINISH_ACTIVITY);
        }
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
