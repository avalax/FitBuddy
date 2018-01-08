package de.avalax.fitbuddy.presentation.summary;

import android.content.Context;

import java.util.Date;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.getRelativeTimeSpanString;

public class FinishedWorkoutViewHelper {
    private final String noExecutionDate;
    private final String executionsPlural;
    private final String executionsSingular;
    private final FinishedWorkoutRepository finishedWorkoutRepository;

    public FinishedWorkoutViewHelper(Context context,
                                     FinishedWorkoutRepository finishedWorkoutRepository) {
        noExecutionDate = context.getResources().getString(R.string.workout_never_executed);
        executionsPlural = context.getResources().getString(R.string.workout_executions);
        executionsSingular = context.getResources().getString(R.string.workout_execution);
        this.finishedWorkoutRepository = finishedWorkoutRepository;
    }

    public String creationDate(FinishedWorkout finishedWorkout) {
        Long created = finishedWorkout.getCreated();
        return getRelativeTimeSpanString(created, getDate(), DAY_IN_MILLIS)
                .toString();
    }

    public String lastExecutionDate(WorkoutId workoutId) {
        Long lastExecution = finishedWorkoutRepository.lastCreation(workoutId);
        if (null == lastExecution) {
            return noExecutionDate;
        }
        return getRelativeTimeSpanString(lastExecution, getDate(), DAY_IN_MILLIS)
                .toString();
    }

    public String executions(WorkoutId workoutId) {
        long executionCount = finishedWorkoutRepository.count(workoutId);
        if (executionCount == 1) {
            return String.format(executionsSingular, executionCount);
        }
        return String.format(executionsPlural, executionCount);
    }

    protected long getDate() {
        return new Date().getTime();
    }
}
