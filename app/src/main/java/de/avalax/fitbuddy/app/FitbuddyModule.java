package de.avalax.fitbuddy.app;

import dagger.Module;
import dagger.Provides;
import de.avalax.fitbuddy.app.edit.WeightExerciseFragment;
import de.avalax.fitbuddy.app.resultChart.ResultChartFragment;
import de.avalax.fitbuddy.app.swipeBar.WeightRaiseCalculator;
import de.avalax.fitbuddy.datalayer.StaticWorkoutDAO;

import javax.inject.Singleton;

@Module(injects = {
        MainActivity.class,
        ManageWorkoutActivity.class,
        CurrentExerciseFragment.class,
        ResultChartFragment.class,
        FinishWorkoutFragment.class,
        WeightExerciseFragment.class,
        ManageWorkoutActivity.class
    })
public class FitbuddyModule {
    @Provides
    @Singleton
    WorkoutSession provideWorkoutSession() {
        StaticWorkoutDAO workoutDAO = new StaticWorkoutDAO();
        return new WorkoutSession(workoutDAO);
    }

    @Provides
    @Singleton
    WeightRaiseCalculator provideWeightRaiseCalculator() {
        return new WeightRaiseCalculator();
    }
}
