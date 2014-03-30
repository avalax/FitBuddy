package de.avalax.fitbuddy.app;

import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;
import de.avalax.fitbuddy.app.editExercise.EditExerciseActivity;
import de.avalax.fitbuddy.app.editExercise.WeightExerciseFragment;
import de.avalax.fitbuddy.app.manageWorkout.ManageWorkoutActivity;
import de.avalax.fitbuddy.app.resultChart.ResultChartFragment;
import de.avalax.fitbuddy.app.swipeBar.WeightRaiseCalculator;
import de.avalax.fitbuddy.datalayer.FakeWorkoutDAO;
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
        return new FakeWorkoutDAO();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return sharedPreferences;
    }

    @Provides
    @Singleton
    WorkoutFactory provideWorkoutFactory() {
        return new WorkoutFactory();
    }
}
