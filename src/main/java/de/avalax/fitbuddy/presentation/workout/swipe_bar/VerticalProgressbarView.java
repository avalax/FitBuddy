package de.avalax.fitbuddy.presentation.workout.swipe_bar;

import android.content.Context;
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
        attributes(attrs);
    }

    public VerticalProgressbarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        attributes(attrs);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_vertical_progressbar, this);
        maxValueTextView = findViewById(R.id.maxValueTextView);
        valueTextView = findViewById(R.id.valueTextView);
        imageView = findViewById(R.id.verticalProgressBar);
    }

    private void attributes(AttributeSet attrs) {
        TypedArray typedArray = getContext()
                .obtainStyledAttributes(attrs, R.styleable.VerticalProgressbarView);
        int color = ContextCompat.getColor(getContext(), R.color.primaryColor);
        imageView.setImageDrawable(typedArray
                .getDrawable(R.styleable.VerticalProgressbarView_progressbarDrawable));
        imageView.setBackgroundColor(typedArray
                .getColor(R.styleable.VerticalProgressbarView_backgroundColor, color));
        typedArray.recycle();
    }

    public void updateProgressbar(double progress, String currentValue, String maxValue) {
        valueTextView.setText(currentValue);
        maxValueTextView.setText(maxValue);
        imageView.setImageLevel(calculateProgressbarHeight(progress));
        postInvalidate();
    }

    private int calculateProgressbarHeight(double progress) {
        return (int) Math.round(progress * 10000);
    }
}
