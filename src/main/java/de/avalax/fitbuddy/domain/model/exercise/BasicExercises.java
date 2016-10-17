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
    public void delete(Exercise exercise) {
        exercises.remove(exercise);
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
    public boolean moveExerciseAtPositionUp(int index) throws ExerciseException {
        if (index == 0) {
            return false;
        }
        Exercise exercise = get(index);
        delete(exercise);
        exercises.add(index - 1, exercise);
        return true;
    }

    @Override
    public boolean moveExerciseAtPositionDown(int index) throws ExerciseException {
        if (index + 1 == size()) {
            return false;
        }
        Exercise exercise = get(index);
        delete(exercise);
        exercises.add(index + 1, exercise);
        return true;
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
