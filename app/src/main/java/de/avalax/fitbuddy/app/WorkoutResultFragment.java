package de.avalax.fitbuddy.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;
import com.google.inject.Inject;
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
        resultChartViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                resultChartViewFlipper.setInAnimation(getActivity(), R.anim.out_to_top);
                resultChartViewFlipper.setOutAnimation(getActivity(), R.anim.in_from_bottom);
                resultChartViewFlipper.showNext();
                return true;
            }
        });
    }

    private void fillListView() {
        for (int i = 0; i < workout.getExerciseCount(); i++) {
            View inflate = layoutInflater.inflate(R.layout.view_exercise_result, null);
            resultChartViewFlipper.addView(inflate);
            //exercices.add(workout.getExercise(i));
        }
    }
}
