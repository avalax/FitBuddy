package de.avalax.fitbuddy.presentation.helper;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.SetException;

public class ExerciseViewHelper {
    private final int titleLength;
    private final String defaultText;
    private final String defaultWeight;
    private final DecimalFormat decimalFormat;

    public ExerciseViewHelper(Context context, Locale locale) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        this.decimalFormat = new DecimalFormat("###.###", symbols);
        this.defaultText = context.getResources().getString(R.string.placeholder_title);
        this.defaultWeight = context.getResources().getString(R.string.default_set_weight);
        this.titleLength = 5;
    }

    public String weightOfExercise(Exercise exercise) {
        if (exercise == null) {
            return defaultWeight;
        }
        try {
            int indexOfCurrentSet = exercise.getSets().indexOfCurrentSet();
            double weight = exercise.getSets().get(indexOfCurrentSet).getWeight();
            if (weight == 0) {
                return defaultWeight;
            }
            return decimalFormat.format(weight) + " kg";
        } catch (SetException e) {
            return defaultWeight;
        }
    }

    public int maxRepsOfExercise(Exercise exercise) {
        if (exercise == null) {
            return 0;
        }
        try {
            int indexOfCurrentSet = exercise.getSets().indexOfCurrentSet();
            return exercise.getSets().get(indexOfCurrentSet).getMaxReps();
        } catch (SetException e) {
            return 0;
        }
    }

    public int setCountOfExercise(Exercise exercise) {
        if (exercise == null) {
            return 0;
        }
        return exercise.getSets().size();
    }

    public String cutPreviousExerciseName(Exercise exercise) {
        String name = exercise.getName();
        if (name.isEmpty()) {
            name = defaultText;
        }
        if (name.length() < titleLength) {
            return name;
        }
        return name.substring(name.length() - titleLength);
    }

    public String cutNextExerciseName(Exercise exercise) {
        String name = exercise.getName();
        if (name.isEmpty()) {
            name = defaultText;
        }
        if (name.length() < titleLength) {
            return name;
        }
        return name.substring(0, titleLength);
    }

    public String exerciseName(Exercise exercise) {
        String name = exercise.getName();
        if (name.isEmpty()) {
            name = defaultText;
        }
        return name + ":";
    }
}
