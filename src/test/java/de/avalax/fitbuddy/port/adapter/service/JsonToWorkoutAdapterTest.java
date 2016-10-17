package de.avalax.fitbuddy.port.adapter.service;

import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParseException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsonToWorkoutAdapterTest {
    private JsonToWorkoutAdapter jsonToWorkoutAdapter;

    @Before
    public void setUp() throws Exception {
        jsonToWorkoutAdapter = new JsonToWorkoutAdapter();
    }

    @Test(expected = WorkoutParseException.class)
    public void nullInstance_shouldThrowWorkoutParseException() throws Exception {
        jsonToWorkoutAdapter.createFromJson(null);
    }

    @Test(expected = WorkoutParseException.class)
    public void emtpyString_shouldThrowWorkoutParseException() throws Exception {
        jsonToWorkoutAdapter.createFromJson("");
    }

    @Test(expected = WorkoutParseException.class)
    public void invalidJasonString_shouldThrowWorkoutParseException() throws Exception {
        jsonToWorkoutAdapter.createFromJson("NotAJsonString");
    }

    @Test
    public void emptyJson_shouldReturnEmptyWorkout() throws Exception {
        Workout workout = jsonToWorkoutAdapter.createFromJson("[\"\",[]]");

        assertThat(workout.getName(), equalTo(""));
        assertThat(workout.getExercises().size(), equalTo(0));
    }

    @Test
    public void jsonWithWorkoutName_shouldReturnWorkoutWithNameSet() throws Exception {
        Workout workout = jsonToWorkoutAdapter.createFromJson("[\"my workout\",[]]");

        assertThat(workout.getName(), equalTo("my workout"));
    }

    @Test
    public void jsonWithOneExecise_shouldReturnWorkoutWithExercise() throws Exception {
        Workout workout = jsonToWorkoutAdapter.createFromJson("[\"\",[[\"bankdrücken\",[[12,80],[12,80],[12,80]]]]]");

        assertThat(workout.getExercises().size(), equalTo(1));
        assertThat(workout.getExercises().get(0).getName(), equalTo("bankdrücken"));
        assertThat(workout.getExercises().get(0).getSets().size(), equalTo(3));
        assertThat(workout.getExercises().get(0).getSets().get(0).getMaxReps(), is(12));
    }
}
