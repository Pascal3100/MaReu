package fr.plopez.mareu.view.add;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import fr.plopez.mareu.R;
import fr.plopez.mareu.databinding.ActivityAddMeetingBinding;

public class AddMeetingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fr.plopez.mareu.databinding.ActivityAddMeetingBinding activityAddMeetingBinding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        View view = activityAddMeetingBinding.getRoot();
        setContentView(view);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.add_meeting_activity_fragment_container, AddMeetingFragment.newInstance(), null)
                    .commit();
        }

        // Clear the filter menu because not needed in this view.
        activityAddMeetingBinding.addMeetingActivityAppBar.getMenu().clear();

        activityAddMeetingBinding.addMeetingActivityAppBar.setNavigationOnClickListener(v -> finish());

    }

}