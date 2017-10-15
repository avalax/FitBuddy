package de.avalax.fitbuddy.presentation.welcome_screen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.MainActivity;

public class WorkoutListFragment extends Fragment {

    @Inject
    WorkoutRepository workoutRepository;

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
        workoutAdapter = new WorkoutAdapter((MainActivity) getActivity(), workouts);
        recyclerView.setAdapter(workoutAdapter);
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

}
