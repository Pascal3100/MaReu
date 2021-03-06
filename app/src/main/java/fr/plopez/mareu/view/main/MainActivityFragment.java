package fr.plopez.mareu.view.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.plopez.mareu.databinding.MainActivityFragmentBinding;
import fr.plopez.mareu.view.ViewModelFactory;
import fr.plopez.mareu.view.model.MeetingViewState;

public class MainActivityFragment extends Fragment implements DeleteMeetingListener {

    private MainActivityFragmentRecyclerViewAdapter adapter;
    private MainActivityViewModel mainActivityViewModel;

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
       return new MainActivityFragment();
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

        mainActivityViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance())
                    .get(MainActivityViewModel.class);

        adapter = new MainActivityFragmentRecyclerViewAdapter(this);

        mainActivityViewModel.getMainActivityViewStatesLiveData().observe(this, meetingViewStates -> adapter.submitList(meetingViewStates));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainActivityFragmentBinding = MainActivityFragmentBinding.inflate(inflater, container, false);
        View view = mainActivityFragmentBinding.getRoot();

        mainActivityFragmentBinding.mainActivityFragmentFab.setOnClickListener(v -> fabOnClickListener.onClickListener());


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
        mainActivityViewModel.deleteMeeting(meeting);
    }
}