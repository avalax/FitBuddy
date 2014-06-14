package de.avalax.fitbuddy.app.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.app.R;

public class EditSetsDialogFragment extends DialogFragment {

    private static final String ARGS_SETS = "sets";

    private int sets;

    public interface DialogListener {
        public void onDialogPositiveClick(EditSetsDialogFragment editSetsDialogFragment);
    }

    @InjectView(R.id.setsNumberPicker)
    protected NumberPicker setsNumberPicker;

    DialogListener listener;

    public static EditSetsDialogFragment newInstance(int sets) {
        EditSetsDialogFragment fragment = new EditSetsDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_SETS, sets);
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
                    + " must implement EditSetsDialogFragment.DialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_sets, null);
        ButterKnife.inject(this, view);

        this.sets = getArguments().getInt(ARGS_SETS);

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
}
