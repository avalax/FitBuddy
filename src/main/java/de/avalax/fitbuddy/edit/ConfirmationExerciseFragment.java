package de.avalax.fitbuddy.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.avalax.fitbuddy.R;
import roboguice.fragment.RoboFragment;

public class ConfirmationExerciseFragment extends RoboFragment {
    private EditableExercise editableExercise;

    public ConfirmationExerciseFragment(EditableExercise editableExercise) {
        super();
        this.editableExercise = editableExercise;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.edit_confirmation, container, false);
    }
}
