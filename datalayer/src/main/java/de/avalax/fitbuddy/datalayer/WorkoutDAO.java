package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.*;

import java.util.List;
import java.util.TreeMap;

public interface WorkoutDAO {
    void save(Workout workout);

    void saveExercise(WorkoutId id, Exercise exercise);

    void deleteExercise(ExerciseId id);

    void saveSet(ExerciseId id, Set set);

    void deleteSet(SetId id);

    Workout load(WorkoutId id);

    List<Workout> getList();

    void delete(WorkoutId id);

    Workout getFirstWorkout();

    void saveExercise(WorkoutId id, Exercise exercise, int position);
}
