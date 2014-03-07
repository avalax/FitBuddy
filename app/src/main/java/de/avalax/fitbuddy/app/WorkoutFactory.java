package de.avalax.fitbuddy.app;

import com.google.gson.Gson;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.basic.BasicWorkout;

import java.util.ArrayList;
import java.util.LinkedList;

public class WorkoutFactory {
    Gson gson = new Gson();

    public Workout fromJson(String contents) {
        if (contents == null || contents.isEmpty()) {
            throw new WorkoutParseException();
        }
        try {
            ArrayList s = gson.fromJson(contents, ArrayList.class);

            LinkedList<Exercise> exercices = new LinkedList<>();
            BasicWorkout workout = new BasicWorkout(exercices);
            workout.setName((String)s.get(0));
            return workout;
        } catch (RuntimeException re) {
            throw new WorkoutParseException(re);
        }
    }
}
