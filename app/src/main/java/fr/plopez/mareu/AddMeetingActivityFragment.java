package fr.plopez.mareu;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.plopez.mareu.data.model.Time;
import fr.plopez.mareu.databinding.FragmentAddMeetingActivityBinding;
import fr.plopez.mareu.view.AddMeetingActivitySaveListener;
import fr.plopez.mareu.view.CustomToasts;
import fr.plopez.mareu.view.MainActivityViewModel;
import fr.plopez.mareu.view.MainActivityViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMeetingActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMeetingActivityFragment extends Fragment implements View.OnClickListener {

    private FragmentAddMeetingActivityBinding fragAddMeetingActBinding;
    private MainActivityViewModel mainActivityViewModel;
    private AddMeetingActivitySaveListener saveMeetingListener;

    private int hour,min;
    private static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public AddMeetingActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AddMeetingActivityFragment.
     */
    public static AddMeetingActivityFragment newInstance() {
        AddMeetingActivityFragment fragment = new AddMeetingActivityFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddMeetingActivitySaveListener) {
            saveMeetingListener = (AddMeetingActivitySaveListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement AddMeetingActivitySaveListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get an instance of ViewModel
        mainActivityViewModel = new ViewModelProvider(
                this,
                MainActivityViewModelFactory.getInstance())
                .get(MainActivityViewModel.class);

        // Inflate the layout for this fragment
        fragAddMeetingActBinding = FragmentAddMeetingActivityBinding.inflate(inflater, container, false);
        View view = fragAddMeetingActBinding.getRoot();

        // Initialize widgets
        init();

        // Set the time picker appears when click on the clock button
        fragAddMeetingActBinding.timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimePicker(v);
            }
        });

        // Set the inputs format check
        fragAddMeetingActBinding.emailInputContent.setOnEditorActionListener(textListener);
        fragAddMeetingActBinding.textInputSubjectContent.setOnEditorActionListener(textListener);
        fragAddMeetingActBinding.roomSelectorMenu.setOnEditorActionListener(textListener);

        // Set the save button behavior
        fragAddMeetingActBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subject = fragAddMeetingActBinding.textInputSubjectContent.getText().toString();
                String selectedRoom = fragAddMeetingActBinding.roomSelectorMenu.getText().toString();
                String time = fragAddMeetingActBinding.timePickerText.getText().toString();
                int nbEmails = fragAddMeetingActBinding.emailsChipGroup.getChildCount();

                //Check if all the fields are correctly filled
                if (subject.isEmpty()) {
                    CustomToasts.showErrorToast(getContext(), "Enter a correct subject");
                    return;
                } else if (selectedRoom.isEmpty() || !mainActivityViewModel.getRoomsNames().contains(selectedRoom)) {
                    CustomToasts.showErrorToast(getContext(), "Enter a correct room");
                    return;
                } else if (nbEmails == 0) {
                    CustomToasts.showErrorToast(getContext(), "Enter at least one email");
                    return;
                }

                // Get all the emails in chips
                int i = 0;
                List<String> emails = new ArrayList<>();
                while (i < nbEmails) {
                    Chip chip = (Chip) fragAddMeetingActBinding.emailsChipGroup.getChildAt(i);
                    emails.add(chip.getText().toString());
                    i++;
                }

                mainActivityViewModel.addMeeting(subject, time, selectedRoom, emails, nbEmails);

                saveMeetingListener.onSaveMeeting();
            }
        });

        return view;
    }

    private void init(){
        Time time = new Time();
        hour = time.getCurrentHour();
        min = time.getCurrentMin();
        updateTimeText();

        List<String> items = mainActivityViewModel.getRoomsNames();
        ArrayAdapter adapter = new ArrayAdapter(requireContext(),R.layout.drop_down_item, items);
        fragAddMeetingActBinding.roomSelectorMenu.setAdapter(adapter);
    }

    // Pops timepicker
    public void popTimePicker(View view){

        //set filled clock icon
        fragAddMeetingActBinding.timePickerButton.setImageResource(R.drawable.ic_baseline_access_time_filled_32);

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

    private TextInputEditText.OnEditorActionListener textListener = new TextInputEditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (v.getId() == fragAddMeetingActBinding.emailInputContent.getId()){
                if (v.getText().toString().trim().matches(emailPattern)) {

                    //Send email in the chip group container
                    LayoutInflater inflater = getLayoutInflater();
                    Chip emailChip = (Chip) inflater.inflate(R.layout.email_chip, null,false);
                    emailChip.setText(v.getText());
                    emailChip.setOnCloseIconClickListener(AddMeetingActivityFragment.this);
                    fragAddMeetingActBinding.emailsChipGroup.addView(emailChip);

                    // Clears the text edit area
                    v.setText("");

                } else {
                    CustomToasts.showErrorToast(getContext(), "Email is not valid");
                }
            } else if (v.getId() == fragAddMeetingActBinding.textInputSubjectContent.getId()) {

                if (v.getText().toString().isEmpty()){
                    CustomToasts.showErrorToast(getContext(), "Subject can't be empty");
                }
            } else if (v.getId() == fragAddMeetingActBinding.roomSelectorMenu.getId()) {

                if (!mainActivityViewModel.getRoomsNames().contains(v.getText().toString())){
                    CustomToasts.showErrorToast(getContext(), "Select an existing room");
                }
            }

            return false;
        }
    };
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragAddMeetingActBinding = null;
        // TODO detruire l'objet meeting 
    }

    // On Click on chip delete button
    @Override
    public void onClick(View v) {
        Chip emailChip = (Chip) v;
        fragAddMeetingActBinding.emailsChipGroup.removeView(emailChip);
    }
}