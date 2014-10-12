package de.avalax.fitbuddy.domain.model.finishedExercise;

import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutId;

public class BasicFinishedExercise implements FinishedExercise {
    private FinishedWorkoutId finishedWorkoutId;
    private String name;
    private double weight;
    private int reps;
    private FinishedExerciseId finishedExerciseId;

    public BasicFinishedExercise(FinishedExerciseId finishedExerciseId, FinishedWorkoutId finishedWorkoutId, String name, double weight, int reps) {
        this.finishedWorkoutId = finishedWorkoutId;
        this.finishedExerciseId = finishedExerciseId;
        this.name = name;
        this.weight = weight;
        this.reps = reps;
    }

    @Override
    public FinishedWorkoutId getFinishedWorkoutId() {
        return finishedWorkoutId;
    }

    @Override
    public FinishedExerciseId getFinishedExerciseId() {
        return finishedExerciseId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public int getReps() {
        return reps;
    }

    @Override
    public String toString() {
        return "BasicFinishedExercise [name=" + name + ", finishedExerciseId=" + finishedExerciseId.toString() +", weight=" + weight + ", reps=" + reps + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FinishedExercise && finishedExerciseId.equals(((FinishedExercise) o).getFinishedExerciseId());
    }

    @Override
    public int hashCode() {
        return finishedExerciseId.hashCode();
    }
}
