package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Set;
import de.avalax.fitbuddy.core.workout.Workout;

import java.util.TreeMap;

public interface WorkoutDAO {
    void save(Workout workout);

    void saveExercise(Workout workout, Exercise exercise);

    void deleteExercise(Exercise exercise);

    void saveSet(Exercise exercise, Set set);

    void deleteSet(Set set);

    Workout load(long position);

    TreeMap<Long, String> getList();

    void delete(Workout workout);

    Workout getFirstWorkout();
}
