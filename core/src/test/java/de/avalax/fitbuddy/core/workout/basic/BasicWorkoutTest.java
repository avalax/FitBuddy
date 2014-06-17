package de.avalax.fitbuddy.core.workout.basic;


import de.avalax.fitbuddy.core.workout.*;
import de.avalax.fitbuddy.core.workout.exceptions.ExerciseNotAvailableException;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

@RunWith(HierarchicalContextRunner.class)
public class BasicWorkoutTest {

    private Workout workout;
    private LinkedList<Exercise> exercises;
    private int exerciseIndex;

    @Before
    public void setUp() throws Exception {
        exerciseIndex = 0;
        exercises = new LinkedList<>();
        workout = new BasicWorkout(exercises);
    }

    @Test
    public void testSameIdentity() throws Exception {
        Workout a1 = new BasicWorkout(exercises);
        a1.setWorkoutId(new WorkoutId("42"));
        Workout a2 = new BasicWorkout(exercises);
        a2.setWorkoutId(new WorkoutId("42"));
        Assert.assertThat(a1, equalTo(a2));
        Assert.assertThat(a1.hashCode(), equalTo(a2.hashCode()));
    }

    @Test
    public void testDifferentIdentity() throws Exception {
        Workout a1 = new BasicWorkout(exercises);
        a1.setWorkoutId(new WorkoutId("21"));
        Workout a2 = new BasicWorkout(exercises);
        a2.setWorkoutId(new WorkoutId("42"));
        Assert.assertThat(a1, not(equalTo(a2)));
        Assert.assertThat(a1.hashCode(), not(equalTo(a2.hashCode())));
    }

    @Test
    public void testDifferentIdentityWithNoId() throws Exception {
        Workout a1 = new BasicWorkout(exercises);
        Workout a2 = new BasicWorkout(exercises);
        Assert.assertThat(a1, not(equalTo(a2)));
        Assert.assertThat(a1.hashCode(), not(equalTo(a2.hashCode())));
    }

    @Test
    public void getExerciseCount_shouldReturnCountOfExercises() throws Exception {
        exercises.add(mock(Exercise.class));
        exercises.add(mock(Exercise.class));
        exercises.add(mock(Exercise.class));

        assertThat(workout.getExerciseCount(), equalTo(exercises.size()));
    }

