package de.avalax.fitbuddy.presentation;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import de.avalax.fitbuddy.presentation.edit.exercise.SetListFragment;
import de.avalax.fitbuddy.presentation.edit.set.EditSetActivity;
import de.avalax.fitbuddy.presentation.edit.set.EditSetFragment;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity;
import de.avalax.fitbuddy.presentation.edit.workout.ExerciseListFragment;
import de.avalax.fitbuddy.presentation.summary.FinishedWorkoutDetailFragment;
import de.avalax.fitbuddy.presentation.summary.FinishedWorkoutListFragment;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutListFragment;
import de.avalax.fitbuddy.presentation.workout.ExerciseFragment;

public class FitbuddyApplication extends Application {
    public static final int ADD_WORKOUT = 1;
    public static final int EDIT_WORKOUT = 2;
    public static final int ADD_EXERCISE = 3;
    public static final int EDIT_EXERCISE = 4;
    public static final int ADD_SET = 5;
    public static final int EDIT_SET = 6;
    public static final int FINISHED_WORKOUT_DETAILS = 7;
    public static final int EDIT_REPS = 8;
    public static final int EDIT_WEIGHT = 9;

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = createComponent();
    }

    protected ApplicationComponent createComponent() {
        return DaggerFitbuddyApplication_ApplicationComponent.builder()
                .fitbuddyModule(new FitbuddyModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    @Singleton
    @Component(modules = FitbuddyModule.class)
    public interface ApplicationComponent {

        void inject(EditWorkoutActivity editWorkoutActivity);

        void inject(ExerciseFragment exerciseFragment);

        void inject(FinishedWorkoutListFragment finishedWorkoutListFragment);

        void inject(WorkoutListFragment workoutListFragment);

        void inject(MainActivity mainActivity);

        void inject(ExerciseListFragment exerciseListFragment);

        void inject(SetListFragment setListFragment);

        void inject(EditSetFragment editSetFragment);

        void inject(FinishedWorkoutDetailFragment finishedWorkoutDetailFragment);

        void inject(EditSetActivity editSetActivity);
    }
}
