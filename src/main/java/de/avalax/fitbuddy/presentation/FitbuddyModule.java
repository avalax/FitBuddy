package de.avalax.fitbuddy.presentation;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import dagger.Module;
import dagger.Provides;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.finishedExercise.FinishedExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutService;
import de.avalax.fitbuddy.port.adapter.persistence.*;
import de.avalax.fitbuddy.port.adapter.service.JsonInWorkoutAdapter;
import de.avalax.fitbuddy.port.adapter.service.TranslatingWorkoutService;
import de.avalax.fitbuddy.port.adapter.service.WorkoutInJsonAdapter;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseDialogFragment;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity;
import de.avalax.fitbuddy.presentation.edit.workout.ExerciseAdapter;
import de.avalax.fitbuddy.presentation.edit.workout.ExerciseListFragment;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;
import de.avalax.fitbuddy.presentation.workout.ExerciseFragment;
import de.avalax.fitbuddy.presentation.workout.WorkoutActivity;
import de.avalax.fitbuddy.R;

import javax.inject.Singleton;
import java.util.Locale;

@Module(injects = {
        WorkoutActivity.class,
        EditWorkoutActivity.class,
        ExerciseFragment.class,
        ExerciseListFragment.class,
        EditExerciseActivity.class,
        ExerciseAdapter.class,
        EditExerciseDialogFragment.class
})
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
    FinishedWorkoutRepository provideFinishWorkoutRepository(SQLiteOpenHelper sqLiteOpenHelper, FinishedExerciseRepository finishedExerciseRepository) {
        return new SQLiteFinishedWorkoutRepository(sqLiteOpenHelper, finishedExerciseRepository);
    }

    @Provides
    @Singleton
    FinishedExerciseRepository provideFinishedExerciseRepository(SQLiteOpenHelper sqLiteOpenHelper) {
        return new SQLiteFinishedExerciseRepository(sqLiteOpenHelper);
    }

    @Provides
    @Singleton
    WorkoutApplicationService provideWorkoutApplicationService(WorkoutSession workoutSession, WorkoutRepository workoutRepository, FinishedWorkoutRepository finishedWorkoutRepository) {
        return new WorkoutApplicationService(workoutSession, workoutRepository, finishedWorkoutRepository);
    }

    @Provides
    @Singleton
    ExerciseViewHelper provideExerciseViewHelper() {
        Locale locale = context.getResources().getConfiguration().locale;
        return new ExerciseViewHelper(locale);
    }

    @Provides
    @Singleton
    WorkoutService provideWorkoutService() {
        JsonInWorkoutAdapter jsonInWorkoutAdapter = new JsonInWorkoutAdapter();
        WorkoutInJsonAdapter workoutInJsonAdapter = new WorkoutInJsonAdapter();
        return new TranslatingWorkoutService(jsonInWorkoutAdapter, workoutInJsonAdapter);
    }

    @Provides
    @Singleton
    EditWorkoutApplicationService provideManageWorkout(WorkoutSession workoutSession, FinishedWorkoutRepository finishedWorkoutRepository, WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, SetRepository setRepository, WorkoutService workoutService) {
        return new EditWorkoutApplicationService(workoutSession, finishedWorkoutRepository, workoutRepository, exerciseRepository, setRepository, workoutService);
    }
}
