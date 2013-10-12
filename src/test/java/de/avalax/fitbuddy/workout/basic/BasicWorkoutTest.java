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
    private int exercisePosition;

    @Before
    public void setUp() throws Exception {
        exercisePosition = 0;
        exercises = new ArrayList<>();
        workout = new BasicWorkout(exercises);
    }

    @Test
    public void getExerciseCount_ShouldReturnCountOfExercises() throws Exception {
        exercises.add(mock(Exercise.class));
        exercises.add(mock(Exercise.class));
        exercises.add(mock(Exercise.class));

        assertThat(workout.getExerciseCount(), equalTo(exercises.size()));
    }

    @Test
    public void getExercise_ShouldReturnFistExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);

        assertThat(workout.getExercise(0), equalTo(exercise));
    }

    @Test
    public void getExercise_ShouldReturnSecondExercise() throws Exception {
        int exercisePosition = 1;
        Exercise exercise = mock(Exercise.class);

        exercises.add(mock(Exercise.class));
        exercises.add(exercise);

        assertThat(workout.getExercise(exercisePosition), equalTo(exercise));
    }

    @Test
    public void getCurrentSet_ShouldReturnCurrentSet() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        Set set = mock(Set.class);

        when(exercise.getCurrentSet()).thenReturn(set);

        assertThat(workout.getCurrentSet(exercisePosition), equalTo(set));
    }

    @Test
    public void setReps_ShouldSetRepsTo12() throws Exception {

        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);

        workout.setReps(exercisePosition,12);

        verify(exercise).setReps(12);
    }

    @Test
    public void getReps_ShouldReturnRepsFromExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        when(exercise.getReps()).thenReturn(12);

        assertThat(workout.getReps(exercisePosition),equalTo(12));
    }

    @Test
    public void setTendency_ShouldSetTendencyInExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        exercises.add(mock(Exercise.class));

        workout.setTendency(exercisePosition,Tendency.NEUTRAL);

        verify(exercise).setTendency(Tendency.NEUTRAL);
    }

    @Test(expected = ExerciseNotAvailableException.class)
    public void getExercise_ShouldThrowExerciseNotAvailableExceptionWhenPositionIsGreaterThanExerciseCount() throws Exception {
        int nextExercisePosition = 1;
        exercises.add(mock(Exercise.class));

        workout.getExercise(nextExercisePosition);
    }


    @Test(expected = ExerciseNotAvailableException.class)
    public void BasicWorkout_ShouldThrowExceptionWhenSmallerThanExerciseCount() throws Exception {
        int previousExercise = -1;
        exercises.add(mock(Exercise.class));

        workout.getExercise(previousExercise);
    }
}
