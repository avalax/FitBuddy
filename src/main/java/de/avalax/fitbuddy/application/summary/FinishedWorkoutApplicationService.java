package de.avalax.fitbuddy.application.summary;

import java.util.List;

import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;

public class FinishedWorkoutApplicationService {
    private FinishedWorkoutRepository finishedWorkoutRepository;

    public FinishedWorkoutApplicationService(FinishedWorkoutRepository finishedWorkoutRepository) {
        this.finishedWorkoutRepository = finishedWorkoutRepository;
    }

    public List<FinishedWorkout> allFinishedWorkouts() {
        return finishedWorkoutRepository.loadAll();
    }
}
