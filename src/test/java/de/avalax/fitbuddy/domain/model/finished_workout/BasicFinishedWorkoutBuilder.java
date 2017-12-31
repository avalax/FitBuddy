package de.avalax.fitbuddy.domain.model.finished_workout;

public class BasicFinishedWorkoutBuilder {
    private FinishedWorkoutId finishedWorkoutId;
    private Long creation;

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

    public FinishedWorkout build() {
        return new BasicFinishedWorkout(finishedWorkoutId, null, null, creation, null);
    }
}
