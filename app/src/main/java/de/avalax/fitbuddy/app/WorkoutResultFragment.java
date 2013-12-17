package de.avalax.fitbuddy.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.google.inject.Inject;
import de.avalax.fitbuddy.app.resultChart.ResultChart;
import de.avalax.fitbuddy.app.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Tendency;
import de.avalax.fitbuddy.core.workout.Workout;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class WorkoutResultFragment extends RoboFragment {
    public static final String RESULT_CHART_DISPLAYED_CHILD = "TAB_NUMBER";
    public static final float ALPHA_NOT_SELECTED = 1F;
    public static final float ALPHA_SELECTED = 0.2F;
    @InjectView(R.id.resultChartViewFlipper)
    private ViewFlipper resultChartViewFlipper;
    @Inject
    private Workout workout;
    @Inject
    private LayoutInflater layoutInflater;
    @Inject
    private Context context;
    private UpdateableActivity updateableActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        updateableActivity = (UpdateableActivity) getActivity();
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewFlipper();
        fillViewFlipper();
        if (savedInstanceState != null) {
            restoreViewFlipper(savedInstanceState);
        }
    }

    private void restoreViewFlipper(Bundle savedInstanceState) {
        resultChartViewFlipper.setDisplayedChild(savedInstanceState.getInt(RESULT_CHART_DISPLAYED_CHILD));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(RESULT_CHART_DISPLAYED_CHILD, resultChartViewFlipper.getDisplayedChild());
    }

    private void initViewFlipper() {
        resultChartViewFlipper.setOnTouchListener(new SwipeBarOnTouchListener(context, resultChartViewFlipper, 1) {
            @Override
            protected void onFlingEvent(int moved) {
                if (moved > 0) {
                    resultChartViewFlipper.setInAnimation(getActivity(), R.anim.out_to_top);
                    resultChartViewFlipper.setOutAnimation(getActivity(), R.anim.in_from_bottom);
                    resultChartViewFlipper.showNext();
                }
                if (moved < 0) {
                    resultChartViewFlipper.setInAnimation(getActivity(), R.anim.out_to_bottom);
                    resultChartViewFlipper.setOutAnimation(getActivity(), R.anim.in_from_top);
                    resultChartViewFlipper.showPrevious();
                }
            }

            @Override
            protected void onLongPressedLeftEvent() {
            }

            @Override
            protected void onLongPressedRightEvent() {
            }
        });
    }

    private void fillViewFlipper() {
        for (int i = 0; i < workout.getExerciseCount(); i++) {
            Exercise exercise = workout.getExercise(i);
            String name = workout.getName(i);
            View resultChartView = createResultChartView(exercise, name);
            resultChartViewFlipper.addView(resultChartView);
        }
    }

    private View createResultChartView(Exercise exercise, String name) {
        View resultChartView = layoutInflater.inflate(R.layout.view_exercise_result, null);
        ResultChart resultChart = (ResultChart) resultChartView.findViewById(R.id.resultChart);
        resultChart.setExercise(exercise);
        TextView editText = (TextView) resultChartView.findViewById(R.id.resultChartEditText);
        editText.setText(name);
        setTendency(resultChartView, exercise);
        return resultChartView;
    }

    private void setTendency(View resultChartView, final Exercise exercise) {
        ImageView minusTendency = (ImageView) resultChartView.findViewById(R.id.minusTendencyImageView);
        ImageView neutralTendency = (ImageView) resultChartView.findViewById(R.id.neutralTendencyImageView);
        ImageView plusTendency = (ImageView) resultChartView.findViewById(R.id.plusTendencyImageView);

        minusTendency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise.setTendency(Tendency.MINUS);
                updateableActivity.notifyDataSetChanged();
            }
        });

        neutralTendency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise.setTendency(Tendency.NEUTRAL);
                updateableActivity.notifyDataSetChanged();
            }
        });

        plusTendency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise.setTendency(Tendency.PLUS);
                updateableActivity.notifyDataSetChanged();
            }
        });

        Tendency tendency = exercise.getTendency();
        minusTendency.setAlpha(enabled(Tendency.MINUS.equals(tendency)));
        neutralTendency.setAlpha(enabled(Tendency.NEUTRAL.equals(tendency)));
        plusTendency.setAlpha(enabled(Tendency.PLUS.equals(tendency)));
    }

    private float enabled(boolean selected) {
        return selected ? ALPHA_NOT_SELECTED : ALPHA_SELECTED;
    }
}
