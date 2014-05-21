package de.avalax.fitbuddy.app.manageWorkout;

import android.content.Context;
import android.view.View;
import de.avalax.fitbuddy.app.WorkoutFactory;
import de.avalax.fitbuddy.app.WorkoutSession;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Set;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.basic.BasicExercise;
import de.avalax.fitbuddy.core.workout.basic.BasicSet;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

import java.util.ArrayList;
import java.util.TreeMap;

public class ManageWorkout {

    private WorkoutFactory workoutFactory;

    private WorkoutDAO workoutDAO;

    private WorkoutSession workoutSession;

    private boolean unsavedChanges;

    private Workout workout;

    public ManageWorkout(Context context, WorkoutSession workoutSession, WorkoutDAO workoutDAO, WorkoutFactory workoutFactory) {
        this.workoutSession = workoutSession;
        this.workoutDAO = workoutDAO;
        this.workoutFactory = workoutFactory;
    }

    public void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    public int unsavedChangesVisibility() {
        return unsavedChanges ? View.VISIBLE : View.GONE;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(long position) {
        unsavedChanges = false;
        this.workout = workoutDAO.load(position);
    }

    public void switchWorkout() {
        workoutSession.switchWorkout(workout.getId());
    }

    public TreeMap<Long, String> getWorkouts() {
        return workoutDAO.getList();
    }

    public void createNewWorkout() {
        Workout workout = workoutFactory.createNew();
        workoutDAO.save(workout);
        setWorkout(workoutDAO.getList().size() - 1);
    }

    public void createWorkoutFromJson(String json) {
        Workout workoutFromJson = workoutFactory.createFromJson(json);
        if (workoutFromJson != null) {
            workoutDAO.save(workoutFromJson);
            setWorkout(workoutDAO.getList().size() - 1);
        }
    }

    public void deleteWorkout() {
        //TODO: undo function delete workout
        workoutDAO.delete(workout);
        if (getWorkouts().size() - 1 >= 0) {
            setWorkout(getWorkouts().size() - 1);
        } else {
            createNewWorkout();
        }
        setUnsavedChanges(true);
    }

    public void undoUnsavedChanges() {
        //TODO: undo changes
        setUnsavedChanges(false);
    }

    public void deleteExercise(int position) {
        //TODO: undo function delete exercise
        workout.removeExercise(position);
        setUnsavedChanges(true);
    }

    public void setExercise(int position, Exercise exercise) {
        workout.setExercise(position, exercise);
        setUnsavedChanges(false);
    }

    public void addNewExercise() {
        //TODO: create a new exercise with better defaults
        ArrayList<Set> sets = new ArrayList<>();
        sets.add(new BasicSet(20, 12));
        sets.add(new BasicSet(20, 12));
        sets.add(new BasicSet(20, 12));
        Exercise exercise = new BasicExercise("new exercise", sets, 0);
        workout.addExercise(exercise);
    }
}
