package de.avalax.fitbuddy.app.manageWorkout;

import android.view.View;
import de.avalax.fitbuddy.app.ExerciseFactory;
import de.avalax.fitbuddy.app.WorkoutFactory;
import de.avalax.fitbuddy.app.WorkoutSession;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.WorkoutId;
import de.avalax.fitbuddy.core.workout.basic.BasicWorkout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(HierarchicalContextRunner.class)
public class ManageWorkoutTest {

    @Mock
    private WorkoutDAO workoutDAO;
    @Mock
    private WorkoutFactory workoutFactory;
    @Mock
    private ExerciseFactory exerciseFactory;
    @Mock
    private WorkoutSession workoutSession;
    @Mock
    private Workout workout;
    @InjectMocks
    private ManageWorkout manageWorkout;
    @Mock
    private Exercise exercise;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void afterInitialization_shouldHideUnsavedChanges() throws Exception {
        assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
    }

    @Test
    public void createWorkout_shouldPersistTheCreatedWorkout() throws Exception {
        when(workoutFactory.createNew()).thenReturn(workout);
        manageWorkout.createWorkout();
        verify(workoutDAO).save(workout);
    }

    @Test
    public void createWorkoutFromJson_shouldPersistTheCreatedWorkout() throws Exception {
        when(workoutFactory.createFromJson("jsonstring")).thenReturn(workout);
        manageWorkout.createWorkoutFromJson("jsonstring");
        verify(workoutDAO).save(workout);
        assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
    }

    public class workoutManipulation {
        @Before
        public void setUp() throws Exception {
            when(workoutDAO.load(new WorkoutId(42))).thenReturn(workout);
            when(workout.getId()).thenReturn(new WorkoutId(42));

            manageWorkout.setWorkout(new WorkoutId(42));
        }

        @Test
        public void deleteWorkout_shouldRemoveTheWorkoutFromThePersistence() throws Exception {
            manageWorkout.deleteWorkout();

            verify(workoutDAO).delete(new WorkoutId(42));
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.VISIBLE));
            assertThat(manageWorkout.hasDeletedWorkout(), equalTo(true));
        }

        @Test
        public void undoDeleteAction_shouldReinsertTheWorkoutToThePersistence() throws Exception {
            manageWorkout.deleteWorkout();
            manageWorkout.undoDeleteAction();

            verify(workoutDAO).save(workout);
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
            assertThat(manageWorkout.hasDeletedWorkout(), equalTo(false));
        }

        @Test
        public void createExercise_shouldPersistTheExercise() throws Exception {
            when(exerciseFactory.createNew()).thenReturn(exercise);

            manageWorkout.createExercise();

            verify(workoutDAO).saveExercise(workout.getId(), exercise);
        }

        public class exerciseManipulation {
            @Test
            public void deleteExercise_shouldDeleteThePersistdExercise() throws Exception {
                manageWorkout.deleteExercise(exercise);

                verify(workout).removeExercise(exercise);
                verify(workoutDAO).deleteExercise(exercise.getId());
                assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.VISIBLE));
                assertThat(manageWorkout.hasDeletedExercise(), equalTo(true));
            }

            @Test
            public void undoDeleteAction_shouldReinsertTheExerciseToThePersistence() throws Exception {
                manageWorkout.deleteExercise(exercise);
                manageWorkout.undoDeleteAction();

                verify(workoutDAO).saveExercise(workout.getId(),exercise);
                verify(workout).addExercise(exercise);
                assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
                assertThat(manageWorkout.hasDeletedExercise(), equalTo(false));
            }

            @Test
            public void undoDeleteActionAfterDeleteAnWorkoutAndExercise_shouldReinsertTheExerciseToThePersistence() throws Exception {
                Workout deletedWorkout = mock(Workout.class);
                when(deletedWorkout.getId()).thenReturn(new WorkoutId(21));
                when(workoutFactory.createNew()).thenReturn(deletedWorkout);
                manageWorkout.createWorkout();

                manageWorkout.deleteWorkout();
                assertThat(manageWorkout.hasDeletedWorkout(), equalTo(true));

                manageWorkout.setWorkout(workout.getId());
                manageWorkout.deleteExercise(exercise);
                assertThat(manageWorkout.hasDeletedWorkout(), equalTo(false));

                manageWorkout.undoDeleteAction();
                assertThat(manageWorkout.hasDeletedExercise(), equalTo(false));
            }

            @Test
            public void undoDeleteActionAfterDeleteAnExerciseAndWorkout_shouldReinsertTheWorkoutToThePersistence() throws Exception {
                manageWorkout.deleteExercise(exercise);
                assertThat(manageWorkout.hasDeletedExercise(), equalTo(true));

                manageWorkout.deleteWorkout();
                assertThat(manageWorkout.hasDeletedExercise(), equalTo(false));

                manageWorkout.undoDeleteAction();
                assertThat(manageWorkout.hasDeletedExercise(), equalTo(false));
            }
        }
    }
}