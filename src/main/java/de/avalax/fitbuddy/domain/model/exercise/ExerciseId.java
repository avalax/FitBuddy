package de.avalax.fitbuddy.domain.model.exercise;

import java.io.Serializable;

public class ExerciseId implements Serializable {
    private String id;

    public ExerciseId(String id) {
        this.id = id;
    }

    public ExerciseId(ExerciseId exerciseId) {
        this(exerciseId.id());
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ExerciseId && id.equals(((ExerciseId) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "ExerciseId [id=" + id + "]";
    }
}
