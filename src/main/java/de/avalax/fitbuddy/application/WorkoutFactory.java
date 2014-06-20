package de.avalax.fitbuddy.application;

import com.google.gson.Gson;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.set.BasicSet;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WorkoutFactory {
    Gson gson = new Gson();

    public Workout createFromJson(String contents) {
        if (contents == null || contents.isEmpty()) {
            throw new WorkoutParseException();
        }
        try {
            ArrayList s = gson.fromJson(contents, ArrayList.class);
            ArrayList<ArrayList> exercises = (ArrayList) s.get(1);
            LinkedList<Exercise> exercices = new LinkedList<>();
            for (ArrayList exercise : exercises) {
                List<Set> sets = new ArrayList<>();
                for(int i=0;i<(double)exercise.get(2);i++) {
                    double weight = (double) exercise.get(3);
                    int maxReps =  (int)((double) exercise.get(1));
                    sets.add(new BasicSet(weight, maxReps));
                }
                exercices.add(new BasicExercise((String)exercise.get(0),sets));
            }
            BasicWorkout workout = new BasicWorkout(exercices);
            workout.setName((String)s.get(0));
            return workout;
        } catch (RuntimeException re) {
            throw new WorkoutParseException(re);
        }
    }

    public Workout createNew() {
        LinkedList<Exercise> exercises = new LinkedList<>();
        BasicWorkout workout = new BasicWorkout(exercises);
        workout.setName("new workout");
        return workout;
    }
}
