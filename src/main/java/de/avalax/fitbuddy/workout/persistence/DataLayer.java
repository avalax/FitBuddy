package de.avalax.fitbuddy.workout.persistence;

import com.google.inject.ImplementedBy;
import de.avalax.fitbuddy.workout.Workout;

@ImplementedBy(DummyDataLayer.class)
public interface DataLayer {
    Workout load();
}
