package de.avalax.fitbuddy.presentation.edit.workout;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class ItemsWithCheckedStateTest {
    private ItemsWithCheckedState itemsWithCheckedState;

    @Before
    public void setUp() throws Exception {
        itemsWithCheckedState = new ItemsWithCheckedState();
    }

    @Test
    public void noItemsChecked_shouldReturnEmptyList() throws Exception {
        assertThat(itemsWithCheckedState.list(), emptyCollectionOf(Integer.class));
    }

    @Test
    public void oneItemChecked_shouldReturnListWithThisItem() throws Exception {
        itemsWithCheckedState.addCheckedItem(42);

        assertThat(itemsWithCheckedState.list(), containsInAnyOrder(42));
    }

    @Test
    public void oneItemCheckedTwice_shouldReturnListWithThisItemOnce() throws Exception {
        itemsWithCheckedState.addCheckedItem(42);
        itemsWithCheckedState.addCheckedItem(42);

        assertThat(itemsWithCheckedState.list(), hasSize(1));
        assertThat(itemsWithCheckedState.list(), containsInAnyOrder(42));
    }

    @Test
    public void twoItemChecked_shouldReturnListWithThisTwoItems() throws Exception {
        itemsWithCheckedState.addCheckedItem(21);
        itemsWithCheckedState.addCheckedItem(42);

        assertThat(itemsWithCheckedState.list(), containsInAnyOrder(21, 42));
    }

    @Test
    public void uncheckUnknownItem_shouldReturnListWithoutThisItem() throws Exception {
        itemsWithCheckedState.removeCheckedItem(21);

        assertThat(itemsWithCheckedState.list(), not(containsInAnyOrder(21)));
    }

    @Test
    public void uncheckItem_shouldReturnListWithoutThisItem() throws Exception {
        itemsWithCheckedState.addCheckedItem(21);
        itemsWithCheckedState.addCheckedItem(42);
        itemsWithCheckedState.removeCheckedItem(21);

        assertThat(itemsWithCheckedState.list(), not(containsInAnyOrder(21)));
    }

    @Test
    public void addedCheckedItemsInWrongOrder_shouldReturnListDescending() throws Exception {
        itemsWithCheckedState.addCheckedItem(1);
        itemsWithCheckedState.addCheckedItem(3);
        itemsWithCheckedState.addCheckedItem(2);

        Iterator<Integer> iterator = itemsWithCheckedState.list().iterator();

        assertThat(iterator.next(), equalTo(3));
        assertThat(iterator.next(), equalTo(2));
        assertThat(iterator.next(), equalTo(1));
    }

    @Test
    public void listWithCheckedItems_shouldBeCleared() throws Exception {
        itemsWithCheckedState.addCheckedItem(1);
        itemsWithCheckedState.addCheckedItem(3);
        itemsWithCheckedState.addCheckedItem(2);

        itemsWithCheckedState.clear();

        assertThat(itemsWithCheckedState.list(), emptyCollectionOf(Integer.class));
    }
}