package de.avalax.fitbuddy.application.edit.workout;

import java.util.List;

import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

public class EditWorkoutApplicationService {

    private WorkoutRepository workoutRepository;

    public EditWorkoutApplicationService(
            WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public void deleteWorkout(Workout workout) {
        workoutRepository.delete(workout.getWorkoutId());
    }

    public void saveWorkout(Workout workout) throws WorkoutException{
        workoutRepository.save(workout);
    }

    public List<Workout> loadAllWorkouts() {
        return workoutRepository.loadAll();
    }
}