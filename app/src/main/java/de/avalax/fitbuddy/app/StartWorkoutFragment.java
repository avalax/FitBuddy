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
import butterknife.OnClick;

public class StartWorkoutFragment extends Fragment {
    @InjectView(R.id.buttonEditWorkout)
    protected Button buttonEditWorkout;
    @InjectView(R.id.buttonSwitchWorkout)
    protected Button buttonSwitchWorkout;
    @InjectView(R.id.buttonAddWorkout)
    protected Button buttonAddWorkout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.buttonEditWorkout)
    protected void toogleEditMode() {
        Log.d("onClick", "buttonFinishWorkout");
    }

    @OnClick(R.id.buttonAddWorkout)
    protected void addNewWorkout() {
        Log.d("onClick", "buttonAddWorkout");
    }

    @OnClick(R.id.buttonSwitchWorkout)
    protected void switchWorkout() {
        Log.d("onClick", "buttonSwitchWorkout");
    }
}
