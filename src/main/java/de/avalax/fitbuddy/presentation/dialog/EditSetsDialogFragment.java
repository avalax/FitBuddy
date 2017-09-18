package de.avalax.fitbuddy.presentation.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.exception.DialogListenerException;

public class EditSetsDialogFragment extends DialogFragment {
    private static final String ARGS_SETS = "sets";
    private NumberPicker setsNumberPicker;
    private DialogListener listener;

    public static EditSetsDialogFragment newInstance(int sets) {
        EditSetsDialogFragment fragment = new EditSetsDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_SETS, sets);
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
                    + " must implement EditSetsDialogFragment.DialogListener", e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_sets, container, false);
        Button button = view.findViewById(R.id.done_button);
        Integer sets = getArguments().getInt(ARGS_SETS);
        getDialog().setTitle(R.string.dialog_change_sets);

        setsNumberPicker = view.findViewById(R.id.setsNumberPicker);
        setsNumberPicker.setMinValue(0);
        setsNumberPicker.setMaxValue(999);
        setsNumberPicker.setValue(sets);

        button.setOnClickListener(v -> {
            listener.onDialogPositiveClick(EditSetsDialogFragment.this);
            getDialog().dismiss();
        });

        return view;
    }

    public int getSets() {
        return setsNumberPicker.getValue();
    }

    public interface DialogListener {
        void onDialogPositiveClick(EditSetsDialogFragment editSetsDialogFragment);
    }
}
