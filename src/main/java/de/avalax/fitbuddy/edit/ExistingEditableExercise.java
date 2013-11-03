package de.avalax.fitbuddy.edit;

import de.avalax.fitbuddy.workout.Exercise;

public class ExistingEditableExercise implements EditableExercise {
    private String name;
    private int reps;
    private int sets;
    private double weight;
    private double weightRaise;

    public ExistingEditableExercise(Exercise exercise) {
        name = exercise.getName();
        reps = exercise.getReps();
        sets = exercise.getMaxSets();
        weight = exercise.getWeight();
        weightRaise = exercise.getWeightRaise();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getReps() {
        return reps;
    }

    @Override
    public void setReps(int reps) {
        this.reps = reps;
    }

    @Override
    public int getSets() {
        return sets;
    }

    @Override
    public void setSets(int sets) {
        this.sets = sets;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeightRaise() {
        return weightRaise;
    }

    @Override
    public void setWeightRaise(double weightRaise) {
        this.weightRaise = weightRaise;
    }
}
