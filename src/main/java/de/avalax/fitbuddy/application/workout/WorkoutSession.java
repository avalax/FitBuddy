package de.avalax.fitbuddy.application.workout;

import android.content.Context;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutNotFoundException;

import java.io.*;

public class WorkoutSession {
    private Workout workout;
    private Context context;


    public WorkoutSession(Context context) {
        this.context = context;
        this.workout = readCurrentWorkoutFromFile();
    }

    public void switchWorkout(Workout workout) throws IOException {
        this.workout = workout;
        writeCurrentWorkoutToFile();
    }

    public void saveCurrentWorkout() throws IOException {
        writeCurrentWorkoutToFile();
    }

    public Workout getWorkout() throws WorkoutNotFoundException {
        if (workout == null) {
            throw new WorkoutNotFoundException();
        }
        return workout;
    }

    private Workout readCurrentWorkoutFromFile() {
        Workout workout;
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), "currentWorkout");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            workout = (Workout) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            workout = null;
        }
        return workout;
    }

    private void writeCurrentWorkoutToFile() throws IOException {
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), "currentWorkout");
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        outputStream.writeObject(workout);
        outputStream.flush();
        outputStream.close();

    }
}