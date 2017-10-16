package de.avalax.fitbuddy.application.workout;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;

public class WorkoutSession {
    private Workout workout;
    private Context context;

    public WorkoutSession(Context context) {
        this.context = context;
        this.workout = readCurrentWorkoutFromFile();
    }

    public void switchWorkout(Workout workout) throws WorkoutException {
        this.workout = workout;
        try {
            writeCurrentWorkoutToFile();
        } catch (IOException e) {
            throw new WorkoutException(e);
        }
    }

    public void saveCurrentWorkout() throws WorkoutException {
        try {
            writeCurrentWorkoutToFile();
        } catch (IOException e) {
            throw new WorkoutException(e);
        }
    }

    public Workout getWorkout() {
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

    protected void writeCurrentWorkoutToFile() throws IOException {
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), "currentWorkout");
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        outputStream.writeObject(workout);
        outputStream.flush();
        outputStream.close();
    }

    public boolean hasWorkout() {
        return workout != null;
    }
}