package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.Workout;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class FakeWorkoutDAOTest {
    @InjectMocks
    private FakeWorkoutDAO workoutDAO;

    @Test
    public void testSave_shouldDoNothing() throws Exception {
        Workout workout = mock(Workout.class);

        workoutDAO.save(workout);

        verifyNoMoreInteractions(workout);
    }

    @Test
    public void testLoad_shouldReturnAWorkout() throws Exception {
        Workout workout = workoutDAO.load(1);

        assertThat(workout, instanceOf(Workout.class));
    }

    @Test(expected = WorkoutNotAvailableException.class)
    public void testLoad_shouldThrowExceptionWhenWorkoutNotFound() throws Exception {
        workoutDAO.load(42);
    }

    @Test
    public void testGetList_shouldReturnAListOfWorkouts() throws Exception {
        List<String> workouts = workoutDAO.getList();

        assertThat(workouts.size(), is(2));
    }

    @Test
    public void testRemove_shouldDoNothingWhenWorkoutIsUnknown() throws Exception {
        Workout workout = mock(Workout.class);
        workoutDAO.remove(workout);

        assertThat(workoutDAO.getList().size(), is(2));
    }

    @Test
    public void testRemove_shouldRemoveWorkout() throws Exception {
        Workout workoutToDelete = workoutDAO.load(0);
        Workout workout = workoutDAO.load(1);

        workoutDAO.remove(workoutToDelete);

        assertThat(workout,equalTo(workoutDAO.load(0)));
        assertThat(workoutDAO.getList().size(), is(1));
    }
}
