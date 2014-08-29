package de.avalax.fitbuddy.application.workout;

import de.avalax.fitbuddy.domain.model.RessourceNotFoundException;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutNotFoundException;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(HierarchicalContextRunner.class)
public class WorkoutApplicationServiceTest {

    private WorkoutSession workoutSession;

    private WorkoutApplicationService workoutApplicationService;

    @Before
    public void setUp() throws Exception {
        workoutSession = mock(WorkoutSession.class);
        workoutApplicationService = new WorkoutApplicationService(workoutSession);
    }

    public class noWorkoutGiven {
        @Before
        public void setUp() throws Exception {
            when(workoutSession.getWorkout()).thenThrow(new WorkoutNotFoundException());
        }

        @Test(expected = RessourceNotFoundException.class)
        public void currentWorkoutId_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.currentWorkoutId();
        }

        @Test(expected = RessourceNotFoundException.class)
        public void indexOfCurrentExercise_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.indexOfCurrentExercise();
        }

        @Test(expected = RessourceNotFoundException.class)
        public void countOfExercises_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.countOfExercises();
        }
    }

    public class aWorkoutGiven {
        private Workout workout;

        @Before
        public void setUp() throws Exception {
            workout = new BasicWorkout();
            when(workoutSession.getWorkout()).thenReturn(workout);
        }

        @Test
        public void currentWorkoutId_shouldReturnWorkoutId() throws Exception {
            WorkoutId workoutId = new WorkoutId("42");
            workout.setWorkoutId(workoutId);

            WorkoutId currentWorkoutId = workoutApplicationService.currentWorkoutId();

            assertThat(currentWorkoutId, equalTo(workoutId));
        }

        @Test(expected = RessourceNotFoundException.class)
        public void indexOfCurrentExerciseWithNoExercises_shouldReturnCurrentIndex() throws Exception {
            workoutApplicationService.indexOfCurrentExercise();
        }

        @Test
        public void indexOfCurrentExercise_shouldReturnIndexOf0() throws Exception {
            workout.createExercise();
            int indexOfCurrentExercise = workoutApplicationService.indexOfCurrentExercise();

            assertThat(indexOfCurrentExercise,equalTo(0));
        }

        public void countOfExercises_shouldReturnCountOfExercies() throws Exception {
            workout.createExercise();

            int countOfExercises = workoutApplicationService.countOfExercises();

            assertThat(countOfExercises,equalTo(1));
        }
    }
}