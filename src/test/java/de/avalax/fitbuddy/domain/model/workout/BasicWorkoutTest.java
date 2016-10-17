package de.avalax.fitbuddy.domain.model.workout;


import org.assertj.core.api.Java6Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.Workout.WorkoutMemento;
import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
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

    @Test(expected = ExerciseException.class)
    public void indexOfCurrentExercise_shouldThrowExerciseNotFoundException() throws Exception {
        workout.getExercises().indexOfCurrentExercise();
    }

    @Test(expected = ExerciseException.class)
    public void setCurrentExercise_shouldThrowExerciseNotFoundException() throws Exception {
        workout.getExercises().setCurrentExercise(0);
    }

    @Test(expected = ExerciseException.class)
    public void setCurrentExerciseToNegativ_shouldThrowExerciseNotFoundException() throws Exception {
        workout.getExercises().setCurrentExercise(-1);
    }

    @Test
    public void setCurrentExercise_shouldReturnIndexOfCurrentExercise() throws Exception {
        workout.getExercises().createExercise();

        workout.getExercises().setCurrentExercise(0);

        assertThat(workout.getExercises().indexOfCurrentExercise(), equalTo(0));
    }

    @Test
    public void exercisesOfWorkout_shouldReturnExercises() throws Exception {
        Exercise exercise = workout.getExercises().createExercise();
        Exercise exercise2 = workout.getExercises().createExercise();

        assertThat(workout.getExercises(), containsInAnyOrder(exercise, exercise2));
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
            exercise = workout.getExercises().createExercise();
            exercise.setName("ExerciseOne");
        }

        @Test(expected = ExerciseException.class)
        public void moveUnknownExerciseUp_shouldThrowExerciseNotFoundException() throws Exception {
            workout.getExercises().moveExerciseAtPositionUp(1);
        }

        @Test
        public void moveFirstExerciseAtPositionUp_shouldDoNothing() throws Exception {
            boolean moved = workout.getExercises().moveExerciseAtPositionUp(0);

            assertThat(moved, is(false));
            assertThat(workout.getExercises().exerciseAtPosition(0), equalTo(exercise));
        }

        @Test
        public void moveExerciseAtPositionUp_shouldPlaceTheExerciseAtTheRightPosition() throws Exception {
            Exercise exerciseToMove = workout.getExercises().createExercise();
            exerciseToMove.setName("ExerciseTwo");

            boolean moved = workout.getExercises().moveExerciseAtPositionUp(1);

            assertThat(moved, is(true));
            assertThat(workout.getExercises().exerciseAtPosition(0), equalTo(exerciseToMove));
        }


        @Test(expected = ExerciseException.class)
        public void moveUnknownExerciseDown_shouldThrowExerciseNotFoundException() throws Exception {
            workout.getExercises().moveExerciseAtPositionDown(-1);
        }

        @Test
        public void moveLastExerciseAtPositionDown_shouldDoNothing() throws Exception {
            Exercise lastExercise = workout.getExercises().createExercise();

            boolean moved = workout.getExercises().moveExerciseAtPositionDown(1);

            assertThat(moved, is(false));
            assertThat(workout.getExercises().exerciseAtPosition(1), equalTo(lastExercise));
        }

        @Test
        public void moveExerciseAtPositionDown_shouldPlaceTheExerciseAtTheSecondPosition() throws Exception {
            Exercise exerciseToMove = workout.getExercises().createExercise();
            exerciseToMove.setName("ExerciseToMove");
            Exercise lastExercise = workout.getExercises().createExercise();
            lastExercise.setName("ExerciseLast");

            boolean moved = workout.getExercises().moveExerciseAtPositionDown(1);

            assertThat(moved, is(true));
            assertThat(workout.getExercises().exerciseAtPosition(2), equalTo(exerciseToMove));
        }
    }

    public class givenAWorkoutForExerciseManipulation {
        @Test
        public void countOfExercises_shouldReturnEmptyListOfExercisesOnConstruction() throws Exception {
            assertThat(workout.getExercises().countOfExercises(), equalTo(0));
        }

        @Test(expected = ExerciseException.class)
        public void exerciseAtPosition_shouldThrowExerciseNotFoundException() throws Exception {
            workout.getExercises().exerciseAtPosition(0);
        }

        @Test(expected = ExerciseException.class)
        public void exerciseAtNegativePosition_shouldThrowExerciseNotFoundException() throws Exception {
            workout.getExercises().exerciseAtPosition(-1);
        }

        @Test
        public void createExercise_shouldAddExerciseToWorkout() throws Exception {
            Exercise exercise = workout.getExercises().createExercise();

            assertThat(workout.getExercises().countOfExercises(), equalTo(1));
            assertThat(workout.getExercises().exerciseAtPosition(0), equalTo(exercise));
        }

        @Test
        public void createExercise_shouldAddExerciseBeforePosition() throws Exception {
            workout.getExercises().createExercise();
            Exercise exercise = workout.getExercises().createExercise(0);

            assertThat(workout.getExercises().countOfExercises(), equalTo(2));
            assertThat(workout.getExercises().exerciseAtPosition(0), equalTo(exercise));
        }

        @Test
        public void createExercise_shouldAddExerciseAfterPosition() throws Exception {
            workout.getExercises().createExercise();

            Exercise exercise = workout.getExercises().createExercise(1);

            assertThat(workout.getExercises().countOfExercises(), equalTo(2));
            assertThat(workout.getExercises().exerciseAtPosition(1), equalTo(exercise));
        }

        @Test
        public void removeExercise_shouldRemoveExerciseFromWorkout() throws Exception {
            Exercise exercise = workout.getExercises().createExercise();

            workout.getExercises().deleteExercise(exercise);

            assertThat(workout.getExercises().countOfExercises(), equalTo(0));
        }

        @Test
        public void deleteExercise_shouldRemoveLastOfTwoExercisesAtPosition() throws Exception {
            Exercise exercise = workout.getExercises().createExercise();
            Exercise exerciseToDelete = workout.getExercises().createExercise();

            workout.getExercises().deleteExercise(exerciseToDelete);

            assertThat(workout.getExercises().countOfExercises(), equalTo(1));
            assertThat(workout.getExercises().exerciseAtPosition(0), equalTo(exercise));
        }

        @Test
        public void deleteExerciseClone_shouldRemoveExercises() throws Exception {
            ExerciseId exerciseId = new ExerciseId("42");
            Exercise exercise = workout.getExercises().createExercise();
            exercise.setExerciseId(exerciseId);

            Exercise clonedExercise = new BasicExercise();
            clonedExercise.setExerciseId(exerciseId);

            workout.getExercises().deleteExercise(clonedExercise);

            assertThat(workout.getExercises().countOfExercises(), equalTo(0));
        }

        @Test
        public void deleteExercise_shouldRemoveFirstOfTwoExercisesAtPosition() throws Exception {
            Exercise exerciseToDelete = workout.getExercises().createExercise();
            Exercise exercise = workout.getExercises().createExercise();

            workout.getExercises().deleteExercise(exerciseToDelete);

            assertThat(workout.getExercises().countOfExercises(), equalTo(1));
            assertThat(workout.getExercises().exerciseAtPosition(0), equalTo(exercise));
        }

        @Test
        public void deleteExercise_shouldDoNothingWhenIndexIsOutOfBounce() throws Exception {
            workout.getExercises().deleteExercise(mock(Exercise.class));
        }

        public class givenAnExerciseProgress {
            @Test
            public void getProgress_shouldReturnZeroProgress() throws Exception {
                workout.getExercises().createExercise();

                assertThat(workout.getProgress(0), equalTo(0.0));
            }

            @Test
            public void getProgress_shouldReturnFullProgress() throws Exception {
                workout.getExercises().createExercise();
                Exercise exercise = workout.getExercises().createExercise();
                Set set = exercise.getSets().setAtPosition(0);
                set.setMaxReps(1);
                set.setReps(1);

                assertThat(workout.getProgress(1), equalTo(1.0));
            }

            @Test
            public void getProgress_shouldReturn1point5() throws Exception {
                workout.getExercises().createExercise();
                Exercise exercise = workout.getExercises().createExercise();
                Set set = exercise.getSets().setAtPosition(0);
                set.setMaxReps(2);
                set.setReps(1);

                assertThat(workout.getProgress(1), equalTo(0.75));
            }
        }

        public class memento {
            @Test
            public void null_shouldNotChangeExercises() throws Exception {
                workout.setMemento(null);

                Java6Assertions.assertThat(workout.getExercises()).isEmpty();
            }

            @Test
            public void anExercisesDeletedAfterMemento_shouldUndDeletionBySetMemento() throws Exception {
                Exercise exercise = workout.getExercises().createExercise();
                WorkoutMemento memento = workout.createMemento();
                workout.getExercises().deleteExercise(exercise);

                workout.setMemento(memento);

                Java6Assertions.assertThat(workout.getExercises()).containsExactly(exercise);
            }
        }
    }
}
