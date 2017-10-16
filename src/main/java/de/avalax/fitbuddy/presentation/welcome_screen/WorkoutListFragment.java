package de.avalax.fitbuddy.presentation.welcome_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.MainActivity;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity;

import static de.avalax.fitbuddy.presentation.MainActivity.ADD_WORKOUT;

public class WorkoutListFragment extends Fragment implements View.OnClickListener {

    @Inject
    WorkoutRepository workoutRepository;
    @Inject
    WorkoutSession workoutSession;

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
        workouts = workoutRepository.getWorkouts();
        workoutAdapter = new WorkoutAdapter((MainActivity) getActivity(), workoutSession, workouts);
        recyclerView.setAdapter(workoutAdapter);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_add_workout);
        floatingActionButton.setOnClickListener(this);
        Toolbar toolbar = view.findViewById(R.id.toolbar_main);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return view;
    }

    public void addWorkout(Workout workout) {
        workouts.add(workout);
        workoutAdapter.notifyItemInserted(workouts.size() - 1);
        recyclerView.updateEmptyView();
    }

    public void updateWorkout(Integer position, Workout workout) {
        workouts.set(position, workout);
        workoutAdapter.notifyItemChanged(position);
        recyclerView.updateEmptyView();
    }

    public void removeWorkout(Workout workout) {
        workouts.remove(workout);
        recyclerView.updateEmptyView();
    }

    @Override
    public void onClick(View view) {
        //TODO: use this fragment in handle result
        Intent intent = new Intent(getActivity(), EditWorkoutActivity.class);
        intent.putExtra("workout", new BasicWorkout());
        getActivity().startActivityForResult(intent, ADD_WORKOUT);
    }
}

