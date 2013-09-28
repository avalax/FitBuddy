package de.avalax.fitbuddy.workout;


public interface Workout {
    WorkoutSet getPreviousWorkoutSet();
    WorkoutSet getCurrentWorkoutSet();
    WorkoutSet getNextWorkoutSet();
    Set getCurrentSet();
    int getRepetitions();
    void setWorkoutSetNumber(int workoutSetNumber);
    void setRepetitions(int repetitions);
}
