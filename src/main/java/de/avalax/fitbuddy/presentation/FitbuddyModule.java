package de.avalax.fitbuddy.presentation;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutApplicationService;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExerciseRepository;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParserService;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.port.adapter.persistence.FitbuddySQLiteOpenHelper;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteExerciseRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteFinishedExerciseRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteFinishedWorkoutRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteSetRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteWorkoutRepository;
import de.avalax.fitbuddy.port.adapter.service.JsonToWorkoutAdapter;
import de.avalax.fitbuddy.port.adapter.service.WorkoutParserJsonService;
import de.avalax.fitbuddy.port.adapter.service.WorkoutToJsonAdapter;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;

@Module
public class FitbuddyModule {
    private Context context;

    public FitbuddyModule(Context context) {
        this.context = context;
    }


    @Provides
    @Singleton
    WorkoutSession provideWorkoutSession() {
        return new WorkoutSession(context);
    }

    @Provides
    @Singleton
    SQLiteOpenHelper provideSQLiteOpenHelper() {
        return new FitbuddySQLiteOpenHelper("fitbuddy", 1, context, R.raw.fitbuddy_db);
    }

    @Provides
    @Singleton
    WorkoutRepository provideWorkoutRepository(
            SQLiteOpenHelper sqLiteOpenHelper,
            ExerciseRepository exerciseRepository) {
        return new SQLiteWorkoutRepository(sqLiteOpenHelper, exerciseRepository);
    }

    @Provides
    @Singleton
    ExerciseRepository provideExerciseRepository(
            SQLiteOpenHelper sqLiteOpenHelper,
            SetRepository setRepository) {
        return new SQLiteExerciseRepository(sqLiteOpenHelper, setRepository);
    }

    @Provides
    @Singleton
    SetRepository provideSetRepository(
            SQLiteOpenHelper sqLiteOpenHelper) {
        return new SQLiteSetRepository(sqLiteOpenHelper);
    }

    @Provides
    @Singleton
    FinishedWorkoutRepository provideFinishWorkoutRepository(
            SQLiteOpenHelper sqLiteOpenHelper,
            FinishedExerciseRepository finishedExerciseRepository) {
        return new SQLiteFinishedWorkoutRepository(sqLiteOpenHelper, finishedExerciseRepository);
    }

    @Provides
    @Singleton
    FinishedExerciseRepository provideFinishedExerciseRepository(
            SQLiteOpenHelper sqLiteOpenHelper) {
        return new SQLiteFinishedExerciseRepository(sqLiteOpenHelper);
    }

    @Provides
    @Singleton
    WorkoutApplicationService provideWorkoutApplicationService(
            WorkoutSession workoutSession,
            WorkoutRepository workoutRepository,
            FinishedWorkoutRepository finishedWorkoutRepository) {
        return new WorkoutApplicationService(
                workoutSession,
                workoutRepository,
                finishedWorkoutRepository);
    }

    @Provides
    @Singleton
    FinishedWorkoutApplicationService provideFinishedWorkoutApplicationService(
            FinishedWorkoutRepository finishedWorkoutRepository) {
        return new FinishedWorkoutApplicationService(finishedWorkoutRepository);
    }

    @Provides
    @Singleton
    ExerciseViewHelper provideExerciseViewHelper() {
        Locale locale = context.getResources().getConfiguration().locale;
        return new ExerciseViewHelper(locale);
    }

    @Provides
    @Singleton
    WorkoutParserService provideWorkoutService() {
        JsonToWorkoutAdapter jsonToWorkoutAdapter = new JsonToWorkoutAdapter();
        WorkoutToJsonAdapter workoutToJsonAdapter = new WorkoutToJsonAdapter();
        return new WorkoutParserJsonService(jsonToWorkoutAdapter, workoutToJsonAdapter);
    }

    @Provides
    @Singleton
    EditWorkoutApplicationService provideManageWorkout(
            WorkoutSession workoutSession,
            FinishedWorkoutRepository finishedWorkoutRepository,
            WorkoutRepository workoutRepository,
            ExerciseRepository exerciseRepository,
            SetRepository setRepository,
            WorkoutParserService workoutParserService) {
        return new EditWorkoutApplicationService(
                workoutSession,
                finishedWorkoutRepository,
                workoutRepository,
                exerciseRepository,
                setRepository,
                workoutParserService);
    }

    @Provides
    @Singleton
    de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutApplicationService provideEditWorkoutApplicationService() {
        return new de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutApplicationService();
    }
}
