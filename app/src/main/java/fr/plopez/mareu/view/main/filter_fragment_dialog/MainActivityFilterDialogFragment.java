package fr.plopez.mareu.view.main.filter_fragment_dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import fr.plopez.mareu.databinding.FragmentFilterDialogBinding;
import fr.plopez.mareu.view.ViewModelFactory;
import fr.plopez.mareu.view.main.MainActivityViewModel;
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
    private MainActivityViewModel mainActivityViewModel;
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

        mainActivityViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance())
                .get(MainActivityViewModel.class);

        LinearLayoutManager roomFilterLinearLayoutManager = new LinearLayoutManager(getContext());
        roomFilterLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentFilterDialogBinding.roomFilter.setLayoutManager(roomFilterLinearLayoutManager);
        fragmentFilterDialogBinding.roomFilter.addItemDecoration(
                new DividerItemDecoration(getContext(),
                DividerItemDecoration.HORIZONTAL));

        LinearLayoutManager timeFilterLinearLayoutManager = new LinearLayoutManager(getContext());
        timeFilterLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragmentFilterDialogBinding.timeFilter.setLayoutManager(timeFilterLinearLayoutManager);

        //TODO: implementer l'observer pour la mise à jour du texte (nb de meetings trouvés avec les filtres en cours)

        fragmentFilterDialogBinding.closeFilterDialogButton.setOnClickListener(v -> dismiss());

        return fragmentFilterDialogBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentFilterDialogBinding.roomFilter.setAdapter(
                new MainActivityFilterDialogRoomRecyclerViewAdapter(
                        mainActivityViewModel.getMeetingRoomItemList(),
                        onModifyFilters
                )
        );
        fragmentFilterDialogBinding.timeFilter.setAdapter(
                new MainActivityFilterDialogTimeRecyclerViewAdapter(
                        mainActivityViewModel.getMeetingTimeItemList(),
                        onModifyFilters
                )
        );
    }

    @Override
    public void onRoomFilterModify(List<MeetingRoomItem> meetingRoomItemList) {
        mainActivityViewModel.updateRoomFilter(meetingRoomItemList);
    }

    @Override
    public void onTimeFilterModify(List<MeetingTimeItem> meetingTimeItemList) {
        mainActivityViewModel.updateTimeFilter(meetingTimeItemList);
    }
}