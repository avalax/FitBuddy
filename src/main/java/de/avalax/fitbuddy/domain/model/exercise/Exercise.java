package de.avalax.fitbuddy.domain.model.exercise;

import java.io.Serializable;

import de.avalax.fitbuddy.domain.model.set.SetException;
import de.avalax.fitbuddy.domain.model.set.Sets;

public interface Exercise extends Serializable {
    String getName();

    double getProgress() throws SetException;

    void setExerciseId(ExerciseId exerciseId);

    ExerciseId getExerciseId();

    void setName(String name);

    Sets getSets();
}
