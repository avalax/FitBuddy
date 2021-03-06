package de.avalax.fitbuddy.domain.model.exercise;

import java.util.Iterator;
import java.util.List;

public class BasicExercises implements Exercises {
    private int exerciseIndex;
    private List<Exercise> exercises;

    public BasicExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public void remove(Exercise exercise) {
        exercises.remove(exercise);
    }

    @Override
    public boolean contains(Exercise exercise) {
        return exercises.contains(exercise);
    }

    @Override
    public Exercise get(int index) throws ExerciseException {
        if (exercises.size() <= index || index < 0) {
            throw new ExerciseException();
        }
        return exercises.get(index);
    }

    @Override
    public Exercise createExercise() {
        Exercise exercise = new BasicExercise();
        exercises.add(exercise);
        return exercise;
    }

    @Override
    public void setCurrentExercise(int index) throws ExerciseException {
        if (exercises.size() <= index || index < 0) {
            throw new ExerciseException();
        }
        exerciseIndex = index;
    }

    @Override
    public int indexOfCurrentExercise() throws ExerciseException {
        if (exercises.isEmpty()) {
            throw new ExerciseException();
        }
        return exerciseIndex;
    }

    @Override
    public void add(Exercise exercise) {
        exercises.add(exercise);
    }

    @Override
    public void set(int position, Exercise exercise) {
        exercises.set(position, exercise);
    }

    @Override
    public int size() {
        return exercises.size();
    }

    @Override
    public Iterator<Exercise> iterator() {
        return exercises.iterator();
    }

    @Override
    public String toString() {
        return "BasicExercises [exercises=" + exercises + "]";
    }
}
