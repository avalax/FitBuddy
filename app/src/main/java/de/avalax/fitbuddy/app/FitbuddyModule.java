package de.avalax.fitbuddy.app;

import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;
import de.avalax.fitbuddy.app.edit.EditExerciseActivity;
import de.avalax.fitbuddy.app.edit.WeightExerciseFragment;
import de.avalax.fitbuddy.app.resultChart.ResultChartFragment;
import de.avalax.fitbuddy.app.swipeBar.WeightRaiseCalculator;
import de.avalax.fitbuddy.datalayer.StaticWorkoutDAO;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

import javax.inject.Singleton;

@Module(injects = {
        MainActivity.class,
        ManageWorkoutActivity.class,
        EditExerciseActivity.class,
        CurrentExerciseFragment.class,
        ResultChartFragment.class,
        WeightExerciseFragment.class
})
public class FitbuddyModule {
    private final SharedPreferences sharedPreferences;

    public FitbuddyModule(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Provides
    @Singleton
    WorkoutSession provideWorkoutSession(SharedPreferences sharedPreferences, WorkoutDAO workoutDAO) {
        return new WorkoutSession(sharedPreferences, workoutDAO);
    }

    @Provides
    @Singleton
    WeightRaiseCalculator provideWeightRaiseCalculator() {
        return new WeightRaiseCalculator();
    }

    @Provides
    @Singleton
    WorkoutDAO provideWorkoutDAO() {
        return new StaticWorkoutDAO();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return sharedPreferences;
    }
}
