package de.avalax.fitbuddy.app.editExercise;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.swipeBar.EnterValueBar;
import de.avalax.fitbuddy.app.swipeBar.SwipeBarOnTouchListener;

public class EffortExerciseFragment extends Fragment {
    private static final String EDITABLE_EXERCISE = "editableExercise";
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
        View view = inflater.inflate(R.layout.fragment_edit_effort, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBars();
        Context context = getActivity();
        leftEnterValueBar.setOnTouchListener(new SwipeBarOnTouchListener(context, leftEnterValueBar, 2) {
            @Override
            protected void onFlingEvent(int moved) {
                changeReps(moved);
            }
        });

        rightEnterValueBar.setOnTouchListener(new SwipeBarOnTouchListener(context, rightEnterValueBar, 2) {
            @Override
            protected void onFlingEvent(int moved) {
                changeSets(moved);
            }
        });
    }

    private void setBars() {
        leftEnterValueBar.updateEnterValueBar(String.valueOf(editableExercise.getReps()));
        rightEnterValueBar.updateEnterValueBar(String.valueOf(editableExercise.getSets()));
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
