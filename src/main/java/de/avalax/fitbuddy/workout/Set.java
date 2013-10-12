package de.avalax.fitbuddy.workout;

import java.io.Serializable;

public interface Set extends Serializable {
    double getWeight();
    int getReps();
    void setReps(int reps);
    int getRepsSize();
}
