package de.avalax.fitbuddy.domain.model.workout;

public class BasicWorkoutBuilder {

    private Long lastExecution;
    private int finishedCount;

    public static BasicWorkoutBuilder aWorkout() {
        return new BasicWorkoutBuilder();
    }

    public Workout build() {
        Workout workout = new BasicWorkout();
        workout.setLastExecution(lastExecution);
        workout.setFinishedCount(finishedCount);
        return workout;
    }

    public BasicWorkoutBuilder withLastExecution(Long lastExecution) {
        this.lastExecution = lastExecution;
        return this;
    }

    public BasicWorkoutBuilder withFinishedCount(int finishedCount) {
        this.finishedCount = finishedCount;
        return this;
    }
}
