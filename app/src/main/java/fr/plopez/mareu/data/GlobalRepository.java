package fr.plopez.mareu.data;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
        }
        return instance;
    }

    //Initialize the rooms repo
    private void setRooms() {
        rooms.put("Bell", new Room("bell", "@drawable/bell.png"));
        rooms.put("Coin", new Room("coin", "@drawable/coin.png"));
        rooms.put("Flower", new Room("flower", "@drawable/flower.png"));
        rooms.put("Leaf", new Room("leaf", "@drawable/leaf.png"));
        rooms.put("Mushroom", new Room("mushroom", "@drawable/mushroom.png"));
        rooms.put("Star", new Room("star", "@drawable/star.png"));
    }

    //Initialize meetings repo
    private void setMeetingsDataSet() {
        meetingsDataSet.add(new Meeting(
                "Mentorat with Nino",
                "12:30",
                "Flower",
                Arrays.asList(new String[] {"pascal.lopez@expleogroup.com", "anthony.delcey.fr@gmail.com"})));
    }

    // Retrieve all rooms names
    public List<String> getRooms() {
        return new ArrayList<String>(rooms.keySet());
    }

    public MutableLiveData<List<Meeting>> getMeetings(){

    }






}
