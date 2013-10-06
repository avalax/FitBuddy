package de.avalax.fitbuddy.workout;


public interface Workout {
    Exercise getPreviousExercise();
    Exercise getCurrentExercise();
    Exercise getNextExercise();
    Set getCurrentSet();
    int getRepetitions();
    void setExerciseNumber(int exerciseNumber);
    void setRepetitions(int repetitions);
    void setTendency(Tendency tendency);
    void switchToNextExercise();
    void switchToPreviousExercise();
}
