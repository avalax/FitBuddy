package de.avalax.fitbuddy.domain.model.finished_set;

import java.util.List;

import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExerciseId;
import de.avalax.fitbuddy.domain.model.set.Set;

public interface FinishedSetRepository {
    void save(FinishedExerciseId finishedExerciseId, Set set);

    List<FinishedSet> allSetsBelongsTo(FinishedExerciseId finishedExerciseId);
}
