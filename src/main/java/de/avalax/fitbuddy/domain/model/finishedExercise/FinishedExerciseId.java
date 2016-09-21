package de.avalax.fitbuddy.domain.model.finishedExercise;

import java.io.Serializable;

public class FinishedExerciseId implements Serializable {
    private String id;

    public FinishedExerciseId(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FinishedExerciseId && id.equals(((FinishedExerciseId) o).id);
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
