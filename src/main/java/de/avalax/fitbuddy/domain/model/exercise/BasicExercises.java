package de.avalax.fitbuddy.domain.model.exercise;

import java.util.Collections;
import java.util.List;

public class BasicExercises implements Exercises {
    private int exerciseIndex;
    private List<Exercise> exercises;

    public BasicExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public void deleteExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    @Override
    public Exercise exerciseAtPosition(int index) throws ExerciseException {
        if (exercises.size() <= index || index < 0) {
            throw new ExerciseException();
        }
        return exercises.get(index);
    }

    @Override
    public Exercise createExercise() {
        return createExercise(exercises.size());
    }

    @Override
    public Exercise createExercise(int position) {
        Exercise exercise = new BasicExercise();
        exercise.createSet();
        exercises.add(position, exercise);
        return exercise;
    }

    @Override
    public void addExercise(int position, Exercise exercise) {
        exercises.add(position, exercise);
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
    public boolean moveExerciseAtPositionUp(int index) throws ExerciseException {
        if (index == 0) {
            return false;
        }
        Exercise exercise = exerciseAtPosition(index);
        deleteExercise(exercise);
        addExercise(index - 1, exercise);
        return true;
    }

    @Override
    public boolean moveExerciseAtPositionDown(int index) throws ExerciseException {
        if (index + 1 == countOfExercises()) {
            return false;
        }
        Exercise exercise = exerciseAtPosition(index);
        deleteExercise(exercise);
        addExercise(index + 1, exercise);
        return true;
    }

    @Override
    public int countOfExercises() {
        return exercises.size();
    }

    @Override
    public List<Exercise> exercisesOfWorkout() {
        return Collections.unmodifiableList(exercises);
    }

    @Override
    public String toString() {
        return "BasicExercises [exercises=" + exercises + "]";
    }
}
