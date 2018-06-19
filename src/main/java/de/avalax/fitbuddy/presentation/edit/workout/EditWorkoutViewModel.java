package de.avalax.fitbuddy.presentation.edit.workout;

import android.arch.lifecycle.ViewModel;

import de.avalax.fitbuddy.domain.model.workout.Workout;

public class EditWorkoutViewModel extends ViewModel {
    private ExerciseAdapter exerciseAdapter;
    private Workout workout;

    public void init(ExerciseAdapter.ExerciseViewHolderCallback clickListener, Workout workout) {
        exerciseAdapter = new ExerciseAdapter(clickListener);
        exerciseAdapter.setExercises(workout.getExercises());
        this.workout = workout;
    }

    public ExerciseAdapter getExerciseAdapter() {
        return exerciseAdapter;
    }

    public String getName() {
        return workout.getName();
    }
}
