package de.avalax.fitbuddy.presentation.edit.exercise;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;

public class EditExerciseViewModel extends ViewModel {
    private SetAdapter setAdapter;
    private Exercise exercise;

    public void init(Activity activity, Exercise exercise) {
        setAdapter = new SetAdapter(activity, exercise.getSets());
        this.exercise = exercise;
    }

    public SetAdapter getSetAdapter() {
        return setAdapter;
    }

    public String getName() {
        return exercise.getName();
    }
}
