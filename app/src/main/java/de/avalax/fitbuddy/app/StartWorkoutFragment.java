package de.avalax.fitbuddy.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class StartWorkoutFragment extends RoboFragment implements View.OnClickListener {
    @InjectView(R.id.buttonEditWorkout)
    private Button buttonEditWorkout;
    @InjectView(R.id.buttonSwitchWorkout)
    private Button buttonSwitchWorkout;
    @InjectView(R.id.buttonAddWorkout)
    private Button buttonAddWorkout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonEditWorkout.setOnClickListener(this);
        buttonSwitchWorkout.setOnClickListener(this);
        buttonAddWorkout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonEditWorkout:
                Log.d("onClick","buttonEditWorkout");
                break;
            case R.id.buttonSwitchWorkout:
                Log.d("onClick","buttonSwitchWorkout");
                break;
            case R.id.buttonAddWorkout:
                Log.d("onClick","buttonAddWorkout");
                break;
        }
    }
}
