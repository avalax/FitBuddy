package de.avalax.fitbuddy.application.edit.workout;

import java.util.List;

import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

public class EditWorkoutApplicationService {

    private FinishedWorkoutRepository finishedWorkoutRepository;

    private WorkoutRepository workoutRepository;

    private WorkoutSession workoutSession;

    public EditWorkoutApplicationService(
            WorkoutSession workoutSession,
            FinishedWorkoutRepository finishedWorkoutRepository,
            WorkoutRepository workoutRepository) {
        this.workoutSession = workoutSession;
        this.finishedWorkoutRepository = finishedWorkoutRepository;
        this.workoutRepository = workoutRepository;
    }

    @Deprecated
    // Move to workout application server
    public void switchWorkout(Workout workout) throws WorkoutException {
        if (workoutSession.hasWorkout()) {
            Workout workoutToSave = workoutSession.getWorkout();
            finishedWorkoutRepository.saveWorkout(workoutToSave);
        }
        workoutSession.switchWorkout(workout);
    }

    public void deleteWorkout(Workout workout) {
        workoutRepository.delete(workout.getWorkoutId());
    }

    public void saveWorkout(Workout workout) {
        workoutRepository.save(workout);
    }

    public List<Workout> loadAllWorkouts() {
        return workoutRepository.loadAll();
    }

    public boolean isActiveWorkout(Workout workout) {
        return workoutSession.hasWorkout()
                && workoutSession.getWorkout().getWorkoutId().equals(workout.getWorkoutId());
    }
}