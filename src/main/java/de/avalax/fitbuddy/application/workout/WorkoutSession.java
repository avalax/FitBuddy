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
        try {
            this.workout = readCurrentWorkoutFromFile();
        } catch (IOException | ClassNotFoundException ignored) {
        }
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

    private Workout readCurrentWorkoutFromFile() throws IOException, ClassNotFoundException {
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), "currentWorkout");
        try (FileInputStream fis = new FileInputStream(file)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                return (Workout) ois.readObject();
            }
        }
    }

    protected void writeCurrentWorkoutToFile() throws IOException {
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), "currentWorkout");
        try (FileOutputStream fis = new FileOutputStream(file)) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(fis)) {
                outputStream.writeObject(workout);
            }
        }
    }

    public boolean hasWorkout() {
        return workout != null;
    }
}