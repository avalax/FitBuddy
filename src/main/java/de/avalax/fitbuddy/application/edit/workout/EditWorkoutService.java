package de.avalax.fitbuddy.application.edit.workout;

import java.util.List;

import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

public class EditWorkoutService {

    private WorkoutRepository workoutRepository;

    public EditWorkoutService(
            WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public void deleteWorkout(WorkoutId workoutId) {
        workoutRepository.delete(workoutId);
    }

    public void saveWorkout(Workout workout) throws WorkoutException {
        workoutRepository.save(workout);
    }

    public List<Workout> loadAllWorkouts() {
        return workoutRepository.loadAll();
    }
}