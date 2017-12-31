package de.avalax.fitbuddy.domain.model.finished_workout;

import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExercise;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class BasicFinishedWorkoutTest {
    private FinishedWorkout finishedWorkout;
    private WorkoutId workoutId;
    private String workoutName;
    private Long created;
    private List<FinishedExercise> finishedExercises;

    @Before
    public void setUp() throws Exception {
        workoutId = new WorkoutId("12");
        workoutName = "NameOfFinishedWorkout";
        created = 42L;
        finishedExercises = new ArrayList<>();
        finishedWorkout = new BasicFinishedWorkout(new FinishedWorkoutId("42"), workoutId, workoutName, created, finishedExercises);
    }

    @Test
    public void equalWorkoutId_shouldHaveSameIdentity() throws Exception {
        FinishedWorkout finishedWorkout2 = new BasicFinishedWorkout(new FinishedWorkoutId("42"), workoutId, null, null, finishedExercises);

        assertThat(finishedWorkout, equalTo(finishedWorkout2));
        assertThat(finishedWorkout.hashCode(), equalTo(finishedWorkout2.hashCode()));
    }

    @Test
    public void differentWorkoutIds_shouldHaveDifferentIdentity() throws Exception {
        FinishedWorkout workout2 = new BasicFinishedWorkout(new FinishedWorkoutId("21"), workoutId, null, null, finishedExercises);

        assertThat(finishedWorkout, not(equalTo(workout2)));
        assertThat(finishedWorkout.hashCode(), not(equalTo(workout2.hashCode())));
    }

    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void differntObject_shouldHaveDifferentIdentity() throws Exception {
        assertThat(finishedWorkout.equals("42"), is(false));
    }

    @Test
    public void getFinishedWorkoutId_shouldReturnId() throws Exception {
        FinishedWorkoutId id = new FinishedWorkoutId("42");

        assertThat(finishedWorkout.getFinishedWorkoutId(), equalTo(id));
    }

    @Test
    public void getWorkoutId_shouldReturnId() throws Exception {
        assertThat(finishedWorkout.getWorkoutId(), equalTo(workoutId));
    }

    @Test
    public void getName_shouldReturnName() throws Exception {
        assertThat(finishedWorkout.getName(), equalTo(workoutName));
    }

    @Test
    public void getFinishedExercises_shouldReturnFinishedExercises() throws Exception {
        assertThat(finishedWorkout.getFinishedExercises(), sameInstance(finishedExercises));
    }

    @Test
    public void getCreated_shouldReturnCreatedDate() throws Exception {
        assertThat(finishedWorkout.getCreated(), equalTo(created));
    }

    @Test
    public void toString_shouldReturnWorkoutInformation() throws Exception {
        assertThat(finishedWorkout.toString(), equalTo("BasicFinishedWorkout [name=" + finishedWorkout.getName() + ", finishedWorkoutId=" + finishedWorkout.getFinishedWorkoutId().toString() + ", workoutId=" + finishedWorkout.getWorkoutId().toString() + ", created=" + finishedWorkout.getCreated() + "]"));
    }
}