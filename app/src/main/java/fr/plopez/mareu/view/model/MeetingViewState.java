package fr.plopez.mareu.view.model;

import fr.plopez.mareu.data.model.Meeting;

public class MeetingViewState {

    private final String meetingResume;
    private final String meetingEmails;
    private final int roomDrawableId;
    private final Meeting meetingObjectRef;

    public MeetingViewState(String meetingResume, String meetingEmails, int roomDrawableId, Meeting meetingObjectRef) {
        this.meetingResume = meetingResume;
        this.meetingEmails = meetingEmails;
        this.roomDrawableId = roomDrawableId;
        this.meetingObjectRef = meetingObjectRef;
    }


    public String getMeetingResume() {
        return meetingResume;
    }

    public String getMeetingEmails() {
        return meetingEmails;
    }

    public int getRoomDrawableId() {
        return roomDrawableId;
    }

    public Meeting getMeetingObjectRef() {
        return meetingObjectRef;
    }
}
