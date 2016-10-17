package de.avalax.fitbuddy.domain.model.set;

import java.util.Iterator;
import java.util.List;

public class BasicSets implements Sets {
    private List<Set> sets;
    private int setIndex;

    public BasicSets(List<Set> sets) {
        this.sets = sets;
        this.setIndex = 0;
    }

    @Override
    public int indexOfCurrentSet() throws SetException {
        if (sets.isEmpty()) {
            throw new SetException();
        }
        return setIndex;
    }

    @Override
    public Set setAtPosition(int position) throws SetException {
        if (sets.size() <= position || position < 0) {
            throw new SetException();
        }
        return sets.get(position);
    }

    @Override
    public void setCurrentSet(int position) throws SetException {
        if (sets.size() <= position || position < 0) {
            throw new SetException();
        }
        this.setIndex = position;
    }

    @Override
    public int countOfSets() {
        return sets.size();
    }

    @Override
    public Set createSet() {
        Set set = new BasicSet();
        sets.add(set);
        return set;
    }

    @Override
    public void removeSet(Set set) {
        sets.remove(set);
    }

    @Override
    public Iterator<Set> iterator() {
        return sets.iterator();
    }

    @Override
    public String toString() {
        return "BasicSets [sets=" + sets + "]";
    }
}
