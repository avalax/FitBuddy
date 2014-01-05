package de.avalax.fitbuddy.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class StartWorkoutFragment extends Fragment implements View.OnClickListener {
    @InjectView(R.id.buttonEditWorkout)
    Button buttonEditWorkout;
    @InjectView(R.id.buttonSwitchWorkout)
    Button buttonSwitchWorkout;
    @InjectView(R.id.buttonAddWorkout)
    Button buttonAddWorkout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        buttonEditWorkout.setOnClickListener(this);
        buttonSwitchWorkout.setOnClickListener(this);
        buttonAddWorkout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonEditWorkout:
                Log.d("onClick", "buttonEditWorkout");
                break;
            case R.id.buttonSwitchWorkout:
                Log.d("onClick", "buttonSwitchWorkout");
                break;
            case R.id.buttonAddWorkout:
                Log.d("onClick", "buttonAddWorkout");
                break;
        }
    }
}
