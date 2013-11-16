package de.avalax.fitbuddy.app.edit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.app.swipeBar.enterValueBar.EnterValueBar;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EffortExerciseFragment extends RoboFragment {
    private static final String EDITABLE_EXERCISE = "editableExercise";
    @Inject
    Context context;
    @InjectView(R.id.leftEnterValueBar)
    EnterValueBar leftEnterValueBar;
    @InjectView(R.id.rightEnterValueBar)
    EnterValueBar rightEnterValueBar;
    private EditableExercise editableExercise;

    public static EffortExerciseFragment newInstance(EditableExercise editableExercise) {
        EffortExerciseFragment effortExerciseFragment = new EffortExerciseFragment();
        Bundle args = new Bundle();
        args.putSerializable(EDITABLE_EXERCISE, editableExercise);
        effortExerciseFragment.setArguments(args);
        return effortExerciseFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        editableExercise = (EditableExercise) getArguments().getSerializable(EDITABLE_EXERCISE);
        return inflater.inflate(R.layout.edit_effort, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBars();
        leftEnterValueBar.setOnTouchListener(new SwipeBarOnTouchListener(context, leftEnterValueBar, 2) {
            @Override
            protected void onFlingEvent(int moved) {
                changeReps(moved);
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
                changeSets(moved);
            }

            @Override
            protected void onLongPressedLeftEvent() {
            }

            @Override
            protected void onLongPressedRightEvent() {
            }
        });
    }

    private void setBars() {
        leftEnterValueBar.setValue(String.valueOf(editableExercise.getReps()));
        rightEnterValueBar.setValue(String.valueOf(editableExercise.getSets()));
    }

    private void changeReps(int moved) {
        int reps = editableExercise.getReps() + moved;
        if (reps < 1) {
            reps = 1;
        }
        editableExercise.setReps(reps);
        setBars();
    }

    private void changeSets(int moved) {
        int sets = editableExercise.getSets() + moved;
        if (sets < 1) {
            sets = 1;
        }
        editableExercise.setSets(sets);
        setBars();
    }
}
