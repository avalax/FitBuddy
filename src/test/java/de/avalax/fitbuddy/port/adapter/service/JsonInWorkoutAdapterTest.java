package de.avalax.fitbuddy.port.adapter.service;

import de.avalax.fitbuddy.domain.model.workout.WorkoutParseException;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class JsonInWorkoutAdapterTest {
    private JsonInWorkoutAdapter jsonInWorkoutAdapter;

    @Before
    public void setUp() throws Exception {
        jsonInWorkoutAdapter = new JsonInWorkoutAdapter();
    }

    @Test(expected = WorkoutParseException.class)
    public void nullInstance_shouldThrowWorkoutParseException() throws Exception {
        jsonInWorkoutAdapter.createFromJson(null);
    }

    @Test(expected = WorkoutParseException.class)
    public void emtpyString_shouldThrowWorkoutParseException() throws Exception {
        jsonInWorkoutAdapter.createFromJson("");
    }

    @Test(expected = WorkoutParseException.class)
    public void invalidJasonString_shouldThrowWorkoutParseException() throws Exception {
        jsonInWorkoutAdapter.createFromJson("NotAJsonString");
    }

    @Test
    public void emptyJson_shouldReturnEmptyWorkout() throws Exception {
        Workout workout = jsonInWorkoutAdapter.createFromJson("[\"\",[]]");

        assertThat(workout.getName(), equalTo(""));
        assertThat(workout.getExercises(), empty());
    }

    @Test
    public void jsonWithWorkoutName_shouldReturnWorkoutWithNameSet() throws Exception {
        Workout workout = jsonInWorkoutAdapter.createFromJson("[\"my workout\",[]]");

        assertThat(workout.getName(), equalTo("my workout"));
    }

    @Test
    public void jsonWithOneExecise_shouldReturnWorkoutWithExercise() throws Exception {
        //TODO: remove weightRaise
        Workout workout = jsonInWorkoutAdapter.createFromJson("[\"\",[[\"bankdrücken\",12,3,80,5]]]");

        assertThat(workout.getExercises(), hasSize(1));
        assertThat(workout.getExercises().get(0).getName(), equalTo("bankdrücken"));
        assertThat(workout.getExercises().get(0).getSets(), hasSize(3));
        assertThat(workout.getExercises().get(0).getCurrentSet().getMaxReps(), is(12));
    }
}
