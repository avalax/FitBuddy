package de.avalax.fitbuddy.app;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import de.avalax.fitbuddy.datalayer.StaticWorkoutDAO;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

public class WorkoutModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(WorkoutDAO.class).to(StaticWorkoutDAO.class).in(Singleton.class);
        bind(WorkoutSession.class).in(Singleton.class);
    }
}
