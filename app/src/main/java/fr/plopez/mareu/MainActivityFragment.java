package fr.plopez.mareu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.plopez.mareu.data.model.Meeting;
import fr.plopez.mareu.view.MainActivityFragmentRecyclerViewAdapter;
import fr.plopez.mareu.view.MainActivityViewModel;
import fr.plopez.mareu.view.MainActivityViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = "MainActivityFragment";

    private RecyclerView meetingsRecyclerView;
    private MainActivityFragmentRecyclerViewAdapter adapter;
    private MainActivityViewModel viewModel;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(
                this,
                MainActivityViewModelFactory.getInstance())
                    .get(MainActivityViewModel.class);

        Log.d(TAG, "onCreate: meetings are " + viewModel.getMeetings().getValue());

        adapter = new MainActivityFragmentRecyclerViewAdapter(viewModel.getMeetings().getValue());

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
        View view = inflater.inflate(R.layout.main_activity_fragment, container, false);

        meetingsRecyclerView = view.findViewById(R.id.main_activity_fragment_meeting_recycler_view);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        meetingsRecyclerView.setLayoutManager(linearLayoutManager);
        meetingsRecyclerView.setAdapter(adapter);
    }

}