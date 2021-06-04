package fr.plopez.mareu.view.add;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.Locale;

import fr.plopez.mareu.R;
import fr.plopez.mareu.data.model.Time;
import fr.plopez.mareu.databinding.FragmentAddMeetingActivityBinding;
import fr.plopez.mareu.view.ViewModelFactory;
import fr.plopez.mareu.view.model.MeetingRoomItem;
import fr.plopez.mareu.view.view_utils.AutoCompleteRoomSelectorMenuAdapter;
import fr.plopez.mareu.view.view_utils.CustomToasts;

public class AddMeetingFragment extends Fragment implements View.OnClickListener, OnRoomSelectorItemClickListener {

    private FragmentAddMeetingActivityBinding fragAddMeetingActBinding;
    private AddMeetingViewModel addMeetingViewModel;

    private final TextInputEditText.OnEditorActionListener textListener =
            new TextInputEditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    String inputFieldText = v.getText().toString();

                    if (v.getId() == fragAddMeetingActBinding.emailInputContent.getId()) {

                        // Emails management
                        addMeetingViewModel.addEmail(inputFieldText);

                    } else if (v.getId() == fragAddMeetingActBinding.textInputSubjectContent.getId()) {

                        // Subject management
                        addMeetingViewModel.addSubject(inputFieldText);

                    } else if (v.getId() == fragAddMeetingActBinding.roomSelectorMenu.getId()) {

                        // Rooms management
                        addMeetingViewModel.addRoom(inputFieldText);
                    }

                    return false;
                }
            };

    private int hour, min;

    public static AddMeetingFragment newInstance() {
        return new AddMeetingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get an instance of ViewModel
        addMeetingViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance())
                .get(AddMeetingViewModel.class);

        // Inflate the layout for this fragment
        fragAddMeetingActBinding = FragmentAddMeetingActivityBinding.inflate(
                inflater,
                container,
                false);
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

            // Add meeting
            addMeetingViewModel.addMeeting();
        });

        // Observer for Chips
        addMeetingViewModel.getEmailsListLiveData().observe(getViewLifecycleOwner(), emailsList -> {

            // Clear all chips
            fragAddMeetingActBinding.emailsChipGroup.removeAllViews();

            // Rebuild all chips
            LayoutInflater inflater1 = getLayoutInflater();

            for (String email : emailsList) {
                Chip emailChip = (Chip) inflater1.inflate(
                        R.layout.email_chip,
                        fragAddMeetingActBinding.emailsChipGroup,
                        false);
                emailChip.setText(email);
                emailChip.setOnCloseIconClickListener(AddMeetingFragment.this);
                fragAddMeetingActBinding.emailsChipGroup.addView(emailChip);
            }

            // Clears the text edit area
            fragAddMeetingActBinding.emailInputContent.setText("");
        });

        // Set the room selector filter utility
        fragAddMeetingActBinding.roomSelectorMenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addMeetingViewModel.updateTextFilterPattern(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void init() {
        // Initializing current time display
        Time time = new Time();
        hour = time.getCurrentHour();
        min = time.getCurrentMin();

        updateTimeText();

        AutoCompleteRoomSelectorMenuAdapter adapter = new AutoCompleteRoomSelectorMenuAdapter(requireContext(), this);
        fragAddMeetingActBinding.roomSelectorMenu.setAdapter(adapter);

        addMeetingViewModel.getFilteredMeetingRoomItemLiveData().observe(
                getViewLifecycleOwner(),
                meetingRoomItemList -> adapter.submitList(meetingRoomItemList)
        );

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
                        case DISPLAY_UNKNOWN_ROOM_MESSAGE:
                        case DISPLAY_EMPTY_SUBJECT_MESSAGE:
                        case DISPLAY_EMPTY_EMAIL_MESSAGE:
                            CustomToasts.showErrorToast(getContext(), action.getMessage());
                            break;
                        default:
                            break;
                    }
                }
        );
    }

    private void updateTimeText() {
        String startTime = String.format(Locale.getDefault(),
                "%02d:%02d",
                hour,
                min);
        fragAddMeetingActBinding.timePickerText.setText(startTime);
        addMeetingViewModel.addStartTime(startTime);
    }

    // Pops timepicker
    public void popTimePicker() {

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
        timePicker.setTitle(getString(R.string.meeting_time_picker_label_text));
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
        addMeetingViewModel.removeEmail(emailChip.getText().toString());
        fragAddMeetingActBinding.emailsChipGroup.removeView(emailChip);
    }

    @Override
    public void onRoomSelectorItemClickListener(MeetingRoomItem meetingRoomItem) {
        fragAddMeetingActBinding.roomSelectorMenu.setText(meetingRoomItem.getRoomName());
    }
}