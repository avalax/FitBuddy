package de.avalax.fitbuddy.domain.model.workout;

import java.util.List;

import de.avalax.fitbuddy.domain.model.ResourceException;

public interface WorkoutRepository {
    void save(Workout workout) throws ResourceException;

    Workout load(WorkoutId id) throws WorkoutException;

    List<Workout> loadAll();

    void delete(WorkoutId id);
}
