package de.avalax.fitbuddy.core.workout;

import java.io.Serializable;

public class WorkoutId implements Serializable {
    private String id;

    public WorkoutId(String id) {
        this.id = id;
    }

    public WorkoutId(WorkoutId workoutId) {
        this(workoutId.id());
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof WorkoutId && id.equals(((WorkoutId) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "WorkoutId [id="+id+"]";
    }
}
