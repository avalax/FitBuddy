package de.avalax.fitbuddy.edit;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.swipeBar.enterValueBar.EnterValueBar;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class WeightExerciseFragment extends RoboFragment {
    @Inject
    Context context;
    @InjectView(R.id.leftEnterValueBar)
    EnterValueBar leftEnterValueBar;
    @InjectView(R.id.rightEnterValueBar)
    EnterValueBar rightEnterValueBar;
    private EditableExercise editableExercise;

    public WeightExerciseFragment(EditableExercise editableExercise) {
        super();
        this.editableExercise = editableExercise;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.edit_weight, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBars(editableExercise.getWeight(), editableExercise.getWeightRaise());

        leftEnterValueBar.setOnTouchListener(new SwipeBarOnTouchListener(context, leftEnterValueBar, 3) {
            @Override
            protected void onFlingEvent(int moved) {
                changeWeight(moved);
                Log.d("leftEnterValueBar", String.valueOf(editableExercise.getWeight()));
            }

            @Override
            protected void onLongPressedLeftEvent() {
            }

            @Override
            protected void onLongPressedRightEvent() {
            }
        });

        rightEnterValueBar.setOnTouchListener(new SwipeBarOnTouchListener(context, rightEnterValueBar, 3) {
            @Override
            protected void onFlingEvent(int moved) {
                changeWeightRaise(moved);
            }

            @Override
            protected void onLongPressedLeftEvent() {
            }

            @Override
            protected void onLongPressedRightEvent() {
            }
        });
    }

    private void changeWeightRaise(int moved) {
        //TODO: stack weight raise 0.75, 1.25, 2.5, 5, 7.5, 10, 15, 20
        editableExercise.setWeightRaise(editableExercise.getWeightRaise() + (moved * 1.25));
        setBars(editableExercise.getWeight(), editableExercise.getWeightRaise());
    }

    private void changeWeight(int moved) {
        editableExercise.setWeight(editableExercise.getWeight() + (moved * editableExercise.getWeightRaise()));
        setBars(editableExercise.getWeight(), editableExercise.getWeightRaise());
    }

    private void setBars(double weight, double weightRaise) {
        leftEnterValueBar.setValue(String.valueOf(weight));
        rightEnterValueBar.setValue(String.valueOf(weightRaise));
    }
}
