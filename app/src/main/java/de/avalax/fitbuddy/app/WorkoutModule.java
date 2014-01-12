package de.avalax.fitbuddy.app;

import com.google.inject.AbstractModule;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.DataLayer;
import de.avalax.fitbuddy.datalayer.DummyDataLayer;
import de.avalax.fitbuddy.datalayer.PersistenceWorkout;

public class WorkoutModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataLayer.class).to(DummyDataLayer.class);
        try {
            bind(Workout.class).toConstructor(PersistenceWorkout.class.getConstructor(DataLayer.class));
        } catch (NoSuchMethodException e) {
            addError(e);
        }
    }
}
