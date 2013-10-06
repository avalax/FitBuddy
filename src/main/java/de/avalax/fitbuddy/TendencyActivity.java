package de.avalax.fitbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Tendency;
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
        Exercise exercise = (Exercise) getIntent().getSerializableExtra("exercise");
        buttonNegative.setText(getNegativeLabel(exercise));
        buttonNeutral.setText(getNeutralLabel(exercise));
        buttonPositive.setText(getPositiveLabel(exercise));
    }

    private String getPositiveLabel(Exercise exercise) {
        double newWeight = exercise.getWeightRaise(Tendency.PLUS);
        double weight = exercise.getWeight();
        return String.format(positiveLabel, newWeight, getWeightRaise(newWeight, weight));
    }

    private String getNeutralLabel(Exercise exercise) {
        return String.format(neutralLabel, exercise.getWeight());
    }

    private String getNegativeLabel(Exercise exercise) {
        double newWeight = exercise.getWeightRaise(Tendency.MINUS);
        double weight = exercise.getWeight();
        return String.format(negativeLabel, newWeight, getWeightRaise(newWeight, weight));
    }

    private double getWeightRaise(double newWeight, double weight) {
        return Math.abs(newWeight-weight);
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