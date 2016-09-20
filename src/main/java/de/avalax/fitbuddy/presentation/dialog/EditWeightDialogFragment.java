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
import de.avalax.fitbuddy.application.dialog.WeightDecimalPlaces;

public class EditWeightDialogFragment extends DialogFragment {

    private static final String ARGS_WEIGHT = "weight";
    private NumberPicker weightNumberPicker;
    private NumberPicker weightDecimalPlacesNumberPicker;
    DialogListener listener;
    private double weight;
    private WeightDecimalPlaces weightDecimalPlaces = new WeightDecimalPlaces();

    public static EditWeightDialogFragment newInstance(double weight) {
        EditWeightDialogFragment fragment = new EditWeightDialogFragment();
        Bundle args = new Bundle();
        args.putDouble(ARGS_WEIGHT, weight);
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
                    + " must implement EditWeightDialogFragment.DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_weight, null);

        this.weight = getArguments().getDouble(ARGS_WEIGHT);

        weightNumberPicker = (NumberPicker) view.findViewById(R.id.weightNumberPicker);
        weightNumberPicker.setMinValue(0);
        weightNumberPicker.setMaxValue(999);
        weightNumberPicker.setValue((int) Math.floor(weight));

        String[] labels = weightDecimalPlaces.getLabels();
        weightDecimalPlacesNumberPicker = (NumberPicker) view.findViewById(R.id.weightDecimalPlacesNumberPicker);
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

    public interface DialogListener {
        void onDialogPositiveClick(EditWeightDialogFragment editWeightDialogFragment);
    }
}
