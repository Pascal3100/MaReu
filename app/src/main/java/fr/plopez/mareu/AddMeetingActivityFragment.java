package fr.plopez.mareu;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.plopez.mareu.data.model.Time;
import fr.plopez.mareu.databinding.ActivityAddMeetingBinding;
import fr.plopez.mareu.databinding.FragmentAddMeetingActivityBinding;
import fr.plopez.mareu.view.MainActivityViewModel;
import fr.plopez.mareu.view.MainActivityViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMeetingActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMeetingActivityFragment extends Fragment {

    private FragmentAddMeetingActivityBinding fragAddMeetingActBinding;
    private MainActivityViewModel viewModel;

    private int hour,min;

    public AddMeetingActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AddMeetingActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMeetingActivityFragment newInstance() {
        AddMeetingActivityFragment fragment = new AddMeetingActivityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(
                this,
                MainActivityViewModelFactory.getInstance())
                .get(MainActivityViewModel.class);

        // Inflate the layout for this fragment
        fragAddMeetingActBinding = FragmentAddMeetingActivityBinding.inflate(inflater, container, false);
        View view = fragAddMeetingActBinding.getRoot();

        init();

        fragAddMeetingActBinding.timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimePicker(v);
            }
        });

        return view;
    }

    private void init(){
        Time time = new Time();
        hour = time.getCurrentHour();
        min = time.getCurrentMin();
        updateTimeText();

        List<String> items = viewModel.getRoomsNames();
        ArrayAdapter adapter = new ArrayAdapter(requireContext(),R.layout.drop_down_item, items);
        fragAddMeetingActBinding.roomSelectorMenu.setAdapter(adapter);
    }

    // Pops timepicker
    public void popTimePicker(View view){

        // Setting current time
        Time time = new Time();

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;
                updateTimeText();
            }
        };

        TimePickerDialog timePicker = new TimePickerDialog(this.getContext(), onTimeSetListener, time.getCurrentHour(), time.getCurrentMin(), true);
        timePicker.setTitle("Please set meeting start time");
        timePicker.show();
    }

    private void updateTimeText(){
        fragAddMeetingActBinding.timePickerText.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, min));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragAddMeetingActBinding = null;
    }

}