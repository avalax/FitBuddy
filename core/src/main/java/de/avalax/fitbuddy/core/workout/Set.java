package de.avalax.fitbuddy.core.workout;

import java.io.Serializable;

public interface Set extends Serializable {
    double getWeight();
    int getReps();
    void setReps(int reps);
    int getMaxReps();
    void setWeight(double weight);
    Long getId();
    void setId(Long id);
}
