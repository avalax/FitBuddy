package de.avalax.fitbuddy.core.workout;

public interface Workout {
    Exercise getExercise(int index);
    int getExerciseCount();
    int getReps(int index);
    void setReps(int index, int reps);
    void setTendency(int index, Tendency tendency);
    void incrementSet(int index);
    Set getCurrentSet(int index);
    String getName(int index);
    float getProgress(int index);
    //TODO: interface segregation
    void addExerciseBefore(int index, Exercise exercise);
    void addExerciseAfter(int index, Exercise exercise);
    void setExercise(int index, Exercise exercise);
    void removeExercise(int index);
}
