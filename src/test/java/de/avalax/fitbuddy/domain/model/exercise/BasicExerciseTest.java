package de.avalax.fitbuddy.domain.model.exercise;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetException;
import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
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
    public void differentObject_shouldHaveDifferentIdentity() throws Exception {
        exercise.setExerciseId(new ExerciseId("42"));
        assertThat(exercise.equals("42"), is(false));
    }

    @Test
    public void setName_shouldSetNameOfExercise() throws Exception {
        String name = "newName";

        exercise.setName(name);

        assertThat(exercise.getName(), equalTo(name));
    }

    @Test
    public void getId_shouldReturnId() throws Exception {
        ExerciseId exerciseId = new ExerciseId("42");
        exercise.setExerciseId(exerciseId);

        assertThat(exercise.getExerciseId(), equalTo(exerciseId));
    }

    @Test
    public void toString_shouldReturnSetInformation() throws Exception {
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
        public void countOfSets_shouldReturnZeroOnConstruction() throws Exception {
            assertThat(exercise.getSets().size(), equalTo(0));
        }

        @Test
        public void addSet_shouldAddSetToExercise() throws Exception {
            Set set = exercise.getSets().createSet();

            assertThat(exercise.getSets().get(0), equalTo(set));
        }

        @Test
        public void removeSet_shouldRemoveSetFromExercise() throws Exception {
            Set set = exercise.getSets().createSet();

            exercise.getSets().remove(set);
            assertThat(exercise.getSets().size(), equalTo(0));
        }

        @Test
        public void setAtPosition_shouldReturnSecondSet() throws Exception {
            exercise.getSets().createSet();
            Set set = exercise.getSets().createSet();

            Set setAtPosition = exercise.getSets().get(1);

            assertThat(setAtPosition, equalTo(set));
        }

        @Test(expected = SetException.class)
        public void setCurrentSetWithoutSets_shouldThrowSetNotFoundException() throws Exception {
            exercise.getSets().setCurrentSet(1);
        }

        @Test(expected = SetException.class)
        public void indexOFCurrentSetWithoutSets_shouldThrowSetNotFoundException() throws Exception {
            exercise.getSets().indexOfCurrentSet();
        }

        @Test(expected = SetException.class)
        public void setCurrentSetToNegativIndex_shouldThrowSetNotFoundException() throws Exception {
            exercise.getSets().createSet();

            exercise.getSets().setCurrentSet(-1);

            assertThat(exercise.getSets().indexOfCurrentSet(), equalTo(0));
        }

        @Test(expected = SetException.class)
        public void setCurrentSet_shouldThrowSetNotFoundException() throws Exception {
            exercise.getSets().createSet();

            exercise.getSets().setCurrentSet(exercise.getSets().size() + 1);
        }

        @Test(expected = SetException.class)
        public void setAtPosition_shouldThrowSetNotFoundExceptionWhenNoSetsAvailable() throws Exception {
            exercise.getSets().get(0);
        }

        @Test(expected = SetException.class)
        public void setAtNegativePosition_shouldThrowSetNotFoundExceptionWhenNoSetsAvailable() throws Exception {
            exercise.getSets().get(-1);
        }

        @Test
        public void setsOfExercise_shouldReturnSets() throws Exception {
            Set set = exercise.getSets().createSet();
            Set set2 = exercise.getSets().createSet();

            assertThat(exercise.getSets(), containsInAnyOrder(set, set2));
        }

        public class givenAnExerciseProgress {
            @Test
            public void withoutSets_shouldHaveZeroProgress() throws Exception {
                assertThat(exercise.getProgress(), equalTo(0.0));
            }

            @Test
            public void oneSetWithoutReps_shouldHaveZeroProgress() throws Exception {
                Set set = exercise.getSets().createSet();
                set.setMaxReps(100);
                set.setReps(0);

                assertThat(exercise.getProgress(), equalTo(0.0));
            }

            @Test
            public void oneSetWithMaxReps_shouldHaveFullProgress() throws Exception {
                Set set = exercise.getSets().createSet();
                set.setMaxReps(100);
                set.setReps(100);
                assertThat(exercise.getProgress(), equalTo(1.0));
            }

            @Test
            public void oneSetWithHalfReps_shouldHaveHalfProgress() throws Exception {
                Set set = exercise.getSets().createSet();
                set.setMaxReps(100);
                set.setReps(50);
                assertThat(exercise.getProgress(), equalTo(0.5));
            }

            @Test
            public void twoSetsWithoutReps_shouldHaveHalfProgress() throws Exception {
                exercise.getSets().createSet();
                Set set = exercise.getSets().createSet();
                set.setMaxReps(100);
                set.setReps(0);
                exercise.getSets().setCurrentSet(1);
                assertThat(exercise.getProgress(), equalTo(0.5));
            }

            @Test
            public void twoSetsWithMaxReps_shouldHaveFallProgress() throws Exception {
                exercise.getSets().createSet();
                Set set = exercise.getSets().createSet();
                set.setMaxReps(100);
                set.setReps(100);
                exercise.getSets().setCurrentSet(1);
                assertThat(exercise.getProgress(), equalTo(1.0));
            }

            @Test
            public void twoSetsWithHalfReps_shouldHave75Progress() throws Exception {
                exercise.getSets().createSet();
                Set set = exercise.getSets().createSet();
                set.setMaxReps(100);
                set.setReps(50);
                exercise.getSets().setCurrentSet(1);
                assertThat(exercise.getProgress(), equalTo(0.75));
            }
        }
    }
}