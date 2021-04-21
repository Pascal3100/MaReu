package fr.plopez.mareu.data.model;

public class Room {
    private String name;
    private int roomId;

    // Constructor
    public Room(String name, int roomId) {
        this.name = name;
        this.roomId = roomId;
    }

    // getters
    public String getName() {
        return name;
    }
    public int getRoomId() {
        return roomId;
    }

}
