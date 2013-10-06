package de.avalax.fitbuddy.workout.basic;


import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.Workout;
import de.avalax.fitbuddy.workout.exceptions.ExerciseNotAvailableException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

public class BasicWorkoutTest {

    private Workout workout;
    private List<Exercise> exercises;

    @Before
    public void setUp() throws Exception {
        exercises = new ArrayList<>();
        workout = new BasicWorkout(exercises);
    }

    @Test
    public void BasicWorkout_ShouldReturnCurrentExerciseOnStartup() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);

        assertThat(workout.getCurrentExercise(), equalTo(exercise));
    }

    @Test
    public void BasicWorkout_ShouldReturnSecondExerciseAsCurrentExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);

        exercises.add(mock(Exercise.class));
        exercises.add(exercise);

        workout.setExerciseNumber(2);

        assertThat(workout.getCurrentExercise(), equalTo(exercise));
    }

    @Test
    public void BasicWorkout_ShouldReturnPreviousExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);

        exercises.add(exercise);
        exercises.add(mock(Exercise.class));

        workout.setExerciseNumber(2);

        assertThat(workout.getPreviousExercise(), equalTo(exercise));
    }

    @Test
    public void BasicWorkout_ShouldReturnNextExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));

        exercises.add(exercise);

        workout.setExerciseNumber(1);

        assertThat(workout.getNextExercise(), equalTo(exercise));
    }

    @Test(expected = ExerciseNotAvailableException.class)
    public void BasicWorkout_ShouldThrowExceptionWhenNoPreviousExerciseAvailable() throws Exception {
        exercises.add(mock(Exercise.class));

        workout.setExerciseNumber(1);

        workout.getPreviousExercise();
    }

    @Test(expected = ExerciseNotAvailableException.class)
    public void BasicWorkout_ShouldThrowExceptionWhenNoNextExerciseAvailable() throws Exception {
        exercises.add(mock(Exercise.class));

        workout.setExerciseNumber(1);

        workout.getNextExercise();
    }

    @Test
    public void BasicWorkout_ShouldReturnCurrentSetOnStartup() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        Set set = mock(Set.class);

        when(exercise.getCurrentSet()).thenReturn(set);

        assertThat(workout.getCurrentSet(), equalTo(set));
    }

    @Test
    public void BasicWorkout_ShouldSetRepetitionsto12() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);

        workout.setRepetitions(12);

        verify(exercise).setRepetitions(12);
    }

    @Test
    public void BasicWorkout_ShouldReturnRepetitionsFromExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        when(exercise.getRepetitions()).thenReturn(12);

        assertThat(workout.getRepetitions(),equalTo(12));
    }

    @Test
    public void BasicWorkout_ShouldSetTendencyInExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        exercises.add(mock(Exercise.class));

        workout.setTendency(Tendency.NEUTRAL);

        verify(exercise).setTendency(Tendency.NEUTRAL);
    }

    @Test
    public void BasicWorkout_ShouldSwitchToTheNextWorkout() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));
        exercises.add(exercise);

        workout.switchToNextExercise();

        assertThat(workout.getCurrentExercise(),equalTo(exercise));
    }

    @Test(expected = ExerciseNotAvailableException.class)
    public void BasicWorkout_ShouldThrowExceptionWhenSwitchingFromTheLastExercise() throws Exception {
        exercises.add(mock(Exercise.class));

        workout.switchToNextExercise();
    }

    @Test
    public void BasicWorkout_ShouldSwitchToThePreviousWorkout() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        exercises.add(mock(Exercise.class));

        workout.setExerciseNumber(2);

        workout.switchToPreviousExercise();

        assertThat(workout.getCurrentExercise(),equalTo(exercise));
    }

    @Test(expected = ExerciseNotAvailableException.class)
    public void BasicWorkout_ShouldThrowExceptionWhenSwitchingFromTheFirstExercise() throws Exception {
        exercises.add(mock(Exercise.class));

        workout.switchToPreviousExercise();
    }

    @Test
    public void BasicWorkout_ShouldSwitchToTheNextExerciseWhenSettingTendency() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));
        exercises.add(exercise);

        workout.setTendency(Tendency.MINUS);

        assertThat(workout.getCurrentExercise(),equalTo(exercise));
    }
}
