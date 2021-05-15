package fr.plopez.mareu.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.data.model.Room;
import fr.plopez.mareu.view.model.MeetingRoomItem;

public class RoomFilterRepository {

    private static RoomFilterRepository roomFilterRepositoryInstance;
    private static RoomsRepository roomsRepositoryInstance;
    private static List<MeetingRoomItem> meetingRoomItemsList;
    private static MutableLiveData<List<MeetingRoomItem>> meetingRoomItemListMutableLiveData = new MutableLiveData<>();

    // Singleton
    public static RoomFilterRepository getRoomFilterRepositoryInstance() {
        if (roomFilterRepositoryInstance == null) {
            roomFilterRepositoryInstance = new RoomFilterRepository();
            roomFilterRepositoryInstance.initRepo();
        }
        return roomFilterRepositoryInstance;
    }

    private void initRepo() {
        roomsRepositoryInstance = RoomsRepository.getRoomsRepositoryInstance();
        buildRoomItems();
        Log.d("------ TAG", "initRepo: "+meetingRoomItemsList.toString());
        meetingRoomItemListMutableLiveData.setValue(meetingRoomItemsList);
    }

    // Provides rooms items list
    private void buildRoomItems() {
        meetingRoomItemsList = new ArrayList<>();

        for (String roomName : roomsRepositoryInstance.getRoomsNames()) {
            meetingRoomItemsList.add(new MeetingRoomItem(roomName,
                    roomsRepositoryInstance.getRoomByName(roomName).getRoomId()));
        }

        Log.d("------ TAG", "buildRoomItems: "+meetingRoomItemsList.toString());
    }

    public LiveData<List<MeetingRoomItem>> getMeetingRoomItemListLiveData() {
        return meetingRoomItemListMutableLiveData;
    }

    public void updateMeetingRoomItemList(List<MeetingRoomItem> modifiedMeetingRoomItemsList) {
        meetingRoomItemsList = modifiedMeetingRoomItemsList;
        meetingRoomItemListMutableLiveData.setValue(meetingRoomItemsList);
    }

    public List<String> getRoomsNames() {
        return roomsRepositoryInstance.getRoomsNames();
    }

    // Get Room Object
    public Room getRoomByName(String roomName){
        return roomsRepositoryInstance.getRoomByName(roomName);
    }
}
