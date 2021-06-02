package fr.plopez.mareu.view.main.filter_fragment_dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import fr.plopez.mareu.databinding.FragmentFilterDialogBinding;
import fr.plopez.mareu.view.ViewModelFactory;
import fr.plopez.mareu.view.main.OnModifyFilters;
import fr.plopez.mareu.view.model.MeetingRoomItem;
import fr.plopez.mareu.view.model.MeetingTimeItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivityFilterDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityFilterDialogFragment extends DialogFragment implements OnModifyFilters {

    private FragmentFilterDialogBinding fragmentFilterDialogBinding;
    private FilterDialogFragmentViewModel filterDialogViewModel;
    private OnModifyFilters onModifyFilters;

    public MainActivityFilterDialogFragment() {
        // Required empty public constructor
    }

    public static MainActivityFilterDialogFragment newInstance() {
        return new MainActivityFilterDialogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment and get binding instance
        fragmentFilterDialogBinding = FragmentFilterDialogBinding.inflate(inflater, container, false);

        onModifyFilters = (OnModifyFilters) this;

        filterDialogViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance())
                .get(FilterDialogFragmentViewModel.class);

        LinearLayoutManager roomFilterLinearLayoutManager = new LinearLayoutManager(getContext());
        roomFilterLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentFilterDialogBinding.roomFilter.setLayoutManager(roomFilterLinearLayoutManager);
        fragmentFilterDialogBinding.roomFilter.addItemDecoration(
                new DividerItemDecoration(getContext(),
                        DividerItemDecoration.HORIZONTAL));

        LinearLayoutManager timeFilterLinearLayoutManager = new LinearLayoutManager(getContext());
        timeFilterLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentFilterDialogBinding.timeFilter.setLayoutManager(timeFilterLinearLayoutManager);

        filterDialogViewModel.getFilterDialogDynamicTextLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                fragmentFilterDialogBinding.filterText.setText(s);
            }
        });

        fragmentFilterDialogBinding.closeFilterDialogButton.setOnClickListener(v -> dismiss());

        return fragmentFilterDialogBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivityFilterDialogRoomRecyclerViewAdapter roomRecyclerViewAdapter =
                new MainActivityFilterDialogRoomRecyclerViewAdapter(
                onModifyFilters
        );

        // Observe roomItemsList
        fragmentFilterDialogBinding.roomFilter.setAdapter(roomRecyclerViewAdapter);

        filterDialogViewModel.getMeetingRoomItemList().observe(
                getViewLifecycleOwner(),
                new Observer<List<MeetingRoomItem>>() {
            @Override
            public void onChanged(List<MeetingRoomItem> meetingRoomItemList) {
                roomRecyclerViewAdapter.submitList(meetingRoomItemList);
            }
        });



        // Observe TimeItemsList
        MainActivityFilterDialogTimeRecyclerViewAdapter timeRecyclerViewAdapter =
                new MainActivityFilterDialogTimeRecyclerViewAdapter(
                onModifyFilters
        );

        fragmentFilterDialogBinding.timeFilter.setAdapter(timeRecyclerViewAdapter);

        filterDialogViewModel.getMeetingTimeItemList().observe(
                getViewLifecycleOwner(),
                new Observer<List<MeetingTimeItem>>() {
                    @Override
                    public void onChanged(List<MeetingTimeItem> meetingTimeItemList) {
                        timeRecyclerViewAdapter.submitList(meetingTimeItemList);
                    }
                });
    }

    @Override
    public void onRoomFilterModify(List<MeetingRoomItem> meetingRoomItemList) {
        filterDialogViewModel.updateRoomFilter(meetingRoomItemList);
    }

    @Override
    public void onTimeFilterModify(List<MeetingTimeItem> meetingTimeItemList) {
        filterDialogViewModel.updateTimeFilter(meetingTimeItemList);
    }
}