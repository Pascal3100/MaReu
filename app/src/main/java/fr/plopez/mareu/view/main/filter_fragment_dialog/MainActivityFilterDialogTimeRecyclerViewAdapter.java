package fr.plopez.mareu.view.main.filter_fragment_dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.plopez.mareu.R;

public class MainActivityFilterDialogTimeRecyclerViewAdapter extends RecyclerView.Adapter<MainActivityFilterDialogTimeViewHolder> {

    private List<String> availableTimes;

    public MainActivityFilterDialogTimeRecyclerViewAdapter(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }

    @NonNull
    @Override
    public MainActivityFilterDialogTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View roomView = inflater.inflate(R.layout.time_filter_recycler_view_item,parent,false);

        // Return a new holder instance
        return new MainActivityFilterDialogTimeViewHolder(roomView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityFilterDialogTimeViewHolder holder, int position) {
        String currentTime = availableTimes.get(position);
        holder.timeText.setText(currentTime);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
