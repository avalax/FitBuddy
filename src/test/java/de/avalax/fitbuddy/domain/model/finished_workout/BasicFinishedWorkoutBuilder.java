package de.avalax.fitbuddy.domain.model.finished_workout;

public class BasicFinishedWorkoutBuilder {
    private FinishedWorkoutId finishedWorkoutId;

    public static BasicFinishedWorkoutBuilder aFinishedWorkout() {
        return new BasicFinishedWorkoutBuilder();
    }

    public BasicFinishedWorkoutBuilder withFinishedWorkoutId(FinishedWorkoutId finishedWorkoutId) {
        this.finishedWorkoutId = finishedWorkoutId;
        return this;
    }

    public FinishedWorkout build() {
        return new BasicFinishedWorkout(finishedWorkoutId, null, null, null, null);
    }
}
