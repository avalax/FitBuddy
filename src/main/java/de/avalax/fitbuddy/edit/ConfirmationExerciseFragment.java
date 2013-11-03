package de.avalax.fitbuddy.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.avalax.fitbuddy.R;
import roboguice.fragment.RoboFragment;

public class ConfirmationExerciseFragment extends RoboFragment {
    private EditExercise editExercise;

    public ConfirmationExerciseFragment(EditExercise editExercise) {
        super();
        this.editExercise = editExercise;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.edit_confirmation, container, false);
    }
}
