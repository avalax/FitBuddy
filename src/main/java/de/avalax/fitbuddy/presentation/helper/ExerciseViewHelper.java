package de.avalax.fitbuddy.presentation.helper;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.SetNotFoundException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ExerciseViewHelper {
    private DecimalFormat decimalFormat;

    public ExerciseViewHelper(Locale locale) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        this.decimalFormat = new DecimalFormat("###.###", symbols);
    }

    public String weightOfExercise(Exercise exercise, int positionOfSet) {
        if (exercise == null) {
            return "-";
        }
        try {
            double weight = exercise.setAtPosition(positionOfSet).getWeight();
            if (weight != 0) {
                return decimalFormat.format(weight);
            } else {
                return "-";
            }
        } catch (SetNotFoundException e) {
            return "-";
        }
    }

    public String nameOfExercise(Exercise exercise) {
        if (exercise == null || exercise.getName().isEmpty()) {
            return "unnamed exercise";
        }
        return exercise.getName();
    }

    public int maxRepsOfExercise(Exercise exercise, int positionOfSet) {
        if (exercise == null) {
            return 0;
        }
        try {
            return exercise.setAtPosition(positionOfSet).getMaxReps();
        } catch (SetNotFoundException e) {
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
