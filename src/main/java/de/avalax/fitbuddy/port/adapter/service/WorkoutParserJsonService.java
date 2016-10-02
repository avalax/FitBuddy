package de.avalax.fitbuddy.port.adapter.service;

import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParseException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParserService;

public class WorkoutParserJsonService implements WorkoutParserService {

    private JsonToWorkoutAdapter jsonToWorkoutAdapter;

    private WorkoutToJsonAdapter workoutToJsonAdapter;

    public WorkoutParserJsonService(
            JsonToWorkoutAdapter jsonToWorkoutAdapter,
            WorkoutToJsonAdapter workoutToJsonAdapter) {
        this.jsonToWorkoutAdapter = jsonToWorkoutAdapter;
        this.workoutToJsonAdapter = workoutToJsonAdapter;
    }

    @Override
    public Workout workoutFromJson(String json) throws WorkoutParseException {
        return jsonToWorkoutAdapter.createFromJson(json);
    }

    @Override
    public String jsonFromWorkout(Workout workout) {
        return workoutToJsonAdapter.fromWorkout(workout);
    }
}