package de.avalax.fitbuddy.core.workout.basic;

import de.avalax.fitbuddy.core.workout.*;
import de.avalax.fitbuddy.core.workout.exceptions.ExerciseNotAvailableException;

import java.util.LinkedList;
import java.util.List;

public class BasicWorkout implements Workout {
    private LinkedList<Exercise> exercises;
    private String name;
    private WorkoutId id;

    public BasicWorkout(LinkedList<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public WorkoutId getId() {
        return id;
    }

    @Override
    public void setId(WorkoutId id) {
        this.id = id;
    }

    @Override
    public Exercise getExercise(int index) {
        if (isIndexOutOfBounds(index)) {
            throw new ExerciseNotAvailableException();
        }
        return exercises.get(index);
    }

    @Override
    public void setReps(int index, int reps) {
        getExercise(index).setReps(reps);
    }

    @Override
    public void setTendency(int index, Tendency tendency) {
        getExercise(index).setTendency(tendency);
    }

    @Override
    public void incrementSet(int index) {
        getExercise(index).incrementCurrentSet();
    }

    @Override
    public Set getCurrentSet(int index) {
        return getExercise(index).getCurrentSet();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getProgress(int index) {
        Exercise exercise = getExercise(index);

        return (index + exercise.getProgress()) / getExerciseCount();
    }

    @Override
    public void addExercise(int index, Exercise exercise) {
        exercises.add(index, exercise);
    }

    @Override
    public void addExerciseAfter(int index, Exercise exercise) {
        exercises.add(index + 1, exercise);
    }

    @Override
    public void replaceExercise(Exercise exercise) {
        int indexOf = exercises.indexOf(exercise);
        if (indexOf >= 0) {
            exercises.set(indexOf, exercise);
        }
    }

    @Override
    public boolean deleteExercise(Exercise exercise) {
        return exercises.remove(exercise);
    }

    @Override
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    @Override
    public List<Exercise> getExercises() {
        return exercises;
    }

    @Override
    public int getExerciseCount() {
        return exercises.size();
    }

    @Override
    public int getReps(int index) {
        return getExercise(index).getReps();
    }


    private boolean isIndexOutOfBounds(int index) {
        return isIndexNegative(index) || isIndexGreaterEqualThan(index);
    }

    private boolean isIndexGreaterEqualThan(int index) {
        return index >= exercises.size();
    }

    private boolean isIndexNegative(int index) {
        return index < 0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (id == null) {
            return super.equals(o);
        }
        return o instanceof BasicWorkout && id.equals(((BasicWorkout) o).id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        return id.hashCode();
    }
}
