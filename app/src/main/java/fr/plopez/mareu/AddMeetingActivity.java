package fr.plopez.mareu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

import fr.plopez.mareu.databinding.ActivityAddMeetingBinding;
import fr.plopez.mareu.databinding.FragmentAddMeetingActivityBinding;
import fr.plopez.mareu.view.AddMeetingActivitySaveListener;

public class AddMeetingActivity extends AppCompatActivity implements AddMeetingActivitySaveListener {

    private ActivityAddMeetingBinding activityAddMeetingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_add_meeting);
        // MaterialToolbar topAppBar = findViewById(R.id.add_meeting_activity_app_bar);

        activityAddMeetingBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = activityAddMeetingBinding.getRoot();
        setContentView(view);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.add_meeting_activity_fragment_container, AddMeetingActivityFragment.newInstance(), null)
                    .commit();
        }

        activityAddMeetingBinding.addMeetingActivityAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onSaveMeeting() {
        finish();
    }
}