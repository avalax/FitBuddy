package de.avalax.fitbuddy.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import javax.inject.Inject;

public class FinishWorkoutFragment extends Fragment {
    @InjectView(R.id.buttonFinishWorkout)
    protected Button buttonFinishWorkout;
    @InjectView(R.id.buttonSwitchWorkout)
    protected Button buttonSwitchWorkout;
    @InjectView(R.id.buttonAddWorkout)
    protected Button buttonAddWorkout;
    protected ViewPager viewPager;
    @Inject
    protected WorkoutSession workoutSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish, container, false);
        ButterKnife.inject(this, view);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        return view;
    }

    @OnClick(R.id.buttonFinishWorkout)
    protected void finishWorkout() {
        Log.d("onClick", "finishWorkout");
        workoutSession.saveWorkout();
        Intent intent = new Intent(getActivity(), ManageWorkoutActivity.class);
        startActivityForResult(intent,ManageWorkoutActivity.SAVE_WORKOUT);
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
