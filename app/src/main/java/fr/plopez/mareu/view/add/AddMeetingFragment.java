package fr.plopez.mareu.view.add;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.plopez.mareu.R;
import fr.plopez.mareu.data.model.Time;
import fr.plopez.mareu.databinding.FragmentAddMeetingActivityBinding;
import fr.plopez.mareu.view.ViewModelFactory;
import fr.plopez.mareu.view.view_utils.AutoCompleteRoomSelectorMenuAdapter;
import fr.plopez.mareu.view.view_utils.CustomToasts;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMeetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMeetingFragment extends Fragment implements View.OnClickListener {

    private FragmentAddMeetingActivityBinding fragAddMeetingActBinding;
    private AddMeetingViewModel addMeetingViewModel;

    private int hour,min;
    private static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AddMeetingActivityFragment.
     */
    public static AddMeetingFragment newInstance() {
        return new AddMeetingFragment();
    }

    private final TextInputEditText.OnEditorActionListener textListener = new TextInputEditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (v.getId() == fragAddMeetingActBinding.emailInputContent.getId()){
                if (v.getText().toString().trim().matches(emailPattern)) {

                    //Send email in the chip group container
                    LayoutInflater inflater = getLayoutInflater();

                    // TODO: corriger le null avec Nino
                    Chip emailChip = (Chip) inflater.inflate(R.layout.email_chip, null,false);
                    emailChip.setText(v.getText());
                    emailChip.setOnCloseIconClickListener(AddMeetingFragment.this);
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

                if (!addMeetingViewModel.getRoomsNames().contains(v.getText().toString())){
                    CustomToasts.showErrorToast(getContext(), "Select an existing room");
                }
            }

            return false;
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get an instance of ViewModel
        addMeetingViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance())
                .get(AddMeetingViewModel.class);

        // Inflate the layout for this fragment
        fragAddMeetingActBinding = FragmentAddMeetingActivityBinding.inflate(inflater, container, false);
        View view = fragAddMeetingActBinding.getRoot();

        // Initialize widgets
        init();

        // Set the time picker appears when click on the clock button
        fragAddMeetingActBinding.timePickerButton.setOnClickListener(v -> popTimePicker());

        // Set the inputs format check
        fragAddMeetingActBinding.emailInputContent.setOnEditorActionListener(textListener);
        fragAddMeetingActBinding.textInputSubjectContent.setOnEditorActionListener(textListener);
        fragAddMeetingActBinding.roomSelectorMenu.setOnEditorActionListener(textListener);

        // Set the save button behavior
        fragAddMeetingActBinding.saveButton.setOnClickListener(v -> {

            String subject = fragAddMeetingActBinding.textInputSubjectContent.getText().toString();
            String selectedRoom = fragAddMeetingActBinding.roomSelectorMenu.getText().toString();
            String time = fragAddMeetingActBinding.timePickerText.getText().toString();
            int nbEmails = fragAddMeetingActBinding.emailsChipGroup.getChildCount();

            // Get all the emails in chips
            int i = 0;
            List<String> emails = new ArrayList<>();
            while (i < nbEmails) {
                Chip chip = (Chip) fragAddMeetingActBinding.emailsChipGroup.getChildAt(i);
                emails.add(chip.getText().toString());
                i++;
            }

            addMeetingViewModel.addMeeting(subject, time, selectedRoom, emails, nbEmails);
        });

        return view;
    }

    private void init(){
        // Initializing current time display
        Time time = new Time();
        hour = time.getCurrentHour();
        min = time.getCurrentMin();

        updateTimeText();

        AutoCompleteRoomSelectorMenuAdapter adapter = new AutoCompleteRoomSelectorMenuAdapter(
                getContext(),
                addMeetingViewModel.getMeetingRoomItemList());
        fragAddMeetingActBinding.roomSelectorMenu.setAdapter(adapter);

        // init observer
        addMeetingViewModel.getAddMeetingViewActionSingleLiveEvent().observe(
            getViewLifecycleOwner(),
            action -> {
                switch (action) {
                    case FINISH_ACTIVITY:
                        getActivity().finish();
                        break;
                    case DISPLAY_INCORRECT_SUBJECT_MESSAGE:
                    case DISPLAY_INCORRECT_ROOM_MESSAGE:
                    case DISPLAY_INCORRECT_EMAIL_MESSAGE:
                        CustomToasts.showErrorToast(getContext(), action.getMessage());
                        break;
                }
            }
        );
    }

    private void updateTimeText(){
        fragAddMeetingActBinding.timePickerText.setText(String.format(Locale.getDefault(),
                "%02d:%02d",
                hour,
                min));
    }

    // Pops timepicker
    public void popTimePicker(){

        //set filled clock icon
        fragAddMeetingActBinding.timePickerButton.setImageResource(R.drawable.ic_baseline_access_time_filled_32);

        // Setting current time
        Time time = new Time();

        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay, minute) -> {
            hour = hourOfDay;
            min = minute;
            updateTimeText();
        };

        TimePickerDialog timePicker = new TimePickerDialog(this.getContext(), onTimeSetListener, time.getCurrentHour(), time.getCurrentMin(), true);
        timePicker.setTitle("Please set meeting start time");
        timePicker.show();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragAddMeetingActBinding = null;
    }

    // On Click on chip delete button
    @Override
    public void onClick(View v) {
        Chip emailChip = (Chip) v;
        fragAddMeetingActBinding.emailsChipGroup.removeView(emailChip);
    }
}