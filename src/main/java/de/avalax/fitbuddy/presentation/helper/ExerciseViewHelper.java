package de.avalax.fitbuddy.presentation.helper;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.SetException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ExerciseViewHelper {
    private DecimalFormat decimalFormat;

    public ExerciseViewHelper(Locale locale) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        this.decimalFormat = new DecimalFormat("###.###", symbols);
    }

    public String weightOfExercise(Exercise exercise) {
        if (exercise == null) {
            return "-";
        }
        try {
            int indexOfCurrentSet = exercise.indexOfCurrentSet();
            double weight = exercise.setAtPosition(indexOfCurrentSet).getWeight();
            if (weight != 0) {
                return decimalFormat.format(weight);
            } else {
                return "-";
            }
        } catch (SetException e) {
            return "-";
        }
    }

    public String nameOfExercise(Exercise exercise) {
        if (exercise == null || exercise.getName().isEmpty()) {
            return "unnamed exercise";
        }
        return exercise.getName();
    }

    public int maxRepsOfExercise(Exercise exercise) {
        if (exercise == null) {
            return 0;
        }
        try {
            int indexOfCurrentSet = exercise.indexOfCurrentSet();
            return exercise.setAtPosition(indexOfCurrentSet).getMaxReps();
        } catch (SetException e) {
            return 0;
        }
    }

    public int setCountOfExercise(Exercise exercise) {
        if (exercise == null) {
            return 0;
        }
        return exercise.countOfSets();
    }
}
