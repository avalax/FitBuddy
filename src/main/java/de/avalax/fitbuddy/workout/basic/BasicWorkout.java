package de.avalax.fitbuddy.workout.basic;

import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.exceptions.ExerciseNotAvailableException;

import java.util.List;

public class BasicWorkout implements Workout {
    private List<Exercise> exercises;

    public BasicWorkout(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public Exercise getExercise(int position) {
        if (!isValid(position)) {
            throw new ExerciseNotAvailableException();
        }
        return exercises.get(position);
    }

    @Override
    public void setReps(int position,int reps) {
        getExercise(position).setReps(reps);
    }

    @Override
    public void setTendency(int position, Tendency tendency) {
        getExercise(position).setTendency(tendency);
    }

    @Override
    public void incrementSet(int position) {
        getExercise(position).incrementSet();
    }

    @Override
    public Set getCurrentSet(int position) {
        return getExercise(position).getCurrentSet();
    }

    @Override
    public String getName(int position) {
        return getExercise(position).getName();
    }

    @Override
    public int getExerciseCount() {
        return exercises.size();
    }

    @Override
    public int getReps(int position) {
        return getExercise(position).getReps();
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
