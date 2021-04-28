package fr.plopez.mareu.data.model;

import android.util.Log;

import java.util.List;

public class Meeting {

    public String subject;
    public String startHour;
    public Room room;
    public List<String> participantsEmailList;
    private final String RESUME_SEPARATOR = " - ";
    private final String EMAIL_SEPARATOR = ", ";

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

    public String getResume() {
        String resume = subject + RESUME_SEPARATOR + startHour;
        return resume;
    }

    public String getEmails() {
        String emails = "";
        for (String email:participantsEmailList) {
            emails = emails.concat(email + EMAIL_SEPARATOR);
        }

        return emails.substring(0, emails.length() - EMAIL_SEPARATOR.length());
    }
}
