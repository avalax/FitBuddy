package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Context;

import java.util.LinkedHashSet;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseViewHelper;

import static android.text.TextUtils.join;

public class EditWorkoutViewHelper {

    private final String defaultWeight;
    private EditExerciseViewHelper editExerciseViewHelper;

    public EditWorkoutViewHelper(Context context, EditExerciseViewHelper editExerciseViewHelper) {
        this.editExerciseViewHelper = editExerciseViewHelper;
        defaultWeight = context.getResources().getString(R.string.default_set_weight);
    }

    public String title(Exercise exercise) {
        String name = exercise.getName();
        if (name == null) {
            return "";
        }
        return name;

    }

    public String subtitle(Exercise exercise) {
        java.util.Set<Integer> reps = repsFromExercise(exercise);
        if (reps.size() == 1) {
            return exercise.getSets().size() + " x " + reps.iterator().next();
        }
        return join(" - ", reps);
    }

    private java.util.Set<Integer> repsFromExercise(Exercise exercise) {
        java.util.Set<Integer> reps = new LinkedHashSet<>();
        for (Set set : exercise.getSets()) {
            reps.add(set.getMaxReps());
        }
        return reps;
    }

    public String weight(Exercise exercise) {
        double exerciseWeight = 0;
        String weight = defaultWeight;
        for (Set set : exercise.getSets()) {
            double setWeight = set.getWeight();
            if (setWeight > exerciseWeight) {
                exerciseWeight = setWeight;
                weight = editExerciseViewHelper.subtitle(set);
            }
        }
        return weight;
    }
}
