package de.avalax.fitbuddy.domain.model.workout;

import java.io.Serializable;

public class WorkoutId implements Serializable {
    private String id;

    public WorkoutId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof WorkoutId && id.equals(((WorkoutId) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "WorkoutId [id=" + id + "]";
    }
}
