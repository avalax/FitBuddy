package de.avalax.fitbuddy.domain.model.finishedExercise;

import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutId;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class BasicFinishedExerciseTest {
    private FinishedExercise finishedWorkout;
    private String exerciseName;
    private FinishedExerciseId finishedExerciseId;
    private int reps;
    private double weight;

    @Before
    public void setUp() throws Exception {
        reps = 15;
        weight = 12.34;
        exerciseName = "NameOfFinishedExercise";
        finishedExerciseId = new FinishedExerciseId("42");
        finishedWorkout = new BasicFinishedExercise(finishedExerciseId, new FinishedWorkoutId("12"), exerciseName, weight, reps);
    }

    @Test
    public void equalWorkoutId_shouldHaveSameIdentity() throws Exception {
        FinishedExercise finishedWorkout2 = new BasicFinishedExercise(new FinishedExerciseId("42"), null, null, weight, reps);

        assertThat(finishedWorkout, equalTo(finishedWorkout2));
        assertThat(finishedWorkout.hashCode(), equalTo(finishedWorkout2.hashCode()));
    }

    @Test
    public void differentWorkoutIds_shouldHaveDifferentIdentity() throws Exception {
        FinishedExercise finishedWorkout2 = new BasicFinishedExercise(new FinishedExerciseId("21"), null, null, weight, reps);

        assertThat(finishedWorkout, not(equalTo(finishedWorkout2)));
        assertThat(finishedWorkout.hashCode(), not(equalTo(finishedWorkout2.hashCode())));
    }

    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void differntObject_shouldHaveDifferentIdentity() throws Exception {
        assertThat(finishedWorkout.equals("42"), is(false));
    }

    @Test
    public void getFinishedExerciseId_shouldReturnId() throws Exception {
        assertThat(finishedWorkout.getFinishedExerciseId(), equalTo(finishedExerciseId));
    }

    @Test
    public void getFinishedWorkoutId_shouldReturnId() throws Exception {
        FinishedWorkoutId id = new FinishedWorkoutId("12");

        assertThat(finishedWorkout.getFinishedWorkoutId(), equalTo(id));
    }

    @Test
    public void getName_shouldReturnName() throws Exception {
        assertThat(finishedWorkout.getName(), equalTo(exerciseName));
    }

    @Test
    public void getReps_shouldReturnReps() throws Exception {
        assertThat(finishedWorkout.getReps(), equalTo(reps));
    }

    @Test
    public void getWeight_shouldReturnWeight() throws Exception {
        assertThat(finishedWorkout.getWeight(), equalTo(weight));
    }

    @Test
    public void toString_shouldReturnWorkoutInformations() throws Exception {
        assertThat(finishedWorkout.toString(), equalTo("BasicFinishedExercise [name=" + finishedWorkout.getName() + ", finishedExerciseId=" + finishedWorkout.getFinishedExerciseId().toString() + ", weight=" + weight + ", reps=" + reps + "]"));
    }
}