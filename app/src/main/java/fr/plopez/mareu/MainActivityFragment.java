package fr.plopez.mareu;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.plopez.mareu.databinding.MainActivityFragmentBinding;
import fr.plopez.mareu.view.DeleteMeetingListener;
import fr.plopez.mareu.view.MainActivityFabOnClickListener;
import fr.plopez.mareu.view.MainActivityFragmentRecyclerViewAdapter;
import fr.plopez.mareu.view.add.AddMeetingViewModel;
import fr.plopez.mareu.view.ViewModelFactory;
import fr.plopez.mareu.view.model.MeetingViewState;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityFragment extends Fragment implements DeleteMeetingListener {

    private static final String TAG = "MainActivityFragment";

    private MainActivityFragmentRecyclerViewAdapter adapter;
    private AddMeetingViewModel viewModel;

    private MainActivityFragmentBinding mainActivityFragmentBinding;

    private MainActivityFabOnClickListener fabOnClickListener;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MainActivityFragment.
     */
    public static MainActivityFragment newInstance() {
        MainActivityFragment fragment = new MainActivityFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivityFabOnClickListener) {
            fabOnClickListener = (MainActivityFabOnClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MainActivityFabOnClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance())
                    .get(AddMeetingViewModel.class);

        adapter = new MainActivityFragmentRecyclerViewAdapter((DeleteMeetingListener) this);

        viewModel.getMainActivityViewStatesLiveData().observe(this, new Observer<List<MeetingViewState>>() {
            @Override
            public void onChanged(List<MeetingViewState> meetingViewStates) {
                adapter.submitList(meetingViewStates);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivityFragmentBinding = MainActivityFragmentBinding.inflate(inflater, container, false);
        View view = mainActivityFragmentBinding.getRoot();

        mainActivityFragmentBinding.mainActivityFragmentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabOnClickListener.onClickListener();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivityFragmentBinding = null;
    }

    private void initRecyclerView(){
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        mainActivityFragmentBinding.mainActivityFragmentMeetingRecyclerView.setLayoutManager(linearLayoutManager);
        mainActivityFragmentBinding.mainActivityFragmentMeetingRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickDeleteMeetingListener(MeetingViewState meeting) {
        viewModel.deleteMeeting(meeting);
        // viewModel.addMeeting("JOJO", "12:00", "star", Arrays.asList("toto@gmail.com","jojo@expleo.com"), 2);
    }
}