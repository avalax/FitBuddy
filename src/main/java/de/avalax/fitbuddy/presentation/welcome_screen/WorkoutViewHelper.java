package de.avalax.fitbuddy.presentation.welcome_screen;

import android.content.Context;

import java.util.Date;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.workout.Workout;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.getRelativeTimeSpanString;

public class WorkoutViewHelper {

    private final String noExecutionDate;
    private final String executionsPlural;
    private final String executionsSingular;

    public WorkoutViewHelper(Context context) {
        noExecutionDate = context.getResources().getString(R.string.workout_never_executed);
        executionsPlural = context.getResources().getString(R.string.workout_executions);
        executionsSingular = context.getResources().getString(R.string.workout_execution);
    }

    public String executions(Workout workout) {
        int executionCount = workout.getFinishedCount();
        if (executionCount == 1) {
            return String.format(executionsSingular, executionCount);
        }
        return String.format(executionsPlural, executionCount);
    }

    public String lastExecutionDate(Workout workout) {
        Long lastExecution = workout.getLastExecution();
        if (null == lastExecution) {
            return noExecutionDate;
        }
        return getRelativeTimeSpanString(lastExecution, getDate(), DAY_IN_MILLIS)
                .toString();
    }

    protected long getDate() {
        return new Date().getTime();
    }
}
