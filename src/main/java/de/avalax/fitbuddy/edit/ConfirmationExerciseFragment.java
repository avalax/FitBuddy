package de.avalax.fitbuddy.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import de.avalax.fitbuddy.R;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class ConfirmationExerciseFragment extends RoboFragment {
    private EditableExercise editableExercise;

    @InjectView(R.id.exerciseNameEditText)
    EditText exerciseNameEditText;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        exerciseNameEditText.setText(editableExercise.getName());
    }
}
