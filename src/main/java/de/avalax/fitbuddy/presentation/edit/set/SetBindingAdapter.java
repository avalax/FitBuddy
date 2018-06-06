package de.avalax.fitbuddy.presentation.edit.set;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.set.Set;

public abstract class SetBindingAdapter {
    private static DecimalFormat decimalFormat;

    static {
        decimalFormat = new DecimalFormat("0.###");
    }

    @BindingAdapter("reps")
    public static void setRepsFromSet(TextView textView, Set set) {
        String label = textView.getContext().getResources()
                .getQuantityString(R.plurals.reps_label, set.getMaxReps(), set.getMaxReps());
        textView.setText(label);
    }

    @BindingAdapter("weight")
    public static void setWeightFromSet(TextView textView, Set set) {
        int quantity = set.getWeight() == 0 ? 0 : 1;
        String value = decimalFormat.format(set.getWeight());
        String label;
        if (quantity == 0) {
            label = textView.getContext().getResources()
                    .getString(R.string.default_set_weight);
        } else {
            label = textView.getContext().getResources()
                    .getQuantityString(R.plurals.weight_label, quantity, value);
        }
        textView.setText(label);
    }
}
