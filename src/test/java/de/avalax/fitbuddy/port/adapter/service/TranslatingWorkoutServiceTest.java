package de.avalax.fitbuddy.port.adapter.service;

import de.avalax.fitbuddy.domain.model.workout.Workout;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TranslatingWorkoutServiceTest {
    @InjectMocks
    private TranslatingWorkoutService workoutService;
    @Mock
    private JsonInWorkoutAdapter jsonInWorkoutAdapter;
    @Mock
    private Workout workout;

    @Test
    public void aJsonString_shouldReturnAWorkout() throws Exception {
        String aJsonString = "json";
        when(jsonInWorkoutAdapter.createFromJson(aJsonString)).thenReturn(workout);

        Workout workout = workoutService.fromJson(aJsonString);

        assertThat(workout, equalTo(workout));
    }
}