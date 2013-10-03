package de.avalax.fitbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        buttonNegative.setText(getNegativeLabel(workoutSet));
        buttonNeutral.setText(getNeutralLabel(workoutSet));
        buttonPositive.setText(getPositiveLabel(workoutSet));
    }

    private String getPositiveLabel(WorkoutSet workoutSet) {
        double weightRaise = workoutSet.getWeightRaise(Tendency.NEUTRAL);
        double newWeight = workoutSet.getWeight() + weightRaise;
        return String.format(positiveLabel, newWeight, weightRaise);
    }

    private String getNeutralLabel(WorkoutSet workoutSet) {
        return String.format(neutralLabel, workoutSet.getWeight());
    }

    private String getNegativeLabel(WorkoutSet workoutSet) {
        double weightRaise = workoutSet.getWeightRaise(Tendency.NEUTRAL);
        double newWeight = workoutSet.getWeight() - weightRaise;
        if (newWeight < 0) {
            weightRaise = weightRaise + newWeight;
            newWeight = 0;
        } //TODO: extreat logic to workoutSet
        return String.format(negativeLabel, newWeight, weightRaise);
    }

    protected void finishActivity() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("tendency", tendency);
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