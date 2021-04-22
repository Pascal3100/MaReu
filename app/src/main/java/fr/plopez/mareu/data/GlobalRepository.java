package fr.plopez.mareu.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import fr.plopez.mareu.R;
import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.data.model.Room;

public class GlobalRepository {

    private static GlobalRepository instance;
    private HashMap<String,Room> rooms = new HashMap<String,Room>();
    private ArrayList<Meeting> meetingsDataSet = new ArrayList<>();

    // Singleton
    public static GlobalRepository getInstance() {
        if (instance == null) {
            instance = new GlobalRepository();
            instance.initRepo();
        }
        return instance;
    }

    //Initialize the rooms repo
    private void setRooms() {
        rooms.put("Bell", new Room("bell", R.drawable.ic_room_bell));
        rooms.put("Coin", new Room("coin", R.drawable.ic_room_coin));
        rooms.put("Flower", new Room("flower", R.drawable.ic_room_flower));
        rooms.put("Leaf", new Room("leaf", R.drawable.ic_room_leaf));
        rooms.put("Mushroom", new Room("mushroom", R.drawable.ic_room_mushroom));
        rooms.put("Star", new Room("star", R.drawable.ic_room_star));
    }

    //Initialize meetings repo
    private void setMeetingsDataSet() {
        meetingsDataSet.add(new Meeting(
                "Mentorat with Nino",
                "12:30",
                getRoom("Flower"),
                Arrays.asList(new String[] {"pascal.lopez@expleogroup.com", "anthony.delcey.fr@gmail.com"})));
        meetingsDataSet.add(new Meeting(
                "Code review",
                "9:30",
                getRoom("Leaf"),
                Arrays.asList(new String[] {"pascal.lopez@expleogroup.com", "jojoLaFrite@gmail.com", "toto@gmail.com"})));
        meetingsDataSet.add(new Meeting(
                "JCVD Interview",
                "11:00",
                getRoom("Mushroom"),
                Arrays.asList(new String[] {"jcvd@gmail.com", "anthony.delcey.fr@gmail.com"})));
    }

    // Retrieve all rooms names
    public List<String> getRooms() {
        return new ArrayList<String>(rooms.keySet());
    }

    // Get Room Object
    public Room getRoom(String roomName){
        if (rooms.containsKey(roomName)){
            return rooms.get(roomName);
        }
        return null;
    }

    public MutableLiveData<List<Meeting>> getMeetings(){
        MutableLiveData<List<Meeting>> data = new MutableLiveData<>();
        data.setValue(meetingsDataSet);
        return data;
    }

    private void initRepo(){
        setRooms();
        //Dummy meetings generation for dev purpose only
        setMeetingsDataSet();
    }

    // Add a new meeting
    public void addMeeting(@NonNull Meeting meeting){
        meetingsDataSet.add(meeting);
    }

    // delete selected meeting
    public void deleteMeeting(@NonNull Meeting meeting){
        meetingsDataSet.remove(meeting);
    }
}
