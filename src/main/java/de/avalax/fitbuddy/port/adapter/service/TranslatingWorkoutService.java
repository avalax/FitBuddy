package de.avalax.fitbuddy.port.adapter.service;

import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParseException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutService;

public class TranslatingWorkoutService implements WorkoutService {

    private JsonInWorkoutAdapter jsonInWorkoutAdapter;

    public TranslatingWorkoutService(JsonInWorkoutAdapter jsonInWorkoutAdapter) {
        this.jsonInWorkoutAdapter = jsonInWorkoutAdapter;
    }

    @Override
    public Workout fromJson(String json) throws WorkoutParseException{
        return jsonInWorkoutAdapter.createFromJson(json);
    }
}
