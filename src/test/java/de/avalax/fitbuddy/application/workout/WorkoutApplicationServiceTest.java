package de.avalax.fitbuddy.application.workout;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutId;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(HierarchicalContextRunner.class)
public class WorkoutApplicationServiceTest {

    @Mock
    private WorkoutSession workoutSession;

    @Mock
    private FinishedWorkoutRepository finishedWorkoutRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @InjectMocks
    private WorkoutApplicationService workoutApplicationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    public class noWorkoutGiven {
        @Test(expected = ResourceException.class)
        public void indexOfCurrentExercise_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.indexOfCurrentExercise();
        }

        @Test(expected = ResourceException.class)
        public void countOfExercises_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.countOfExercises();
        }

        @Test(expected = ResourceException.class)
        public void requestExercise_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.requestExercise(0);
        }

        @Test(expected = ResourceException.class)
        public void workoutProgress_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.workoutProgress(0);
        }

        @Test(expected = ResourceException.class)
        public void setCurrentExercise_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.setCurrentExercise(0);
        }

        @Test(expected = ResourceException.class)
        public void switchToSet_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.switchToSet(0, 0);
        }

        @Test(expected = ResourceException.class)
        public void weightOfCurrentSet_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.weightOfCurrentSet(0);
        }

        @Test(expected = ResourceException.class)
        public void addRepsToSet_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.addRepsToSet(0, 0);
        }

        @Test(expected = ResourceException.class)
        public void updateWeightOfCurrentSet_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.updateWeightOfCurrentSet(0, 0);
        }

        @Test(expected = ResourceException.class)
        public void finishWorkout_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.finishCurrentWorkout();
        }

        @Test
        public void hasNextExercise_shouldReturnFalse() throws Exception {
            assertThat(workoutApplicationService.hasNextExercise(0), equalTo(false));
        }

        @Test
        public void hasPreviousExercise_shouldReturnFalse() throws Exception {
            assertThat(workoutApplicationService.hasPreviousExercise(0), equalTo(false));
        }

        @Test
        public void hasActiveWorkout_shouldReturnFalse() throws Exception {
            assertThat(workoutApplicationService.hasActiveWorkout(), equalTo(false));
        }
    }

    public class aWorkoutGiven {
        private Workout workout;

        @Before
        public void setUp() throws Exception {
            workout = new BasicWorkout();
            when(workoutSession.getWorkout()).thenReturn(workout);
            when(workoutSession.hasWorkout()).thenReturn(true);
        }

        @Test
        public void noActiveWorkout_shouldReturnFalse() throws Exception {
            when(workoutSession.hasWorkout()).thenReturn(false);
            when(workoutSession.getWorkout()).thenReturn(null);

            Assertions.assertThat(workoutApplicationService.isActiveWorkout(workout)).isFalse();
        }

        @Test
        public void currentWorkoutIsActive_shouldReturnTrue() throws Exception {
            Assertions.assertThat(workoutApplicationService.isActiveWorkout(workout)).isTrue();
        }

        @Test
        public void switchWorkout_shouldSetWorkout() throws Exception {
            workoutApplicationService.switchWorkout(workout);

            verify(workoutSession).switchWorkout(workout);
        }

        @Test
        public void switchWorkout_shouldPersistCurrentWorkout() throws Exception {
            workoutApplicationService.switchWorkout(workout);

            verify(finishedWorkoutRepository).saveWorkout(workout);
        }

        @Test(expected = ResourceException.class)
        public void indexOfCurrentExerciseWithNoExercises_shouldReturnCurrentIndex() throws Exception {
            workoutApplicationService.indexOfCurrentExercise();
        }

        @Test(expected = ResourceException.class)
        public void workoutProgress_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.workoutProgress(0);
        }

        @Test(expected = ResourceException.class)
        public void setCurrentExercise_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.setCurrentExercise(0);
        }

        @Test(expected = ResourceException.class)
        public void switchToSetWithNoExercise_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.switchToSet(0, 0);
        }

        @Test(expected = ResourceException.class)
        public void weightOfCurrentSetWithNoExercise_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.weightOfCurrentSet(0);
        }

        @Test(expected = ResourceException.class)
        public void addRepsToSetWithNoExercise_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.addRepsToSet(0, 0);
        }

        @Test(expected = ResourceException.class)
        public void updateWeightOfCurrentSetWithoutExercise_shouldThrowResourceNotFoundException() throws Exception {
            workoutApplicationService.updateWeightOfCurrentSet(0, 0);
        }

        @Test
        public void finishWorkout_shouldPersistAndRemoveCurrentWorkoutFromSession() throws Exception {
            doReturn(new FinishedWorkoutId("1")).when(finishedWorkoutRepository).saveWorkout(workout);

            FinishedWorkoutId finishedWorkoutId = workoutApplicationService.finishCurrentWorkout();

            assertThat(finishedWorkoutId, equalTo(new FinishedWorkoutId("1")));
            verify(workoutSession).switchWorkout(null);
        }

        @Test
        public void hasActiveWorkout_shouldReturnTrue() throws Exception {
            assertThat(workoutApplicationService.hasActiveWorkout(), equalTo(true));
        }

        public class anExerciseGiven {
            private Exercise exercise;

            @Before
            public void setUp() throws Exception {
                exercise = workout.getExercises().createExercise();
            }

            @Test
            public void indexOfCurrentExercise_shouldReturnIndexOf0() throws Exception {
                int indexOfCurrentExercise = workoutApplicationService.indexOfCurrentExercise();

                assertThat(indexOfCurrentExercise, equalTo(0));
            }

            @Test
            public void countOfExercises_shouldReturnCountOfExercies() throws Exception {
                int countOfExercises = workoutApplicationService.countOfExercises();

                assertThat(countOfExercises, equalTo(1));
            }

            @Test
            public void requestExercise_shouldReturnExercise() throws Exception {
                Exercise requestedExercise = workoutApplicationService.requestExercise(0);

                assertThat(requestedExercise, equalTo(exercise));
            }

            @Test
            public void setCurrentExercise_shouldSetCurrentExercise() throws Exception {
                workout.getExercises().createExercise();

                workoutApplicationService.setCurrentExercise(1);

                assertThat(workoutApplicationService.indexOfCurrentExercise(), equalTo(1));
            }

            @Test(expected = ResourceException.class)
            public void switchToSetWithNoSet_shouldThrowResourceNotFoundException() throws Exception {
                workoutApplicationService.switchToSet(1, 0);
            }

            @Test(expected = ResourceException.class)
            public void weightOfCurrentSetWithNoSet_shouldThrowResourceNotFoundException() throws Exception {
                workoutApplicationService.weightOfCurrentSet(1);
            }

            @Test(expected = ResourceException.class)
            public void addRepsToSetWithNoSet_shouldThrowResourceNotFoundException() throws Exception {
                workoutApplicationService.addRepsToSet(1, 0);
            }

            @Test(expected = ResourceException.class)
            public void updateWeightOfCurrentSetWithoutSet_shouldThrowResourceNotFoundException() throws Exception {
                workoutApplicationService.updateWeightOfCurrentSet(1, 0);
            }

            @Test
            public void hasNextExercise_shouldReturnFalse() throws Exception {
                assertThat(workoutApplicationService.hasNextExercise(0), equalTo(false));
            }

            @Test
            public void hasNextExercise_shouldReturnTrue() throws Exception {
                workout.getExercises().createExercise();

                assertThat(workoutApplicationService.hasNextExercise(0), equalTo(true));
            }

            @Test
            public void hasPreviousExercise_shouldReturnFalse() throws Exception {
                assertThat(workoutApplicationService.hasPreviousExercise(0), equalTo(false));
            }

            @Test
            public void hasPreviousExercise_shouldReturnTrue() throws Exception {
                workout.getExercises().createExercise();

                assertThat(workoutApplicationService.hasPreviousExercise(1), equalTo(true));
            }

            public class aSetGiven {
                private Set set;

                @Before
                public void setUp() throws Exception {
                    set = exercise.getSets().createSet();
                }

                @Test
                public void workoutProgress_shouldReturnProgressOfWorkout() throws Exception {
                    set.setMaxReps(50);
                    set.setReps(50);
                    int progress = workoutApplicationService.workoutProgress(0);

                    assertThat(progress, equalTo(100));
                }

                @Test
                public void switchToSetSet_shouldSwitchTheFirstSet() throws Exception {
                    workoutApplicationService.switchToSet(0, 0);

                    assertThat(exercise.getSets().indexOfCurrentSet(), equalTo(0));
                }

                @Test
                public void switchToSetSet_shouldSwitchTheSecondSet() throws Exception {
                    exercise.getSets().createSet();

                    workoutApplicationService.switchToSet(0, 1);

                    assertThat(exercise.getSets().indexOfCurrentSet(), equalTo(1));
                }

                @Test
                public void weightOfCurrentSet_shouldReturnWeight() throws Exception {
                    set.setWeight(42.5);
                    exercise.getSets().setCurrentSet(0);

                    double weightOfCurrentSet = workoutApplicationService.weightOfCurrentSet(0);

                    assertThat(weightOfCurrentSet, equalTo(42.5));
                }

                @Test
                public void addRepsToSet_shouldAddRepsToSet() throws Exception {
                    set.setMaxReps(15);

                    workoutApplicationService.addRepsToSet(0, 15);

                    assertThat(set.getReps(), equalTo(15));
                }

                @Test
                public void updateWeightOfCurrentSet_shouldSetWeightOfCurrentSet() throws Exception {
                    set.setWeight(21);

                    workoutApplicationService.updateWeightOfCurrentSet(0, 42);

                    assertThat(set.getWeight(), equalTo(42.0));
                }
            }
        }
    }
}