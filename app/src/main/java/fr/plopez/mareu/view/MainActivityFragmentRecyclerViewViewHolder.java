package fr.plopez.mareu.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import fr.plopez.mareu.R;
import fr.plopez.mareu.view.model.MeetingViewState;

public class MainActivityFragmentRecyclerViewViewHolder extends RecyclerView.ViewHolder {

    public ImageView roomIcon;
    public TextView meetingResume;
    public TextView meetingEmails;
    public AppCompatImageButton trashIcon;
    private MeetingViewState meeting;
    private DeleteMeetingListener deleteMeetingListener;

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
        trashIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMeetingListener.onClickDeleteMeetingListener(meeting);
            }
        });
    }

    public void setMeeting(MeetingViewState meeting) {
        this.meeting = meeting;
    }
}
