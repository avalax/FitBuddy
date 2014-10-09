package de.avalax.fitbuddy.domain.model.workout;


import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;

@RunWith(HierarchicalContextRunner.class)
public class BasicWorkoutTest {
    private Workout workout;

    @Before
    public void setUp() throws Exception {
        workout = new BasicWorkout();
    }

    @Test
    public void equalWorkoutId_shouldHaveSameIdentity() throws Exception {
        workout.setWorkoutId(new WorkoutId("42"));
        Workout workout2 = new BasicWorkout();
        workout2.setWorkoutId(new WorkoutId("42"));

        assertThat(workout, equalTo(workout2));
        assertThat(workout.hashCode(), equalTo(workout2.hashCode()));
    }

    @Test
    public void differentWorkoutIds_shouldHaveDifferentIdentity() throws Exception {
        workout.setWorkoutId(new WorkoutId("21"));

        Workout workout2 = new BasicWorkout();
        workout2.setWorkoutId(new WorkoutId("42"));

        assertThat(workout, not(equalTo(workout2)));
        assertThat(workout.hashCode(), not(equalTo(workout2.hashCode())));
    }

    @Test
    public void nullValue_shouldHaveDifferentIdentity() throws Exception {
        Workout workout2 = new BasicWorkout();

        assertThat(workout, not(equalTo(workout2)));
        assertThat(workout.hashCode(), not(equalTo(workout2.hashCode())));
    }

    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void differntObject_shouldHaveDifferentIdentity() throws Exception {
        workout.setWorkoutId(new WorkoutId("42"));
        assertThat(workout.equals("42"), is(false));
    }

    @Test
    public void getId_shouldReturnId() throws Exception {
        WorkoutId id = new WorkoutId("42");

        workout.setWorkoutId(id);

        assertThat(workout.getWorkoutId(), equalTo(id));
    }

