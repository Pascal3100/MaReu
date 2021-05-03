package fr.plopez.mareu.view.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import fr.plopez.mareu.R;
import fr.plopez.mareu.view.add.AddMeetingActivity;

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

    }

    @Override
    public void onClickListener() {
        Intent intent = new Intent(MainActivity.this, AddMeetingActivity.class);
        startActivity(intent);
    }
}