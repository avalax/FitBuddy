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
    public Exercise getExercise(int position) {
        return workout.getExercise(position);
    }

    @Override
    public Set getCurrentSet(int position) {
        return workout.getCurrentSet(position);
    }

    @Override
    public String getName(int position) {
        return workout.getName(position);
    }

    @Override
    public int getReps(int position) {
        return workout.getReps(position);
    }

    @Override
    public void setReps(int position, int reps) {
        workout.setReps(position, reps);
    }

    @Override
    public void setTendency(int position, Tendency tendency) {
        workout.setTendency(position, tendency);
    }

    @Override
    public int getExerciseCount() {
        return workout.getExerciseCount();
    }
}
