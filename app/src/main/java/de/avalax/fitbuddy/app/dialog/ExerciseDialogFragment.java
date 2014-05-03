package de.avalax.fitbuddy.app.dialog;

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

abstract public class ExerciseDialogFragment extends DialogFragment {

    @InjectView(R.id.weightNumberPicker)
    protected NumberPicker weightNumberPicker;

    @InjectView(R.id.weightDecimalPlacesNumberPicker)
    protected NumberPicker weightDecimalPlacesNumberPicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_weight, null);
        ButterKnife.inject(this, view);
        String[] nums = {"0","875","75","675","5","375","25","125"};

        weightNumberPicker.setMinValue(0);
        weightNumberPicker.setMaxValue(999);
        weightNumberPicker.setValue(10);

        weightDecimalPlacesNumberPicker.setMinValue(0);
        weightDecimalPlacesNumberPicker.setMaxValue(nums.length - 1);
        weightDecimalPlacesNumberPicker.setValue(0);
        weightDecimalPlacesNumberPicker.setDisplayedValues(nums);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setMessage(R.string.dialog_change_weight)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ExerciseDialogFragment.this.onDialogPositiveClick();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ExerciseDialogFragment.this.onDialogNegativeClick();
                    }
                });
        return builder.create();
    }

    abstract public void onDialogNegativeClick();

    abstract public void onDialogPositiveClick();
}
