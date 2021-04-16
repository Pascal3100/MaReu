package fr.plopez.mareu.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.plopez.mareu.R;
import fr.plopez.mareu.data.model.Meeting;

public class MainActivityFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MainActivityFragmentRecyclerViewViewHolder> {

    List<Meeting> meetingsList;

    public MainActivityFragmentRecyclerViewAdapter(List<Meeting> meetingsList) {
        this.meetingsList = meetingsList;
    }

    @NonNull
    @Override
    public MainActivityFragmentRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View meetingView = inflater.inflate(R.layout.meeting_recycler_view_item,parent,false);

        // Return a new holder instance
        return new MainActivityFragmentRecyclerViewViewHolder(meetingView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityFragmentRecyclerViewViewHolder holder, int position) {
        Meeting meeting = meetingsList.get(position);

        Glide.with(holder.roomIcon.getContext())
                .load(meeting.getRoom().getAvatarUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.roomIcon);

        holder.meetingResume.setText(meeting.getResume());
        holder.meetingEmails.setText(meeting.getEmails());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
