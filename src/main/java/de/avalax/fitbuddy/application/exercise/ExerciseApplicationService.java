package de.avalax.fitbuddy.application.exercise;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;

import java.text.DecimalFormat;

public class ExerciseApplicationService {
    private DecimalFormat decimalFormat;

    public ExerciseApplicationService() {
        this.decimalFormat = new DecimalFormat("###.#");
    }

    public String weightOfExercise(Exercise exercise) {
        if (exercise.getSets().isEmpty()) {
            return "-";
        }
        double weight = exercise.getCurrentSet().getWeight();
        if (weight > 0) {
            return decimalFormat.format(weight);
        } else {
            return "-";
        }
    }

    public String nameOfExercise(Exercise exercise) {
        return exercise.getName().length() > 0 ? exercise.getName() : "unnamed exercise";
    }

    public int maxRepsOfExercise(Exercise exercise) {
        return exercise.getSets().isEmpty() ? 0 : exercise.getCurrentSet().getMaxReps();
    }

    public int setCountOfExercise(Exercise exercise) {
        return exercise.getSets().isEmpty() ? 0 : exercise.getSets().size();
    }
}
