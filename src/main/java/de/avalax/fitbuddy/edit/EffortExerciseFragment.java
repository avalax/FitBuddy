package de.avalax.fitbuddy.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.enterValueBar.EnterValueBar;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EffortExerciseFragment extends RoboFragment {
    private EditExercise editExercise;

    @InjectView(R.id.leftEnterValueBar)
    EnterValueBar leftEnterValueBar;

    @InjectView(R.id.rightEnterValueBar)
    EnterValueBar rightEnterValueBar;

    public EffortExerciseFragment(EditExercise editExercise) {
        super();
        this.editExercise = editExercise;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.edit_effort, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        leftEnterValueBar.setValue(String.valueOf(editExercise.getReps()));
        rightEnterValueBar.setValue(String.valueOf(editExercise.getSets()));
    }
}
