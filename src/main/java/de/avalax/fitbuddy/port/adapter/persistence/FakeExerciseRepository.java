package de.avalax.fitbuddy.port.adapter.persistence;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import java.util.UUID;

public class FakeExerciseRepository implements ExerciseRepository {
    @Override
    public void save(WorkoutId id, Exercise exercise) {
        setExerciseId(exercise);
    }

    @Override
    public void save(WorkoutId id, Exercise exercise, int position) {
        setExerciseId(exercise);
    }

    private void setExerciseId(Exercise exercise) {
        if (exercise.getExerciseId() == null) {
            UUID uuid = UUID.randomUUID();
            exercise.setExerciseId(new ExerciseId(uuid.toString()));
        }
    }

    @Override
    public void delete(ExerciseId id) {
        // FAKE
    }
}
