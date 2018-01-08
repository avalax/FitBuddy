package de.avalax.fitbuddy.domain.model.workout;

import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

public class WorkoutIdBuilder {
    private String id;
    private FinishedWorkoutRepository finishedWorkoutRepository;

    public WorkoutIdBuilder(FinishedWorkoutRepository finishedWorkoutRepository) {
        this.finishedWorkoutRepository = finishedWorkoutRepository;
        doReturn(null).when(finishedWorkoutRepository).lastCreation(any(WorkoutId.class));
    }

    public static WorkoutIdBuilder aWorkoutId(FinishedWorkoutRepository finishedWorkoutRepository) {
        return new WorkoutIdBuilder(finishedWorkoutRepository);
    }

    public WorkoutIdBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public WorkoutIdBuilder withLastExecution(Long today) {
        doReturn(today).when(finishedWorkoutRepository).lastCreation(any(WorkoutId.class));
        return this;
    }

    public WorkoutIdBuilder withFinishedCount(long count, FinishedWorkoutRepository finishedWorkoutRepository) {
        doReturn(count).when(finishedWorkoutRepository).count(any(WorkoutId.class));
        return this;
    }

    public WorkoutId build() {
        return new WorkoutId(id);
    }
}
