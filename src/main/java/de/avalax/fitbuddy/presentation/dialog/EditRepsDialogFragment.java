package de.avalax.fitbuddy.presentation.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.avalax.fitbuddy.R;

public class EditRepsDialogFragment extends DialogFragment {

    private static final String ARGS_REPS = "reps";
    @Bind(R.id.repsNumberPicker)
    protected NumberPicker repsNumberPicker;
    DialogListener listener;
    private int reps;

    public static EditRepsDialogFragment newInstance(int reps) {
        EditRepsDialogFragment fragment = new EditRepsDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_REPS, reps);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EditRepsDialogFragment.DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_reps, null);
        ButterKnife.bind(this, view);

        this.reps = getArguments().getInt(ARGS_REPS);

        repsNumberPicker.setMinValue(0);
        repsNumberPicker.setMaxValue(999);
        repsNumberPicker.setValue(reps);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setMessage(R.string.dialog_change_reps)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setReps();
                        listener.onDialogPositiveClick(EditRepsDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditRepsDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void setReps() {
        this.reps = repsNumberPicker.getValue();
    }

    public int getReps() {
        return reps;
    }

    public interface DialogListener {
        public void onDialogPositiveClick(EditRepsDialogFragment editRepsDialogFragment);
    }
}
