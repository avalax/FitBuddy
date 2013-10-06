package de.avalax.fitbuddy.workout;


public interface Workout {
    Exercise getPreviousExercise();
    Exercise getCurrentExercise();
    Exercise getNextExercise();
    Set getCurrentSet();
    int getReps();
    void setReps(int reps);
    void setExerciseNumber(int exerciseNumber);
    void setTendency(Tendency tendency);
    void switchToNextExercise();
    void switchToPreviousExercise();
}
