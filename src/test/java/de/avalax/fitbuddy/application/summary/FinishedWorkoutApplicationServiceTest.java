package de.avalax.fitbuddy.application.summary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.finished_workout.BasicFinishedWorkout;
import de.avalax.fitbuddy.domain.model.finished_workout.BasicFinishedWorkoutBuilder;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutException;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;

import static de.avalax.fitbuddy.domain.model.finished_workout.BasicFinishedWorkoutBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinishedWorkoutApplicationServiceTest {
    private FinishedWorkoutApplicationService finishedWorkoutApplicationService;
    private List<FinishedWorkout> finishedWorkoutsFromRepository;
    @Mock
    private FinishedWorkoutRepository finishedWorkoutRepository;

    @Before
    public void setUp() throws Exception {
        finishedWorkoutApplicationService = new FinishedWorkoutApplicationService(finishedWorkoutRepository);
        finishedWorkoutsFromRepository = new ArrayList<>();
        when(finishedWorkoutRepository.loadAll()).thenReturn(finishedWorkoutsFromRepository);
    }

    @Test
    public void noFinishedWorkouts_shouldReturnEmptyList() throws Exception {
        List<FinishedWorkout> finishedWorkouts = finishedWorkoutApplicationService.loadAllFinishedWorkouts();

        assertThat(finishedWorkouts).isEmpty();
    }

    @Test
    public void finishedWorkouts_shouldReturnListFromRepository() throws Exception {
        finishedWorkoutsFromRepository.add(mock(FinishedWorkout.class));
        finishedWorkoutsFromRepository.add(mock(FinishedWorkout.class));

        List<FinishedWorkout> finishedWorkouts = finishedWorkoutApplicationService.loadAllFinishedWorkouts();

        assertThat(finishedWorkouts).containsAll(finishedWorkoutsFromRepository);
    }

    @Test
    public void loadWorkout_shouldReturnWorkoutFromRepository() throws Exception {
        FinishedWorkoutId finishedWorkoutId = new FinishedWorkoutId("1");
        FinishedWorkout expectedWorkout = mock(FinishedWorkout.class);
        doReturn(expectedWorkout).when(finishedWorkoutRepository).load(finishedWorkoutId);

        FinishedWorkout workout = finishedWorkoutApplicationService.load(finishedWorkoutId);

        assertThat(workout).isEqualTo(expectedWorkout);
    }

    @Test
    public void deleteWorkout_shouldDeleteWorkoutFromRepository() throws Exception {
        FinishedWorkoutId finishedWorkoutId = new FinishedWorkoutId("1");
        FinishedWorkout finishedWorkout = aFinishedWorkout().withFinishedWorkoutId(finishedWorkoutId).build();
        finishedWorkoutApplicationService.delete(finishedWorkout);

        verify(finishedWorkoutRepository).delete(finishedWorkoutId);
    }

    @Test(expected = FinishedWorkoutException.class)
    public void loadUnknownWorkout_shouldThrowFinishedWorkoutException() throws Exception {
        FinishedWorkoutId finishedWorkoutId = new FinishedWorkoutId("1");
        doThrow(new FinishedWorkoutException()).when(finishedWorkoutRepository).load(finishedWorkoutId);

        finishedWorkoutApplicationService.load(finishedWorkoutId);
    }
}