package de.avalax.fitbuddy.workout;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class BasicWorkoutSetTest {

    WorkoutSet workoutSet;
    List<Set> sets;

    @Before
    public void setUp() throws Exception{
        sets = new ArrayList<Set>();
        workoutSet = new BasicWorkoutSet("Bankdrücken",4,sets);
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnNameBankdrücken() throws Exception {
        assertThat(workoutSet.getName(), equalTo("Bankdrücken"));
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnNumberOfSets() throws Exception {
        assertThat(workoutSet.getNumberOfSets(),equalTo(4));
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnTendencyPlus() throws Exception {
        workoutSet.setTendency(Tendency.PLUS);

        assertThat(workoutSet.getTendency(), equalTo(Tendency.PLUS));
    }

    @Test
    public void WorkoutSet_ShouldReturnFirstSet() throws Exception {
        Set set = createBasicSet();

        sets.add(set);

        assertThat(workoutSet.getSet(1),equalTo(set));
    }

    @Test
    public void BasicWorkoutSet_ShouldReturnLastSet() throws Exception {
        Set set = createBasicSet();

        for (int i=0;i<workoutSet.getNumberOfSets()-1;i++) {
            sets.add(createBasicSet());
        }
        sets.add(set);

        assertThat(workoutSet.getSet(workoutSet.getNumberOfSets()),equalTo(set));
    }

    private BasicSet createBasicSet() {
        return new BasicSet(12.5);
    }
}
