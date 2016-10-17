package de.avalax.fitbuddy.domain.model.set;

import java.io.Serializable;

public interface Sets extends Serializable, Iterable<Set> {
    Set createSet();

    Set get(int index) throws SetException;

    void remove(Set set);

    int size();

    void setCurrentSet(int index) throws SetException;

    int indexOfCurrentSet() throws SetException;
}
