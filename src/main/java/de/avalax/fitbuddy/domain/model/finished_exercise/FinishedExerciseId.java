package de.avalax.fitbuddy.domain.model.finished_exercise;

import java.io.Serializable;

public class FinishedExerciseId implements Serializable {
    private String id;

    public FinishedExerciseId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FinishedExerciseId && id.equals(((FinishedExerciseId) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "FinishedExerciseId [id=" + id + "]";
    }
}
