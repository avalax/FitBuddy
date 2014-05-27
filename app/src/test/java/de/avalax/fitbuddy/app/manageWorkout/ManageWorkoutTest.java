package de.avalax.fitbuddy.app.manageWorkout;

import android.view.View;
import de.avalax.fitbuddy.app.ExerciseFactory;
import de.avalax.fitbuddy.app.WorkoutFactory;
import de.avalax.fitbuddy.app.WorkoutSession;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.WorkoutId;
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
    public void createNewWorkout_shouldAlsoPersistTheNewlyCreatedWorkout() throws Exception {
        when(workoutFactory.createNew()).thenReturn(workout);
        manageWorkout.createNewWorkout();
        verify(workoutDAO).save(workout);
    }

    @Test
    public void createNewJsonWorkout_shouldAlsoPersistTheNewlyCreatedWorkout() throws Exception {
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
        public void deleteWorkout_shouldAlsoDeleteThePersistedWorkout() throws Exception {
            manageWorkout.deleteWorkout();

            verify(workoutDAO).delete(new WorkoutId(42));
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.VISIBLE));
            assertThat(manageWorkout.hasDeletedWorkout(), equalTo(true));
        }

        @Test
        public void undoDeleteWorkout_shouldAlsoPersistTheWorkout() throws Exception {
            manageWorkout.deleteWorkout();
            manageWorkout.undoUnsavedChanges();

            verify(workoutDAO).save(workout);
            assertThat(manageWorkout.unsavedChangesVisibility(), equalTo(View.GONE));
            assertThat(manageWorkout.hasDeletedWorkout(), equalTo(false));
        }

        @Test
        public void addNewExercise_shouldAlsoPersistTheExercise() throws Exception {
            when(exerciseFactory.createNew()).thenReturn(exercise);

            manageWorkout.addNewExercise();

            verify(workoutDAO).saveExercise(workout.getId(), exercise);
        }
    }
}