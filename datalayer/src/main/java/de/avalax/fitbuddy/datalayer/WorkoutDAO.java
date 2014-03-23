package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.Workout;

import java.util.List;

public interface WorkoutDAO {
    void save(Workout workout);
    Workout load(int position);
    List<String> getList();
    void remove(Workout workout);
}
