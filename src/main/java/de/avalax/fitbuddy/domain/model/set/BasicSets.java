package de.avalax.fitbuddy.domain.model.set;

import java.util.Collection;
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
    public void add(Set set) {
        sets.add(set);
    }

    @Override
    public void set(int position, Set set) {
        sets.set(position, set);
    }

    @Override
    public Set get(int position) throws SetException {
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
    public int size() {
        return sets.size();
    }

    @Override
    public Set createSet() {
        Set set = new BasicSet();
        sets.add(set);
        return set;
    }

    @Override
    public void removeAll(Collection<Set> sets) {
        this.sets.removeAll(sets);
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
