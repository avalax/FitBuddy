package de.avalax.fitbuddy.edit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.swipeBar.WeightRaiseCalculator;
import de.avalax.fitbuddy.swipeBar.enterValueBar.EnterValueBar;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class WeightExerciseFragment extends RoboFragment {
    @Inject
    WeightRaiseCalculator weightRaiseCalculator;
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
        double weightRaise = weightRaiseCalculator.calculate(editableExercise.getWeightRaise(),moved);
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
              return String.valueOf((int)weight);
        } else {
           return String.valueOf(weight);
       }
    }
}
