package de.avalax.fitbuddy.domain.model.finished_exercise;

import java.util.List;

import de.avalax.fitbuddy.domain.model.finished_set.FinishedSet;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;

public class BasicFinishedExercise implements FinishedExercise {
    private FinishedWorkoutId finishedWorkoutId;
    private String name;
    private List<FinishedSet> sets;
    private FinishedExerciseId finishedExerciseId;

    public BasicFinishedExercise(
            FinishedExerciseId finishedExerciseId,
            FinishedWorkoutId finishedWorkoutId,
            String name,
            List<FinishedSet> sets) {
        this.finishedWorkoutId = finishedWorkoutId;
        this.finishedExerciseId = finishedExerciseId;
        this.name = name;
        this.sets = sets;
    }

    @Override
    public FinishedWorkoutId getFinishedWorkoutId() {
        return finishedWorkoutId;
    }

    @Override
    public FinishedExerciseId getFinishedExerciseId() {
        return finishedExerciseId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<FinishedSet> getSets() {
        return sets;
    }

    @Override
    public String toString() {
        return "BasicFinishedExercise [name=" + name
                + ", finishedExerciseId=" + finishedExerciseId.toString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FinishedExercise
                && finishedExerciseId.equals(((FinishedExercise) obj).getFinishedExerciseId());
    }

    @Override
    public int hashCode() {
        return finishedExerciseId.hashCode();
    }
}
