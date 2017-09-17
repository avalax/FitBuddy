package de.avalax.fitbuddy.presentation.edit.exercise;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import de.avalax.fitbuddy.domain.model.set.Set;

import static java.lang.String.valueOf;

public class EditExerciseApplicationService {
    public String title(Set set) {
        return valueOf(set.getMaxReps());
    }

    public String subtitle(Set set) {
        //TODO: get from res/strings
        if (set.getWeight() == 0) {
            return "-";
        }
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        DecimalFormat format = new DecimalFormat("0.#", otherSymbols);
        return format.format(set.getWeight());
    }
}
