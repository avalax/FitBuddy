package de.avalax.fitbuddy.core.workout;

import java.io.Serializable;

public class WorkoutId implements Serializable {

    public long getId() {
        return id;
    }

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
}
