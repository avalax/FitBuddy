package de.avalax.fitbuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.WorkoutSet;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

@ContentView(R.layout.tendency)
public class TendencyActivity extends RoboActivity {
    private Tendency tendency = null;

    @InjectView(R.id.buttonNegative)
    private Button buttonNegative;

    @InjectView(R.id.buttonNeutral)
    private Button buttonNeutral;

    @InjectView(R.id.buttonPositive)
    private Button buttonPositive;

    @InjectResource(R.string.negativeLabel)
    private String negativeLabel;

    @InjectResource(R.string.neutralLabel)
    private String neutralLabel;

    @InjectResource(R.string.positiveLabel)
    private String positiveLabel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WorkoutSet workoutSet = (WorkoutSet) getIntent().getSerializableExtra("workoutSet");
        buttonNegative.setText(String.format(negativeLabel,workoutSet.getWeight()));
        buttonNeutral.setText(String.format(neutralLabel,workoutSet.getWeight()));
        buttonPositive.setText(String.format(positiveLabel,workoutSet.getWeight()));
    }

    protected void finishActivity() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("tendency",tendency);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void clickEvent(View v) {
        if (v.getId() == R.id.buttonNegative) {
            tendency = Tendency.MINUS;
        }
        if (v.getId() == R.id.buttonNeutral) {
            tendency = Tendency.NEUTRAL;
        }
        if (v.getId() == R.id.buttonPositive) {
            tendency = Tendency.PLUS;
        }
        finishActivity();
    }
}