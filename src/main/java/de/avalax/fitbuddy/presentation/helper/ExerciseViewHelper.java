package de.avalax.fitbuddy.presentation.helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.SetException;

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
            int indexOfCurrentSet = exercise.getSets().indexOfCurrentSet();
            double weight = exercise.getSets().get(indexOfCurrentSet).getWeight();
            if (weight == 0) {
                return "-";
            }
            return decimalFormat.format(weight);
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
}
