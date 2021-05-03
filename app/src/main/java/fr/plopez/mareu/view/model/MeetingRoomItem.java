package fr.plopez.mareu.view.model;

public class MeetingRoomItem {
    private String roomName;
    private int roomImageId;

    public MeetingRoomItem(String roomName, int roomImageId) {
        this.roomName = roomName;
        this.roomImageId = roomImageId;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getRoomImageId() {
        return roomImageId;
    }
}
