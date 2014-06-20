package de.avalax.fitbuddy.domain.model.workout;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetId;

import java.util.List;

public interface WorkoutRepository {
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
