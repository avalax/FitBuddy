package de.avalax.fitbuddy.presentation.edit.workout;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;

import de.avalax.fitbuddy.domain.model.workout.Workout;

public class EditWorkoutViewModel extends ViewModel {
    private ExerciseAdapter exerciseAdapter;
    private Workout workout;

    public void init(Activity activity, Workout workout) {
        exerciseAdapter = new ExerciseAdapter(activity, workout.getExercises());
        this.workout = workout;
    }

    public ExerciseAdapter getExerciseAdapter() {
        return exerciseAdapter;
    }

    public String getName() {
        return workout.getName();
    }
}
