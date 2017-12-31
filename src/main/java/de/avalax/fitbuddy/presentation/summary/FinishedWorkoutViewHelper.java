package de.avalax.fitbuddy.presentation.summary;

import android.content.Context;

import java.util.Date;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutViewHelper;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.getRelativeTimeSpanString;

public class FinishedWorkoutViewHelper {
    private final String creationDatePlaceholder;
    private final String executionsPlural;
    private final WorkoutViewHelper workoutViewHelper;
    private final WorkoutRepository workoutRepository;

    public FinishedWorkoutViewHelper(Context context,
                                     WorkoutViewHelper workoutViewHelper,
                                     WorkoutRepository workoutRepository) {
        creationDatePlaceholder = context.getResources().getString(R.string.placeholder_title);
        executionsPlural = context.getResources().getString(R.string.workout_executions);
        this.workoutViewHelper = workoutViewHelper;
        this.workoutRepository = workoutRepository;
    }

    public String creationDate(FinishedWorkout workout) {
        Long created = workout.getCreated();
        if (null == created) {
            return creationDatePlaceholder;
        }
        return getRelativeTimeSpanString(created, getDate(), DAY_IN_MILLIS)
                .toString();
    }

    public String executions(FinishedWorkout finishedWorkout) {
        WorkoutId workoutId = finishedWorkout.getWorkoutId();
        if (workoutId == null) {
            return String.format(executionsPlural, 0);
        }
        try {
            Workout workout = workoutRepository.load(workoutId);
            return workoutViewHelper.executions(workout);
        } catch (WorkoutException e) {
            return String.format(executionsPlural, 0);
        }
    }

    protected long getDate() {
        return new Date().getTime();
    }
}
