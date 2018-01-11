package de.avalax.fitbuddy.presentation.welcome_screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdView;

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutService;
import de.avalax.fitbuddy.application.workout.WorkoutService;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.MainActivity;
import de.avalax.fitbuddy.presentation.ad_mob.AdMobProvider;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity;
import de.avalax.fitbuddy.presentation.summary.FinishedWorkoutViewHelper;

public class WorkoutListFragment extends Fragment implements View.OnClickListener {

    private static final int ADD_WORKOUT = 1;

    @Inject
    EditWorkoutService editWorkoutService;

    @Inject
    WorkoutService workoutService;

    @Inject
    FinishedWorkoutViewHelper finishedWorkoutViewHelper;

    @Inject
    AdMobProvider adMobProvider;

    private WorkoutAdapter workoutAdapter;
    private List<Workout> workouts;
    private WorkoutRecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);

        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        recyclerView = view.findViewById(android.R.id.list);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));
        workouts = editWorkoutService.loadAllWorkouts();
        workoutAdapter = new WorkoutAdapter((MainActivity) getActivity(),
                workoutService, finishedWorkoutViewHelper, workouts);
        recyclerView.setAdapter(workoutAdapter);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_add_workout);
        floatingActionButton.setOnClickListener(this);
        Toolbar toolbar = view.findViewById(R.id.toolbar_main);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        AdView adView = view.findViewById(R.id.adView);
        adMobProvider.initAdView(adView);
        return view;
    }

    public void updateWorkout(Integer position, Workout workout) {
        workouts.set(position, workout);
        workoutAdapter.notifyItemChanged(position);
        recyclerView.updateEmptyView();
        ((MainActivity) getActivity()).mainToolbar();
    }

    public void removeSelection() {
        workoutAdapter.removeSelection();
    }

    public void removeWorkout(Workout workout) {
        removeSelection();
        workouts.remove(workout);
        recyclerView.updateEmptyView();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), EditWorkoutActivity.class);
        intent.putExtra("workout", new BasicWorkout());
        startActivityForResult(intent, ADD_WORKOUT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_WORKOUT && resultCode == Activity.RESULT_OK) {
            Workout workout = (Workout) data.getSerializableExtra("workout");
            workouts.add(workout);
            workoutAdapter.notifyItemInserted(workouts.size() - 1);
            recyclerView.updateEmptyView();
        }
    }
}

