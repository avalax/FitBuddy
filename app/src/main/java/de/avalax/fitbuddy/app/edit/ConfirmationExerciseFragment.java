package de.avalax.fitbuddy.app.edit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.app.R;

public class ConfirmationExerciseFragment extends Fragment {
    private static final String EDITABLE_EXERCISE = "editableExercise";
    @InjectView(R.id.exerciseNameEditText)
    protected EditText exerciseNameEditText;
    @InjectView(R.id.buttonCancel)
    protected Button buttonCancel;
    private EditableExercise editableExercise;
    private ViewPager viewPager;

    public static ConfirmationExerciseFragment newInstance(EditableExercise editableExercise) {
        ConfirmationExerciseFragment confirmationExerciseFragment = new ConfirmationExerciseFragment();
        Bundle args = new Bundle();
        args.putSerializable(EDITABLE_EXERCISE, editableExercise);
        confirmationExerciseFragment.setArguments(args);
        return confirmationExerciseFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        editableExercise = (EditableExercise) getArguments().getSerializable(EDITABLE_EXERCISE);
        View view = inflater.inflate(R.layout.fragment_edit_confirmation, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        if (!exerciseNameEditText.getHint().toString().equals(editableExercise.getName())) {
            exerciseNameEditText.setText(editableExercise.getName());
        }
        if (editableExercise instanceof ExistingEditableExercise) {
            buttonCancel.setText(R.string.action_delete);
        }
        exerciseNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editableExercise.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewPager.getAdapter().notifyDataSetChanged();
                viewPager.invalidate();
            }
        });
    }
}
