package de.avalax.fitbuddy.port.adapter.service;

import org.junit.Before;
import org.junit.Test;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class WorkoutToJsonAdapterTest {
    private WorkoutToJsonAdapter workoutToJsonAdapter;
    private Workout workout;

    @Before
    public void setUp() throws Exception {
        workoutToJsonAdapter = new WorkoutToJsonAdapter();
        workout = new BasicWorkout();
    }

    @Test
    public void emptyWorkout_shouldReturnJson() throws Exception {
        String json = workoutToJsonAdapter.fromWorkout(workout);

        assertThat(json, equalTo("[\"\",[]]"));
    }

    @Test
    public void emptyWorkoutWithName_shouldReturnJson() throws Exception {
        workout.setName("nameOfWorkout");

        String json = workoutToJsonAdapter.fromWorkout(workout);

        assertThat(json, equalTo("[\"nameOfWorkout\",[]]"));
    }

    @Test
    public void workoutWithOneExerciseAndNoSets_shouldReturnJson() throws Exception {
        workout.setName("nameOfWorkout");
        Exercise exercise = workout.getExercises().createExercise();

        String json = workoutToJsonAdapter.fromWorkout(workout);

        assertThat(json, equalTo("[\"nameOfWorkout\",[[\"\",[]]]]"));
    }

    @Test
    public void workoutWithOneExerciseWithNameAndOneSet_shouldReturnJson() throws Exception {
        workout.setName("nameOfWorkout");
        workout.getExercises().createExercise();
        Exercise exercise = workout.getExercises().get(0);
        exercise.setName("bankdrücken");
        Set set = exercise.getSets().createSet();
        set.setMaxReps(15);
        set.setWeight(42);

        String json = workoutToJsonAdapter.fromWorkout(workout);

        assertThat(json, equalTo("[\"nameOfWorkout\",[[\"bankdrücken\",[[42.0,15]]]]]"));
    }
}