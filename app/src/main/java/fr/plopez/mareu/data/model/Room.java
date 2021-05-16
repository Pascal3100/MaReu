package fr.plopez.mareu.data.model;

public class Room {
    private final String name;
    private final int roomId;

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
