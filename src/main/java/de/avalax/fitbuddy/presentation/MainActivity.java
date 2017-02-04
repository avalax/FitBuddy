package de.avalax.fitbuddy.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.presentation.workout.WorkoutActivity;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void continueWorkout(View view) {
        Intent intent = new Intent(this, WorkoutActivity.class);
        startActivity(intent);
    }
}
