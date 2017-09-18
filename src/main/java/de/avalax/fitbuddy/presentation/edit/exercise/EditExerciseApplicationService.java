package de.avalax.fitbuddy.presentation.edit.exercise;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.set.Set;

import static java.lang.String.valueOf;

public class EditExerciseApplicationService {

    private final String defaultWeight;
    private final DecimalFormat decimalFormat;

    public EditExerciseApplicationService(Context context) {
        defaultWeight = context.getResources().getString(R.string.default_weight);
        String decimalSeparator = context.getResources().getString(R.string.decimal_separator);
        String groupingSeparator = context.getResources().getString(R.string.grouping_separator);
        DecimalFormatSymbols otherSymbols = formatSymbols(decimalSeparator, groupingSeparator);
        decimalFormat = new DecimalFormat("0.#", otherSymbols);
    }

    private DecimalFormatSymbols formatSymbols(String decimalSeparator, String groupingSeparator) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
        otherSymbols.setGroupingSeparator(groupingSeparator.charAt(0));
        return otherSymbols;
    }

    public String title(Set set) {
        return valueOf(set.getMaxReps());
    }

    public String subtitle(Set set) {
        if (set.getWeight() == 0) {
            return defaultWeight;
        }
        return decimalFormat.format(set.getWeight());
    }
}
