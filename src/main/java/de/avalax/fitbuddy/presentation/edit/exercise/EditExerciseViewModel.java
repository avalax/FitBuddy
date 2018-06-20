package de.avalax.fitbuddy.presentation.edit.exercise;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Sets;

public class EditExerciseViewModel extends ViewModel {
    private MediatorLiveData<Sets> sets;
    private MutableLiveData<String> name;

    public void init(Exercise exercise) {
        sets = new MediatorLiveData<>();
        sets.setValue(exercise.getSets());
        name = new MutableLiveData<>();
        name.setValue(exercise.getName());
    }

    public LiveData<String> getName() {
        return name;
    }

    public MediatorLiveData<Sets> getSets() {
        return sets;
    }
}
