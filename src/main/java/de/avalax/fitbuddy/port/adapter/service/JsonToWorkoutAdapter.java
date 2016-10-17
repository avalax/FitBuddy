package de.avalax.fitbuddy.port.adapter.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParseException;

public class JsonToWorkoutAdapter {
    private Gson gson = new Gson();

    public Workout createFromJson(String contents) throws WorkoutParseException {
        if (contents == null || contents.isEmpty()) {
            throw new WorkoutParseException();
        }
        try {
            List jsonWorkout = gson.fromJson(contents, List.class);
            List<List> jsonExercises = (List<List>) jsonWorkout.get(1);
            Workout workout = new BasicWorkout();
            workout.setName((String) jsonWorkout.get(0));

            for (List jsonExercise : jsonExercises) {
                Exercise exercise = workout.getExercises().createExercise();
                exercise.setName((String) jsonExercise.get(0));

                List<List> jsonSets = (List<List>) jsonExercise.get(1);
                for (List jsonSet : jsonSets) {
                    double weight = (double) jsonSet.get(1);
                    int maxReps = (int) ((double) jsonSet.get(0));
                    Set set = exercise.getSets().createSet();
                    set.setWeight(weight);
                    set.setMaxReps(maxReps);
                }
            }
            return workout;
        } catch (JsonSyntaxException re) {
            throw new WorkoutParseException(re);
        }
    }
}
