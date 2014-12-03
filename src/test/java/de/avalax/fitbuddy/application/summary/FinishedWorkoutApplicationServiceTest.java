package de.avalax.fitbuddy.application.summary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutRepository;

import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
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
        List<FinishedWorkout> finishedWorkouts = finishedWorkoutApplicationService.allFinishedWorkouts();

        assertThat(finishedWorkouts, emptyCollectionOf(FinishedWorkout.class));
    }

    @Test
    public void finishedWorkouts_shouldReturnListFromRepository() throws Exception {
        finishedWorkoutsFromRepository.add(mock(FinishedWorkout.class));
        finishedWorkoutsFromRepository.add(mock(FinishedWorkout.class));

        List<FinishedWorkout> finishedWorkouts = finishedWorkoutApplicationService.allFinishedWorkouts();

        assertThat(finishedWorkouts, containsInAnyOrder(finishedWorkoutsFromRepository.toArray()));
    }
}