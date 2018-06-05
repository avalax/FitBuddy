package de.avalax.fitbuddy.presentation.edit.workout;

import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.LinkedHashSet;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.Sets;

import static android.text.TextUtils.join;

public class ExerciseBindingAdapter {
    private static DecimalFormat decimalFormat;

    static {
        decimalFormat = new DecimalFormat("0.###");
    }

    public static void setTitleFromExercise(TextView textView, Exercise exercise) {
        textView.setText(exercise.getName());
    }

    public static void setRepsFromExercise(TextView textView, Exercise exercise) {
        textView.setText(repsFromExercise(exercise));
    }

    private static String repsFromExercise(Exercise exercise) {
        java.util.Set<Integer> reps = repsFromSets(exercise.getSets());
        String label;
        if (reps.size() == 1) {
            label = exercise.getSets().size() + " x " + reps.iterator().next();
        } else {
            label = join(" - ", reps);
        }
        return label;
    }

    private static java.util.Set<Integer> repsFromSets(Sets sets) {
        java.util.Set<Integer> reps = new LinkedHashSet<>();
        for (Set set : sets) {
            reps.add(set.getMaxReps());
        }
        return reps;
    }

    public static void setWeightFromExercise(TextView textView, Exercise exercise) {
        double weight = weightFromExercise(exercise);
        int quantity = weight == 0 ? 0 : 1;
        String value = decimalFormat.format(weight);
        String label;
        if (quantity == 0) {
            label = textView.getContext().getResources()
                    .getString(R.string.default_set_weight);
        } else {
            label = textView.getContext().getResources()
                    .getQuantityString(R.plurals.weight_label, quantity, value);
        }
        textView.setText(label);
    }

    private static double weightFromExercise(Exercise exercise) {
        double exerciseWeight = 0;
        for (Set set : exercise.getSets()) {
            double setWeight = set.getWeight();
            if (setWeight > exerciseWeight) {
                exerciseWeight = setWeight;
            }
        }
        return exerciseWeight;
    }
}
