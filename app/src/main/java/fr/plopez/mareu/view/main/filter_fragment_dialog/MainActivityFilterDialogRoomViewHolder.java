package fr.plopez.mareu.view.main.filter_fragment_dialog;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.plopez.mareu.R;

public class MainActivityFilterDialogRoomViewHolder extends RecyclerView.ViewHolder {

    public ImageView roomLogo;

    public MainActivityFilterDialogRoomViewHolder(@NonNull View itemView) {
        super(itemView);

        roomLogo = itemView.findViewById(R.id.room_logo);
    }
}
