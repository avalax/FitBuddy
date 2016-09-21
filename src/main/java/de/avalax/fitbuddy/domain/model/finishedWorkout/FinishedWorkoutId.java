package de.avalax.fitbuddy.domain.model.finishedWorkout;

import java.io.Serializable;

public class FinishedWorkoutId implements Serializable {
    private String id;

    public FinishedWorkoutId(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FinishedWorkoutId && id.equals(((FinishedWorkoutId) o).id);
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
