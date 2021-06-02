package fr.plopez.mareu.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.plopez.mareu.data.RoomsRepository;
import fr.plopez.mareu.data.model.Meeting;

public class FakeMeetingsGen {

    public static List<Meeting> generateFakeMeetingList(RoomsRepository roomsRepositoryInstance){

        List<Meeting> meetingList = new ArrayList<>();
        int lastGeneratedId = 0;

        meetingList.add(new Meeting(
                lastGeneratedId++,
                "Mentorat with Nino",
                "12:30",
                roomsRepositoryInstance.getRoomByName("Flower"),
                Arrays.asList("pascal.lopez@expleogroup.com", "anthony.delcey.fr@gmail.com")));
        meetingList.add(new Meeting(
                lastGeneratedId++,
                "Code review",
                "9:30",
                roomsRepositoryInstance.getRoomByName("Leaf"),
                Arrays.asList("pascal.lopez@expleogroup.com", "jojoLaFrite@gmail.com", "toto@gmail.com")));
        meetingList.add(new Meeting(
                lastGeneratedId,
                "JCVD Interview",
                "11:00",
                roomsRepositoryInstance.getRoomByName("Mushroom"),
                Arrays.asList("jcvd@gmail.com", "anthony.delcey.fr@gmail.com")));

        return meetingList;
    }

}
