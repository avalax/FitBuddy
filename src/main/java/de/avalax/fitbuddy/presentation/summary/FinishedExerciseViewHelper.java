package de.avalax.fitbuddy.presentation.summary;

import java.util.LinkedHashSet;

import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.finished_set.FinishedSet;

import static android.text.TextUtils.join;

public class FinishedExerciseViewHelper {
    public String subtitle(FinishedExercise exercise) {
        java.util.Set<Integer> reps = repsFromExercise(exercise);
        if (reps.size() == 1) {
            return exercise.getSets().size() + " x " + reps.iterator().next();
        }
        return join(" - ", reps);
    }

    private java.util.Set<Integer> repsFromExercise(FinishedExercise exercise) {
        java.util.Set<Integer> reps = new LinkedHashSet<>();
        for (FinishedSet set : exercise.getSets()) {
            reps.add(set.getMaxReps());
        }
        return reps;
    }
}
