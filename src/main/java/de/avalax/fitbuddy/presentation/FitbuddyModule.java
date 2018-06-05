package de.avalax.fitbuddy.presentation;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.ad_mod.AdMobProvider;
import de.avalax.fitbuddy.application.billing.BillingProvider;
import de.avalax.fitbuddy.application.billing.NotificationProvider;
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
import de.avalax.fitbuddy.port.adapter.service.ad_mob.GmsAdMobProvider;
import de.avalax.fitbuddy.port.adapter.service.billing.GooglePlayBillingProvider;
import de.avalax.fitbuddy.port.adapter.service.billing.HttpNotificationProvider;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;
import de.avalax.fitbuddy.presentation.summary.FinishedExerciseViewHelper;
import de.avalax.fitbuddy.presentation.summary.FinishedWorkoutViewHelper;

@Module
public class FitbuddyModule {
    private final WorkoutSession workoutSession;
    private final Context context;
    private final WorkoutRepository workoutRepository;
    private final FinishedWorkoutRepository finishedWorkoutRepository;
    protected final NotificationProvider notificationProvider;

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
        notificationProvider = new HttpNotificationProvider("fitbuddy");
    }

    @Provides
    @Singleton
    public WorkoutService provideWorkoutService() {
        return new WorkoutService(
                workoutSession,
                finishedWorkoutRepository);
    }

    @Provides
    @Singleton
    public FinishedWorkoutService provideFinishedWorkoutService() {
        return new FinishedWorkoutService(finishedWorkoutRepository);
    }

    @Provides
    @Singleton
    public ExerciseViewHelper provideExerciseViewHelper() {
        Locale locale = context.getResources().getConfiguration().locale;
        return new ExerciseViewHelper(context, locale);
    }

    @Provides
    @Singleton
    public EditWorkoutService provideManageWorkout() {
        return new EditWorkoutService(
                workoutRepository);
    }

    @Provides
    @Singleton
    public FinishedWorkoutViewHelper provideFinishedWorkoutViewHelper() {
        return new FinishedWorkoutViewHelper(context, finishedWorkoutRepository);
    }

    @Provides
    @Singleton
    public FinishedExerciseViewHelper provideFinishedExerciseViewHelper() {
        return new FinishedExerciseViewHelper();
    }

    @Provides
    @Singleton
    public BillingProvider provideBillingProvider() {
        return new GooglePlayBillingProvider(context, notificationProvider);
    }

    @Provides
    @Singleton
    public AdMobProvider provideAdMobProvider(BillingProvider billingProvider) {
        return new GmsAdMobProvider(billingProvider);
    }


}
