package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.Workout;

public interface WorkoutDAO {
    void save(Workout workout);
    Workout load();
}
