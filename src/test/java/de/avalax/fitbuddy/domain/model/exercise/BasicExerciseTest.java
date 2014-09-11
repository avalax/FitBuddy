package de.avalax.fitbuddy.domain.model.exercise;


import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetNotFoundException;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(HierarchicalContextRunner.class)
public class BasicExerciseTest {
    private Exercise exercise;

    @Before
    public void setUp() throws Exception {
        exercise = new BasicExercise();
    }

    @Test
    public void equalExerciseId_shouldHaveTheSameIdentity() throws Exception {
        ExerciseId exerciseId = new ExerciseId("42");
        exercise.setExerciseId(exerciseId);
        Exercise exercise2 = new BasicExercise();
        exercise2.setExerciseId(exerciseId);

        assertThat(exercise, equalTo(exercise2));
        assertThat(exercise.hashCode(), equalTo(exercise2.hashCode()));
    }

    @Test
    public void differentExerciseIds_shouldHaveDifferentIdentity() throws Exception {
        exercise.setExerciseId(new ExerciseId("21"));
        Exercise exercise2 = new BasicExercise();
        exercise2.setExerciseId(new ExerciseId("42"));

        assertThat(exercise, not(equalTo(exercise2)));
        assertThat(exercise.hashCode(), not(equalTo(exercise2.hashCode())));
    }

    @Test
    public void nullValue_shouldHaveDifferentIdentity() throws Exception {
        Exercise exercise2 = new BasicExercise();

        assertThat(exercise, not(equalTo(exercise2)));
        assertThat(exercise.hashCode(), not(equalTo(exercise2.hashCode())));
    }

    @Test
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public void differntObject_shouldHaveDifferentIdentity() throws Exception {
        exercise.setExerciseId(new ExerciseId("42"));
        assertThat(exercise.equals("42"), is(false));
    }

    @Test
    public void nullValue_shouldSetNameToEmptyString() throws Exception {
        exercise.setName(null);

        assertThat(exercise.getName(), equalTo(""));
    }

    @Test
    public void setName_shouldSetNameOfExercise() throws Exception {
        String name = "newName";

        exercise.setName(name);

        assertThat(exercise.getName(), equalTo(name));
    }

    @Test
    public void setNameWithSpace_shouldSetTrimedName() throws Exception {
        String name = " newName ";

        exercise.setName(name);

        assertThat(exercise.getName(), equalTo("newName"));
    }

    @Test
    public void getId_shouldReturnId() throws Exception {
        ExerciseId exerciseId = new ExerciseId("42");
        exercise.setExerciseId(exerciseId);

        assertThat(exercise.getExerciseId(), equalTo(exerciseId));
    }

    @Test
    public void toString_shouldReturnSetInformations() throws Exception {
        String name = "NameOfExercise";
        exercise.setName(name);

        assertThat(exercise.toString(), equalTo("BasicExercise [name=" + name + "]"));
        ExerciseId exerciseId = new ExerciseId("42");
        exercise.setExerciseId(exerciseId);
        assertThat(exercise.toString(), equalTo("BasicExercise [name=" + name + ", exerciseId=" + exerciseId.toString() + "]"));
    }

    public class givenAnExerciseWithSets {
        @Before
        public void setUp() throws Exception {
            exercise = new BasicExercise();
        }

        @Test
        public void getName_shouldReturnNameFromInitialization() throws Exception {
            assertThat(exercise.getName(), equalTo(""));
        }

        @Test
        public void countOfSets_shouldReturnZeroOnConstruction() throws Exception {
            assertThat(exercise.countOfSets(), equalTo(0));
        }

        @Test
        public void addSet_shouldAddSetToExercise() throws Exception {
            Set set = exercise.createSet();

            assertThat(exercise.setAtPosition(0), equalTo(set));
        }

        @Test
        public void removeSet_shouldRemoveSetFromExercise() throws Exception {
            Set set = exercise.createSet();

            exercise.removeSet(set);
            assertThat(exercise.countOfSets(), equalTo(0));
        }

