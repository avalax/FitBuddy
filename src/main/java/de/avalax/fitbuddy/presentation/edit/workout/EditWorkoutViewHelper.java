package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedHashSet;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;

import static android.text.TextUtils.join;

@Deprecated
public class EditWorkoutViewHelper {

    private final String defaultWeight;
    private final String weightSuffix;
    private final DecimalFormat decimalFormat;

    public EditWorkoutViewHelper(Context context) {
        defaultWeight = context.getResources().getString(R.string.default_set_weight);
        weightSuffix = " " + context.getResources().getString(R.string.weight_suffix);
        String decimalSeparator = context.getResources().getString(R.string.decimal_separator);
        String groupingSeparator = context.getResources().getString(R.string.grouping_separator);
        DecimalFormatSymbols otherSymbols = formatSymbols(decimalSeparator, groupingSeparator);
        decimalFormat = new DecimalFormat("0.###", otherSymbols);
    }

    private DecimalFormatSymbols formatSymbols(String decimalSeparator, String groupingSeparator) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
        otherSymbols.setGroupingSeparator(groupingSeparator.charAt(0));
        return otherSymbols;
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
                weight = weightFrom(set);
            }
        }
        return weight;
    }

    private String weightFrom(Set set) {
        String weight = weightValue(set);
        if ("0".equals(weight)) {
            return defaultWeight;
        }
        return weight + weightSuffix;
    }

    private String weightValue(Set set) {
        return decimalFormat.format(set.getWeight());
    }
}
