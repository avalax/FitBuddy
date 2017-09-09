package de.avalax.fitbuddy.domain.model.workout;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class WorkoutListEntryTest {
    private WorkoutListEntry workoutListEntry;
    private WorkoutId workoutId;
    private String name;

    @Before
    public void setUp() throws Exception {
        workoutId = new WorkoutId("42");
        name = "name";
        workoutListEntry = new WorkoutListEntry(workoutId, name);
    }

    @Test
    public void testGetWorkoutId() throws Exception {
        assertThat(workoutListEntry.getWorkoutId(), equalTo(workoutId));
    }

    @Test
    public void testGetName() throws Exception {
        assertThat(workoutListEntry.getName(), equalTo(name));
    }

    @Test
    public void toString_shouldReturnWorkoutInformation() throws Exception {
        assertThat(workoutListEntry.toString(), equalTo(name));
    }

    @Test
    public void toStringWithEmptyName_shouldReturnPlaceholder() throws Exception {
        workoutListEntry = new WorkoutListEntry(workoutId, "");
        assertThat(workoutListEntry.toString(), equalTo("unnamed workout"));
    }
}