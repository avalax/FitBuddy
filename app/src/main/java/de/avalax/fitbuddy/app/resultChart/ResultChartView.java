package de.avalax.fitbuddy.app.resultChart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.inject.Inject;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.core.workout.Exercise;

public class ResultChartView extends LinearLayout {

    @Inject
    private LayoutInflater layoutInflater;
    private Context context;

    public ResultChartView(Context context) {
        super(context);
        init(context);
    }

    public ResultChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ResultChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    public void setExercise(Exercise exercise) {
        removeAllViews();
        //TODO: do not setCurrentSet
        LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        p.weight = 1;
        int setNumber = exercise.getSetNumber();
        for (int i = 0; i < exercise.getMaxSets(); i++) {
            exercise.setCurrentSet(i + 1);
            ProgressbarView child = new ProgressbarView(context);
            child.setLayoutParams(p);
            //TODO: move to ProgressbarView
            //TODO: move stylings to xml
            ImageView imageView = (ImageView) child.findViewById(R.id.verticalProgressBar);
            imageView.setBackgroundColor(getResources().getColor(R.color.primary_background));
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.primary_progress_bar));
            child.updateProgressbar(exercise.getCurrentSet());
            addView(child);
        }
        exercise.setCurrentSet(setNumber);
    }
}