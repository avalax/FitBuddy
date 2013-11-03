package de.avalax.fitbuddy.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.enterValueBar.EnterValueBar;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class WeightExerciseFragment extends RoboFragment {
    private EditableExercise editableExercise;

    @InjectView(R.id.leftEnterValueBar)
    EnterValueBar leftEnterValueBar;

    @InjectView(R.id.rightEnterValueBar)
    EnterValueBar rightEnterValueBar;

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
        leftEnterValueBar.setValue(String.valueOf(editableExercise.getWeight()));
        rightEnterValueBar.setValue(String.valueOf(editableExercise.getWeightRaise()));
    }
}
