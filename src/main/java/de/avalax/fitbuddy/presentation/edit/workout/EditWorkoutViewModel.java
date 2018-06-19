package de.avalax.fitbuddy.presentation.edit.workout;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import de.avalax.fitbuddy.domain.model.exercise.Exercises;
import de.avalax.fitbuddy.domain.model.workout.Workout;

public class EditWorkoutViewModel extends ViewModel {
    private MediatorLiveData<Exercises> exercises;
    private MutableLiveData<String> name;

    public void init(Workout workout) {
        exercises = new MediatorLiveData<>();
        exercises.setValue(workout.getExercises());
        name = new MutableLiveData<>();
        name.setValue(workout.getName());
    }

    public LiveData<String> getName() {
        return name;
    }

    public MediatorLiveData<Exercises> getExercises() {
        return exercises;
    }
}
