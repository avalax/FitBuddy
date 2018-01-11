package de.avalax.fitbuddy.presentation;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutService;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutService;
import de.avalax.fitbuddy.application.workout.WorkoutService;
import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExerciseRepository;
import de.avalax.fitbuddy.domain.model.finished_set.FinishedSetRepository;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.port.adapter.persistence.FitbuddySQLiteOpenHelper;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteExerciseRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteFinishedExerciseRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteFinishedSetRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteFinishedWorkoutRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteSetRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteWorkoutRepository;
import de.avalax.fitbuddy.presentation.ad_mob.AdMobProvider;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseViewHelper;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutViewHelper;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;
import de.avalax.fitbuddy.presentation.summary.FinishedExerciseViewHelper;
import de.avalax.fitbuddy.presentation.summary.FinishedWorkoutViewHelper;

@Module
public class FitbuddyModule {
    private final WorkoutSession workoutSession;
    private final Context context;
    private final WorkoutRepository workoutRepository;
    private final FinishedWorkoutRepository finishedWorkoutRepository;

    public FitbuddyModule(Context context) {
        this.context = context;
        this.workoutSession = new WorkoutSession(context);
        SQLiteOpenHelper sqLiteOpenHelper =
                new FitbuddySQLiteOpenHelper("fitbuddy", 1, context, R.raw.fitbuddy_db);
        SetRepository setRepository = new SQLiteSetRepository(sqLiteOpenHelper);
        ExerciseRepository exerciseRepository =
                new SQLiteExerciseRepository(sqLiteOpenHelper, setRepository);
        workoutRepository = new SQLiteWorkoutRepository(sqLiteOpenHelper, exerciseRepository);
        FinishedSetRepository finishedSetRepository = new SQLiteFinishedSetRepository(sqLiteOpenHelper);
        FinishedExerciseRepository finishedExerciseRepository
                = new SQLiteFinishedExerciseRepository(sqLiteOpenHelper, finishedSetRepository);
        finishedWorkoutRepository = new SQLiteFinishedWorkoutRepository(
                sqLiteOpenHelper, finishedExerciseRepository);
    }

    @Provides
    @Singleton
    WorkoutService provideWorkoutService() {
        return new WorkoutService(
                workoutSession,
                finishedWorkoutRepository);
    }

    @Provides
    @Singleton
    FinishedWorkoutService provideFinishedWorkoutService() {
        return new FinishedWorkoutService(finishedWorkoutRepository);
    }

    @Provides
    @Singleton
    ExerciseViewHelper provideExerciseViewHelper() {
        Locale locale = context.getResources().getConfiguration().locale;
        return new ExerciseViewHelper(context, locale);
    }

    @Provides
    @Singleton
    EditWorkoutService provideManageWorkout() {
        return new EditWorkoutService(
                workoutRepository);
    }

    @Provides
    @Singleton
    EditWorkoutViewHelper provideEditWorkoutViewHelper(EditExerciseViewHelper editExerciseViewHelper) {
        return new EditWorkoutViewHelper(context, editExerciseViewHelper);
    }

    @Provides
    @Singleton
    FinishedWorkoutViewHelper provideFinishedWorkoutViewHelper() {
        return new FinishedWorkoutViewHelper(context, finishedWorkoutRepository);
    }

    @Provides
    @Singleton
    FinishedExerciseViewHelper provideFinishedExerciseViewHelper() {
        return new FinishedExerciseViewHelper();
    }

    @Provides
    @Singleton
    EditExerciseViewHelper provideEditExerciseViewHelper() {
        return new EditExerciseViewHelper(context);
    }

    @Provides
    @Singleton
    AdMobProvider provideAdMobProvider() {
        return new AdMobProvider(context);
    }
}
