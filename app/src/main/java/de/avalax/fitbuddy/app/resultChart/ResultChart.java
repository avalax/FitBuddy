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

    public ResultChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources res = getResources();
        iconNegative = BitmapFactory.decodeResource(res, R.drawable.icon_negative);
        iconNeutral = BitmapFactory.decodeResource(res, R.drawable.icon_neutral);
        iconPositive = BitmapFactory.decodeResource(res, R.drawable.icon_positive);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawTitlebar(canvas);
        drawResultChart(canvas);
        drawTendency(canvas);
    }

    private void drawResultChart(Canvas canvas) {
        Paint contentPaint = new Paint();
        contentPaint.setAntiAlias(true);
        contentPaint.setStyle(Paint.Style.FILL);
        contentPaint.setColor(Color.parseColor("#33B5E5"));
        Rect progressBody = new Rect(0, iconNegative.getHeight(), this.getWidth(), getResultChartHeight());
        canvas.drawRect(progressBody, contentPaint);
        String repsText = "12";
        Paint titleTextPaint = getTextPaint();
        float repsTextWidth = titleTextPaint.measureText(repsText, 0, repsText.length());
        canvas.drawText(repsText, getWidth() / 2 - (repsTextWidth / 2), getResultChartHeight() - titleTextPaint.getTextSize(), titleTextPaint);
    }

    private int getResultChartHeight() {
        return getHeight() - iconNegative.getHeight();
    }

    private void drawTendency(Canvas canvas) {
        Paint contentPaint = new Paint();
        contentPaint.setAntiAlias(true);
        contentPaint.setStyle(Paint.Style.FILL);
        contentPaint.setColor(Color.parseColor("#333333"));
        Rect progressBody = new Rect(0, getHeight() - iconNegative.getHeight(), getWidth(), getHeight());
        canvas.drawRect(progressBody, contentPaint);
        canvas.drawBitmap(iconNegative, 0, getResultChartHeight(), null);
        canvas.drawBitmap(iconNeutral, getWidth() / 2 - (iconNeutral.getWidth() / 2), getResultChartHeight(), null);
        canvas.drawBitmap(iconPositive, getWidth() - iconPositive.getWidth(), getResultChartHeight(), null);
    }

    private void drawTitlebar(Canvas canvas) {
        Paint contentPaint = new Paint();
        contentPaint.setAntiAlias(true);
        contentPaint.setStyle(Paint.Style.FILL);
        contentPaint.setColor(Color.parseColor("#333333"));
        Rect progressBody = new Rect(0, 0, this.getWidth(), iconNegative.getHeight());
        canvas.drawRect(progressBody, contentPaint);

        String weightText = "120 kg";
        String titleText = "Bankdrücken";
        Paint titleTextPaint = getTextPaint();
        float weigthWidth = titleTextPaint.measureText(weightText, 0, weightText.length());
        canvas.drawText(titleText, 0, titleTextPaint.getTextSize(), titleTextPaint);
        canvas.drawText(weightText, getWidth() - weigthWidth, titleTextPaint.getTextSize(), titleTextPaint);
    }

    private Paint getTextPaint() {
        Paint titleTextPaint = new Paint();
        titleTextPaint.setColor(Color.parseColor("#ffffff"));
        titleTextPaint.setTypeface(Typeface.create("sans", Typeface.NORMAL));
        titleTextPaint.setTextSize(14);
        titleTextPaint.setAntiAlias(true);
        return titleTextPaint;
    }
}
