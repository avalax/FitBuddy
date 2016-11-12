package de.avalax.fitbuddy.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.presentation.workout.WorkoutActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("hello world");
    }

    public void continueWorkout(View view) {
        Intent intent = new Intent(this, WorkoutActivity.class);
        startActivity(intent);
    }
}
