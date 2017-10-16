package de.avalax.fitbuddy.presentation.workout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetException;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;

public class WorkoutFragment extends Fragment {

    @Inject
    WorkoutSession workoutSession;

    private TextView exerciseNameTextView;
    private TextView exerciseWeightTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        exerciseNameTextView = view.findViewById(R.id.exercises_bar_name);
        exerciseWeightTextView = view.findViewById(R.id.exercises_bar_weight);
        Workout workout = workoutSession.getWorkout();
        try {
            int exerciseIndex = workout.getExercises().indexOfCurrentExercise();
            Exercise exercise = workout.getExercises().get(exerciseIndex);
            int setIndex = exercise.getSets().indexOfCurrentSet();
            Set set = exercise.getSets().get(setIndex);
            exerciseNameTextView.setText(exercise.getName());
            exerciseWeightTextView.setText(set.getWeight() + " kg");

        } catch (ExerciseException | SetException e) {
            Log.e("ExerciseException", e.getMessage(), e);
        }

        return view;
    }
}
