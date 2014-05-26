package de.avalax.fitbuddy.app.manageWorkout;

import android.view.View;
import de.avalax.fitbuddy.app.WorkoutFactory;
import de.avalax.fitbuddy.app.WorkoutSession;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Set;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.WorkoutId;
import de.avalax.fitbuddy.core.workout.basic.BasicExercise;
import de.avalax.fitbuddy.core.workout.basic.BasicSet;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

import java.util.ArrayList;
import java.util.List;

public class ManageWorkout {

    private WorkoutFactory workoutFactory;

    private WorkoutDAO workoutDAO;

    private WorkoutSession workoutSession;

    private boolean unsavedChanges;

    private Workout workout;

    public ManageWorkout(WorkoutSession workoutSession, WorkoutDAO workoutDAO, WorkoutFactory workoutFactory) {
        this.workoutSession = workoutSession;
        this.workoutDAO = workoutDAO;
        this.workoutFactory = workoutFactory;
    }

    private void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    public int unsavedChangesVisibility() {
        return unsavedChanges ? View.VISIBLE : View.GONE;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(WorkoutId id) {
        unsavedChanges = false;
        this.workout = workoutDAO.load(id);
    }

    public void switchWorkout() {
        workoutSession.switchWorkout(workout.getId());
    }

    public List<Workout> getWorkouts() {
        return workoutDAO.getList();
    }

    public void createNewWorkout() {
        workout = workoutFactory.createNew();
        workoutDAO.save(workout);
        unsavedChanges = false;
    }

    public void createWorkoutFromJson(String json) {
        Workout workoutFromJson = workoutFactory.createFromJson(json);
        if (workoutFromJson != null) {
            workout = workoutFromJson;
            workoutDAO.save(workoutFromJson);
            unsavedChanges = false;
        }
    }

    public void deleteWorkout() {
        //TODO: undo function delete workout
        workoutDAO.delete(workout.getId());
        if (getWorkouts().size() - 1 >= 0) {
            setWorkout(getWorkouts().get(getWorkouts().size() - 1).getId());
        } else {
            createNewWorkout();
        }
        setUnsavedChanges(true);
    }

    public void undoUnsavedChanges() {
        //TODO: undo changes
        setUnsavedChanges(false);
    }

    public void deleteExercise(Exercise exercise) {
        //TODO: undo function delete exercise
        workoutDAO.deleteExercise(exercise.getId());
        workout.removeExercise(exercise);
        setUnsavedChanges(true);
    }

    public void replaceExercise(Exercise exercise) {
        workout.replaceExercise(exercise);
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
        workoutDAO.saveExercise(workout.getId(), exercise);
    }
}
