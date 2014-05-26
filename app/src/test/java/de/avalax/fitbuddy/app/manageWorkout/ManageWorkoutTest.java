package de.avalax.fitbuddy.app.manageWorkout;

import android.view.View;
import de.avalax.fitbuddy.app.WorkoutFactory;
import de.avalax.fitbuddy.app.WorkoutSession;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ManageWorkoutTest {

    @Mock
    private WorkoutDAO workoutDAO;
    @Mock
    private WorkoutFactory workoutFactory;
    @Mock
    private WorkoutSession workoutSession;
    @Mock
    private Workout workout;
    @InjectMocks
    private ManageWorkout manageWorkout;

    @Test
    public void afterInitialization_shouldHideUnsavedChanges() throws Exception {
        assertThat(manageWorkout.unsavedChangesVisibility(),equalTo(View.GONE));
    }

    @Test
    public void createNewWorkout_shouldAlsoPersistTheNewlyCreatedWorkout() throws Exception {
        when(workoutFactory.createNew()).thenReturn(workout);
        manageWorkout.createNewWorkout();
        verify(workoutDAO).save(workout);
        assertThat(manageWorkout.unsavedChangesVisibility(),equalTo(View.GONE));
    }

    @Test
    public void createNewJsonWorkout_shouldAlsoPersistTheNewlyCreatedWorkout() throws Exception {
        when(workoutFactory.createFromJson("jsonstring")).thenReturn(workout);
        manageWorkout.createWorkoutFromJson("jsonstring");
        verify(workoutDAO).save(workout);
        assertThat(manageWorkout.unsavedChangesVisibility(),equalTo(View.GONE));
    }
}