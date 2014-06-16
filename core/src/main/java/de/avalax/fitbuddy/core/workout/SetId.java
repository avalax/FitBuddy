package de.avalax.fitbuddy.core.workout;

import java.io.Serializable;

public class SetId implements Serializable {

    private long id;

    public SetId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SetId && id == ((SetId) o).id;
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
