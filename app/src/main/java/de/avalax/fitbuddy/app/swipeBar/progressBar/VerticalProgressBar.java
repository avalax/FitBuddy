package de.avalax.fitbuddy.app.swipeBar.progressBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.swipeBar.SwipeableBar;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Set;
import de.avalax.fitbuddy.core.workout.Workout;

public class VerticalProgressBar extends FrameLayout implements SwipeableBar {
    private TextView maxValueTextView;
    private TextView valueTextView;
    private ImageView imageView;

    public VerticalProgressBar(Context context) {
        super(context);
        init(context);
    }

    public VerticalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        attributs(attrs);
    }

    public VerticalProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        attributs(attrs);
    }

    private void attributs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        imageView.setImageDrawable(a.getDrawable(R.styleable.ProgressBar_progressbar));
        imageView.setBackgroundColor(a.getColor(R.styleable.ProgressBar_background, R.color.primary_background));
        a.recycle();
    }

    private void init(Context context) {
        inflate(context, R.layout.vertical_bar, this);
        maxValueTextView = (TextView) findViewById(R.id.maxValueTextView);
        valueTextView = (TextView) findViewById(R.id.valueTextView);
        imageView = (ImageView) findViewById(R.id.verticalProgressBar);
    }

    public void setProgressBar(Workout workout, int exercisePosition) {
        Exercise exercise = workout.getExercise(exercisePosition);
        float currentValue = workout.getProgress(exercisePosition);
        int maxValue = workout.getExerciseCount();
        String currentValueText = String.valueOf(exercise.getSetNumber());
        String maxValueText = String.valueOf(exercise.getMaxSets());
        updateProgressBar(currentValue, maxValue, currentValueText, maxValueText);
    }

    public void setProgressBar(Set set) {
        int currentValue = set.getReps();
        int maxValue = set.getMaxReps();
        updateProgressBar(currentValue, maxValue, String.valueOf(currentValue), String.valueOf(maxValue));
    }

    private void updateProgressBar(float currentValue, int maxValue, String currentValueText, String maxValueText) {
        valueTextView.setText(currentValueText);
        maxValueTextView.setText(maxValueText);
        imageView.setImageLevel(calculateProgressBarHeight(currentValue, maxValue));
        postInvalidate();
    }

    private int calculateProgressBarHeight(float currentValue, int maxValue) {
        float scale = currentValue / maxValue;
        return Math.round(scale * 10000);
    }
}
