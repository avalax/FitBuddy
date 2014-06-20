package de.avalax.fitbuddy.domain.model.workout;

import java.util.List;

public interface WorkoutRepository {
    void save(Workout workout);

    Workout load(WorkoutId id);

    List<Workout> getList();

    void delete(WorkoutId id);

    Workout getFirstWorkout();
}
