package de.avalax.fitbuddy.port.adapter.persistence;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FakeExerciseRepositoryTest {
    @InjectMocks
    private FakeExerciseRepository exerciseRepository;

    @Test
    public void testSave_shouldGenerateNewId() throws Exception {
        Exercise exercise = mock(Exercise.class);

        exerciseRepository.save(new WorkoutId("1"), exercise);

        verify(exercise).setExerciseId(any(ExerciseId.class));
    }

    @Test
    public void testSave_shouldKeepId() throws Exception {
        Exercise exercise = mock(Exercise.class);
        ExerciseId exerciseId = new ExerciseId("42");
        when(exercise.getExerciseId()).thenReturn(exerciseId);

        exerciseRepository.save(new WorkoutId("1"), exercise);

        assertThat(exercise.getExerciseId(), equalTo(exerciseId));
    }

    @Test
    public void testSaveWithPosition_shouldGenerateNewId() throws Exception {
        Exercise exercise = mock(Exercise.class);

        exerciseRepository.save(new WorkoutId("1"), exercise, 1);

        verify(exercise).setExerciseId(any(ExerciseId.class));
    }
}
