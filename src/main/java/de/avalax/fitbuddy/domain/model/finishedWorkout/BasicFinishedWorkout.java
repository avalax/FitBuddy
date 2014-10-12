package de.avalax.fitbuddy.domain.model.finishedWorkout;

import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

public class BasicFinishedWorkout implements FinishedWorkout {
    private FinishedWorkoutId finishedWorkoutId;
    private WorkoutId workoutId;
    private String workoutName;
    private String created;

    public BasicFinishedWorkout(FinishedWorkoutId finishedWorkoutId, WorkoutId workoutId, String workoutName, String created) {
        this.finishedWorkoutId = finishedWorkoutId;
        this.workoutId = workoutId;
        this.workoutName = workoutName;
        this.created = created;
    }

    @Override
    public FinishedWorkoutId getFinishedWorkoutId() {
        return finishedWorkoutId;
    }

    @Override
    public String getCreated() {
        return created;
    }

    @Override
    public WorkoutId getWorkoutId() {
        return workoutId;
    }

    @Override
    public String getName() {
        return workoutName;
    }

    @Override
    public String toString() {
        return "BasicFinishedWorkout [name=" + workoutName + ", finishedWorkoutId=" + finishedWorkoutId.toString() + ", workoutId=" + workoutId.toString() + ", created=" + created + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FinishedWorkout && finishedWorkoutId.equals(((FinishedWorkout) o).getFinishedWorkoutId());
    }

    @Override
    public int hashCode() {
        return finishedWorkoutId.hashCode();
    }
}
