package fr.plopez.mareu.data.model;

import java.util.List;

public class Meeting {

    private final int id;
    private final String subject;
    private final String startHour;
    private final Room room;
    private final List<String> participantsEmailList;

    // Constructor
    public Meeting(int id, String subject, String startHour, Room room, List<String> participantsEmailList) {
        this.id = id;
        this.subject = subject;
        this.startHour = startHour;
        this.room = room;
        this.participantsEmailList = participantsEmailList;
    }

    //Getters

    public int getId() { return id; }
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
