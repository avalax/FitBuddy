package de.avalax.fitbuddy.app.resultChart;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.app.FitbuddyApplication;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.WorkoutSession;
import de.avalax.fitbuddy.app.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Tendency;
import de.avalax.fitbuddy.core.workout.Workout;

import javax.inject.Inject;
import java.text.DecimalFormat;

public class ResultChartFragment extends Fragment {
    private static final String RESULT_CHART_DISPLAYED_CHILD = "TAB_NUMBER";
    private static final float ALPHA_NOT_SELECTED = 0.8F;
    private static final float ALPHA_SELECTED = 0.3F;
    @Inject
    protected WorkoutSession workoutSession;
    @InjectView(R.id.resultChartViewFlipper)
    protected ViewFlipper resultChartViewFlipper;
    private String exerciseTitle;
    private LayoutInflater inflater;
    private View.OnClickListener tendencyOnClickListener;
    private DecimalFormat decimalFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        this.inflater = inflater;
        this.exerciseTitle = getResources().getString(R.string.title_exercise_with_weight);
        this.decimalFormat = new DecimalFormat("###.#");
        ButterKnife.inject(this, view);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        return view;
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
        Context context = getActivity();
        resultChartViewFlipper.setOnTouchListener(new SwipeBarOnTouchListener(context, resultChartViewFlipper, 1) {
            @Override
            protected void onFlingEvent(int moved) {
                if (moved > 0 && resultChartViewFlipper.getDisplayedChild() +1 < workoutSession.getWorkout().getExerciseCount()) {
                    resultChartViewFlipper.setInAnimation(getActivity(), R.anim.out_to_top);
                    resultChartViewFlipper.setOutAnimation(getActivity(), R.anim.in_from_bottom);
                    resultChartViewFlipper.showNext();
                }
                if (moved < 0 && resultChartViewFlipper.getDisplayedChild() > 0) {
                    resultChartViewFlipper.setInAnimation(getActivity(), R.anim.out_to_bottom);
                    resultChartViewFlipper.setOutAnimation(getActivity(), R.anim.in_from_top);
                    resultChartViewFlipper.showPrevious();
                }
            }
        });
    }

    private void initViewFlipper() {
        Workout workout = workoutSession.getWorkout();
        for (int i = 0; i < workout.getExerciseCount(); i++) {
            resultChartViewFlipper.addView(createResultChartView(i));
        }
    }

    private View createResultChartView(int exercisePosition) {
        Workout workout = workoutSession.getWorkout();
        Exercise exercise = workout.getExercise(exercisePosition);
        View resultChartView = inflater.inflate(R.layout.view_exercise_result, null);

        setExerciseToResultChart(resultChartView, exercise);
        registerOnClickListener(resultChartView, exercise);
        updateResultChartView(resultChartView, exercise);
        return resultChartView;
    }

    private void setExerciseToResultChart(View resultChartView, Exercise exercise) {
        ResultChartView resultChart = (ResultChartView) resultChartView.findViewById(R.id.resultChart);
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
        //TODO: textformating

        double weightRaise = exercise.getWeightRaise(tendency);

        editText.setText(getWeightText(exercise, weightRaise));
    }

    private CharSequence getWeightText(Exercise exercise, double weight) {
        if (weight > 0) {
            return String.format(exerciseTitle, exercise.getName(), decimalFormat.format(weight));
        } else {
            return exercise.getName();
        }
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