    @Test
    public void getExercise_shouldReturnFistExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);

        assertThat(workout.getExercise(0), equalTo(exercise));
    }

    @Test
    public void getExercise_shouldReturnSecondExercise() throws Exception {
        int exercisePosition = 1;
        Exercise exercise = mock(Exercise.class);

        exercises.add(mock(Exercise.class));
        exercises.add(exercise);

        assertThat(workout.getExercise(exercisePosition), equalTo(exercise));
    }

    @Test
    public void getCurrentSet_shouldReturnCurrentSet() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        Set set = mock(Set.class);

        when(exercise.getCurrentSet()).thenReturn(set);

        assertThat(workout.getCurrentSet(exerciseIndex), equalTo(set));
    }

    @Test
    public void setReps_shouldSetRepsTo12() throws Exception {

        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);

        workout.setReps(exerciseIndex, 12);

        verify(exercise).setReps(12);
    }

    @Test
    public void getReps_shouldReturnRepsFromExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        when(exercise.getReps()).thenReturn(12);

        assertThat(workout.getReps(exerciseIndex), equalTo(12));
    }

    @Test
    public void getId_shouldReturnId() throws Exception {
        WorkoutId id = new WorkoutId("42");

        workout.setWorkoutId(id);
        assertThat(workout.getWorkoutId(), equalTo(id));
    }

    @Test
    public void getName_shouldReturnName() throws Exception {
        String name = "NameOfWorkout";

        workout.setName(name);
        assertThat(workout.getName(), equalTo(name));
    }

    @Test
    public void toString_shouldReturnName() throws Exception {
        String name = "NameOfWorkout";

        workout.setName(name);
        assertThat(workout.toString(), equalTo(name));
    }

    @Test
    public void incrementSet_shouldIncrementSetFromExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        exercises.add(mock(Exercise.class));

        workout.incrementSet(exerciseIndex);

        verify(exercise).incrementCurrentSet();

    }

    @Test(expected = ExerciseNotAvailableException.class)
    public void getExercise_shouldThrowExerciseNotAvailableExceptionWhenPositionIsGreaterThanExerciseCount() throws Exception {
        int nextExercisePosition = 1;
        exercises.add(mock(Exercise.class));

        workout.getExercise(nextExercisePosition);
    }

    @Test(expected = ExerciseNotAvailableException.class)
    public void getExercise_shouldThrowExceptionWhenSmallerThanExerciseCount() throws Exception {
        int previousExercise = -1;
        exercises.add(mock(Exercise.class));

        workout.getExercise(previousExercise);
    }

    @Test
    public void addExercise_shouldAddExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));

        workout.addExercise(exercise);

        assertThat(exercises.size(), is(2));
        assertThat(exercises.getLast(), equalTo(exercise));
    }

    @Test
    public void addExerciseBefore_shouldAddExerciseBeforePosition() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));

        workout.addExercise(exerciseIndex, exercise);

        assertThat(exercises.size(), is(2));
        assertThat(workout.getExercise(exerciseIndex), equalTo(exercise));
    }

    @Test
    public void addExerciseBefore_shouldAddExerciseAfterPosition() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));

        workout.addExerciseAfter(exerciseIndex, exercise);

        assertThat(exercises.size(), is(2));
        assertThat(workout.getExercise(exerciseIndex + 1), equalTo(exercise));
    }

    @Test
    public void replaceExercise_shouldNotAddExerciseAtPosition() throws Exception {
        Exercise exercise = new BasicExercise("", Collections.<Set>emptyList());
        exercise.setExerciseId(new ExerciseId("42"));

        workout.replaceExercise(exercise);

        assertThat(exercises.size(), is(0));
    }

    @Test
    public void setExercise_shouldReplaceExerciseAtPosition() throws Exception {
        Exercise exerciseToReplace = new BasicExercise("", Collections.<Set>emptyList());
        ExerciseId exerciseId = new ExerciseId("42");
        exerciseToReplace.setExerciseId(exerciseId);
        BasicExercise exercise = new BasicExercise("", Collections.<Set>emptyList());
        exercise.setExerciseId(exerciseId);
        exercises.add(exercise);

        workout.replaceExercise(exerciseToReplace);

        assertThat(exercises.size(), is(1));
        assertThat(workout.getExercise(exerciseIndex), equalTo(exerciseToReplace));
    }

    @Test
    public void removeExercise_shouldRemoveExerciseAtPosition() throws Exception {
        Exercise exercise = new BasicExercise("", Collections.<Set>emptyList());
        exercise.setExerciseId(new ExerciseId("42"));
        exercises.add(exercise);

        boolean isDeleted = workout.deleteExercise(exercise);

        assertThat(isDeleted, equalTo(true));
        assertThat(exercises.size(), is(0));
    }

    @Test
    public void deleteExercise_shouldRemoveLastOfTwoExercisesAtPosition() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        Exercise exerciseToDelete = mock(Exercise.class);
        exercises.add(exerciseToDelete);

        workout.deleteExercise(exerciseToDelete);

        assertThat(exercises.size(), is(1));
        assertThat(exercises.get(0), equalTo(exercise));
    }

    @Test
    public void deleteExerciseClone_shouldRemoveExercises() throws Exception {
        ExerciseId exerciseId = new ExerciseId("42");
        Exercise exercise = new BasicExercise("orginal",new ArrayList<Set>());
        exercise.setExerciseId(exerciseId);
        exercises.add(exercise);
        Exercise clonedExercise = new BasicExercise("clone",new ArrayList<Set>());
        clonedExercise.setExerciseId(new ExerciseId(exerciseId));

        workout.deleteExercise(clonedExercise);

        assertThat(exercises.size(), is(0));
    }

    @Test
    public void deleteExercise_shouldRemoveFirstOfTwoExercisesAtPosition() throws Exception {
        Exercise exercise = new BasicExercise("", Collections.<Set>emptyList());
        ExerciseId exerciseId = new ExerciseId("42");
        exercise.setExerciseId(exerciseId);
        Exercise exerciseToDelete = new BasicExercise("", Collections.<Set>emptyList());
        exerciseToDelete.setExerciseId(exerciseId);
        exercises.add(exerciseToDelete);
        exercises.add(exercise);

        workout.deleteExercise(exerciseToDelete);

        assertThat(exercises.size(), is(1));
        assertThat(exercises.get(0), equalTo(exercise));
    }

    @Test
    public void removeExercise_shouldDoNothingWhenIndexIsOutOfBounce() throws Exception {
        boolean b = workout.deleteExercise(mock(Exercise.class));
        assertThat(b, equalTo(false));
    }

    public class givenAnExerciseProgress {
        @Test
        public void getProgress_shouldReturnZeroProgress() throws Exception {
            Exercise exercise = mock(Exercise.class);
            exercises.add(exercise);
            when(exercise.getProgress()).thenReturn(0.0);

            assertThat(workout.getProgress(exerciseIndex), equalTo(0.0));
        }

        @Test
        public void getProgress_shouldReturnExerciseCount() throws Exception {
            Exercise exercise = mock(Exercise.class);
            exercises.add(mock(Exercise.class));
            exercises.add(exercise);
            when(exercise.getProgress()).thenReturn(1.0);

            assertThat(workout.getProgress(1), equalTo(1.0));
        }

        @Test
        public void getProgress_shouldReturn1point5() throws Exception {
            Exercise exercise = mock(Exercise.class);
            exercises.add(mock(Exercise.class));
            exercises.add(exercise);
            when(exercise.getProgress()).thenReturn(0.5);

            assertThat(workout.getProgress(1), equalTo(0.75));
        }
    }

    @Test
    public void getExercises_shouldReturnExercises() throws Exception {
        assertThat(exercises, is(workout.getExercises()));
    }
}
