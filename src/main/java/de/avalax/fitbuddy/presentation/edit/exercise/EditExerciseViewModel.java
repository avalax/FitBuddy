package de.avalax.fitbuddy.presentation.edit.exercise;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;

public class EditExerciseViewModel extends ViewModel {
    private EditExerciseViewHelper editExerciseViewHelper;
    private SetAdapter setAdapter;

    public void init(Activity activity, EditExerciseViewHelper editExerciseViewHelper, Exercise exercise) {
        this.editExerciseViewHelper = editExerciseViewHelper;
        setAdapter = new SetAdapter(activity, exercise.getSets(), editExerciseViewHelper);
    }

    public SetAdapter getSetAdapter() {
        return setAdapter;
    }
}
