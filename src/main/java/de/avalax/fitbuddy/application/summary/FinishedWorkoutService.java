package de.avalax.fitbuddy.application.summary;

import java.util.List;

import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutException;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;

public class FinishedWorkoutService {
    private FinishedWorkoutRepository finishedWorkoutRepository;

    public FinishedWorkoutService(FinishedWorkoutRepository finishedWorkoutRepository) {
        this.finishedWorkoutRepository = finishedWorkoutRepository;
    }

    public List<FinishedWorkout> loadAllFinishedWorkouts() {
        return finishedWorkoutRepository.loadAll();
    }

    public FinishedWorkout load(FinishedWorkoutId finishedWorkoutId) throws FinishedWorkoutException {
        return finishedWorkoutRepository.load(finishedWorkoutId);
    }

    public void delete(FinishedWorkout finishedWorkout) {
        finishedWorkoutRepository.delete(finishedWorkout.getFinishedWorkoutId());
    }

    public boolean hasFinishedWorkouts() {
        return finishedWorkoutRepository.size() > 0;
    }
}
