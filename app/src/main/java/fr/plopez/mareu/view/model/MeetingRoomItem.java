package fr.plopez.mareu.view.model;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class MeetingRoomItem {
    private final String roomName;
    private final int roomImageId;
    private boolean isChecked = false;

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return roomName;
    }
}
