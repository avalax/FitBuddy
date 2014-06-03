package de.avalax.fitbuddy.app.manageWorkout;

import android.view.View;
import de.avalax.fitbuddy.app.ExerciseFactory;
import de.avalax.fitbuddy.app.WorkoutFactory;
import de.avalax.fitbuddy.app.WorkoutSession;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;
import de.avalax.fitbuddy.core.workout.WorkoutId;
import de.avalax.fitbuddy.datalayer.WorkoutDAO;

import java.util.List;

public class ManageWorkout {

    private WorkoutFactory workoutFactory;

    private ExerciseFactory exerciseFactory;

    private WorkoutDAO workoutDAO;

    private WorkoutSession workoutSession;

    private boolean unsavedChanges;

    private Workout workout;
    private Workout deletedWorkout;
    private Exercise deletedExercise;
    private Integer deletedExerciseIndex;

    public ManageWorkout(WorkoutSession workoutSession, WorkoutDAO workoutDAO, WorkoutFactory workoutFactory, ExerciseFactory exerciseFactory) {
        this.workoutSession = workoutSession;
        this.workoutDAO = workoutDAO;
        this.workoutFactory = workoutFactory;
        this.exerciseFactory = exerciseFactory;
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
        this.workout = workoutDAO.load(id);
    }

    public void switchWorkout() {
        workoutSession.switchWorkout(workout.getId());
    }

    public List<Workout> getWorkouts() {
        return workoutDAO.getList();
    }

    public void createWorkout() {
        workout = workoutFactory.createNew();
        workoutDAO.save(workout);
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
        workoutDAO.delete(workout.getId());
        deletedExercise = null;
        setUnsavedChanges(workout);
        workout = null;
    }

    private void setUnsavedChanges(Workout workout) {
        deletedWorkout = workout;
        setUnsavedChanges(true);
    }

    private void setUnsavedChanges(int index, Exercise exercise) {
        this.deletedExerciseIndex = index;
        this.deletedExercise = exercise;
        setUnsavedChanges(true);
    }

    public void undoDeleteExercise() {
        workout.addExercise(deletedExerciseIndex, deletedExercise);
        workoutDAO.saveExercise(workout.getId(), deletedExercise);
        deletedExerciseIndex = null;
        deletedExercise = null;
        setUnsavedChanges(false);
    }

    public void undoDeleteWorkout() {
        workout = deletedWorkout;
        workoutDAO.save(deletedWorkout);
        deletedWorkout = null;
        setUnsavedChanges(false);
    }

    public void deleteExercise(Exercise exercise) {
        workoutDAO.deleteExercise(exercise.getId());
        int index = workout.getExercises().indexOf(exercise);
        if (workout.deleteExercise(exercise)) {
            setUnsavedChanges(index,exercise);
            deletedWorkout = null;
        }
    }

    public void replaceExercise(Exercise exercise) {
        workout.replaceExercise(exercise);
        setUnsavedChanges(false);
    }

    public void createExercise() {
        Exercise exercise = exerciseFactory.createNew();
        workout.addExercise(exercise);
        workoutDAO.saveExercise(workout.getId(), exercise);
    }

    public boolean hasDeletedWorkout() {
        return deletedWorkout != null;
    }

    public boolean hasDeletedExercise() {
        return deletedExercise != null;
    }
}
