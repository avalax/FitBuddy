package de.avalax.fitbuddy.port.adapter.service;

import com.google.gson.Gson;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.BasicSet;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParseException;

import java.util.ArrayList;
import java.util.List;

public class JsonInWorkoutAdapter {
    Gson gson = new Gson();

    public Workout createFromJson(String contents) throws WorkoutParseException {
        if (contents == null || contents.isEmpty()) {
            throw new WorkoutParseException();
        }
        try {
            List s = gson.fromJson(contents, ArrayList.class);
            List<ArrayList> exercises = (ArrayList) s.get(1);
            List<Exercise> exerciseList = new ArrayList<>();
            for (ArrayList exercise : exercises) {
                List<Set> sets = new ArrayList<>();
                for (int i = 0; i < (double) exercise.get(2); i++) {
                    double weight = (double) exercise.get(3);
                    int maxReps = (int) ((double) exercise.get(1));
                    sets.add(new BasicSet(weight, maxReps));
                }
                exerciseList.add(new BasicExercise((String) exercise.get(0), sets));
            }
            return new BasicWorkout((String) s.get(0), exerciseList);
        } catch (RuntimeException re) {
            throw new WorkoutParseException(re);
        }
    }
}
