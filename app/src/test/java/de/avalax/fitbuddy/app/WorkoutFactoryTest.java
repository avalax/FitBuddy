package de.avalax.fitbuddy.app;

import de.avalax.fitbuddy.core.workout.Workout;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class WorkoutFactoryTest {
    private WorkoutFactory workoutFactory;

    @Before
    public void setUp() throws Exception {
        workoutFactory = new WorkoutFactory();
    }

    @Test(expected = WorkoutParseException.class)
    public void nullInstance_shouldThrowWorkoutParseException() throws Exception {
        workoutFactory.fromJson(null);
    }

    @Test(expected = WorkoutParseException.class)
         public void emtpyString_shouldThrowWorkoutParseException() throws Exception {
        workoutFactory.fromJson("");
    }

    @Test(expected = WorkoutParseException.class)
    public void invalidJasonString_shouldThrowWorkoutParseException() throws Exception {
        workoutFactory.fromJson("NotAJsonString");
    }

    @Test
    public void emptyJson_shouldReturnEmptyWorkout() throws Exception {
        Workout workout = workoutFactory.fromJson("[\"\",[]]");

        assertThat(workout.getName(), equalTo(""));
        assertThat(workout.getExerciseCount(), is(0));
    }

    @Test
    public void jsonWithWorkoutName_shouldReturnWorkoutWithNameSet() throws Exception {
        Workout workout = workoutFactory.fromJson("[\"my workout\",[]]");

        assertThat(workout.getName(), equalTo("my workout"));
    }

    @Test
    public void jsonWithOneExecise_shouldReturnWorkoutWithExercise() throws Exception {
        Workout workout = workoutFactory.fromJson("[\"\",[[\"bankdrücken\",12,3,80,5]]]");

        assertThat(workout.getExerciseCount(), is(1));
        assertThat(workout.getExercise(0).getName(), equalTo("bankdrücken"));
        assertThat(workout.getExercise(0).getMaxSets(), is(3));
        assertThat(workout.getExercise(0).getMaxReps(), is(12));
    }
}
