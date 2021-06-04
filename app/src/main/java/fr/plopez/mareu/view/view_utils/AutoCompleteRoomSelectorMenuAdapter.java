package fr.plopez.mareu.view.view_utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fr.plopez.mareu.R;
import fr.plopez.mareu.databinding.DropDownItemBinding;
import fr.plopez.mareu.view.add.OnRoomSelectorItemClickListener;
import fr.plopez.mareu.view.model.MeetingRoomItem;

public class AutoCompleteRoomSelectorMenuAdapter extends ArrayAdapter<MeetingRoomItem> {

    @NonNull
    private final OnRoomSelectorItemClickListener onRoomSelectorItemClick;

    public AutoCompleteRoomSelectorMenuAdapter(
        @NonNull Context context,
        @NonNull OnRoomSelectorItemClickListener onRoomSelectorItemClick
    ) {
        super(context, R.layout.drop_down_item);
        this.onRoomSelectorItemClick = onRoomSelectorItemClick;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @NonNull
    private View getCustomView(int position, @NonNull ViewGroup parent) {
        DropDownItemBinding dropDownItemBinding = DropDownItemBinding.inflate(LayoutInflater.from(getContext()), parent, false);
        View root = dropDownItemBinding.getRoot();
        MeetingRoomItem item = getItem(position);

        if (item != null) {
            dropDownItemBinding.roomName.setText(item.getRoomName());
            dropDownItemBinding.roomLogo.setImageResource(item.getRoomImageId());
        }

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRoomSelectorItemClick.onRoomSelectorItemClickListener(item);
            }
        });

        return root;
    }

    public void submitList(List<MeetingRoomItem> meetingRoomItemList) {
        clear();
        addAll(meetingRoomItemList);
    }

}
