package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.Workout;

import java.util.TreeMap;

public interface WorkoutDAO {
    void save(Workout workout);

    Workout load(long position);

    TreeMap<Long, String> getList();

    void delete(Workout workout);

    Workout getFirstWorkout();
}
