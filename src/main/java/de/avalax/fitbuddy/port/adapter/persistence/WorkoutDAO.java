package de.avalax.fitbuddy.port.adapter.persistence;

import de.avalax.fitbuddy.domain.model.*;

import java.util.List;

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
