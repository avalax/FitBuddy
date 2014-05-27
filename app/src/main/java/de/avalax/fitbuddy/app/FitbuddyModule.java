package de.avalax.fitbuddy.app;

import android.content.SharedPreferences;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import de.avalax.fitbuddy.app.manageWorkout.EditExerciseDialogFragment;
import de.avalax.fitbuddy.app.manageWorkout.ExerciseListFragment;
import de.avalax.fitbuddy.app.manageWorkout.ManageWorkout;
import de.avalax.fitbuddy.app.manageWorkout.ManageWorkoutActivity;
import de.avalax.fitbuddy.app.resultChart.ResultChartFragment;
import de.avalax.fitbuddy.datalayer.FakeWorkoutDAO;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

import javax.inject.Singleton;

@Module(injects = {
        MainActivity.class,
        ManageWorkoutActivity.class,
        CurrentExerciseFragment.class,
        ResultChartFragment.class,
        ExerciseListFragment.class,
        EditExerciseDialogFragment.class
})
public class FitbuddyModule {
    private SharedPreferences sharedPreferences;

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
    WorkoutDAO provideWorkoutDAO() {
        return new FakeWorkoutDAO();
        //return new SqliteWorkoutDAO(context, R.raw.fitbuddy_db);
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

    @Provides
    @Singleton
    ExerciseFactory provideExerciseFactory() {
        return new ExerciseFactory();
    }

    @Provides
    @Singleton
    ManageWorkout provideManageWorkout(WorkoutSession workoutSession, WorkoutDAO workoutDAO, WorkoutFactory workoutFactory, ExerciseFactory exerciseFactory) {
        return new ManageWorkout(workoutSession, workoutDAO, workoutFactory, exerciseFactory);
    }

    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus();
    }
}
