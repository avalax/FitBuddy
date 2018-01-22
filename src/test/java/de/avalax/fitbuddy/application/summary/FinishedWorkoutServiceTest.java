package de.avalax.fitbuddy.application.summary;

import org.assertj.core.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutException;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;

import static de.avalax.fitbuddy.domain.model.finished_workout.BasicFinishedWorkoutBuilder.aFinishedWorkout;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinishedWorkoutServiceTest {
    private FinishedWorkoutService finishedWorkoutService;
    private List<FinishedWorkout> finishedWorkoutsFromRepository;
    @Mock
    private FinishedWorkoutRepository finishedWorkoutRepository;

    @Before
    public void setUp() throws Exception {
        finishedWorkoutService = new FinishedWorkoutService(finishedWorkoutRepository);
        finishedWorkoutsFromRepository = new ArrayList<>();
        when(finishedWorkoutRepository.loadAll()).thenReturn(finishedWorkoutsFromRepository);
    }

    @Test
    public void noFinishedWorkouts_shouldReturnEmptyList() throws Exception {
        List<FinishedWorkout> finishedWorkouts = finishedWorkoutService.loadAllFinishedWorkouts();

        assertThat(finishedWorkouts).isEmpty();
        assertThat(finishedWorkoutService.hasFinishedWorkouts()).isFalse();
    }

    @Test
    public void finishedWorkouts_shouldReturnListFromRepository() throws Exception {
        finishedWorkoutsFromRepository.add(mock(FinishedWorkout.class));
        finishedWorkoutsFromRepository.add(mock(FinishedWorkout.class));
        when(finishedWorkoutRepository.size()).thenReturn((long) finishedWorkoutsFromRepository.size());

        List<FinishedWorkout> finishedWorkouts = finishedWorkoutService.loadAllFinishedWorkouts();

        assertThat(finishedWorkouts).containsAll(finishedWorkoutsFromRepository);
        assertThat(finishedWorkoutService.hasFinishedWorkouts()).isTrue();
    }

    @Test
    public void loadWorkout_shouldReturnWorkoutFromRepository() throws Exception {
        FinishedWorkoutId finishedWorkoutId = new FinishedWorkoutId("1");
        FinishedWorkout expectedWorkout = mock(FinishedWorkout.class);
        doReturn(expectedWorkout).when(finishedWorkoutRepository).load(finishedWorkoutId);

        FinishedWorkout workout = finishedWorkoutService.load(finishedWorkoutId);

        assertThat(workout).isEqualTo(expectedWorkout);
    }

    @Test
    public void deleteWorkout_shouldDeleteWorkoutFromRepository() throws Exception {
        FinishedWorkoutId finishedWorkoutId = new FinishedWorkoutId("1");
        FinishedWorkout finishedWorkout = aFinishedWorkout().withFinishedWorkoutId(finishedWorkoutId).build();
        finishedWorkoutService.delete(finishedWorkout);

        verify(finishedWorkoutRepository).delete(finishedWorkoutId);
    }

    @Test(expected = FinishedWorkoutException.class)
    public void loadUnknownWorkout_shouldThrowFinishedWorkoutException() throws Exception {
        FinishedWorkoutId finishedWorkoutId = new FinishedWorkoutId("1");
        doThrow(new FinishedWorkoutException()).when(finishedWorkoutRepository).load(finishedWorkoutId);

        finishedWorkoutService.load(finishedWorkoutId);
    }

    @Test
    public void updateCreationDate_shouldUpdateFinishedWorkoutInRepository() throws Exception {
        FinishedWorkoutId finishedWorkoutId = new FinishedWorkoutId("1");
        Date date = DateUtil.parse("2017-12-31");

        finishedWorkoutService.updatedCreation(finishedWorkoutId, date);

        verify(finishedWorkoutRepository).updateCreation(finishedWorkoutId, date.getTime());
    }
}