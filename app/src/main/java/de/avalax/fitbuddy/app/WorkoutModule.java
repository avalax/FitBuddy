package de.avalax.fitbuddy.app;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.StaticWorkoutDAO;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

public class WorkoutModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(WorkoutDAO.class).to(StaticWorkoutDAO.class).in(Singleton.class);
        bind(Workout.class).toProvider(WorkoutProvider.class).in(Singleton.class);
    }
}
