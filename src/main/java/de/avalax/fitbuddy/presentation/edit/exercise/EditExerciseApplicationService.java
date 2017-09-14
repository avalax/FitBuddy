package de.avalax.fitbuddy.presentation.edit.exercise;

import java.text.DecimalFormat;

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
        DecimalFormat format = new DecimalFormat("0.#");
        return format.format(set.getWeight());
    }
}
