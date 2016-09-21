package de.avalax.fitbuddy.port.adapter.service;

import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParseException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutService;

public class TranslatingWorkoutService implements WorkoutService {

    private JsonInWorkoutAdapter jsonInWorkoutAdapter;

    private WorkoutInJsonAdapter workoutInJsonAdapter;

    public TranslatingWorkoutService(
            JsonInWorkoutAdapter jsonInWorkoutAdapter,
            WorkoutInJsonAdapter workoutInJsonAdapter) {
        this.jsonInWorkoutAdapter = jsonInWorkoutAdapter;
        this.workoutInJsonAdapter = workoutInJsonAdapter;
    }

    @Override
    public Workout workoutFromJson(String json) throws WorkoutParseException {
        return jsonInWorkoutAdapter.createFromJson(json);
    }

    @Override
    public String jsonFromWorkout(Workout workout) {
        return workoutInJsonAdapter.fromWorkout(workout);
    }
}