package de.avalax.fitbuddy.domain.model.workout;

public interface WorkoutService {
    Workout fromJson(String json) throws WorkoutParseException;
}
