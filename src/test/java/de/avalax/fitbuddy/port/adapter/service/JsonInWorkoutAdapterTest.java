package de.avalax.fitbuddy.port.adapter.service;

import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParseException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
        assertThat(workout.countOfExercises(), equalTo(0));
    }

    @Test
    public void jsonWithWorkoutName_shouldReturnWorkoutWithNameSet() throws Exception {
        Workout workout = jsonInWorkoutAdapter.createFromJson("[\"my workout\",[]]");

        assertThat(workout.getName(), equalTo("my workout"));
    }

    @Test
    public void jsonWithOneExecise_shouldReturnWorkoutWithExercise() throws Exception {
        Workout workout = jsonInWorkoutAdapter.createFromJson("[\"\",[[\"bankdrücken\",[[12,80],[12,80],[12,80]]]]]");

        assertThat(workout.countOfExercises(), equalTo(1));
        assertThat(workout.exerciseAtPosition(0).getName(), equalTo("bankdrücken"));
        assertThat(workout.exerciseAtPosition(0).countOfSets(), equalTo(3));
        assertThat(workout.exerciseAtPosition(0).setAtPosition(0).getMaxReps(), is(12));
    }
}
