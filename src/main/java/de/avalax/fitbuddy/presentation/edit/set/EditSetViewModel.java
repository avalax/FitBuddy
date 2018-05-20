package de.avalax.fitbuddy.presentation.edit.set;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseViewHelper;

public class EditSetViewModel extends ViewModel {
    private EditExerciseViewHelper editExerciseViewHelper;
    private Set set;
    private MutableLiveData<String> weight;
    private MutableLiveData<String> reps;

    public void init(EditExerciseViewHelper editExerciseViewHelper, Set set) {
        this.editExerciseViewHelper = editExerciseViewHelper;
        this.set = set;
        this.weight = new MutableLiveData<>();
        this.reps = new MutableLiveData<>();

        setWeightFromSet();
        setRepsFromSet();
    }

    private void setRepsFromSet() {
        this.reps.setValue(this.editExerciseViewHelper.repsFrom(this.set));
    }

    private void setWeightFromSet() {
        this.weight.setValue(this.editExerciseViewHelper.weightFrom(this.set));
    }

    public Set getSet() {
        return set;
    }

    public LiveData<String> getWeight() {
        return weight;
    }

    public LiveData<String> getReps() {
        return reps;
    }

    public void setReps(int reps) {
        set.setMaxReps(reps);
        setRepsFromSet();
    }

    public void setWeight(double weight) {
        set.setWeight(weight);
        setWeightFromSet();
    }
}

