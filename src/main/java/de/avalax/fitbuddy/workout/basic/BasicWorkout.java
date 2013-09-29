package de.avalax.fitbuddy.workout.basic;

import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.WorkoutSet;
import de.avalax.fitbuddy.workout.exceptions.WorkoutSetNotAvailableException;

import java.util.List;

public class BasicWorkout implements Workout {
    private List<WorkoutSet> workoutSets;
    private int workoutSetNumber;

    public BasicWorkout(List<WorkoutSet> workoutSets) {
        this.workoutSets = workoutSets;
        this.workoutSetNumber = 1;
    }

    @Override
    public WorkoutSet getPreviousWorkoutSet() {
        int index = previousWorkoutSetIndex();
        if (!isValid(index)) {
            throw new WorkoutSetNotAvailableException();
        }
        return workoutSets.get(index);
    }

    @Override
    public WorkoutSet getCurrentWorkoutSet() {
        return workoutSets.get(currentWorkoutSetIndex());
    }

    @Override
    public WorkoutSet getNextWorkoutSet() {
        int index = nextWorkoutSetIndex();
        if (!isValid(index)){
            throw new WorkoutSetNotAvailableException();
        }
        return workoutSets.get(index);
    }

    @Override
    public void setWorkoutSetNumber(int workoutSetNumber) {
        this.workoutSetNumber = workoutSetNumber;
    }

    @Override
    public void setRepetitions(int repetitions) {
        getCurrentWorkoutSet().setRepetitions(repetitions);
    }

    @Override
    public void setTendency(Tendency tendency) {
        getCurrentWorkoutSet().setTendency(tendency);
        incrementWorkoutSetNumber();
    }

    public void incrementWorkoutSetNumber() {
        if(isValid(nextWorkoutSetIndex())){
            workoutSetNumber++;
        }
        else{
            throw new WorkoutSetNotAvailableException();
        }
    }

    @Override
    public void decrementWorkoutSetNumber() {
        if(isValid(previousWorkoutSetIndex())){
            workoutSetNumber--;
        }
        else{
            throw new WorkoutSetNotAvailableException();
        }
    }

    @Override
    public int getRepetitions() {
        return getCurrentWorkoutSet().getRepetitions();
    }

    @Override
    public Set getCurrentSet() {
        return getCurrentWorkoutSet().getCurrentSet();
    }

    private int previousWorkoutSetIndex() {
        return currentWorkoutSetIndex() - 1;
    }

    private int currentWorkoutSetIndex() {
        return workoutSetNumber - 1;
    }

    private int nextWorkoutSetIndex() {
        return currentWorkoutSetIndex()+1;
    }

    private boolean isValid(int index) {
        return isIndexNotNegative(index) && isIndexInArray(index);
    }

    private boolean isIndexInArray(int index) {
        return index <= workoutSets.size()-1;
    }

    private boolean isIndexNotNegative(int index) {
        return index >= 0;
    }
}