    @Test
    public void nullValue_shouldSetNameToEmptyString() throws Exception {
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

    @Test(expected = ExerciseNotFoundException.class)
    public void indexOfCurrentExercise_shouldThrowExerciseNotFoundException() throws Exception {
        workout.indexOfCurrentExercise();
    }

    @Test(expected = ExerciseNotFoundException.class)
    public void setCurrentExercise_shouldThrowExerciseNotFoundException() throws Exception {
        workout.setCurrentExercise(0);
    }

    @Test(expected = ExerciseNotFoundException.class)
    public void setCurrentExerciseToNegativ_shouldThrowExerciseNotFoundException() throws Exception {
        workout.setCurrentExercise(-1);
    }

    @Test
    public void setCurrentExercise_shouldReturnIndexOfCurrentExercise() throws Exception {
        workout.createExercise();

        workout.setCurrentExercise(0);

        assertThat(workout.indexOfCurrentExercise(), equalTo(0));
    }

    @Test
    public void exercisesOfWorkout_shouldReturnExercises() throws Exception {
        Exercise exercise = workout.createExercise();
        Exercise exercise2 = workout.createExercise();

        assertThat(workout.exercisesOfWorkout(), hasItems(exercise, exercise2));
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

    public class givenAnExerciseToMoveInWorkout {
        private Exercise exercise;

        @Before
        public void setUp() throws Exception {
            exercise = workout.createExercise();
            exercise.setName("ExerciseOne");
        }

        @Test(expected = ExerciseNotFoundException.class)
        public void moveUnknownExerciseUp_shouldThrowExerciseNotFoundException() throws Exception {
            workout.moveExerciseAtPositionUp(1);
        }

        @Test
        public void moveFirstExerciseAtPositionUp_shouldDoNothing() throws Exception {
            boolean moved = workout.moveExerciseAtPositionUp(0);

            assertThat(moved, is(false));
            assertThat(workout.exerciseAtPosition(0), equalTo(exercise));
        }

        @Test
        public void moveExerciseAtPositionUp_shouldPlaceTheExerciseAtTheRightPosition() throws Exception {
            Exercise exerciseToMove = workout.createExercise();
            exerciseToMove.setName("ExerciseTwo");

            boolean moved = workout.moveExerciseAtPositionUp(1);

            assertThat(moved, is(true));
            assertThat(workout.exerciseAtPosition(0), equalTo(exerciseToMove));
        }


        @Test(expected = ExerciseNotFoundException.class)
        public void moveUnknownExerciseDwon_shouldThrowExerciseNotFoundException() throws Exception {
            workout.moveExerciseAtPositionDown(1);
        }

        @Test
        public void moveLastExerciseAtPositionDown_shouldDoNothing() throws Exception {
            Exercise lastExercise = workout.createExercise();

            boolean moved = workout.moveExerciseAtPositionDown(1);

            assertThat(moved, is(false));
            assertThat(workout.exerciseAtPosition(1), equalTo(lastExercise));
        }

        @Test
        public void moveExerciseAtPositionDown_shouldPlaceTheExerciseAtTheSecondPosition() throws Exception {
            Exercise exerciseToMove = workout.createExercise();
            exerciseToMove.setName("ExerciseToMove");
            Exercise lastExercise = workout.createExercise();
            lastExercise.setName("ExerciseLast");

            boolean moved = workout.moveExerciseAtPositionDown(1);

            assertThat(moved, is(true));
            assertThat(workout.exerciseAtPosition(2), equalTo(exerciseToMove));
        }
    }

    public class givenAWorkoutForExerciseManipulation {
        @Test
        public void countOfExercises_shouldReturnEmptyListOfExercisesOnConstruction() throws Exception {
            assertThat(workout.countOfExercises(), equalTo(0));
        }

        @Test(expected = ExerciseNotFoundException.class)
        public void exerciseAtPosition_shouldThrowExerciseNotFoundException() throws Exception {
            workout.exerciseAtPosition(0);
        }

        @Test(expected = ExerciseNotFoundException.class)
        public void exerciseAtNegativePosition_shouldThrowExerciseNotFoundException() throws Exception {
            workout.exerciseAtPosition(-1);
        }

        @Test
        public void createExercise_shouldAddExerciseToWorkout() throws Exception {
            Exercise exercise = workout.createExercise();

            assertThat(workout.countOfExercises(), equalTo(1));
            assertThat(workout.exerciseAtPosition(0), equalTo(exercise));
        }

        @Test
        public void createExercise_shouldAddExerciseBeforePosition() throws Exception {
            workout.createExercise();
            Exercise exercise = workout.createExercise(0);

            assertThat(workout.countOfExercises(), equalTo(2));
            assertThat(workout.exerciseAtPosition(0), equalTo(exercise));
        }

        @Test
        public void createExercise_shouldAddExerciseAfterPosition() throws Exception {
            workout.createExercise();

            Exercise exercise = workout.createExercise(1);

            assertThat(workout.countOfExercises(), equalTo(2));
            assertThat(workout.exerciseAtPosition(1), equalTo(exercise));
        }

        @Test
        public void replaceExercise_shouldNotAddExerciseAtPosition() throws Exception {
            Exercise exercise = new BasicExercise();
            exercise.setExerciseId(new ExerciseId("42"));

            workout.replaceExercise(exercise);

            assertThat(workout.countOfExercises(), equalTo(0));
        }

        @Test
        public void replaceExercise_shouldReplaceExerciseAtPosition() throws Exception {
            Exercise exerciseToReplace = new BasicExercise();
            ExerciseId exerciseId = new ExerciseId("42");
            exerciseToReplace.setExerciseId(exerciseId);
            Exercise exercise = workout.createExercise();
            exercise.setExerciseId(exerciseId);

            workout.replaceExercise(exerciseToReplace);

            assertThat(workout.countOfExercises(), equalTo(1));
            assertThat(workout.exerciseAtPosition(0), equalTo(exerciseToReplace));
        }

        @Test
        public void removeExercise_shouldRemoveExerciseFromWorkout() throws Exception {
            Exercise exercise = workout.createExercise();

            workout.deleteExercise(exercise);

            assertThat(workout.countOfExercises(), equalTo(0));
        }

        @Test
        public void deleteExercise_shouldRemoveLastOfTwoExercisesAtPosition() throws Exception {
            Exercise exercise = workout.createExercise();
            Exercise exerciseToDelete = workout.createExercise();

            workout.deleteExercise(exerciseToDelete);

            assertThat(workout.countOfExercises(), equalTo(1));
            assertThat(workout.exerciseAtPosition(0), equalTo(exercise));
        }

        @Test
        public void deleteExerciseClone_shouldRemoveExercises() throws Exception {
            ExerciseId exerciseId = new ExerciseId("42");
            Exercise exercise = workout.createExercise();
            exercise.setExerciseId(exerciseId);

            Exercise clonedExercise = new BasicExercise();
            clonedExercise.setExerciseId(new ExerciseId(exerciseId));

            workout.deleteExercise(clonedExercise);

            assertThat(workout.countOfExercises(), equalTo(0));
        }

        @Test
        public void deleteExercise_shouldRemoveFirstOfTwoExercisesAtPosition() throws Exception {
            Exercise exerciseToDelete = workout.createExercise();
            Exercise exercise = workout.createExercise();

            workout.deleteExercise(exerciseToDelete);

            assertThat(workout.countOfExercises(), equalTo(1));
            assertThat(workout.exerciseAtPosition(0), equalTo(exercise));
        }

        @Test
        public void deleteExercise_shouldDoNothingWhenIndexIsOutOfBounce() throws Exception {
            workout.deleteExercise(mock(Exercise.class));
        }

        public class givenAnExerciseProgress {
            @Test
            public void getProgress_shouldReturnZeroProgress() throws Exception {
                workout.createExercise();

                assertThat(workout.getProgress(0), equalTo(0.0));
            }

            @Test
            public void getProgress_shouldReturnFullProgress() throws Exception {
                workout.createExercise();
                Exercise exercise = workout.createExercise();
                Set set = exercise.createSet();
                set.setMaxReps(1);
                set.setReps(1);

                assertThat(workout.getProgress(1), equalTo(1.0));
            }

            @Test
            public void getProgress_shouldReturn1point5() throws Exception {
                workout.createExercise();
                Exercise exercise = workout.createExercise();
                Set set = exercise.createSet();
                set.setMaxReps(2);
                set.setReps(1);

                assertThat(workout.getProgress(1), equalTo(0.75));
            }
        }
    }
}
