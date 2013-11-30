package de.avalax.fitbuddy.app.swipeBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import de.avalax.fitbuddy.app.R;

public class EnterValueBar extends FrameLayout implements SwipeableBar {
    private TextView valueTextView;
    private TextView labelTextView;

    public EnterValueBar(Context context) {
        super(context);
        init(context);
    }

    public EnterValueBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        attributs(attrs);
    }

    public EnterValueBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        attributs(attrs);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_edit, this);
        valueTextView = (TextView) findViewById(R.id.valueTextView);
        labelTextView = (TextView) findViewById(R.id.labelTextView);
    }

    private void attributs(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.EnterValueBar);
        labelTextView.setText(a.getString(R.styleable.EnterValueBar_label));
        valueTextView.setText(a.getString(R.styleable.EnterValueBar_value));
        a.recycle();
    }

    public synchronized void updateEnterValueBar(String value) {
        valueTextView.setText(value);
        postInvalidate();
    }
}
