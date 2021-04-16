package fr.plopez.mareu.data.model;

public class Room {
    private String name;
    private String avatarUrl;

    // Constructor
    public Room(String name, String avatarUrl) {
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    // getters
    public String getName() {
        return name;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }

}
