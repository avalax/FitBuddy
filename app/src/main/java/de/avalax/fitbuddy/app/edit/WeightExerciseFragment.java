package de.avalax.fitbuddy.app.edit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.app.swipeBar.WeightRaiseCalculator;
import de.avalax.fitbuddy.app.swipeBar.enterValueBar.EnterValueBar;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class WeightExerciseFragment extends RoboFragment {
    private static final String EDITABLE_EXERCISE = "editableExercise";
    @Inject
    WeightRaiseCalculator weightRaiseCalculator;
    @Inject
    Context context;
    @InjectView(R.id.leftEnterValueBar)
    EnterValueBar leftEnterValueBar;
    @InjectView(R.id.rightEnterValueBar)
    EnterValueBar rightEnterValueBar;
    private EditableExercise editableExercise;

    public static WeightExerciseFragment newInstance(EditableExercise editableExercise) {
        WeightExerciseFragment weightExerciseFragment = new WeightExerciseFragment();
        Bundle args = new Bundle();
        args.putSerializable(EDITABLE_EXERCISE, editableExercise);
        weightExerciseFragment.setArguments(args);
        return weightExerciseFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        editableExercise = (EditableExercise) getArguments().getSerializable(EDITABLE_EXERCISE);
        return inflater.inflate(R.layout.edit_weight, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBars();

        leftEnterValueBar.setOnTouchListener(new SwipeBarOnTouchListener(context, leftEnterValueBar, 2) {
            @Override
            protected void onFlingEvent(int moved) {
                changeWeight(moved);
            }

            @Override
            protected void onLongPressedLeftEvent() {
            }

            @Override
            protected void onLongPressedRightEvent() {
            }
        });

        rightEnterValueBar.setOnTouchListener(new SwipeBarOnTouchListener(context, rightEnterValueBar, 2) {
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
        double weightRaise = weightRaiseCalculator.calculate(editableExercise.getWeightRaise(), moved);
        editableExercise.setWeightRaise(weightRaise);
        setBars();
    }

    private void changeWeight(int moved) {
        double weight = editableExercise.getWeight() + (moved * editableExercise.getWeightRaise());
        if (weight < 0) {
            weight = 0;
        }
        editableExercise.setWeight(weight);
        setBars();
    }

    private void setBars() {
        leftEnterValueBar.setValue(getShortenDouble(editableExercise.getWeight()));
        rightEnterValueBar.setValue(getShortenDouble(editableExercise.getWeightRaise()));
    }

    private String getShortenDouble(double weight) {
        if (weight == (int) weight) {
            return String.valueOf((int) weight);
        } else {
            return String.valueOf(weight);
        }
    }
}
