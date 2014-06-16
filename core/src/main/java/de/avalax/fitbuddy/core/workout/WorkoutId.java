package de.avalax.fitbuddy.core.workout;

import java.io.Serializable;

public class WorkoutId implements Serializable {
    private long id;

    public WorkoutId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof WorkoutId && id == ((WorkoutId) o).id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
