package de.avalax.fitbuddy.presentation.edit.workout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ItemsWithCheckedState {
    private List<Integer> itemsChecked;

    public ItemsWithCheckedState() {
        itemsChecked = new ArrayList<>();
    }

    public Collection<Integer> list() {
        Collections.sort(itemsChecked);
        Collections.reverse(itemsChecked);
        return Collections.unmodifiableCollection(itemsChecked);
    }

    public void addCheckedItem(int position) {
        if (!itemsChecked.contains(position)) {
            itemsChecked.add(position);
        }
    }

    public void removeCheckedItem(int position) {
        if (itemsChecked.contains(position)) {
            int pos = itemsChecked.indexOf(position);
            itemsChecked.remove(pos);
        }
    }

    public void clear() {
        itemsChecked.clear();
    }
}
