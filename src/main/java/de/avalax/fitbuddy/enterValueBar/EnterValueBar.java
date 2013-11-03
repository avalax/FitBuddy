package de.avalax.fitbuddy.enterValueBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import de.avalax.fitbuddy.R;

public class EnterValueBar extends View {
    private Typeface typeface = Typeface.create("sans", Typeface.BOLD);
    private int textColor;
    private float textSize;
    private float labelTextSize;
    private String currentTextValue;

    public EnterValueBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setAttributeSet(attributeSet);
    }

    private void setAttributeSet(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.ProgressBar);
        this.textColor = a.getColor(R.styleable.ProgressBar_textColor, Color.parseColor("#ffffff"));
        this.textSize = a.getDimension(R.styleable.ProgressBar_textSize, 50);
        this.labelTextSize = 14;
        setCurrentValue(a.getInt(R.styleable.ProgressBar_currentValue, 0));
        a.recycle();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTextInProgressBar(canvas);
    }

    private void drawTextInProgressBar(Canvas canvas) {
        String textCurrentValue = currentTextValue;
        String textMaxValue = "label";
        drawCurrentValue(canvas, textCurrentValue);
        drawLabel(canvas, textMaxValue);
    }

    private void drawLabel(Canvas canvas, String label) {
        DrawTextDimensionValues drawTextDimensionValues = new DrawTextDimensionValues(textColor, labelTextSize, typeface, label, getWidth(), getHeight()).invoke();
        canvas.drawText(label, drawTextDimensionValues.getX(), getHeight() - labelTextSize, drawTextDimensionValues.getPaint());
    }

    private void drawCurrentValue(Canvas canvas, String textCurrentValue) {
        DrawTextDimensionValues drawTextDimensionValues = new DrawTextDimensionValues(textColor, textSize, typeface, textCurrentValue, getWidth(), getHeight()).invoke();
        canvas.drawText(textCurrentValue, drawTextDimensionValues.getX(), drawTextDimensionValues.getY() + (textSize / 2), drawTextDimensionValues.getPaint());
    }

    private synchronized void setCurrentValue(int currentValue) {
        this.currentTextValue = String.valueOf(currentValue);
    }
}
