package de.avalax.fitbuddy.datalayer;

import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.ExerciseId;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FakeWorkoutDAOTest {
    @InjectMocks
    private FakeWorkoutDAO workoutDAO;

    @Test
    public void testSave_shouldGenerateNewId() throws Exception {
        Workout workout = mock(Workout.class);

        workoutDAO.save(workout);

        verify(workout).setWorkoutId(any(WorkoutId.class));
    }

    @Test
    public void testSaveExercise_shouldGenerateNewId() throws Exception {
        Exercise exercise = mock(Exercise.class);

        workoutDAO.saveExercise(new WorkoutId("1"), exercise);

        verify(exercise).setExerciseId(any(ExerciseId.class));
    }

    @Test
    public void testSaveExerciseWithPosition_shouldGenerateNewId() throws Exception {
        Exercise exercise = mock(Exercise.class);

        workoutDAO.saveExercise(new WorkoutId("1"), exercise, 1);

        verify(exercise).setExerciseId(any(ExerciseId.class));
    }

    @Test
    public void testSave_shouldKeepId() throws Exception {
        Workout workout = mock(Workout.class);
        WorkoutId workoutId = new WorkoutId("42");
        when(workout.getWorkoutId()).thenReturn(workoutId);
        workoutDAO.save(workout);

        assertThat(workout.getWorkoutId(), equalTo(workoutId));
    }

    @Test
    public void testLoad_shouldReturnAWorkout() throws Exception {
        Workout workout = workoutDAO.load(new WorkoutId("1"));

        assertThat(workout, instanceOf(Workout.class));
    }

    @Test(expected = WorkoutNotAvailableException.class)
    public void testLoad_shouldThrowExceptionWhenWorkoutNotFound() throws Exception {
        workoutDAO.load(new WorkoutId("42"));
    }

    @Test
    public void testGetList_shouldReturnAListOfWorkouts() throws Exception {
        List<Workout> workouts = workoutDAO.getList();

        assertThat(workouts.size(), is(2));
    }

    @Test
    public void testRemove_shouldDoNothingWhenWorkoutIsUnknown() throws Exception {
        workoutDAO.delete(new WorkoutId("123"));

        assertThat(workoutDAO.getList().size(), is(2));
    }

    @Test
    public void testRemove_shouldRemoveWorkout() throws Exception {
        Workout workoutToDelete = workoutDAO.load(new WorkoutId("1"));
        Workout workout = workoutDAO.load(new WorkoutId("2"));

        workoutDAO.delete(workoutToDelete.getWorkoutId());

        assertThat(workout, equalTo(workoutDAO.load(new WorkoutId("2"))));
        assertThat(workoutDAO.getList().size(), is(1));
    }
}
