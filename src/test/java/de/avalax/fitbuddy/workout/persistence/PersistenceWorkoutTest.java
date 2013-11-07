package de.avalax.fitbuddy.workout.persistence;

import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Tendency;
import de.avalax.fitbuddy.workout.Workout;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersistenceWorkoutTest {

    private DataLayer dataLayer;
    private Workout persistenceWorkout;
    private Workout workout;
    private int exerciseIndex;

    @Before
    public void setUp() {
        exerciseIndex = 0;
        dataLayer = mock(DataLayer.class);
        workout = mock(Workout.class);
        when(dataLayer.load()).thenReturn(workout);
        persistenceWorkout = new PersistenceWorkout(dataLayer);
    }

    @Test
    public void persistenceWorkout_shouldCallCreatePersistenceWorkout() throws Exception {
        verify(dataLayer).load();
    }

    @Test
    public void getExerciseCount_shouldCallGetExerciseCountFromWorkout() throws Exception {
        persistenceWorkout.getExerciseCount();

        verify(workout).getExerciseCount();
    }

    @Test
    public void getExercise_shouldCallGetExerciseFromWorkout() throws Exception {
        persistenceWorkout.getExercise(exerciseIndex);

        verify(workout).getExercise(exerciseIndex);
    }

    @Test
    public void getCurrentSet_shouldCallGetCurrentSetFromWorkout() throws Exception {
        persistenceWorkout.getCurrentSet(exerciseIndex);

        verify(workout).getCurrentSet(exerciseIndex);
    }

    @Test
    public void getReps_shouldCallGetRepsFromWorkout() throws Exception {
        persistenceWorkout.getReps(exerciseIndex);

        verify(workout).getReps(exerciseIndex);
    }

    @Test
    public void getName_shouldCallGetNameFromWorkout() throws Exception {
        persistenceWorkout.getName(exerciseIndex);

        verify(workout).getName(exerciseIndex);
    }

    @Test
    public void getProgress_shouldCallGetProgressFromWorkout() throws Exception {
        persistenceWorkout.getProgress(exerciseIndex);

        verify(workout).getProgress(exerciseIndex);
    }

    @Test
    public void incrementSet_shouldCallIncrementSetFromWorkout() throws Exception {
        persistenceWorkout.incrementSet(exerciseIndex);

        verify(workout).incrementSet(exerciseIndex);
    }

    @Test
    public void setReps_shouldCallSetRepsFromWorkout() throws Exception {
        int repsCount = 12;
        persistenceWorkout.setReps(exerciseIndex, repsCount);

        verify(workout).setReps(exerciseIndex, repsCount);
    }

    @Test
    public void addExerciseBefore_shouldCallAddExerciseBeforeFromWorkout() throws Exception {
        Exercise exercise = mock(Exercise.class);
        persistenceWorkout.addExerciseBefore(exerciseIndex, exercise);

        verify(workout).addExerciseBefore(exerciseIndex, exercise);
    }

    @Test
    public void addExerciseAfter_shouldCallAddExerciseAfterFromWorkout() throws Exception {
        Exercise exercise = mock(Exercise.class);
        persistenceWorkout.addExerciseAfter(exerciseIndex, exercise);

        verify(workout).addExerciseAfter(exerciseIndex, exercise);
    }

    @Test
    public void setExercise_shouldCallSetExerciseFromWorkout() throws Exception {
        Exercise exercise = mock(Exercise.class);
        persistenceWorkout.setExercise(exerciseIndex, exercise);

        verify(workout).setExercise(exerciseIndex, exercise);
    }

    @Test
    public void setTendency_shouldCallSetTendencyFromWorkout() throws Exception {
        persistenceWorkout.setTendency(exerciseIndex, Tendency.MINUS);

        verify(workout).setTendency(exerciseIndex, Tendency.MINUS);
    }

    @Test
    public void removeExercise_shouldCallRemoveExerciseFromWorkout() throws Exception {
        persistenceWorkout.removeExercise(exerciseIndex);

        verify(workout).removeExercise(exerciseIndex);
    }
}
