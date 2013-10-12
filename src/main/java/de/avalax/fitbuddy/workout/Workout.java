package de.avalax.fitbuddy.workout;

import com.google.inject.ImplementedBy;
import de.avalax.fitbuddy.workout.persistence.PersistenceWorkout;

@ImplementedBy(PersistenceWorkout.class)
public interface Workout {
    Exercise getCurrentExercise();
    Set getCurrentSet();
    int getReps();
    void setReps(int reps);
    void setExerciseNumber(int exerciseNumber);
    void setTendency(Tendency tendency);
    void switchToNextExercise();
    void switchToPreviousExercise();
}
