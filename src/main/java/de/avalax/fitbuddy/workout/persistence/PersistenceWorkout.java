package de.avalax.fitbuddy.workout.persistence;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.Workout;

@Singleton
public class PersistenceWorkout implements Workout{
    private Workout workout;

    @Inject
    public PersistenceWorkout(DataLayer dataLayer) {
        workout = dataLayer.load();
    }

    @Override
    public Exercise getCurrentExercise() {
        return workout.getCurrentExercise();
    }

    @Override
    public Set getCurrentSet() {
        return workout.getCurrentSet();
    }

    @Override
    public int getReps() {
        return workout.getReps();
    }

    @Override
    public void setReps(int reps) {
        workout.setReps(reps);
    }

    @Override
    public void setExerciseNumber(int exerciseNumber) {
        workout.setExerciseNumber(exerciseNumber);
    }

    @Override
    public void setTendency(Tendency tendency) {
        workout.setTendency(tendency);
    }

    @Override
    public void switchToNextExercise() {
        workout.switchToNextExercise();
    }

    @Override
    public void switchToPreviousExercise() {
        workout.switchToPreviousExercise();
    }
}
