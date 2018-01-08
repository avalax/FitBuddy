package de.avalax.fitbuddy.domain.model.workout;

import de.avalax.fitbuddy.domain.model.ResourceException;

public class WorkoutException extends ResourceException {
    public WorkoutException() {
        super();
    }

    public WorkoutException(Throwable e) {
        super(e);
    }

    public WorkoutException(String message) {
        super(message);
    }
}
