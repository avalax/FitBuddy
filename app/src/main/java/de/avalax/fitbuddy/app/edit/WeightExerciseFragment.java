package de.avalax.fitbuddy.app.edit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.app.FitbuddyApplication;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.swipeBar.EnterValueBar;
import de.avalax.fitbuddy.app.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.app.swipeBar.WeightRaiseCalculator;

import javax.inject.Inject;

public class WeightExerciseFragment extends Fragment {
    private static final String EDITABLE_EXERCISE = "editableExercise";
    @Inject
    WeightRaiseCalculator weightRaiseCalculator;
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
        View view = inflater.inflate(R.layout.fragment_edit_weight, container, false);
        ButterKnife.inject(this, view);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBars();

        final Activity activity = getActivity();
        leftEnterValueBar.setOnTouchListener(new SwipeBarOnTouchListener(activity, leftEnterValueBar, 2) {
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

        rightEnterValueBar.setOnTouchListener(new SwipeBarOnTouchListener(activity, rightEnterValueBar, 2) {
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
        leftEnterValueBar.updateEnterValueBar(getShortenDouble(editableExercise.getWeight()));
        rightEnterValueBar.updateEnterValueBar(getShortenDouble(editableExercise.getWeightRaise()));
    }

    private String getShortenDouble(double weight) {
        if (weight == (int) weight) {
            return String.valueOf((int) weight);
        } else {
            return String.valueOf(weight);
        }
    }
}
