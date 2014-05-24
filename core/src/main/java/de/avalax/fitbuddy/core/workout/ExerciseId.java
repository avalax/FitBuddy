package de.avalax.fitbuddy.core.workout;

import java.io.Serializable;

public class ExerciseId implements Serializable {

    public long getId() {
        return id;
    }

    private long id;

    public ExerciseId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ExerciseId && id == ((ExerciseId) o).id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
