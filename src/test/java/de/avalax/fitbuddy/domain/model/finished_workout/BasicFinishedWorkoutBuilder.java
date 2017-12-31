package de.avalax.fitbuddy.domain.model.finished_workout;

import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

import static de.avalax.fitbuddy.domain.model.workout.BasicWorkoutBuilder.aWorkout;
import static java.lang.String.valueOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

public class BasicFinishedWorkoutBuilder {
    private FinishedWorkoutId finishedWorkoutId;
    private Long creation;
    private WorkoutId workoutId;

    public static BasicFinishedWorkoutBuilder aFinishedWorkout() {
        return new BasicFinishedWorkoutBuilder();
    }

    public BasicFinishedWorkoutBuilder withFinishedWorkoutId(FinishedWorkoutId finishedWorkoutId) {
        this.finishedWorkoutId = finishedWorkoutId;
        return this;
    }

    public BasicFinishedWorkoutBuilder withCreationDate(long creation) {
        this.creation = creation;
        return this;
    }

    public BasicFinishedWorkoutBuilder withInvalidWorkoutId(String workoutId, WorkoutRepository workoutRepository) throws WorkoutException {
        this.workoutId = new WorkoutId(workoutId);
        doThrow(new WorkoutException()).when(workoutRepository).load(this.workoutId);
        return this;
    }

    public BasicFinishedWorkoutBuilder withFinishedCount(int executions, WorkoutRepository workoutRepository) throws WorkoutException {
        workoutId = new WorkoutId(valueOf(executions));
        doReturn(aWorkout().withFinishedCount(executions).build())
                .when(workoutRepository).load(workoutId);
        return this;
    }

    public FinishedWorkout build() {
        return new BasicFinishedWorkout(finishedWorkoutId, workoutId, null, creation, null);
    }
}
