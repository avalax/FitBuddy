package de.avalax.fitbuddy.domain.model.set;

import java.io.Serializable;

public interface Set extends Serializable {

    double getWeight();

    int getReps();

    void setReps(int reps);

    int getMaxReps();

    void setMaxReps(int reps);

    void setWeight(double weight);

    SetId getSetId();

    void setSetId(SetId setId);
}
