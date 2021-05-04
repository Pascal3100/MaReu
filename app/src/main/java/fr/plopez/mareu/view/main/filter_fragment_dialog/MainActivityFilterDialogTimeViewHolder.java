package fr.plopez.mareu.view.main.filter_fragment_dialog;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.plopez.mareu.R;

public class MainActivityFilterDialogTimeViewHolder  extends RecyclerView.ViewHolder {

    public TextView timeText;

    public MainActivityFilterDialogTimeViewHolder(@NonNull View itemView) {
        super(itemView);

        timeText = itemView.findViewById(R.id.time_text);
    }
}
