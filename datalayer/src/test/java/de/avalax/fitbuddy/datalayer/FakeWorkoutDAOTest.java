package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.WorkoutId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class FakeWorkoutDAOTest {
    @InjectMocks
    private FakeWorkoutDAO workoutDAO;

    @Test
    public void testSave_shouldDoNothing() throws Exception {
        Workout workout = mock(Workout.class);

        workoutDAO.save(workout);

        verify(workout).setId(any(WorkoutId.class));
    }

    @Test
    public void testLoad_shouldReturnAWorkout() throws Exception {
        Workout workout = workoutDAO.load(new WorkoutId(1));

        assertThat(workout, instanceOf(Workout.class));
    }

    @Test(expected = WorkoutNotAvailableException.class)
    public void testLoad_shouldThrowExceptionWhenWorkoutNotFound() throws Exception {
        workoutDAO.load(new WorkoutId(42));
    }

    @Test
    public void testGetList_shouldReturnAListOfWorkouts() throws Exception {
        List<Workout> workouts = workoutDAO.getList();

        assertThat(workouts.size(), is(2));
    }

    @Test
    public void testRemove_shouldDoNothingWhenWorkoutIsUnknown() throws Exception {
        workoutDAO.delete(new WorkoutId(123));

        assertThat(workoutDAO.getList().size(), is(2));
    }

    @Test
    public void testRemove_shouldRemoveWorkout() throws Exception {
        Workout workoutToDelete = workoutDAO.load(new WorkoutId(0));
        Workout workout = workoutDAO.load(new WorkoutId(1));

        workoutDAO.delete(workoutToDelete.getId());

        assertThat(workout, equalTo(workoutDAO.load(new WorkoutId(1))));
        assertThat(workoutDAO.getList().size(), is(1));
    }
}
