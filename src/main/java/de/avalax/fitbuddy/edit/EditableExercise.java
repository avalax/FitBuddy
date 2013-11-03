package de.avalax.fitbuddy.edit;

import de.avalax.fitbuddy.workout.Exercise;

import java.io.Serializable;

public interface EditableExercise extends Serializable {
    String getName();

    void setName(String name);

    int getReps();

    void setReps(int reps);

    int getSets();

    void setSets(int sets);

    double getWeight();

    void setWeight(double weight);

    double getWeightRaise();

    void setWeightRaise(double weightRaise);

    Exercise createExercise();
}
