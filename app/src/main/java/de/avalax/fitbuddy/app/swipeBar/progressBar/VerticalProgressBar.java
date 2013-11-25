package de.avalax.fitbuddy.app.swipeBar.progressBar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.swipeBar.SwipeableBar;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Set;
import de.avalax.fitbuddy.core.workout.Workout;

public class VerticalProgressBar extends FrameLayout implements SwipeableBar {
    private int barColor;
    private float currentValue;
    private float maxValue;
    private TextView maxValueTextView;
    private TextView valueTextView;
    private XmlVerticalProgressBar xmlVerticalProgressBar;

    public VerticalProgressBar(Context context) {
        super(context);
        init(context);
    }

    public VerticalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerticalProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.vertical_bar, this);
        maxValueTextView = (TextView) findViewById(R.id.maxValueTextView);
        valueTextView = (TextView) findViewById(R.id.valueTextView);
        xmlVerticalProgressBar = (XmlVerticalProgressBar) findViewById(R.id.verticalProgressBar);
        xmlVerticalProgressBar.setBackgroundColor(R.styleable.ProgressBar_barColor);
        //TODO: progressbar color

        this.currentValue = 0;
        this.maxValue = 0;
        valueTextView.setText(String.valueOf(currentValue));
        maxValueTextView.setText(String.valueOf(maxValue));
    }

    public void setProgressBar(Workout workout, int exercisePosition) {
        Exercise exercise = workout.getExercise(exercisePosition);
        this.currentValue = workout.getProgress(exercisePosition);
        this.maxValue = workout.getExerciseCount();
        valueTextView.setText(String.valueOf(exercise.getSetNumber()));
        maxValueTextView.setText(String.valueOf(exercise.getMaxSets()));
        xmlVerticalProgressBar.setImageLevel(calculateProgressBarHeight());
        postInvalidate();
    }

    public void setProgressBar(Set set) {
        this.currentValue = set.getReps();
        this.maxValue = set.getMaxReps();
        valueTextView.setText(String.valueOf(set.getReps()));
        maxValueTextView.setText(String.valueOf(set.getMaxReps()));
        xmlVerticalProgressBar.setImageLevel(calculateProgressBarHeight());
        postInvalidate();
    }

    private int calculateProgressBarHeight() {
        float scale = currentValue / maxValue;
        return Math.round(scale * 10000);
    }
}
