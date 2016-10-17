package de.avalax.fitbuddy.domain.model.set;

import java.io.Serializable;

public interface Sets extends Serializable, Iterable<Set> {
    void setCurrentSet(int index) throws SetException;

    Set setAtPosition(int index) throws SetException;

    int countOfSets();

    Set createSet();

    void removeSet(Set set);

    int indexOfCurrentSet() throws SetException;
}
