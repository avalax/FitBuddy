package de.avalax.fitbuddy.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import de.avalax.fitbuddy.application.manageWorkout.ExerciseListFragment;
import de.avalax.fitbuddy.application.manageWorkout.ManageWorkout;
import de.avalax.fitbuddy.application.manageWorkout.ManageWorkoutActivity;
import de.avalax.fitbuddy.application.manageWorkout.editExercise.EditExerciseActivity;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.port.adapter.persistence.FitbuddySQLiteOpenHelper;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteExerciseRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteSetRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteWorkoutRepository;

import javax.inject.Singleton;

@Module(injects = {
        MainActivity.class,
        ManageWorkoutActivity.class,
        CurrentExerciseFragment.class,
        ExerciseListFragment.class,
        EditExerciseActivity.class
})
public class FitbuddyModule {
    private Context context;
    private SharedPreferences sharedPreferences;

    public FitbuddyModule(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    @Provides
    @Singleton
    WorkoutSession provideWorkoutSession(SharedPreferences sharedPreferences, WorkoutRepository workoutRepository, WorkoutFactory workoutFactory) {
        return new WorkoutSession(sharedPreferences, workoutRepository, workoutFactory);
    }

    @Provides
    @Singleton
    SQLiteOpenHelper provideSQLiteOpenHelper() {
        return new FitbuddySQLiteOpenHelper("fitbuddy", 1, context, R.raw.fitbuddy_db);
    }

    @Provides
    @Singleton
    WorkoutRepository provideWorkoutRepository(SQLiteOpenHelper sqLiteOpenHelper, ExerciseRepository exerciseRepository) {
        return new SQLiteWorkoutRepository(sqLiteOpenHelper, exerciseRepository);
    }

    @Provides
    @Singleton
    ExerciseRepository provideExerciseRepository(SQLiteOpenHelper sqLiteOpenHelper, SetRepository setRepository) {
        return new SQLiteExerciseRepository(sqLiteOpenHelper, setRepository);
    }

    @Provides
    @Singleton
    SetRepository provideSetRepository(SQLiteOpenHelper sqLiteOpenHelper) {
        return new SQLiteSetRepository(sqLiteOpenHelper);
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
    ManageWorkout provideManageWorkout(WorkoutSession workoutSession, WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, SetRepository setRepository, WorkoutFactory workoutFactory, ExerciseFactory exerciseFactory) {
        return new ManageWorkout(workoutSession, workoutRepository, exerciseRepository, setRepository, workoutFactory, exerciseFactory);
    }

    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus();
    }
}
