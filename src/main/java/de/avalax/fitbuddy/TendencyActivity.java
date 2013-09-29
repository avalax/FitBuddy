package de.avalax.fitbuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import de.avalax.fitbuddy.workout.Tendency;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.tendency)
public class TendencyActivity extends RoboActivity {
    private Tendency tendency = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (v.getId() == R.id.buttonPositive) {
            tendency = Tendency.PLUS;
        }
        if (v.getId() == R.id.buttonNeutral) {
            tendency = Tendency.MINUS;
        }
        finishActivity();
    }
}