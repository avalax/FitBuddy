package de.avalax.fitbuddy.presentation.workout.swipeBar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import de.avalax.fitbuddy.R;

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
        imageView.setBackgroundColor(a.getColor(R.styleable.ProgressBar_background, ContextCompat.getColor(getContext(), R.color.primary_background)));
        a.recycle();
    }

    public void updateProgressbar(double progress, String currentValue, String maxValue) {
        valueTextView.setText(currentValue);
        maxValueTextView.setText(maxValue);
        imageView.setImageLevel(calculateProgressbarHeight(progress, 1));
        postInvalidate();
    }

    private int calculateProgressbarHeight(double currentValue, int maxValue) {
        double scale = currentValue / maxValue;
        return (int) Math.round(scale * 10000);
    }
}
