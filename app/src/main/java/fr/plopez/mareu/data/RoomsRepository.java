package fr.plopez.mareu.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.plopez.mareu.R;
import fr.plopez.mareu.data.model.Room;

public class RoomsRepository {

    private static RoomsRepository instance;
    private final HashMap<String, Room> rooms = new HashMap<>();

    // Singleton
    public static RoomsRepository getRoomsRepositoryInstance() {
        if (instance == null) {
            instance = new RoomsRepository();
        }
        return instance;
    }

    //Initialize the rooms repo
    public RoomsRepository() {

        // Simulate external Query for getting available meeting rooms
        populateRoomsRepository();
    }
    //Initialize the rooms repo
    private void populateRoomsRepository() {
        rooms.put("Bell", new Room("bell", R.drawable.ic_room_bell));
        rooms.put("Coin", new Room("coin", R.drawable.ic_room_coin));
        rooms.put("Flower", new Room("flower", R.drawable.ic_room_flower));
        rooms.put("Leaf", new Room("leaf", R.drawable.ic_room_leaf));
        rooms.put("Mushroom", new Room("mushroom", R.drawable.ic_room_mushroom));
        rooms.put("Star", new Room("star", R.drawable.ic_room_star));
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
