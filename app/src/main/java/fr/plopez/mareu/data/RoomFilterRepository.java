package fr.plopez.mareu.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.data.model.Room;
import fr.plopez.mareu.view.model.MeetingRoomItem;

public class RoomFilterRepository {

    private final RoomsRepository roomsRepositoryInstance;
    private List<MeetingRoomItem> meetingRoomItemsList;
    private final MutableLiveData<List<MeetingRoomItem>> meetingRoomItemListMutableLiveData = new MutableLiveData<>();

    public RoomFilterRepository(RoomsRepository roomsRepositoryInstance) {
        this.roomsRepositoryInstance = roomsRepositoryInstance;
        initRepo();
    }

    private void initRepo() {
        buildRoomItems();
        meetingRoomItemListMutableLiveData.setValue(meetingRoomItemsList);
    }

    // Provides rooms items list
    private void buildRoomItems() {
        meetingRoomItemsList = new ArrayList<>();

        for (String roomName : roomsRepositoryInstance.getRoomsNames()) {
            meetingRoomItemsList.add(new MeetingRoomItem(roomName,
                    roomsRepositoryInstance.getRoomByName(roomName).getRoomId()));
        }
    }

    public LiveData<List<MeetingRoomItem>> getMeetingRoomItemListLiveData() {
        return meetingRoomItemListMutableLiveData;
    }

    public void updateMeetingRoomItemList(List<MeetingRoomItem> modifiedMeetingRoomItemsList) {
        meetingRoomItemsList = modifiedMeetingRoomItemsList;
        meetingRoomItemListMutableLiveData.setValue(meetingRoomItemsList);
    }

    // Get all rooms names
    public List<String> getRoomsNames() {
        return roomsRepositoryInstance.getRoomsNames();
    }

    // Get Room Object
    public Room getRoomByName(String roomName) {
        return roomsRepositoryInstance.getRoomByName(roomName);
    }
}
