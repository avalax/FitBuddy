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

    @InjectView(R.id.numberPicker1)
    protected NumberPicker numberPicker;

    @InjectView(R.id.numberPicker2)
    protected NumberPicker numberPicker2;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_weight, null);
        ButterKnife.inject(this, view);
        String[] nums = {"0","875","75","675","5","375","25","125"};

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(999);
        numberPicker.setValue(10);

        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(nums.length-1);
        numberPicker2.setValue(0);
        numberPicker2.setDisplayedValues(nums);

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
