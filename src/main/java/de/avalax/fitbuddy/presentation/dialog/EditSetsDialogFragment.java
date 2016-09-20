package de.avalax.fitbuddy.presentation.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import de.avalax.fitbuddy.R;

public class EditSetsDialogFragment extends DialogFragment {

    private static final String ARGS_SETS = "sets";
    protected NumberPicker setsNumberPicker;
    DialogListener listener;
    private int sets;

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
            throw new ClassCastException(context.toString()
                    + " must implement EditSetsDialogFragment.DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_sets, null);

        this.sets = getArguments().getInt(ARGS_SETS);

        setsNumberPicker = (NumberPicker) view.findViewById(R.id.setsNumberPicker);
        setsNumberPicker.setMinValue(0);
        setsNumberPicker.setMaxValue(999);
        setsNumberPicker.setValue(sets);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setMessage(R.string.dialog_change_sets)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setSets();
                        listener.onDialogPositiveClick(EditSetsDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditSetsDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void setSets() {
        this.sets = setsNumberPicker.getValue();
    }

    public int getSets() {
        return sets;
    }

    public interface DialogListener {
        void onDialogPositiveClick(EditSetsDialogFragment editSetsDialogFragment);
    }
}
