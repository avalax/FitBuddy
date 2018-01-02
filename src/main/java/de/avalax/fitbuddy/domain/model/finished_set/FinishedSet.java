package de.avalax.fitbuddy.domain.model.finished_set;

import java.io.Serializable;

public interface FinishedSet extends Serializable {
    double getWeight();

    int getReps();

    int getMaxReps();
}
