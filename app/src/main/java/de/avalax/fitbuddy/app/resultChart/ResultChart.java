package de.avalax.fitbuddy.app.resultChart;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Tendency;

public class ResultChart extends View {

    private final Bitmap iconNegative;
    private final Bitmap iconNeutral;
    private final Bitmap iconPositive;
    private String backgroundColor;
    private String barBackgroundColor;
    private Tendency tendency;
    private String barTextColor;
    private Exercise exercise;

    public ResultChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources res = getResources();
        //TODO: roboguice
        iconNegative = BitmapFactory.decodeResource(res, R.drawable.ic_action_tendency_negative);
        iconNeutral = BitmapFactory.decodeResource(res, R.drawable.ic_action_tendency_neutral);
        iconPositive = BitmapFactory.decodeResource(res, R.drawable.ic_action_tendency_positive);
        //TODO: stylable
        backgroundColor = getResources().getString(R.color.pager_background);
        barBackgroundColor = getResources().getString(R.color.primary_background);
        barTextColor = getResources().getString(R.color.main_text);
        tendency = Tendency.NEUTRAL;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawResultChart(canvas);
        drawTendency(canvas);
        drawTitle(canvas);
    }

    private void drawTitle(Canvas canvas) {
        int yOffset = iconNegative.getHeight();
        Paint paint = getTextPaint(14, Typeface.BOLD);
        int barHeight = getHeight() - yOffset;
        String title = exercise != null ? exercise.getName() : "title";
        canvas.drawText(title, getWidth() / 2, barHeight - (paint.getTextSize() * 2), paint);
    }

    private void drawResultChart(Canvas canvas) {
        int yOffset = iconNegative.getHeight();
        int barHeight = getHeight() - yOffset;
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

    private void drawTendency(Canvas canvas) {
        Rect progressBody = new Rect(0, getHeight() - iconNegative.getHeight(), getWidth(), getHeight());
        canvas.drawRect(progressBody, getBackgroundPaint(backgroundColor));
        if (tendency == Tendency.MINUS) {
            drawBitmap(canvas, iconNegative, (float) 0, (float) getTitleBarWithResultChartHeight());
        } else {
            drawBitmapDeactivated(canvas, iconNegative, (float) 0, (float) getTitleBarWithResultChartHeight());
        }
        if (tendency == Tendency.NEUTRAL) {
            drawBitmap(canvas, iconNeutral, getWidth() / 2 - (iconNeutral.getWidth() / 2), getTitleBarWithResultChartHeight());
        } else {
            drawBitmapDeactivated(canvas, iconNeutral, getWidth() / 2 - (iconNeutral.getWidth() / 2), getTitleBarWithResultChartHeight());
        }
        if (tendency == Tendency.PLUS) {
            drawBitmap(canvas, iconPositive, getWidth() - iconPositive.getWidth(), getTitleBarWithResultChartHeight());
        } else {
            drawBitmapDeactivated(canvas, iconPositive, getWidth() - iconPositive.getWidth(), getTitleBarWithResultChartHeight());
        }
    }

    private void drawBitmap(Canvas canvas, Bitmap bitmap, float left, float top) {
        canvas.drawBitmap(bitmap, left, top, null);
    }

    private void drawBitmapDeactivated(Canvas canvas, Bitmap bitmap, float left, float top) {
        Paint paint = new Paint();
        paint.setAlpha(70);
        canvas.drawBitmap(bitmap, left, top, paint);
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

    private int getTitleBarWithResultChartHeight() {
        return getHeight() - iconNegative.getHeight();
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}
