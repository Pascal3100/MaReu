package fr.plopez.mareu.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.plopez.mareu.R;
import fr.plopez.mareu.data.model.Room;

public class RoomsRepository {

    private final HashMap<String, Room> rooms = new HashMap<>();

    //Initialize the rooms repo
    public RoomsRepository() {

        // Simulate external Query for getting available meeting rooms
        populateRoomsRepository();
    }
    //Initialize the rooms repo
    private void populateRoomsRepository() {
        rooms.put("Bell", new Room("Bell", R.drawable.ic_room_bell));
        rooms.put("Coin", new Room("Coin", R.drawable.ic_room_coin));
        rooms.put("Flower", new Room("Flower", R.drawable.ic_room_flower));
        rooms.put("Leaf", new Room("Leaf", R.drawable.ic_room_leaf));
        rooms.put("Mushroom", new Room("Mushroom", R.drawable.ic_room_mushroom));
        rooms.put("Star", new Room("Star", R.drawable.ic_room_star));
    }

    // Retrieve all rooms names
    public List<String> getRoomsNames() {
        return new ArrayList<>(rooms.keySet());
    }

    // Get Room Object
    public Room getRoomByName(String roomName){
        if (rooms.containsKey(roomName)){
            return rooms.get(roomName);
        }
        return null;
    }
}
