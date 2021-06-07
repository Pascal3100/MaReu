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
                "Scrum meeting",
                "12:30",
                roomsRepositoryInstance.getRoomByName("Flower"),
                Arrays.asList("anonyme1@expleogroup.com", "anonyme2@expleogroup.com", "anonyme3@expleogroup.com")));
        meetingList.add(new Meeting(
                lastGeneratedId++,
                "Code review",
                "09:30",
                roomsRepositoryInstance.getRoomByName("Leaf"),
                Arrays.asList("some.one@expleogroup.com", "another.one@expleogroup.com")));
        meetingList.add(new Meeting(
                lastGeneratedId,
                "JCVD Interview",
                "11:00",
                roomsRepositoryInstance.getRoomByName("Mushroom"),
                Arrays.asList("jcvd@gmail.com", "xor@gmail.com", "candy@gmail.com")));

        return meetingList;
    }

}
