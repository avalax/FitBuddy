package de.avalax.fitbuddy.domain.model.set;

import java.io.Serializable;
import java.util.Collection;

public interface Sets extends Serializable, Iterable<Set> {
    Set createSet();

    @Deprecated
        // Remove exception
    Set get(int index) throws SetException;

    int size();

    void setCurrentSet(int index) throws SetException;

    @Deprecated
    // Remove exception
    int indexOfCurrentSet() throws SetException;

    void add(Set set);

    void set(int position, Set set);

    void removeAll(Collection<Set> sets);
}
