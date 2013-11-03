package de.avalax.fitbuddy.edit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


public class NewEditableExerciseTest {
    private EditableExercise editableExercise;

    @Before
    public void setUp() throws Exception {
        editableExercise = new NewEditableExercise();
    }

    @Test
    public void testSetName() throws Exception {
        String name = "new name";
        editableExercise.setName(name);

        assertThat(editableExercise.getName(), equalTo(name));
    }

    @Test
    public void testSetReps() throws Exception {
        int reps = 4;
        editableExercise.setReps(4);

        assertThat(editableExercise.getReps(), equalTo(reps));
    }

    @Test
    public void testSetSets() throws Exception {
        int sets = 4;
        editableExercise.setSets(4);

        assertThat(editableExercise.getSets(), equalTo(sets));
    }

    @Test
    public void testSetWeight() throws Exception {
        double weight = 42.5;
        editableExercise.setWeight(weight);

        assertThat(editableExercise.getWeight(), equalTo(weight));
    }

    @Test
    public void testSetWeightRaise() throws Exception {
        double weightRaise = 42.5;
        editableExercise.setWeightRaise(weightRaise);

        assertThat(editableExercise.getWeightRaise(), equalTo(weightRaise));
    }
}
