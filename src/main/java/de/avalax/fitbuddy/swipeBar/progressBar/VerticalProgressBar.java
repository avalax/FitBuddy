package de.avalax.fitbuddy.swipeBar.progressBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.swipeBar.SwipeableBar;
import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Set;
import de.avalax.fitbuddy.workout.Workout;

public class VerticalProgressBar extends View implements SwipeableBar {
    private Typeface typeface = Typeface.create("sans", Typeface.BOLD);
    private int textColor;
    private int barColor;
    private float textSize;
    private float currentValue;
    private float maxValue;
    private String currentTextValue;
    private String maxTextValue;

    public VerticalProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setAttributeSet(attributeSet);
    }

    private void setAttributeSet(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.ProgressBar);
        setTextColor(a.getColor(R.styleable.ProgressBar_textColor, Color.parseColor("#ffffff")));
        setTextSize(a.getDimension(R.styleable.ProgressBar_textSize, 50));
        setBarColor(a.getColor(R.styleable.ProgressBar_barColor, Color.parseColor("#CC0000")));
        setCurrentValue(a.getInt(R.styleable.ProgressBar_currentValue, 0));
        setMaxValue(a.getInt(R.styleable.ProgressBar_maxValue, 0));
        a.recycle();
    }

    public void setProgressBar(Workout workout, int exercisePosition) {
        Exercise exercise = workout.getExercise(exercisePosition);
        this.currentValue = workout.getProgress(exercisePosition);
        this.currentTextValue = String.valueOf(exercise.getSetNumber());
        this.maxValue = workout.getExerciseCount();
        this.maxTextValue = String.valueOf(exercise.getMaxSets());
        postInvalidate();
    }

    public void setProgressBar(Set set) {
        setCurrentValue(set.getReps());
        setMaxValue(set.getMaxReps());
        postInvalidate();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgressBar(canvas);
        drawTextInProgressBar(canvas);
    }

    private void drawProgressBar(Canvas canvas) {
        Paint contentPaint = new Paint();
        contentPaint.setAntiAlias(true);
        int height = calculateProgressBarHeight();
        Rect progressBody = new Rect(0, getHeight() - height, this.getWidth(), getHeight());
        contentPaint.setStyle(Paint.Style.FILL);
        contentPaint.setColor(barColor);
        canvas.drawRect(progressBody, contentPaint);
    }

    private int calculateProgressBarHeight() {
        float scale = currentValue / maxValue;
        return Math.round(scale * this.getHeight());
    }

    private void drawTextInProgressBar(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(typeface);
        String textCurrentValue = currentTextValue;
        String textMaxValue = maxTextValue;
        DrawTextDimensionValues drawTextDimensionValuesCurrentValue = new DrawTextDimensionValues(textPaint, textCurrentValue, getWidth(), getHeight()).invoke();
        DrawTextDimensionValues drawTextDimensionValuesMaxValue = new DrawTextDimensionValues(textPaint, textMaxValue, getWidth(), getHeight()).invoke();
        canvas.drawText(textCurrentValue, drawTextDimensionValuesCurrentValue.getX(), drawTextDimensionValuesCurrentValue.getY(), textPaint);
        canvas.drawText(textMaxValue, drawTextDimensionValuesMaxValue.getX(), drawTextDimensionValuesMaxValue.getY() + textSize, textPaint);
    }

    private synchronized void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    private synchronized void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    private synchronized void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        this.maxTextValue = String.valueOf(maxValue);
    }

    private synchronized void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
        this.currentTextValue = String.valueOf(currentValue);
    }

    private synchronized void setTextSize(float textSize) {
        this.textSize = textSize;
    }
}
