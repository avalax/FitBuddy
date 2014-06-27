package de.avalax.fitbuddy.domain.model.workout;


import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(HierarchicalContextRunner.class)
public class BasicWorkoutTest {

    private Workout workout;
    private LinkedList<Exercise> exercises;
    private int exerciseIndex;

    @Before
    public void setUp() throws Exception {
        exerciseIndex = 0;
        exercises = new LinkedList<>();
        workout = new BasicWorkout("", exercises);
    }

    @Test
    public void testSameIdentity() throws Exception {
        Workout a1 = new BasicWorkout();
        a1.setWorkoutId(new WorkoutId("42"));
        Workout a2 = new BasicWorkout();
        a2.setWorkoutId(new WorkoutId("42"));
        Assert.assertThat(a1, equalTo(a2));
        Assert.assertThat(a1.hashCode(), equalTo(a2.hashCode()));
    }

    @Test
    public void testDifferentIdentity() throws Exception {
        Workout a1 = new BasicWorkout();
        a1.setWorkoutId(new WorkoutId("21"));
        Workout a2 = new BasicWorkout();
        a2.setWorkoutId(new WorkoutId("42"));
        Assert.assertThat(a1, not(equalTo(a2)));
        Assert.assertThat(a1.hashCode(), not(equalTo(a2.hashCode())));
    }

    @Test
    public void testDifferentIdentityWithNoId() throws Exception {
        Workout a1 = new BasicWorkout();
        Workout a2 = new BasicWorkout();
        Assert.assertThat(a1, not(equalTo(a2)));
        Assert.assertThat(a1.hashCode(), not(equalTo(a2.hashCode())));
    }

    @Test
    public void getId_shouldReturnId() throws Exception {
        WorkoutId id = new WorkoutId("42");

        workout.setWorkoutId(id);
        assertThat(workout.getWorkoutId(), equalTo(id));
    }

    @Test
    public void setNameNullValue_shouldSetNameToEmptyString() throws Exception {
        workout.setName(null);
        assertThat(workout.getName(), equalTo(""));
    }

    @Test
    public void getName_shouldReturnName() throws Exception {
        String name = "NameOfWorkout";

        workout.setName(name);
        assertThat(workout.getName(), equalTo(name));
    }

    @Test
    public void setNameWithSpace_shouldSetTrimedName() throws Exception {
        String name = " newName ";
        workout.setName(name);
        assertThat(workout.getName(), equalTo("newName"));
    }

    @Test
    public void toString_shouldReturnWorkoutInformations() throws Exception {
        String name = "NameOfWorkout";

        workout.setName(name);
        assertThat(workout.toString(), equalTo("BasicWorkout [name=" + name + "]"));
        WorkoutId workoutId = new WorkoutId("42");
        workout.setWorkoutId(workoutId);
        assertThat(workout.toString(), equalTo("BasicWorkout [name=" + name + ", workoutId=" + workoutId.toString() + "]"));
    }

    @Test
    public void addExercise_shouldAddExercise() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));

        workout.addExercise(exercise);

        assertThat(exercises, hasSize(2));
        assertThat(exercises.getLast(), equalTo(exercise));
    }

    @Test
    public void addExerciseBefore_shouldAddExerciseBeforePosition() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));

        workout.addExercise(exerciseIndex, exercise);

        assertThat(exercises, hasSize(2));
        assertThat(workout.getExercises().get(exerciseIndex), equalTo(exercise));
    }

    @Test
    public void addExerciseBefore_shouldAddExerciseAfterPosition() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(mock(Exercise.class));

        workout.addExerciseAfter(exerciseIndex, exercise);

        assertThat(exercises, hasSize(2));
        assertThat(workout.getExercises().get(exerciseIndex + 1), equalTo(exercise));
    }

    @Test
    public void replaceExercise_shouldNotAddExerciseAtPosition() throws Exception {
        Exercise exercise = new BasicExercise();
        exercise.setExerciseId(new ExerciseId("42"));

        workout.replaceExercise(exercise);

        assertThat(exercises, empty());
    }

    @Test
    public void setExercise_shouldReplaceExerciseAtPosition() throws Exception {
        Exercise exerciseToReplace = new BasicExercise();
        ExerciseId exerciseId = new ExerciseId("42");
        exerciseToReplace.setExerciseId(exerciseId);
        BasicExercise exercise = new BasicExercise();
        exercise.setExerciseId(exerciseId);
        exercises.add(exercise);

        workout.replaceExercise(exerciseToReplace);

        assertThat(exercises, hasSize(1));
        assertThat(workout.getExercises().get(exerciseIndex), equalTo(exerciseToReplace));
    }

    @Test
    public void removeExercise_shouldRemoveExerciseAtPosition() throws Exception {
        Exercise exercise = new BasicExercise();
        exercise.setExerciseId(new ExerciseId("42"));
        exercises.add(exercise);

        boolean isDeleted = workout.deleteExercise(exercise);

        assertThat(isDeleted, equalTo(true));
        assertThat(exercises, empty());
    }

    @Test
    public void deleteExercise_shouldRemoveLastOfTwoExercisesAtPosition() throws Exception {
        Exercise exercise = mock(Exercise.class);
        exercises.add(exercise);
        Exercise exerciseToDelete = mock(Exercise.class);
        exercises.add(exerciseToDelete);

        workout.deleteExercise(exerciseToDelete);

        assertThat(exercises, hasSize(1));
        assertThat(exercises.get(0), equalTo(exercise));
    }

    @Test
    public void deleteExerciseClone_shouldRemoveExercises() throws Exception {
        ExerciseId exerciseId = new ExerciseId("42");
        Exercise exercise = new BasicExercise();
        exercise.setExerciseId(exerciseId);
        exercises.add(exercise);
        Exercise clonedExercise = new BasicExercise();
        clonedExercise.setExerciseId(new ExerciseId(exerciseId));

        workout.deleteExercise(clonedExercise);

        assertThat(exercises, empty());
    }

    @Test
    public void deleteExercise_shouldRemoveFirstOfTwoExercisesAtPosition() throws Exception {
        Exercise exercise = new BasicExercise();
        ExerciseId exerciseId = new ExerciseId("42");
        exercise.setExerciseId(exerciseId);
        Exercise exerciseToDelete = new BasicExercise();
        exerciseToDelete.setExerciseId(exerciseId);
        exercises.add(exerciseToDelete);
        exercises.add(exercise);

        workout.deleteExercise(exerciseToDelete);

        assertThat(exercises, hasSize(1));
        assertThat(exercises.get(0), equalTo(exercise));
    }

    @Test
    public void removeExercise_shouldDoNothingWhenIndexIsOutOfBounce() throws Exception {
        assertThat(workout.deleteExercise(mock(Exercise.class)), equalTo(false));
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
