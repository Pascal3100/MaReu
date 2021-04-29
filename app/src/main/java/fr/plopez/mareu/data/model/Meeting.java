package fr.plopez.mareu.data.model;

import android.util.Log;

import java.util.List;

public class Meeting {

    public String subject;
    public String startHour;
    public Room room;
    public List<String> participantsEmailList;

    // Constructor
    public Meeting(String subject, String startHour, Room room, List<String> participantsEmailList) {
        this.subject = subject;
        this.startHour = startHour;
        this.room = room;
        this.participantsEmailList = participantsEmailList;
    }

    //Getters
    public String getSubject() {
        return subject;
    }
    public String getStartHour() {
        return startHour;
    }
    public Room getRoom() {
        return room;
    }
    public List<String> getParticipantsEmailList() {
        return participantsEmailList;
    }
}
