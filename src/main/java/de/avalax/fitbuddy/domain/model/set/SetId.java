package de.avalax.fitbuddy.domain.model.set;

import java.io.Serializable;

public class SetId implements Serializable {
    private String id;

    public SetId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SetId && id.equals(((SetId) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "SetId [id=" + id + "]";
    }
}
