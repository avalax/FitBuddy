package de.avalax.fitbuddy.workout.persistence;

import de.avalax.fitbuddy.workout.Workout;

public interface DataLayer {
    Workout load();
}
