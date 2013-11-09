package de.avalax.fitbuddy.app.swipeBar.enterValueBar;

import android.graphics.Paint;
import android.graphics.Typeface;

public class DrawTextDimensionValues {
    private Paint paint;

    private String text;
    private int width;
    private int height;
    private float x;
    private float y;

    public DrawTextDimensionValues(int textColor, float textSize, Typeface typeface, String text, int width, int height) {
        this.paint = createTextPaint(textColor, textSize, typeface);
        this.text = text;
        this.width = width;
        this.height = height;
    }

    public long getX() {
        return Math.round(x);
    }

    public long getY() {
        return Math.round(y);
    }

    public Paint getPaint() {
        return paint;
    }

    public DrawTextDimensionValues invoke() {
        float fontWidth = paint.measureText(text, 0, text.length());
        x = (width / 2) - (fontWidth / 2);
        y = height / 2;
        return this;
    }

    private Paint createTextPaint(int textColor, float textSize, Typeface typeface) {
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(typeface);
        return textPaint;
    }
}
