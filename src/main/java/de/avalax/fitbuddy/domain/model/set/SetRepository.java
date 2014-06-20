package de.avalax.fitbuddy.domain.model.set;

import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;

public interface SetRepository {
    void save(ExerciseId id, Set set);

    void delete(SetId id);
}
