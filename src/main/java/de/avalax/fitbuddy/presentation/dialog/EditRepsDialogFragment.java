package de.avalax.fitbuddy.presentation.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.exception.DialogListenerException;

public class EditRepsDialogFragment extends AppCompatDialogFragment {
    private static final String ARGS_REPS = "reps";
    private NumberPicker repsNumberPicker;
    private DialogListener listener;

    public static EditRepsDialogFragment newInstance(int reps) {
        EditRepsDialogFragment fragment = new EditRepsDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_REPS, reps);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new DialogListenerException(context.toString()
                    + " must implement EditRepsDialogFragment.DialogListener", e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_reps, container, false);
        Button button = view.findViewById(R.id.done_button);
        Integer reps = getArguments().getInt(ARGS_REPS);
        getDialog().setTitle(R.string.dialog_change_reps);

        repsNumberPicker = view.findViewById(R.id.repsNumberPicker);
        repsNumberPicker.setMinValue(0);
        repsNumberPicker.setMaxValue(999);
        repsNumberPicker.setValue(reps);

        button.setOnClickListener(onClickListener -> {
            listener.onDialogPositiveClick(this);
            getDialog().dismiss();
        });

        return view;
    }

    public int getReps() {
        return repsNumberPicker.getValue();
    }

    public interface DialogListener {
        void onDialogPositiveClick(EditRepsDialogFragment editRepsDialogFragment);
    }
}
