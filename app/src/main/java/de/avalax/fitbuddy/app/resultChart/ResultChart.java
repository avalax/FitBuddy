package de.avalax.fitbuddy.app.resultChart;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.core.workout.Exercise;

public class ResultChart extends View {

    private String barBackgroundColor;
    private String barTextColor;
    private Exercise exercise;

    public ResultChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        //TODO: stylable
        barBackgroundColor = getResources().getString(R.color.primary_background);
        barTextColor = getResources().getString(R.color.main_text);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawResultChart(canvas);
        drawTitle(canvas);
    }

    private void drawTitle(Canvas canvas) {
        Paint paint = getTextPaint(14, Typeface.BOLD);
        int barHeight = getHeight();
        String title = exercise != null ? exercise.getName() : "title";
        canvas.drawText(title, getWidth() / 2, barHeight - (paint.getTextSize() * 2), paint);
    }

    private void drawResultChart(Canvas canvas) {
        int barHeight = getHeight();
        if (exercise != null) {
            for (int i = 0; i < exercise.getMaxSets(); i++) {
                exercise.setCurrentSet(i + 1);
                String repsText = String.valueOf(exercise.getCurrentSet().getReps());
                int barWidth = Math.round(getWidth() / exercise.getMaxSets());
                int xOffset = barWidth * i;
                drawBar(canvas, barWidth, barHeight, repsText, barBackgroundColor, xOffset);
            }
        }
    }

    private void drawBar(Canvas canvas, int barWidth, int barHeight, String repsText, String barBackgroundColor, int xOffset) {
        Rect progressBody = new Rect(xOffset, 0, barWidth + xOffset, barHeight);
        canvas.drawRect(progressBody, getBackgroundPaint(barBackgroundColor));
        //TODO: dimens
        Paint paint = getTextPaint(50, Typeface.BOLD);
        canvas.drawText(repsText, xOffset + (barWidth / 2), (barHeight / 2) + (paint.getTextSize() / 2), paint);
    }

    private Paint getTextPaint(float textSize, int typeface) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor(barTextColor));
        //TODO: styles.xml
        paint.setTypeface(Typeface.create("sans", typeface));
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        return paint;
    }

    private Paint getBackgroundPaint(String colorString) {
        Paint contentPaint = new Paint();
        contentPaint.setAntiAlias(true);
        contentPaint.setStyle(Paint.Style.FILL);
        contentPaint.setColor(Color.parseColor(colorString));
        return contentPaint;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}