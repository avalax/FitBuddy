package de.avalax.fitbuddy.app.resultChart;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import de.avalax.fitbuddy.app.R;

public class ResultChart extends View {

    private final Bitmap iconNegative;
    private final Bitmap iconNeutral;
    private final Bitmap iconPositive;
    private String backgroundColor;

    public ResultChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources res = getResources();
        iconNegative = BitmapFactory.decodeResource(res, R.drawable.icon_negative);
        iconNeutral = BitmapFactory.decodeResource(res, R.drawable.icon_neutral);
        iconPositive = BitmapFactory.decodeResource(res, R.drawable.icon_positive);
        backgroundColor = "#333333";
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTitlebar(canvas);
        drawResultChart(canvas);
        drawTendency(canvas);
    }

    private void drawResultChart(Canvas canvas) {
        Rect progressBody = new Rect(0, iconNegative.getHeight(), this.getWidth(), getTitleBarWithResultChartHeight());
        canvas.drawRect(progressBody, getBackgroundPaint("#33B5E5"));
        String repsText = "12";
        Paint titleTextPaint = getTextPaint(50, Typeface.BOLD);
        float repsTextWidth = titleTextPaint.measureText(repsText, 0, repsText.length());
        canvas.drawText(repsText, getWidth() / 2 - (repsTextWidth / 2), getHeight() / 2 + (titleTextPaint.getTextSize() / 2), titleTextPaint);
    }

    private void drawTendency(Canvas canvas) {
        Rect progressBody = new Rect(0, getHeight() - iconNegative.getHeight(), getWidth(), getHeight());
        canvas.drawRect(progressBody, getBackgroundPaint(backgroundColor));
        canvas.drawBitmap(iconNegative, 0, getTitleBarWithResultChartHeight(), null);
        canvas.drawBitmap(iconNeutral, getWidth() / 2 - (iconNeutral.getWidth() / 2), getTitleBarWithResultChartHeight(), null);
        canvas.drawBitmap(iconPositive, getWidth() - iconPositive.getWidth(), getTitleBarWithResultChartHeight(), null);
    }

    private void drawTitlebar(Canvas canvas) {
        Rect progressBody = new Rect(0, 0, this.getWidth(), iconNegative.getHeight());
        canvas.drawRect(progressBody, getBackgroundPaint(backgroundColor));

        String weightText = "120 kg";
        String titleText = "Bankdrücken";
        Paint titleTextPaint = getTextPaint(14, Typeface.NORMAL);
        float weigthWidth = titleTextPaint.measureText(weightText, 0, weightText.length());
        canvas.drawText(titleText, 0, titleTextPaint.getTextSize(), titleTextPaint);
        canvas.drawText(weightText, getWidth() - weigthWidth, titleTextPaint.getTextSize(), titleTextPaint);
    }

    private Paint getTextPaint(float textSize, int typeface) {
        Paint titleTextPaint = new Paint();
        titleTextPaint.setColor(Color.parseColor("#ffffff"));
        titleTextPaint.setTypeface(Typeface.create("sans", typeface));
        titleTextPaint.setTextSize(textSize);
        titleTextPaint.setAntiAlias(true);
        return titleTextPaint;
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
}
