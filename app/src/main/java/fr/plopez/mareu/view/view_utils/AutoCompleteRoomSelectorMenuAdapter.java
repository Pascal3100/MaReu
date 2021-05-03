package fr.plopez.mareu.view.view_utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fr.plopez.mareu.R;
import fr.plopez.mareu.databinding.DropDownItemBinding;
import fr.plopez.mareu.databinding.FragmentAddMeetingActivityBinding;
import fr.plopez.mareu.view.model.MeetingRoomItem;

public class AutoCompleteRoomSelectorMenuAdapter extends ArrayAdapter<MeetingRoomItem> {

    private static final int resource = 0;
    private DropDownItemBinding dropDownItemBinding;

    // Saves the list of items because of the filtering operations
    private List<MeetingRoomItem> meetingRoomItemListFull;

    public AutoCompleteRoomSelectorMenuAdapter(@NonNull Context context, @NonNull List<MeetingRoomItem> meetingRoomItemList) {
        super(context, resource, meetingRoomItemList);
        meetingRoomItemListFull = new ArrayList<>(meetingRoomItemList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return roomsFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Inflate a nex item only if its not recycled
        if (convertView == null) {
/*
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.drop_down_item, parent, false
            );
*/
            dropDownItemBinding = DropDownItemBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = dropDownItemBinding.getRoot();

        }

        MeetingRoomItem item = getItem(position);
        if (item != null) {
            dropDownItemBinding.roomName.setText(item.getRoomName());
            dropDownItemBinding.roomLogo.setImageResource(item.getRoomImageId());
        }

        return convertView;
    }

    private Filter roomsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filteredRoomsResult = new FilterResults();
            List<MeetingRoomItem> filteredRooms = new ArrayList<>();


            if (constraint == null || constraint.length() == 0) {
                // No filter pattern applied, all the rooms will be available
                filteredRooms.addAll(meetingRoomItemListFull);
            } else {
                // Making some formatting operations on the filter pattern
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (MeetingRoomItem item: meetingRoomItemListFull) {
                    if (item.getRoomName().toLowerCase().contains(filterPattern)){
                        filteredRooms.add(item);
                    }
                }
            }

            filteredRoomsResult.values = filteredRooms;
            filteredRoomsResult.count = filteredRooms.size();

            return filteredRoomsResult;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((MeetingRoomItem) resultValue).getRoomName();
        }
    };
}
