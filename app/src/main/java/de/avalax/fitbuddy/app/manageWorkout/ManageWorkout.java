package de.avalax.fitbuddy.app.manageWorkout;

import android.view.View;
import de.avalax.fitbuddy.app.WorkoutFactory;
import de.avalax.fitbuddy.app.WorkoutSession;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

import java.util.List;

public class ManageWorkout {

    private WorkoutFactory workoutFactory;

    private WorkoutDAO workoutDAO;

    private WorkoutSession workoutSession;

    private boolean unsavedChanges;

    private Integer exercisePosition;

    private Workout workout;

    public ManageWorkout(WorkoutSession workoutSession, WorkoutDAO workoutDAO, WorkoutFactory workoutFactory) {
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

    public void setExercisePosition(Integer exercisePosition) {
        this.exercisePosition = exercisePosition;
    }

    public Integer getExercisePosition() {
        return exercisePosition;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(int position) {
        unsavedChanges = false;
        this.workout = workoutDAO.load(position);
    }

    public void switchWorkout(int position) {
        workoutSession.switchWorkout(position);
    }

    public List<String> getWorkouts() {
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
        //TODO: undo function remove workout
        workoutDAO.remove(workout);
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
}