        @Test
        public void setAtPosition_shouldReturnSecondSet() throws Exception {
            exercise.createSet();
            Set set = exercise.createSet();

            Set setAtPosition = exercise.setAtPosition(1);

            assertThat(setAtPosition, equalTo(set));
        }

        @Test(expected = SetNotFoundException.class)
        public void setCurrentSetWithoutSets_shouldThrowSetNotFoundException() throws Exception {
            exercise.setCurrentSet(1);
        }

        @Test(expected = SetNotFoundException.class)
        public void indexOFCurrentSetWithoutSets_shouldThrowSetNotFoundException() throws Exception {
            exercise.indexOfCurrentSet();
        }

        @Test(expected = SetNotFoundException.class)
        public void setCurrentSetToNegativIndex_shouldThrowSetNotFoundException() throws Exception {
            exercise.createSet();

            exercise.setCurrentSet(-1);

            assertThat(exercise.indexOfCurrentSet(), equalTo(0));
        }

        @Test(expected = SetNotFoundException.class)
        public void setCurrentSet_shouldThrowSetNotFoundException() throws Exception {
            exercise.createSet();

            exercise.setCurrentSet(exercise.countOfSets() + 1);
        }

        @Test(expected = SetNotFoundException.class)
        public void setAtPosition_shouldThrowSetNotFoundExceptionWhenNoSetsAvailable() throws Exception {
            exercise.setAtPosition(0);
        }

        @Test(expected = SetNotFoundException.class)
        public void setAtNegativePosition_shouldThrowSetNotFoundExceptionWhenNoSetsAvailable() throws Exception {
            exercise.setAtPosition(-1);
        }

        @Test
        public void setsOfExercise_shouldReturnSets() throws Exception {
            Set set = exercise.createSet();
            Set set2 = exercise.createSet();

            assertThat(exercise.setsOfExercise(), hasItems(set, set2));
        }

        public class givenAnExerciseProgress {
            @Test
            public void withoutSets_shouldHaveZeroProgress() throws Exception {
                assertThat(exercise.getProgress(), equalTo(0.0));
            }

            @Test
            public void oneSetWithoutReps_shouldHaveZeroProgress() throws Exception {
                Set set = exercise.createSet();
                set.setMaxReps(100);
                set.setReps(0);

                assertThat(exercise.getProgress(), equalTo(0.0));
            }

            @Test
            public void oneSetWithMaxReps_shouldHaveFullProgress() throws Exception {
                Set set = exercise.createSet();
                set.setMaxReps(100);
                set.setReps(100);
                assertThat(exercise.getProgress(), equalTo(1.0));
            }

            @Test
            public void oneSetWithHalfReps_shouldHaveHalfProgress() throws Exception {
                Set set = exercise.createSet();
                set.setMaxReps(100);
                set.setReps(50);
                assertThat(exercise.getProgress(), equalTo(0.5));
            }

            @Test
            public void twoSetsWithoutReps_shouldHaveHalfProgress() throws Exception {
                exercise.createSet();
                Set set = exercise.createSet();
                set.setMaxReps(100);
                set.setReps(0);
                exercise.setCurrentSet(1);
                assertThat(exercise.getProgress(), equalTo(0.5));
            }

            @Test
            public void twoSetsWithMaxReps_shouldHaveFallProgress() throws Exception {
                exercise.createSet();
                Set set = exercise.createSet();
                set.setMaxReps(100);
                set.setReps(100);
                exercise.setCurrentSet(1);
                assertThat(exercise.getProgress(), equalTo(1.0));
            }

            @Test
            public void twoSetsWithHalfReps_shouldHave75Progress() throws Exception {
                exercise.createSet();
                Set set = exercise.createSet();
                set.setMaxReps(100);
                set.setReps(50);
                exercise.setCurrentSet(1);
                assertThat(exercise.getProgress(), equalTo(0.75));
            }
        }
    }
}