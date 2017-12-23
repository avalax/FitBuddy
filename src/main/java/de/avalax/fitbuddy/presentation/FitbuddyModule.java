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
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.port.adapter.persistence.FitbuddySQLiteOpenHelper;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteExerciseRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteFinishedExerciseRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteFinishedWorkoutRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteSetRepository;
import de.avalax.fitbuddy.port.adapter.persistence.SQLiteWorkoutRepository;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseViewHelper;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutViewHelper;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutViewHelper;

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
        FinishedExerciseRepository finishedExerciseRepository
                = new SQLiteFinishedExerciseRepository(sqLiteOpenHelper);
        finishedWorkoutRepository = new SQLiteFinishedWorkoutRepository(
                sqLiteOpenHelper, finishedExerciseRepository, workoutRepository);
    }

    @Provides
    @Singleton
    WorkoutApplicationService provideWorkoutApplicationService() {
        return new WorkoutApplicationService(
                workoutSession,
                finishedWorkoutRepository);
    }

    @Provides
    @Singleton
    FinishedWorkoutApplicationService provideFinishedWorkoutApplicationService() {
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
    EditWorkoutApplicationService provideManageWorkout() {
        return new EditWorkoutApplicationService(
                workoutRepository);
    }

    @Provides
    @Singleton
    EditWorkoutViewHelper provideEditWorkoutViewHelper(EditExerciseViewHelper editExerciseViewHelper) {
        return new EditWorkoutViewHelper(context, editExerciseViewHelper);
    }

    @Provides
    @Singleton
    WorkoutViewHelper provideWorkoutViewHelper() {
        return new WorkoutViewHelper(context);
    }

    @Provides
    @Singleton
    EditExerciseViewHelper provideEditExerciseApplicationService() {
        return new EditExerciseViewHelper(context);
    }
}
