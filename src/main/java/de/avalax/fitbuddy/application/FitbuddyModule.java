package de.avalax.fitbuddy.application;

import android.content.SharedPreferences;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;
import de.avalax.fitbuddy.application.manageWorkout.ExerciseListFragment;
import de.avalax.fitbuddy.application.manageWorkout.ManageWorkout;
import de.avalax.fitbuddy.application.manageWorkout.ManageWorkoutActivity;
import de.avalax.fitbuddy.application.manageWorkout.editExercise.EditExerciseActivity;
import de.avalax.fitbuddy.port.adapter.persistence.FakeWorkoutRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

import javax.inject.Singleton;

@Module(injects = {
        MainActivity.class,
        ManageWorkoutActivity.class,
        CurrentExerciseFragment.class,
        ExerciseListFragment.class,
        EditExerciseActivity.class
})
public class FitbuddyModule {
    private SharedPreferences sharedPreferences;

    public FitbuddyModule(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Provides
    @Singleton
    WorkoutSession provideWorkoutSession(SharedPreferences sharedPreferences, WorkoutRepository workoutRepository) {
        return new WorkoutSession(sharedPreferences, workoutRepository);
    }

    @Provides
    @Singleton
    WorkoutRepository provideWorkoutDAO() {
        return new FakeWorkoutRepository();
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
    ManageWorkout provideManageWorkout(WorkoutSession workoutSession, WorkoutRepository workoutRepository, WorkoutFactory workoutFactory, ExerciseFactory exerciseFactory) {
        return new ManageWorkout(workoutSession, workoutRepository, workoutFactory, exerciseFactory);
    }

    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus();
    }
}
