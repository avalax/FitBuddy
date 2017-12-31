package de.avalax.fitbuddy.presentation.summary;

import android.content.Context;

import java.util.Date;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.getRelativeTimeSpanString;

public class FinishedWorkoutViewHelper {
    private final String noExecutionDate;

    public FinishedWorkoutViewHelper(Context context) {
        noExecutionDate = context.getResources().getString(R.string.workout_never_executed);
    }

    public String executionDate(FinishedWorkout workout) {
        Long lastExecution = workout.getCreated();
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
