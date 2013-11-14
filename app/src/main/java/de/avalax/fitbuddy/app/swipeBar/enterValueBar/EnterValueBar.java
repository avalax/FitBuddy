package de.avalax.fitbuddy.app.swipeBar.enterValueBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.swipeBar.SwipeableBar;

public class EnterValueBar extends View implements SwipeableBar {
    private Typeface typeface = Typeface.create("sans", Typeface.BOLD);
    private int textColor;
    private float valueTextSize;
    private float labelTextSize;
    private String value;
    private String label;

    public EnterValueBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setAttributeSet(attributeSet);
    }

    private void setAttributeSet(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.EnterValueBar);
        this.textColor = a.getColor(R.styleable.EnterValueBar_textColor, Color.parseColor("#ffffff"));
        this.valueTextSize = a.getDimension(R.styleable.EnterValueBar_valueTextSize, 50);
        this.label = a.getString(R.styleable.EnterValueBar_label);
        this.labelTextSize = a.getDimension(R.styleable.EnterValueBar_labelTextSize, 14);
        setValue(a.getString(R.styleable.EnterValueBar_value));
        a.recycle();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTextInProgressBar(canvas);
    }

    private void drawTextInProgressBar(Canvas canvas) {
        drawCurrentValue(canvas, value);
        drawLabel(canvas, label);
    }

    private void drawLabel(Canvas canvas, String label) {
        DrawTextDimensionValues drawTextDimensionValues = new DrawTextDimensionValues(textColor, labelTextSize, typeface, label, getWidth(), getHeight()).invoke();
        canvas.drawText(label, drawTextDimensionValues.getX(), getHeight() - labelTextSize, drawTextDimensionValues.getPaint());
    }

    private void drawCurrentValue(Canvas canvas, String textCurrentValue) {
        DrawTextDimensionValues drawTextDimensionValues = new DrawTextDimensionValues(textColor, valueTextSize, typeface, textCurrentValue, getWidth(), getHeight()).invoke();
        canvas.drawText(textCurrentValue, drawTextDimensionValues.getX(), drawTextDimensionValues.getY(), drawTextDimensionValues.getPaint());
    }

    public synchronized void setValue(String value) {
        this.value = value;
        postInvalidate();
    }
}
