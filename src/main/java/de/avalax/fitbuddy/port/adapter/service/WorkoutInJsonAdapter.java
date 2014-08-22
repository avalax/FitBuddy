package de.avalax.fitbuddy.port.adapter.service;

import android.util.Log;
import com.google.gson.Gson;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetNotAvailableException;
import de.avalax.fitbuddy.domain.model.workout.Workout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WorkoutInJsonAdapter {
    private Gson gson = new Gson();

    public String fromWorkout(Workout workout) {
        Collection<Object> collection = new ArrayList<>();
        collection.add(workout.getName());
        List<Object> exercises = new ArrayList<>();
        for (int i = 0; i < workout.countOfExercises(); i++) {
            try {
                Exercise exercise = workout.exerciseAtPosition(i);
                exercises.add(fromExercise(exercise));
            } catch (ExerciseNotFoundException e) {
                Log.d("Can't export exercise", e.getMessage(), e);
            }
        }
        collection.add(exercises);
        return gson.toJson(collection);
    }

    private List<Object> fromExercise(Exercise exercise) {
        List<Object> exportedExercise = new ArrayList<>();
        exportedExercise.add(exercise.getName());
        List<Object> sets = new ArrayList<>();
        for (int i = 0; i < exercise.countOfSets(); i++) {
            try {
                Set set = exercise.setAtPosition(i);
                sets.add(fromSet(set));
            } catch (SetNotAvailableException e) {
                Log.d("can't export set", e.getMessage(), e);
            }
        }
        exportedExercise.add(sets);
        return exportedExercise;
    }

    private List<Object> fromSet(Set set) {
        List<Object> s = new ArrayList<>();
        s.add(set.getWeight());
        s.add(set.getMaxReps());
        return s;
    }
}