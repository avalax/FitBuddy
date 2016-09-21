package de.avalax.fitbuddy.presentation.summary;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import de.avalax.fitbuddy.R;

public class FinishedWorkoutActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_workout);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FinishedWorkoutListFragment()).commit();
    }
}
