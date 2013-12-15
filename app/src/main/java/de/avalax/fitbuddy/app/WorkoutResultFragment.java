package de.avalax.fitbuddy.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.google.inject.Inject;
import de.avalax.fitbuddy.app.resultChart.ResultChart;
import de.avalax.fitbuddy.app.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.core.workout.Workout;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class WorkoutResultFragment extends RoboFragment {
    @InjectView(R.id.resultChartViewFlipper)
    private ViewFlipper resultChartViewFlipper;
    @Inject
    private Workout workout;
    @Inject
    private LayoutInflater layoutInflater;
    @Inject
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewFlipper();
        fillListView();
    }

    private void initViewFlipper() {
        //TODO: restore view on recreate
        //TODO: update when exercise is changed
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

    private void fillListView() {
        for (int i = 0; i < workout.getExerciseCount(); i++) {
            View inflate = layoutInflater.inflate(R.layout.view_exercise_result, null);
            resultChartViewFlipper.addView(inflate);
            ResultChart resultChart = (ResultChart) inflate.findViewById(R.id.resultChart);
            resultChart.setExercise(workout.getExercise(i));
            TextView editText = (TextView) inflate.findViewById(R.id.resultChartEditText);
            editText.setText(workout.getName(i));
        }
    }
}
