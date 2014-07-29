package de.avalax.fitbuddy.application.workout;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.set.SetId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WorkoutSession {
    private SharedPreferences sharedPreferences;
    private Context context;
    private Map<ExerciseId, Integer> selectedSets;
    private Map<SetId, Integer> repsForSet;


    public WorkoutSession(SharedPreferences sharedPreferences, Context context) {
        this.sharedPreferences = sharedPreferences;
        this.context = context;
        this.selectedSets = readSelectedSetsFromFile();
        this.repsForSet = readRepsForSetFromFile();
    }

    public void switchWorkoutById(WorkoutId workoutId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WorkoutApplicationService.WORKOUT_ID_SHARED_KEY, workoutId.id());
        editor.commit();
    }

    public Integer selectedSetOfExercise(ExerciseId exerciseId) {
        return selectedSets.containsKey(exerciseId) ? selectedSets.get(exerciseId) : 0;
    }

    private Map<ExerciseId, Integer> readSelectedSetsFromFile() {
        Map<ExerciseId, Integer> selectedSets;
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), "selectedSets");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            selectedSets = (Map<ExerciseId, Integer>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            selectedSets = new HashMap<>();
        }
        return selectedSets;
    }

    private Map<SetId, Integer> readRepsForSetFromFile() {
        Map<SetId, Integer> repsForSet;
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), "repsForSet");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            repsForSet = (Map<SetId, Integer>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            repsForSet = new HashMap<>();
        }
        return repsForSet;
    }

    public void setSelectedSetOfExercise(ExerciseId exerciseId, int expectedPosition) {
        selectedSets.put(exerciseId, expectedPosition);
        writeSelectedSetsToFile();
    }

    private void writeSelectedSetsToFile() {
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), "selectedSets");
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(selectedSets);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            Log.d("Failed to write file", e.getMessage(), e);
        }
    }

    private void writeRepsForSetToFile() {
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), "repsForSet");
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(repsForSet);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            Log.d("Failed to write file", e.getMessage(), e);
        }
    }

    public void setRepsOfSet(SetId setId, int reps) {
        repsForSet.put(setId, reps);
        writeRepsForSetToFile();
    }

    public Integer repsForSet(SetId setId) {
        return repsForSet.containsKey(setId) ? repsForSet.get(setId) : 0;
    }

    public int selectedExercise() {
        return sharedPreferences.getInt("selectedExercise", 0);
    }

    public void setSelectedExercise(int selectedExercise) {
        sharedPreferences.edit().putInt("selectedExercise", selectedExercise).commit();
    }
}