package fr.plopez.mareu.view.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import fr.plopez.mareu.R;
import fr.plopez.mareu.view.model.MeetingViewState;

public class MainActivityFragmentRecyclerViewViewHolder extends RecyclerView.ViewHolder {

    public final ImageView roomIcon;
    public final TextView meetingResume;
    public final TextView meetingEmails;
    public final AppCompatImageButton trashIcon;
    private MeetingViewState meeting;
    private final DeleteMeetingListener deleteMeetingListener;

    public MainActivityFragmentRecyclerViewViewHolder(@NonNull View itemView, DeleteMeetingListener deleteMeetingListener) {
        super(itemView);
        this.deleteMeetingListener = deleteMeetingListener;
        //
        roomIcon = itemView.findViewById(R.id.fragment_main_activity_view_holder_room_icon);
        meetingResume = itemView.findViewById(R.id.fragment_main_activity_view_holder_meeting_resume_text);
        meetingEmails = itemView.findViewById(R.id.fragment_main_activity_view_holder_meeting_emails_text);
        trashIcon = itemView.findViewById(R.id.fragment_main_activity_view_holder_delete_button);

        setOnTrashClickAction();
    }

    private void setOnTrashClickAction(){
        trashIcon.setOnClickListener(v -> deleteMeetingListener.onClickDeleteMeetingListener(meeting));
    }

    public void setMeeting(MeetingViewState meeting) {
        this.meeting = meeting;
    }
}
