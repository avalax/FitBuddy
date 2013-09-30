package de.avalax.fitbuddy.workout;

import java.io.Serializable;

public interface Set extends Serializable {
    double getWeight();
    int getRepetitions();
    void setRepetitions(int repetitions);
    int getMaxRepetition();
}
