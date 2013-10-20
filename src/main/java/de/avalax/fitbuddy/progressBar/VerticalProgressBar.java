package de.avalax.fitbuddy.progressBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Set;

public class VerticalProgressBar extends View {
    private Typeface mTypeface = Typeface.create("sans", Typeface.BOLD);
    private int textColor;
    private int barColor;
    private float textSize;
    private int currentValue;
    private int maxValue;

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
        setCurrentValue(a.getInt(R.styleable.ProgressBar_current_value, 0));
        setMaxValue(a.getInt(R.styleable.ProgressBar_max_value, 0));
        a.recycle();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawProgressBar(canvas);
        DrawTextInProgressBar(canvas);
    }

    private void DrawProgressBar(Canvas canvas) {
        Paint contentPaint = new Paint();
        contentPaint.setAntiAlias(true);
        int height = CalculateProgressBarHeight();
        Rect progressBody = new Rect(0, getHeight() - height, this.getWidth(), getHeight());
        contentPaint.setStyle(Paint.Style.FILL);
        contentPaint.setColor(barColor);
        canvas.drawRect(progressBody, contentPaint);
    }

    private int CalculateProgressBarHeight() {
        float scale = (float) currentValue / (float) maxValue;
        return (int) (scale * this.getHeight());
    }

    private void DrawTextInProgressBar(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(mTypeface);
        String textCurrentValue = currentValue + "";
        String textMaxValue = maxValue + "";
        DrawTextDimensionValues drawTextDimensionValuesCurrentValue = new DrawTextDimensionValues(textPaint, textCurrentValue, getWidth(), getHeight()).invoke();
        DrawTextDimensionValues drawTextDimensionValuesMaxValue = new DrawTextDimensionValues(textPaint, textMaxValue, getWidth(), getHeight()).invoke();
        canvas.drawText(textCurrentValue, drawTextDimensionValuesCurrentValue.getX(), drawTextDimensionValuesCurrentValue.getY(), textPaint);
        canvas.drawText(textMaxValue, drawTextDimensionValuesMaxValue.getX(), drawTextDimensionValuesMaxValue.getY() + textSize, textPaint);

    }

    public synchronized void setTextColor(int textColor) {
        this.textColor = textColor;
        postInvalidate();
    }

    public void setProgressBar(Exercise exercise) {
        setCurrentValue(exercise.getSetNumber());
        setMaxValue(exercise.getMaxSets());
    }

    public void setProgressBar(Set set) {
        setCurrentValue(set.getReps());
        setMaxValue(set.getMaxReps());
    }

    public synchronized void setBarColor(int barColor) {
        this.barColor = barColor;
        postInvalidate();
    }

    public synchronized void setMaxValue(int reps) {
        this.maxValue = reps;
        postInvalidate();
    }

    public synchronized void setCurrentValue(int reps) {
        this.currentValue = reps;
        postInvalidate();
    }

    public synchronized void setTextSize(float textSize) {
        this.textSize = textSize;
        postInvalidate();
    }
}
