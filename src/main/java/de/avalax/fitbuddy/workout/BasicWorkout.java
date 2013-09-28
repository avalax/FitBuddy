package de.avalax.fitbuddy.workout;

import java.util.List;

public class BasicWorkout implements Workout{
    private List<WorkoutSet> workoutSets;
    private int workoutSetNumber;

    public BasicWorkout(List<WorkoutSet> workoutSets) {
        this.workoutSets = workoutSets;
        this.workoutSetNumber = 1;
    }

    @Override
    public WorkoutSet getPreviousWorkoutSet() {
        int index = previousWorkoutSetIndex();
        if (isValid(index)) {
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
        if (isValid(index)){
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
        getCurrentSet().setRepetitions(repetitions);
    }

    @Override
    public int getRepetitions() {
        return getCurrentSet().getRepetitions();
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
        return index < 0 || index > workoutSets.size()-1;
    }
}
