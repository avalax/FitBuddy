package de.avalax.fitbuddy.presentation.workout.swipeBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import de.avalax.fitbuddy.presentation.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;

public class VerticalProgressbarView extends FrameLayout {
    private TextView maxValueTextView;
    private TextView valueTextView;
    private ImageView imageView;

    public VerticalProgressbarView(Context context) {
        super(context);
        init(context);
    }

    public VerticalProgressbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        attributs(attrs);
    }

    public VerticalProgressbarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        attributs(attrs);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_vertical_progressbar, this);
        maxValueTextView = (TextView) findViewById(R.id.maxValueTextView);
        valueTextView = (TextView) findViewById(R.id.valueTextView);
        imageView = (ImageView) findViewById(R.id.verticalProgressBar);
    }

    private void attributs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        imageView.setImageDrawable(a.getDrawable(R.styleable.ProgressBar_progressbar));
        imageView.setBackgroundColor(a.getColor(R.styleable.ProgressBar_background, R.color.primary_background));
        a.recycle();
    }

    public void updateProgressbar(Exercise exercise) {
        String currentValueText = exercise.countOfSets() > 0 ? String.valueOf(exercise.indexOfCurrentSet() + 1) : "0";
        String maxValueText = String.valueOf(exercise.countOfSets());
        updateProgressbar(exercise.getProgress(), currentValueText, maxValueText);
    }

    public void updateProgressbar(Set set) {
        double currentValue = set.getReps();
        int maxValue = set.getMaxReps();
        updateProgressbar(currentValue / maxValue, String.valueOf(set.getReps()), String.valueOf(maxValue));
    }

    private void updateProgressbar(double progress, String currentValueText, String maxValueText) {
        valueTextView.setText(currentValueText);
        maxValueTextView.setText(maxValueText);
        imageView.setImageLevel(calculateProgressbarHeight(progress, 1));
        postInvalidate();
    }

    private int calculateProgressbarHeight(double currentValue, int maxValue) {
        double scale = currentValue / maxValue;
        return (int) Math.round(scale * 10000);
    }
}
