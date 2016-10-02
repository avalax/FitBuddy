package de.avalax.fitbuddy.domain.model.workout;

public interface WorkoutParserService {
    Workout workoutFromJson(String json) throws WorkoutParseException;

    String jsonFromWorkout(Workout workout);
}
