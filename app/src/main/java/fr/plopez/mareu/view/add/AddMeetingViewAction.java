package fr.plopez.mareu.view.add;

import androidx.annotation.Nullable;

public enum AddMeetingViewAction {
    FINISH_ACTIVITY(null),
    DISPLAY_INCORRECT_SUBJECT_MESSAGE("Enter a correct subject"),
    DISPLAY_INCORRECT_ROOM_MESSAGE("Enter a correct room"),
    DISPLAY_INCORRECT_EMAIL_MESSAGE("Email is not valid"),
    DISPLAY_UNKNOWN_ROOM_MESSAGE("Select an existing room"),
    DISPLAY_EMPTY_SUBJECT_MESSAGE("Subject can't be empty"),
    DISPLAY_EMPTY_EMAIL_MESSAGE("Enter at least one email");
    
    @Nullable
    private final String mMessage;

    AddMeetingViewAction(@Nullable String message) {
        mMessage = message;
    }

    @Nullable
    public String getMessage() {
        return mMessage;
    }
}
