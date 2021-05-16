package fr.plopez.mareu.view.main.filter_fragment_dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.plopez.mareu.R;
import fr.plopez.mareu.view.main.OnModifyFilters;
import fr.plopez.mareu.view.model.MeetingRoomItem;

public class MainActivityFilterDialogRoomRecyclerViewAdapter
        extends RecyclerView.Adapter<MainActivityFilterDialogRoomViewHolder> {

    private final List<MeetingRoomItem> meetingRoomItemList;
    private final OnModifyFilters onModifyFilters;


    public MainActivityFilterDialogRoomRecyclerViewAdapter(List<MeetingRoomItem> meetingRoomItemList, OnModifyFilters onModifyFilters) {
        this.meetingRoomItemList = meetingRoomItemList;
        this.onModifyFilters = onModifyFilters;
    }

    @NonNull
    @Override
    public MainActivityFilterDialogRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

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
                .load(ResourcesCompat.getDrawable(context.getResources(),
                        meetingRoomItem.getRoomImageId(),
                        context.getTheme()))
                .into(holder.roomLogo);

        holder.roomCheckBox.setChecked(meetingRoomItem.isChecked());
        holder.roomCheckBox.setOnClickListener(v -> {
            meetingRoomItemList.get(holder.getBindingAdapterPosition()).setChecked(holder.roomCheckBox.isChecked());
            onModifyFilters.onRoomFilterModify(meetingRoomItemList);
        });
    }

    @Override
    public int getItemCount() {
        if (meetingRoomItemList == null) {
            return 0;
        }
        return meetingRoomItemList.size();
    }
}
