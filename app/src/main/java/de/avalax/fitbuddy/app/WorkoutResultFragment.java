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
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class WorkoutResultFragment extends RoboFragment {
    private static final String RESULT_CHART_DISPLAYED_CHILD = "TAB_NUMBER";
    private static final float ALPHA_NOT_SELECTED = 1F;
    private static final float ALPHA_SELECTED = 0.2F;
    @InjectResource(R.string.exercise_title)
    private String exerciseTitle;
    @InjectView(R.id.resultChartViewFlipper)
    private ViewFlipper resultChartViewFlipper;
    @Inject
    private Workout workout;
    @Inject
    private LayoutInflater layoutInflater;
    @Inject
    private Context context;
    private View.OnClickListener tendencyOnClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initOnClickListener();
        initViewFlipper();
        setOnTouchListener();

        if (savedInstanceState != null) {
            restoreViewFlipper(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(RESULT_CHART_DISPLAYED_CHILD, resultChartViewFlipper.getDisplayedChild());
    }

    private void restoreViewFlipper(Bundle savedInstanceState) {
        resultChartViewFlipper.setDisplayedChild(savedInstanceState.getInt(RESULT_CHART_DISPLAYED_CHILD));
    }

    private void initOnClickListener() {
        tendencyOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tendency tendency = (Tendency) v.getTag(R.id.tendency);
                Exercise exercise = (Exercise) v.getTag(R.id.exercise);
                exercise.setTendency(tendency);
                updateCurrentResultChartView(exercise);
            }
        };
    }

    private void setOnTouchListener() {
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

    private void initViewFlipper() {
        for (int i = 0; i < workout.getExerciseCount(); i++) {
            resultChartViewFlipper.addView(createResultChartView(i));
        }
    }

    private View createResultChartView(int exercisePosition) {
        Exercise exercise = workout.getExercise(exercisePosition);
        View resultChartView = layoutInflater.inflate(R.layout.view_exercise_result, null);

        setExerciseToResultChart(resultChartView, exercise);
        registerOnClickListener(resultChartView, exercise);
        updateResultChartView(resultChartView, exercise);
        return resultChartView;
    }

    private void setExerciseToResultChart(View resultChartView, Exercise exercise) {
        ResultChart resultChart = (ResultChart) resultChartView.findViewById(R.id.resultChart);
        resultChart.setExercise(exercise);
    }

    private void registerOnClickListener(View resultChartView, Exercise exercise) {
        ImageView minusTendency = (ImageView) resultChartView.findViewById(R.id.minusTendencyImageView);
        ImageView neutralTendency = (ImageView) resultChartView.findViewById(R.id.neutralTendencyImageView);
        ImageView plusTendency = (ImageView) resultChartView.findViewById(R.id.plusTendencyImageView);

        setOnClickListener(minusTendency, exercise, Tendency.MINUS);
        setOnClickListener(neutralTendency, exercise, Tendency.NEUTRAL);
        setOnClickListener(plusTendency, exercise, Tendency.PLUS);
    }

    private void setOnClickListener(ImageView imageView, Exercise exercise, Tendency tendency) {
        imageView.setTag(R.id.tendency, tendency);
        imageView.setTag(R.id.exercise, exercise);
        imageView.setOnClickListener(tendencyOnClickListener);
    }

    private void updateCurrentResultChartView(Exercise exercise) {
        View resultChartView = resultChartViewFlipper.getCurrentView();
        updateResultChartView(resultChartView, exercise);
    }

    private void updateResultChartView(View resultChartView, Exercise exercise) {
        updateExerciseText(resultChartView, exercise);
        updateTendencyImageViews(resultChartView, exercise);
    }

    private void updateExerciseText(View resultChartView, Exercise exercise) {
        TextView editText = (TextView) resultChartView.findViewById(R.id.resultChartEditText);
        Tendency tendency = exercise.getTendency();
        String exerciseName = exercise.getName();
        //TODO: textformating
        String text = String.format(exerciseTitle, exerciseName, exercise.getWeightRaise(tendency));

        editText.setText(text);
    }

    private void updateTendencyImageViews(View resultChartView, Exercise exercise) {
        Tendency tendency = exercise.getTendency();
        ImageView minusTendency = (ImageView) resultChartView.findViewById(R.id.minusTendencyImageView);
        ImageView neutralTendency = (ImageView) resultChartView.findViewById(R.id.neutralTendencyImageView);
        ImageView plusTendency = (ImageView) resultChartView.findViewById(R.id.plusTendencyImageView);

        minusTendency.setAlpha(enabled(Tendency.MINUS.equals(tendency)));
        neutralTendency.setAlpha(enabled(Tendency.NEUTRAL.equals(tendency)));
        plusTendency.setAlpha(enabled(Tendency.PLUS.equals(tendency)));
    }

    private float enabled(boolean selected) {
        return selected ? ALPHA_NOT_SELECTED : ALPHA_SELECTED;
    }
}