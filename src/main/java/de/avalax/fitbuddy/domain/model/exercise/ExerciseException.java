package de.avalax.fitbuddy.domain.model.exercise;

import de.avalax.fitbuddy.domain.model.ResourceException;

public class ExerciseException extends ResourceException {
    public ExerciseException() {
        super();
    }

    public ExerciseException(String message) {
        super(message);
    }
}
