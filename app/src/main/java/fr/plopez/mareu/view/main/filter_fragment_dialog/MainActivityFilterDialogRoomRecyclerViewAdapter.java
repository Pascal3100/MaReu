package fr.plopez.mareu.view.main.filter_fragment_dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import fr.plopez.mareu.R;
import fr.plopez.mareu.view.model.MeetingRoomItem;

public class MainActivityFilterDialogRoomRecyclerViewAdapter extends RecyclerView.Adapter<MainActivityFilterDialogRoomViewHolder> {

    private List<MeetingRoomItem> meetingRoomItemList;

    public MainActivityFilterDialogRoomRecyclerViewAdapter(List<MeetingRoomItem> meetingRoomItemList) {
        this.meetingRoomItemList = meetingRoomItemList;
    }

    @NonNull
    @Override
    public MainActivityFilterDialogRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View timeView = inflater.inflate(R.layout.room_filter_recycler_view_item,parent,false);

        // Return a new holder instance
        return new MainActivityFilterDialogRoomViewHolder(timeView);

    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityFilterDialogRoomViewHolder holder, int position) {
        MeetingRoomItem meetingRoomItem = meetingRoomItemList.get(position);

        Context context = holder.roomLogo.getContext();
        Glide.with(context)
                .load(context.getResources().getDrawable(meetingRoomItem.getRoomImageId()))
                .into(holder.roomLogo);

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
