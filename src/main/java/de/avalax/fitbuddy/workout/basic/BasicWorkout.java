package de.avalax.fitbuddy.workout.basic;

import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.exceptions.ExerciseNotAvailableException;

import java.util.List;

public class BasicWorkout implements Workout {
    private List<Exercise> exercises;
    private int exerciseNumber;

    public BasicWorkout(List<Exercise> exercises) {
        this.exercises = exercises;
        this.exerciseNumber = 1;
    }

    @Override
    public Exercise getPreviousExercise() {
        int index = previousExerciseIndex();
        if (!isValid(index)) {
            throw new ExerciseNotAvailableException();
        }
        return exercises.get(index);
    }

    @Override
    public Exercise getCurrentExercise() {
        return exercises.get(currentExerciseIndex());
    }

    @Override
    public Exercise getNextExercise() {
        int index = nextExerciseIndex();
        if (!isValid(index)){
            throw new ExerciseNotAvailableException();
        }
        return exercises.get(index);
    }

    @Override
    public void setExerciseNumber(int exerciseNumber) {
        this.exerciseNumber = exerciseNumber;
    }

    @Override
    public void setReps(int reps) {
        getCurrentExercise().setReps(reps);
    }

    @Override
    public void setTendency(Tendency tendency) {
        getCurrentExercise().setTendency(tendency);
        switchToNextExercise();
    }

    public void switchToNextExercise() {
        if(isValid(nextExerciseIndex())){
            exerciseNumber++;
        }
        else{
            throw new ExerciseNotAvailableException();
        }
    }

    @Override
    public void switchToPreviousExercise() {
        if(isValid(previousExerciseIndex())){
            exerciseNumber--;
        }
        else{
            throw new ExerciseNotAvailableException();
        }
    }

    @Override
    public int getReps() {
        return getCurrentExercise().getReps();
    }

    @Override
    public Set getCurrentSet() {
        return getCurrentExercise().getCurrentSet();
    }

    private int previousExerciseIndex() {
        return currentExerciseIndex() - 1;
    }

    private int currentExerciseIndex() {
        return exerciseNumber - 1;
    }

    private int nextExerciseIndex() {
        return currentExerciseIndex()+1;
    }

    private boolean isValid(int index) {
        return isIndexNotNegative(index) && isIndexInArray(index);
    }

    private boolean isIndexInArray(int index) {
        return index <= exercises.size()-1;
    }

    private boolean isIndexNotNegative(int index) {
        return index >= 0;
    }
}
