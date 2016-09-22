package de.avalax.fitbuddy.domain.model.workout;

import java.io.IOException;

import de.avalax.fitbuddy.domain.model.ResourceException;

public class WorkoutException extends ResourceException {
    public WorkoutException() {
        super();
    }

    public WorkoutException(Throwable e) {
        super(e);
    }
}
