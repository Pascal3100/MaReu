package fr.plopez.mareu.data;

import java.util.Arrays;
import java.util.List;

import fr.plopez.mareu.data.model.Room;

public class RoomsPool {

    public static final List<Room> ROOMS_OBJECTS = Arrays.asList(
        new Room("bell", "@drawable/bell.png"),
        new Room("coin", "@drawable/coin.png"),
        new Room("flower", "@drawable/flower.png"),
        new Room("leaf", "@drawable/leaf.png"),
        new Room("mushroom", "@drawable/mushroom.png"),
        new Room("star", "@drawable/star.png"));
    public static final List<String> ROOMS_NAMES = Arrays.asList(

    );
}
