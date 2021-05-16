package fr.plopez.mareu.view.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import fr.plopez.mareu.R;
import fr.plopez.mareu.view.model.MeetingViewState;

public class MainActivityFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MainActivityFragmentRecyclerViewViewHolder> {

    List<MeetingViewState> meetingViewStates;
    final DeleteMeetingListener deleteMeetingListener;

    public MainActivityFragmentRecyclerViewAdapter(DeleteMeetingListener deleteMeetingListener) {
        this.deleteMeetingListener = deleteMeetingListener;
    }

    @NonNull
    @Override
    public MainActivityFragmentRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View meetingView = inflater.inflate(R.layout.meeting_recycler_view_item,parent,false);

        // Return a new holder instance
        return new MainActivityFragmentRecyclerViewViewHolder(meetingView, deleteMeetingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityFragmentRecyclerViewViewHolder holder, int position) {
        Log.d("TAG", "onBindViewHolder: item id " + position + "created");
        MeetingViewState meeting = meetingViewStates.get(position);

        Context context = holder.roomIcon.getContext();
        Glide.with(context)
                .load(ResourcesCompat.getDrawable(context.getResources(),
                                                  meeting.getRoomDrawableId(),
                                                  context.getTheme()))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.roomIcon);

        holder.meetingResume.setText(meeting.getMeetingResume());
        holder.meetingEmails.setText(meeting.getMeetingEmails());

        holder.setMeeting(meeting);
    }

    @Override
    public int getItemCount() {
        if (meetingViewStates == null) {
            return 0;
        }
        return meetingViewStates.size();
    }

    public void submitList(List<MeetingViewState> meetings) {
        meetingViewStates = meetings;
        notifyDataSetChanged();
    }
}
