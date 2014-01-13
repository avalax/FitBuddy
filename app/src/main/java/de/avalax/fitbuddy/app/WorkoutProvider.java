package de.avalax.fitbuddy.app;


import com.google.inject.Inject;
import com.google.inject.Provider;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

public class WorkoutProvider implements Provider<Workout> {

    @Inject
    public WorkoutDAO workoutDAO;

    @Override
    public Workout get() {
        return workoutDAO.load();
    }
}
