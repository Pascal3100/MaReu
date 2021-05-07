package fr.plopez.mareu.view.main.filter_fragment_dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.plopez.mareu.R;
import fr.plopez.mareu.view.main.OnModifyFilters;
import fr.plopez.mareu.view.model.MeetingTimeItem;

public class MainActivityFilterDialogTimeRecyclerViewAdapter
        extends RecyclerView.Adapter<MainActivityFilterDialogTimeViewHolder> {

    private final List<MeetingTimeItem> meetingTimeItemList;
    private final OnModifyFilters onModifyFilters;

    public MainActivityFilterDialogTimeRecyclerViewAdapter(List<MeetingTimeItem> meetingTimeItemList, OnModifyFilters onModifyFilters) {
        this.meetingTimeItemList = meetingTimeItemList;
        this.onModifyFilters = onModifyFilters;
    }

    @NonNull
    @Override
    public MainActivityFilterDialogTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View roomView = inflater.inflate(R.layout.time_filter_recycler_view_item, parent, false);

        // Return a new holder instance
        return new MainActivityFilterDialogTimeViewHolder(roomView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityFilterDialogTimeViewHolder holder, int position) {
        MeetingTimeItem currentTime = meetingTimeItemList.get(position);
        holder.timeText.setText(currentTime.getTime());
        holder.timeCheckBox.setChecked(currentTime.isChecked());

        holder.timeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetingTimeItemList.get(holder.getBindingAdapterPosition()).setChecked(holder.timeCheckBox.isChecked());
                onModifyFilters.onTimeFilterModify(meetingTimeItemList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meetingTimeItemList.size();
    }
}
