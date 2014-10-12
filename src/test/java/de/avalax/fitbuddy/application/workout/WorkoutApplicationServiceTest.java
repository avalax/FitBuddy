package de.avalax.fitbuddy.application.workout;

import de.avalax.fitbuddy.domain.model.RessourceNotFoundException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.*;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(HierarchicalContextRunner.class)
public class WorkoutApplicationServiceTest {

    private WorkoutSession workoutSession;

    private WorkoutApplicationService workoutApplicationService;

    private FinishedWorkoutRepository finishedWorkoutRepository;

    private WorkoutRepository workoutRepository;

    @Before
    public void setUp() throws Exception {
        workoutSession = mock(WorkoutSession.class);
        finishedWorkoutRepository = mock(FinishedWorkoutRepository.class);
        workoutRepository = mock(WorkoutRepository.class);
        workoutApplicationService = new WorkoutApplicationService(workoutSession, workoutRepository, finishedWorkoutRepository);
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

        @Test(expected = RessourceNotFoundException.class)
        public void requestExercise_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.requestExercise(0);
        }

        @Test(expected = RessourceNotFoundException.class)
        public void workoutProgress_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.workoutProgress(0);
        }

        @Test(expected = RessourceNotFoundException.class)
        public void setCurrentExercise_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.setCurrentExercise(0);
        }

        @Test(expected = RessourceNotFoundException.class)
        public void switchToSet_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.switchToSet(0, 0);
        }

        @Test(expected = RessourceNotFoundException.class)
        public void weightOfCurrentSet_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.weightOfCurrentSet(0);
        }

        @Test(expected = RessourceNotFoundException.class)
        public void addRepsToSet_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.addRepsToSet(0, 0);
        }

        @Test(expected = RessourceNotFoundException.class)
        public void updateWeightOfCurrentSet_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.updateWeightOfCurrentSet(0, 0);
        }

        @Test(expected = RessourceNotFoundException.class)
        public void finishWorkout_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.finishCurrentWorkout();
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

        @Test(expected = RessourceNotFoundException.class)
        public void workoutProgress_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.workoutProgress(0);
        }

        @Test(expected = RessourceNotFoundException.class)
        public void setCurrentExercise_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.setCurrentExercise(0);
        }

        @Test(expected = RessourceNotFoundException.class)
        public void switchToSetWithNoExercise_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.switchToSet(0, 0);
        }

        @Test(expected = RessourceNotFoundException.class)
        public void weightOfCurrentSetWithNoExercise_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.weightOfCurrentSet(0);
        }

        @Test(expected = RessourceNotFoundException.class)
        public void addRepsToSetWithNoExercise_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.addRepsToSet(0, 0);
        }

        @Test(expected = RessourceNotFoundException.class)
        public void updateWeightOfCurrentSetWithoutExercise_shouldThrowRessourceNotFoundExeption() throws Exception {
            workoutApplicationService.updateWeightOfCurrentSet(0, 0);
        }

        @Test
        public void finishWorkout_shouldPersistCurrentWorkout() throws Exception {
            workoutApplicationService.finishCurrentWorkout();

            verify(finishedWorkoutRepository).saveWorkout(workout);
        }

        @Test
        public void finishWorkout_shouldResetCurrentWorkout() throws Exception {
            WorkoutId workoutId = workout.getWorkoutId();
            BasicWorkout newWorkoutFromRepository = new BasicWorkout();
            newWorkoutFromRepository.setName("newWorkoutFromRepository");
            when(workoutRepository.load(workoutId)).thenReturn(newWorkoutFromRepository);

            workoutApplicationService.finishCurrentWorkout();

            verify(workoutSession).switchWorkout(newWorkoutFromRepository);
        }

        public class anExerciseGiven {
            private Exercise exercise;

            @Before
            public void setUp() throws Exception {
                exercise = workout.createExercise();
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
                workout.createExercise();

                workoutApplicationService.setCurrentExercise(1);

                assertThat(workoutApplicationService.indexOfCurrentExercise(), equalTo(1));
            }

            @Test(expected = RessourceNotFoundException.class)
            public void switchToSetWithNoSet_shouldThrowRessourceNotFoundExeption() throws Exception {
                workoutApplicationService.switchToSet(0, 0);
            }

            @Test(expected = RessourceNotFoundException.class)
            public void weightOfCurrentSetWithNoSet_shouldThrowRessourceNotFoundExeption() throws Exception {
                workoutApplicationService.weightOfCurrentSet(0);
            }

            @Test(expected = RessourceNotFoundException.class)
            public void addRepsToSetWithNoSet_shouldThrowRessourceNotFoundExeption() throws Exception {
                workoutApplicationService.addRepsToSet(0, 0);
            }

            @Test(expected = RessourceNotFoundException.class)
            public void updateWeightOfCurrentSetWithoutSet_shouldThrowRessourceNotFoundExeption() throws Exception {
                workoutApplicationService.updateWeightOfCurrentSet(0, 0);
            }

            public class aSetGiven {
                private Set set;

                @Before
                public void setUp() throws Exception {
                    set = exercise.createSet();
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

                    assertThat(exercise.indexOfCurrentSet(), equalTo(0));
                }

                @Test
                public void switchToSetSet_shouldSwitchTheSecondSet() throws Exception {
                    exercise.createSet();

                    workoutApplicationService.switchToSet(0, 1);

                    assertThat(exercise.indexOfCurrentSet(), equalTo(1));
                }

                @Test
                public void weightOfCurrentSet_shouldReturnWeight() throws Exception {
                    set.setWeight(42.5);
                    exercise.setCurrentSet(0);

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