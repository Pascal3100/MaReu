package fr.plopez.mareu.view.main.filter_fragment_dialog;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.plopez.mareu.R;

public class MainActivityFilterDialogRoomViewHolder extends RecyclerView.ViewHolder{

    public final CheckBox roomCheckBox;
    public final ImageView roomLogo;

    public MainActivityFilterDialogRoomViewHolder(@NonNull View itemView) {
        super(itemView);

        roomLogo = itemView.findViewById(R.id.room_logo);
        roomCheckBox = itemView.findViewById(R.id.room_check_box);
    }
}
