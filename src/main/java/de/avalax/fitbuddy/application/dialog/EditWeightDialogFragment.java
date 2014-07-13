package de.avalax.fitbuddy.application.dialog;

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
import de.avalax.fitbuddy.application.R;

public class EditWeightDialogFragment extends DialogFragment {

    private static final String ARGS_WEIGHT = "weight";

    private double weight;

    private WeightDecimalPlaces weightDecimalPlaces = new WeightDecimalPlaces();

    public interface DialogListener {
        public void onDialogPositiveClick(EditWeightDialogFragment editWeightDialogFragment);
    }

    @InjectView(R.id.weightNumberPicker)
    protected NumberPicker weightNumberPicker;

    @InjectView(R.id.weightDecimalPlacesNumberPicker)
    protected NumberPicker weightDecimalPlacesNumberPicker;

    DialogListener listener;

    public static EditWeightDialogFragment newInstance(double weight) {
        EditWeightDialogFragment fragment = new EditWeightDialogFragment();
        Bundle args = new Bundle();
        args.putDouble(ARGS_WEIGHT, weight);
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
                    + " must implement EditWeightDialogFragment.DialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_weight, null);
        ButterKnife.inject(this, view);

        this.weight = getArguments().getDouble(ARGS_WEIGHT);

        weightNumberPicker.setMinValue(0);
        weightNumberPicker.setMaxValue(999);
        weightNumberPicker.setValue((int) Math.floor(weight));

        String[] labels = weightDecimalPlaces.getLabels();
        weightDecimalPlacesNumberPicker.setMinValue(0);
        weightDecimalPlacesNumberPicker.setMaxValue(labels.length - 1);
        weightDecimalPlacesNumberPicker.setValue(weightDecimalPlaces.getPosition(weight - Math.floor(weight)));
        weightDecimalPlacesNumberPicker.setDisplayedValues(labels);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setMessage(R.string.dialog_change_weight)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setWeight();
                        listener.onDialogPositiveClick(EditWeightDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditWeightDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void setWeight() {
        this.weight = weightNumberPicker.getValue() + weightDecimalPlaces.getWeight(weightDecimalPlacesNumberPicker.getValue());
    }

    public double getWeight() {
        return weight;
    }
}
