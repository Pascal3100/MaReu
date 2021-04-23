package fr.plopez.mareu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.databinding.MainActivityFragmentBinding;
import fr.plopez.mareu.view.DeleteMeetingListener;
import fr.plopez.mareu.view.MainActivityFabOnClickListener;
import fr.plopez.mareu.view.MainActivityFragmentRecyclerViewAdapter;
import fr.plopez.mareu.view.MainActivityViewModel;
import fr.plopez.mareu.view.MainActivityViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityFragment extends Fragment implements DeleteMeetingListener {

    private static final String TAG = "MainActivityFragment";

    private MainActivityFragmentRecyclerViewAdapter adapter;
    private MainActivityViewModel viewModel;

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
                MainActivityViewModelFactory.getInstance())
                    .get(MainActivityViewModel.class);

        adapter = new MainActivityFragmentRecyclerViewAdapter(viewModel.getMeetings().getValue(), (DeleteMeetingListener) this);

        viewModel.getMeetings().observe(this, new Observer<List<Meeting>>() {
            @Override
            public void onChanged(List<Meeting> meetings) {
                adapter.notifyDataSetChanged();
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
    public void onClickDeleteMeetingListener(Meeting meeting) {
        viewModel.deleteMeeting(meeting);
    }
}