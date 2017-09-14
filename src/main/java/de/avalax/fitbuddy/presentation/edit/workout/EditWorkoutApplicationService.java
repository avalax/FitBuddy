package de.avalax.fitbuddy.presentation.edit.workout;

import android.support.annotation.NonNull;

import java.util.LinkedHashSet;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;

import static android.text.TextUtils.join;

public class EditWorkoutApplicationService {
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

    @NonNull
    private java.util.Set<Integer> repsFromExercise(Exercise exercise) {
        java.util.Set<Integer> reps = new LinkedHashSet<>();
        for (Set set : exercise.getSets()) {
            reps.add(set.getMaxReps());
        }
        return reps;
    }
}
