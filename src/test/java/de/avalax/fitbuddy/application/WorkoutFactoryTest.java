package de.avalax.fitbuddy.application;

import de.avalax.fitbuddy.domain.model.workout.Workout;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class WorkoutFactoryTest {
    private WorkoutFactory workoutFactory;

    @Before
    public void setUp() throws Exception {
        workoutFactory = new WorkoutFactory();
    }

    @Test(expected = WorkoutParseException.class)
    public void nullInstance_shouldThrowWorkoutParseException() throws Exception {
        workoutFactory.createFromJson(null);
    }

    @Test(expected = WorkoutParseException.class)
    public void emtpyString_shouldThrowWorkoutParseException() throws Exception {
        workoutFactory.createFromJson("");
    }

    @Test(expected = WorkoutParseException.class)
    public void invalidJasonString_shouldThrowWorkoutParseException() throws Exception {
        workoutFactory.createFromJson("NotAJsonString");
    }

    @Test
    public void emptyJson_shouldReturnEmptyWorkout() throws Exception {
        Workout workout = workoutFactory.createFromJson("[\"\",[]]");

        assertThat(workout.getName(), equalTo(""));
        assertThat(workout.getExercises(), empty());
    }

    @Test
    public void jsonWithWorkoutName_shouldReturnWorkoutWithNameSet() throws Exception {
        Workout workout = workoutFactory.createFromJson("[\"my workout\",[]]");

        assertThat(workout.getName(), equalTo("my workout"));
    }

    @Test
    public void jsonWithOneExecise_shouldReturnWorkoutWithExercise() throws Exception {
        //TODO: remove weightRaise
        Workout workout = workoutFactory.createFromJson("[\"\",[[\"bankdrücken\",12,3,80,5]]]");

        assertThat(workout.getExercises(), hasSize(1));
        assertThat(workout.getExercises().get(0).getName(), equalTo("bankdrücken"));
        assertThat(workout.getExercises().get(0).getSets(), hasSize(3));
        assertThat(workout.getExercises().get(0).getCurrentSet().getMaxReps(), is(12));
    }

    @Test
    public void createNewWorkout_shouldReturnAWorkoutWithoutExercises() throws Exception {
        Workout workout = workoutFactory.createNew();

        assertThat(workout.getName(), equalTo(""));
        assertThat(workout.getExercises(), empty());
    }
}
