package de.avalax.fitbuddy.presentation.summary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;

import static de.avalax.fitbuddy.presentation.summary.FinishedWorkoutDetailFragment.ARGS_FINISHED_WORKOUT;

public class FinishedWorkoutDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_workout_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_finished_workout_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FinishedWorkout finishedWorkout = (FinishedWorkout) getIntent().getSerializableExtra(ARGS_FINISHED_WORKOUT);
        TextView nameTextView = findViewById(R.id.finished_workout_name);
        nameTextView.setText(finishedWorkout.getName());
    }

    public void onCancelButtonClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}