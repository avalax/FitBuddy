package de.avalax.fitbuddy.datalayer.persistence;

import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.DataLayer;
import de.avalax.fitbuddy.datalayer.DummyDataLayer;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import static org.junit.Assert.*;

public class DummyDataLayerTest {

    private DataLayer dataLayer;

    @Before
    public void setUp() {
        dataLayer = new DummyDataLayer();
    }

    @Test
    public void load_shouldReturnAWorkout() throws Exception {
        assertThat(dataLayer.load(),instanceOf(Workout.class));
    }
}
