package fr.plopez.mareu.view.view_utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fr.plopez.mareu.databinding.DropDownItemBinding;
import fr.plopez.mareu.view.add.OnRoomSelectorItemClickListener;
import fr.plopez.mareu.view.model.MeetingRoomItem;

public class AutoCompleteRoomSelectorMenuAdapter extends ArrayAdapter<MeetingRoomItem> {

    private static final int resource = 0;
    private DropDownItemBinding dropDownItemBinding;
    private OnRoomSelectorItemClickListener onRoomSelectorItemClick;

    public AutoCompleteRoomSelectorMenuAdapter(
            @NonNull Context context,
            OnRoomSelectorItemClickListener onRoomSelectorItemClick) {
        super(context, resource);
        this.onRoomSelectorItemClick = onRoomSelectorItemClick;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Inflate a new item only if its not recycled
        if (convertView == null) {
            dropDownItemBinding = DropDownItemBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = dropDownItemBinding.getRoot();
        }

        MeetingRoomItem item = getItem(position);

        if (item != null) {
            dropDownItemBinding.roomName.setText(item.getRoomName());
            dropDownItemBinding.roomLogo.setImageResource(item.getRoomImageId());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRoomSelectorItemClick.onRoomSelectorItemClickListener(item);
            }
        });

        return convertView;
    }

    public void submitList(List<MeetingRoomItem> meetingRoomItemList) {
        clear();
        addAll(meetingRoomItemList);
        //notifyDataSetChanged();
/*
        Log.d("TAG", "-----------------------------------------------");
        Log.d("TAG", "meetingRoomItemList size = "+meetingRoomItemList.size());
        Log.d("TAG", "-----------------------------------------------");
        for (MeetingRoomItem m : meetingRoomItemList){
            Log.d("TAG", "-------- "+m.getRoomName());
        }
        Log.d("TAG", "-----------------------------------------------");
*/

    }

}
