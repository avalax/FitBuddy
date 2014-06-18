package de.avalax.fitbuddy.domain.model;

import java.io.Serializable;

public interface Set extends Serializable {
    double getWeight();
    int getReps();
    void setReps(int reps);
    int getMaxReps();
    void setMaxReps(int reps);
    void setWeight(double weight);
    SetId getSetId();
    void setId(SetId setId);
}
