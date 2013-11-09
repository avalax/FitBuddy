package de.avalax.fitbuddy.core.workout.persistence;

import com.google.inject.ImplementedBy;
import de.avalax.fitbuddy.core.workout.Workout;

@ImplementedBy(DummyDataLayer.class)
public interface DataLayer {
    Workout load();
}
