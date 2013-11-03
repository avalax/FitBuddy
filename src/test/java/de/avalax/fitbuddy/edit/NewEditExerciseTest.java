package de.avalax.fitbuddy.edit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


public class NewEditExerciseTest {
    private EditExercise editExercise;

    @Before
    public void setUp() throws Exception {
        editExercise = new NewEditExercise();
    }

    @Test
    public void testSetName() throws Exception {
        String name = "new name";
        editExercise.setName(name);

        assertThat(editExercise.getName(), equalTo(name));
    }

    @Test
    public void testSetReps() throws Exception {
        int reps = 4;
        editExercise.setReps(4);

        assertThat(editExercise.getReps(), equalTo(reps));
    }

    @Test
    public void testSetSets() throws Exception {
        int sets = 4;
        editExercise.setSets(4);

        assertThat(editExercise.getSets(), equalTo(sets));
    }

    @Test
    public void testSetWeight() throws Exception {
        double weight = 42.5;
        editExercise.setWeight(weight);

        assertThat(editExercise.getWeight(), equalTo(weight));
    }

    @Test
    public void testSetWeightRaise() throws Exception {
        double weightRaise = 42.5;
        editExercise.setWeightRaise(weightRaise);

        assertThat(editExercise.getWeightRaise(), equalTo(weightRaise));
    }
}
