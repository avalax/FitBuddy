package de.avalax.fitbuddy.workout.persistence;

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

    @Before
    public void setUp() {
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
    public void getCurrentExercise_shouldCallGetCurrentExerciseFromWorkout() throws Exception {
        persistenceWorkout.getCurrentExercise();

        verify(workout).getCurrentExercise();
    }

    @Test
    public void getExerciseCount_shouldCallGetExerciseCountFromWorkout() throws Exception {
        persistenceWorkout.getExerciseCount();

        verify(workout).getExerciseCount();
    }

    @Test
    public void getExercise_shouldCallGetExerciseFromWorkout() throws Exception {
        persistenceWorkout.getExercise(1);

        verify(workout).getExercise(1);
    }

    @Test
    public void getCurrentSet_shouldCallGetCurrentSetFromWorkout() throws Exception {
        persistenceWorkout.getCurrentSet();

        verify(workout).getCurrentSet();
    }

    @Test
    public void getReps_shouldCallGetRepsFromWorkout() throws Exception {
        persistenceWorkout.getReps();

        verify(workout).getReps();
    }

    @Test
    public void setReps_shouldCallSetRepsFromWorkout() throws Exception {
        persistenceWorkout.setReps(2);

        verify(workout).setReps(2);
    }

    @Test
    public void setExerciseNumber_shouldCallSetExerciseNumberFromWorkout() throws Exception {
        persistenceWorkout.setExerciseNumber(2);

        verify(workout).setExerciseNumber(2);
    }

    @Test
    public void setTendency_shouldCallSetTendencyFromWorkout() throws Exception {
        persistenceWorkout.setTendency(Tendency.MINUS);

        verify(workout).setTendency(Tendency.MINUS);
    }

    @Test
    public void switchToNextExercise_shouldCallSwitchToNextExerciseFromWorkout() throws Exception {
        persistenceWorkout.switchToNextExercise();

        verify(workout).switchToNextExercise();
    }

    @Test
    public void switchToPreviousExercise_shouldCallSwitchToPreviousExerciseFromWorkout() throws Exception {
        persistenceWorkout.switchToPreviousExercise();

        verify(workout).switchToPreviousExercise();
    }
}
