package de.avalax.fitbuddy.port.adapter.persistence;

import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutNotAvailableException;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FakeWorkoutRepositoryTest {
    @InjectMocks
    private FakeWorkoutRepository workoutRepository;

    @Test
    public void testSave_shouldGenerateNewId() throws Exception {
        Workout workout = mock(Workout.class);

        workoutRepository.save(workout);

        verify(workout).setWorkoutId(any(WorkoutId.class));
    }

    @Test
    public void testSave_shouldKeepId() throws Exception {
        Workout workout = mock(Workout.class);
        WorkoutId workoutId = new WorkoutId("42");
        when(workout.getWorkoutId()).thenReturn(workoutId);
        workoutRepository.save(workout);

        assertThat(workout.getWorkoutId(), equalTo(workoutId));
    }

    @Test
    public void testLoad_shouldReturnAWorkout() throws Exception {
        Workout workout = workoutRepository.load(new WorkoutId("1"));

        assertThat(workout, instanceOf(Workout.class));
    }

    @Test(expected = WorkoutNotAvailableException.class)
    public void testLoad_shouldThrowExceptionWhenWorkoutNotFound() throws Exception {
        workoutRepository.load(new WorkoutId("42"));
    }

    @Test
    public void testGetList_shouldReturnAListOfWorkouts() throws Exception {
        List<Workout> workouts = workoutRepository.getList();

        assertThat(workouts.size(), is(2));
    }

    @Test
    public void testRemove_shouldDoNothingWhenWorkoutIsUnknown() throws Exception {
        workoutRepository.delete(new WorkoutId("123"));

        assertThat(workoutRepository.getList().size(), is(2));
    }

    @Test
    public void testRemove_shouldRemoveWorkout() throws Exception {
        Workout workoutToDelete = workoutRepository.load(new WorkoutId("1"));
        Workout workout = workoutRepository.load(new WorkoutId("2"));

        workoutRepository.delete(workoutToDelete.getWorkoutId());

        assertThat(workout, equalTo(workoutRepository.load(new WorkoutId("2"))));
        assertThat(workoutRepository.getList().size(), is(1));
    }
}
