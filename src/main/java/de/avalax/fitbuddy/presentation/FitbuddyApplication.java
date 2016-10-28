package de.avalax.fitbuddy.presentation;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseDialogFragment;
import de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity;
import de.avalax.fitbuddy.presentation.edit.workout.ExerciseAdapter;
import de.avalax.fitbuddy.presentation.edit.workout.ExerciseListFragment;
import de.avalax.fitbuddy.presentation.summary.FinishedWorkoutListFragment;
import de.avalax.fitbuddy.presentation.workout.ExerciseFragment;
import de.avalax.fitbuddy.presentation.workout.WorkoutActivity;

public class FitbuddyApplication extends Application {
    @Singleton
    @Component(modules = FitbuddyModule.class)
    public interface ApplicationComponent {
        void inject(WorkoutActivity workoutActivity);
        void inject(EditWorkoutActivity editWorkoutActivity);
        void inject(ExerciseFragment exerciseFragment);
        void inject(ExerciseListFragment exerciseListFragment);
        void inject(EditExerciseActivity editExerciseActivity);
        void inject(ExerciseAdapter exerciseAdapter);
        void inject(EditExerciseDialogFragment editExerciseDialogFragment);
        void inject(FinishedWorkoutListFragment finishedWorkoutListFragment);
    }

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerFitbuddyApplication_ApplicationComponent.builder()
                .fitbuddyModule(new FitbuddyModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
