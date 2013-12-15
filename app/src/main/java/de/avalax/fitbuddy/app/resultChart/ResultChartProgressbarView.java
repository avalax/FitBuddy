package de.avalax.fitbuddy.app.resultChart;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.core.workout.Set;

public class ResultChartProgressbarView extends FrameLayout {
    private TextView valueTextView;
    private ImageView imageView;

    public ResultChartProgressbarView(Context context) {
        super(context);
        init(context);
    }

    public ResultChartProgressbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        attributs(attrs);
    }

    public ResultChartProgressbarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        attributs(attrs);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_vertical_result_chart_bar, this);
        valueTextView = (TextView) findViewById(R.id.valueTextView);
        imageView = (ImageView) findViewById(R.id.verticalProgressBar);
    }

    private void attributs(AttributeSet attrs) {
        //TODO: colors switchable on value <> maxvalue
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        imageView.setImageDrawable(a.getDrawable(R.styleable.ProgressBar_progressbar));
        imageView.setBackgroundColor(a.getColor(R.styleable.ProgressBar_background, R.color.primary_background));
        a.recycle();
    }

    public void updateProgressbar(Set set) {
        float currentValue = set.getReps();
        int maxValue = set.getMaxReps();
        String currentValueText = String.valueOf(currentValue);
        updateProgressbar(currentValue, maxValue, currentValueText);
    }

    private void updateProgressbar(float currentValue, int maxValue, String currentValueText) {
        valueTextView.setText(currentValueText);
        imageView.setImageLevel(calculateProgressbarHeight(currentValue, maxValue));
    }

    private int calculateProgressbarHeight(float currentValue, int maxValue) {
        float scale = currentValue / maxValue;
        return Math.round(scale * 10000);
    }
}
