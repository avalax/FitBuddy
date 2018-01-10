package de.avalax.fitbuddy.domain.model.finished_workout;

import java.io.Serializable;

public class FinishedWorkoutId implements Serializable {
    private String id;

    public FinishedWorkoutId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FinishedWorkoutId && id.equals(((FinishedWorkoutId) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "FinishedWorkoutId [id=" + id + "]";
    }
}
