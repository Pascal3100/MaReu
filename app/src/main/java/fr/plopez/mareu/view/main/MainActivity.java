package fr.plopez.mareu.view.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import fr.plopez.mareu.R;
import fr.plopez.mareu.view.add.AddMeetingActivity;
import fr.plopez.mareu.view.main.filter_fragment_dialog.MainActivityFilterDialogFragment;

public class MainActivity extends AppCompatActivity implements MainActivityFabOnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.main_activity_fragment_container, MainActivityFragment.newInstance(), null)
                    .commit();
        }

        MaterialToolbar toolbar = findViewById(R.id.main_activity_app_bar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sort_meetings) {
                new MainActivityFilterDialogFragment().show(getSupportFragmentManager(), TAG);
            }
            return false;
        });

    }

    @Override
    public void onClickListener() {
        Intent intent = new Intent(MainActivity.this, AddMeetingActivity.class);
        startActivity(intent);
    }
}