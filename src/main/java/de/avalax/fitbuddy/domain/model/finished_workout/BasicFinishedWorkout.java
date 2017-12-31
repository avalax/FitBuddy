package de.avalax.fitbuddy.domain.model.finished_workout;

import java.util.List;

import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

public class BasicFinishedWorkout implements FinishedWorkout {
    private FinishedWorkoutId finishedWorkoutId;
    private WorkoutId workoutId;
    private String workoutName;
    private Long created;
    private List<FinishedExercise> finishedExercises;

    public BasicFinishedWorkout(
            FinishedWorkoutId finishedWorkoutId,
            WorkoutId workoutId,
            String workoutName,
            Long created,
            List<FinishedExercise> finishedExercises) {
        this.finishedWorkoutId = finishedWorkoutId;
        this.workoutId = workoutId;
        this.workoutName = workoutName;
        this.created = created;
        this.finishedExercises = finishedExercises;
    }

    @Override
    public FinishedWorkoutId getFinishedWorkoutId() {
        return finishedWorkoutId;
    }

    @Override
    public Long getCreated() {
        return created;
    }

    @Override
    public List<FinishedExercise> getFinishedExercises() {
        return finishedExercises;
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
        return "BasicFinishedWorkout [name=" + workoutName
                + ", finishedWorkoutId=" + finishedWorkoutId.toString()
                + ", workoutId=" + workoutId.toString()
                + ", created=" + created + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FinishedWorkout
                && finishedWorkoutId.equals(((FinishedWorkout) o).getFinishedWorkoutId());
    }

    @Override
    public int hashCode() {
        return finishedWorkoutId.hashCode();
    }
}
