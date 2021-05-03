package fr.plopez.mareu.view.add;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import fr.plopez.mareu.R;
import fr.plopez.mareu.databinding.ActivityAddMeetingBinding;

public class AddMeetingActivity extends AppCompatActivity {

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
                    .add(R.id.add_meeting_activity_fragment_container, AddMeetingFragment.newInstance(), null)
                    .commit();
        }

        activityAddMeetingBinding.addMeetingActivityAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}