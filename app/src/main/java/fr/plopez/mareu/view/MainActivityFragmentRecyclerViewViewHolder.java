package fr.plopez.mareu.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.plopez.mareu.R;

public class MainActivityFragmentRecyclerViewViewHolder extends RecyclerView.ViewHolder {

    public ImageView roomIcon;
    public TextView meetingResume;
    public TextView meetingEmails;

    public MainActivityFragmentRecyclerViewViewHolder(@NonNull View itemView) {
        super(itemView);
        //
        roomIcon = itemView.findViewById(R.id.fragment_main_activity_view_holder_room_icon);
        meetingResume = itemView.findViewById(R.id.fragment_main_activity_view_holder_meeting_resume_text);
        meetingEmails = itemView.findViewById(R.id.fragment_main_activity_view_holder_meeting_emails_text);
    }
}
