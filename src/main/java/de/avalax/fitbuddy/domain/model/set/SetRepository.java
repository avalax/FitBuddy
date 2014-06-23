package de.avalax.fitbuddy.domain.model.set;

import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;

import java.util.List;

public interface SetRepository {
    void save(ExerciseId id, Set set);

    void delete(SetId id);

    List<Set> allSetsBelongsTo(ExerciseId exerciseId);
}
