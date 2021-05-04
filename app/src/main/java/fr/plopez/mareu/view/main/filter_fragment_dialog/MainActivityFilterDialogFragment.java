package fr.plopez.mareu.view.main.filter_fragment_dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.plopez.mareu.databinding.FragmentFilterDialogBinding;
import fr.plopez.mareu.utils.TimeGen;
import fr.plopez.mareu.view.ViewModelFactory;
import fr.plopez.mareu.view.main.MainActivityViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivityFilterDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityFilterDialogFragment extends DialogFragment {

    private FragmentFilterDialogBinding fragmentFilterDialogBinding;
    private MainActivityViewModel mainActivityViewModel;

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

        mainActivityViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance())
                .get(MainActivityViewModel.class);

        fragmentFilterDialogBinding.roomFilter.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));

        //TODO: implementer l'observer pour la mise à jour du texte (nb de metings trouvés avec les filtres en cours)

        return fragmentFilterDialogBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO : mise à jour du recyclerView des rooms
        fragmentFilterDialogBinding.roomFilter.setAdapter(new MainActivityFilterDialogRoomRecyclerViewAdapter(mainActivityViewModel.getRoomsItems()));
        //TODO : mise à jour du recyclerView des horaires
        fragmentFilterDialogBinding.timeFilter.setAdapter(new MainActivityFilterDialogTimeRecyclerViewAdapter(TimeGen.getAvailableTimes(8,18)));
    }
}