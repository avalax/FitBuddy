package de.avalax.fitbuddy.domain.model.exercise;

import java.io.Serializable;

public class ExerciseId implements Serializable {
    private String id;

    public ExerciseId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ExerciseId && id.equals(((ExerciseId) obj).id);
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
