package de.avalax.fitbuddy.application.manageWorkout;

import android.view.View;
import de.avalax.fitbuddy.application.WorkoutSession;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.*;

import java.util.ArrayList;
import java.util.List;

public class ManageWorkout {

    private WorkoutRepository workoutRepository;

    private ExerciseRepository exerciseRepository;

    private SetRepository setRepository;

    private WorkoutService workoutService;

    private WorkoutSession workoutSession;

    private boolean unsavedChanges;

    private Workout workout;

    private Workout deletedWorkout;

    private Exercise deletedExercise;

    private Integer deletedExerciseIndex;

    public ManageWorkout(WorkoutSession workoutSession, WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, SetRepository setRepository, WorkoutService workoutService) {
        this.workoutSession = workoutSession;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.setRepository = setRepository;
        this.workoutService = workoutService;
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

    public void setWorkout(WorkoutId id) throws WorkoutNotFoundException {
        this.workout = workoutRepository.load(id);
    }

    public void switchWorkout() throws WorkoutNotFoundException {
        if (workout == null) {
            throw new WorkoutNotFoundException();
        }
        workoutSession.switchWorkoutById(workout.getWorkoutId());
        setUnsavedChanges(false);
    }

    public List<WorkoutListEntry> getWorkoutList() {
        return workoutRepository.getWorkoutList();
    }

    public Workout createWorkout() {
        workout = new BasicWorkout();
        workoutRepository.save(workout);
        setUnsavedChanges(false);
        return workout;
    }

    public void createWorkoutFromJson(String json) throws WorkoutParseException {
        Workout workoutFromJson = workoutService.workoutFromJson(json);
        if (workoutFromJson != null) {
            workout = workoutFromJson;
            workoutRepository.save(workoutFromJson);
            setUnsavedChanges(false);
        }
    }

    public void deleteWorkout() {
        if (workout == null) {
            return;
        }
        workoutRepository.delete(workout.getWorkoutId());
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
        exerciseRepository.save(workout.getWorkoutId(), deletedExerciseIndex, deletedExercise);
        deletedExerciseIndex = null;
        deletedExercise = null;
        setUnsavedChanges(false);
    }

    public void undoDeleteWorkout() {
        workout = deletedWorkout;
        workoutRepository.save(deletedWorkout);
        deletedWorkout = null;
        setUnsavedChanges(false);
    }

    public void deleteExercise(Exercise exercise) {
        exerciseRepository.delete(exercise.getExerciseId());
        int index = workout.getExercises().indexOf(exercise);
        if (workout.deleteExercise(exercise)) {
            setUnsavedChanges(index, exercise);
            deletedWorkout = null;
        }
    }

    public void replaceExercise(int position, Exercise exercise) {
        workout.replaceExercise(exercise);
        exerciseRepository.save(workout.getWorkoutId(), position, exercise);
        setUnsavedChanges(false);
    }

    public void createExercise() {
        workout.addExercise();
        workoutRepository.save(workout);
        setUnsavedChanges(false);
    }

    public void createExerciseBefore(Exercise exercise) {
        //TODO: move to Workout
        Exercise newExercise = new BasicExercise();
        List<Exercise> exercises = workout.getExercises();
        workout.addExercise(exercises.indexOf(exercise), newExercise);
        workoutRepository.save(workout);
        setUnsavedChanges(false);
    }

    public void createExerciseAfter(Exercise exercise) {
        //TODO: move to Workout
        Exercise newExercise = new BasicExercise();
        List<Exercise> exercises = workout.getExercises();
        workout.addExerciseAfter(exercises.indexOf(exercise), newExercise);
        workoutRepository.save(workout);
        setUnsavedChanges(false);
    }

    public boolean hasDeletedWorkout() {
        return deletedWorkout != null;
    }

    public boolean hasDeletedExercise() {
        return deletedExercise != null;
    }

    public void changeName(String name) {
        workout.setName(name);
        workoutRepository.save(workout);
        setUnsavedChanges(false);
    }

    public void replaceSets(Exercise exercise, List<Set> setToAdd) {
        List<Set> setsToRemove = new ArrayList<>(exercise.getSets());
        for (Set set : setsToRemove) {
            setRepository.delete(set.getSetId());
            exercise.removeSet(set);
        }
        for (Set set : setToAdd) {
            setRepository.save(exercise.getExerciseId(), set);
            exercise.addSet(set);
        }
        setUnsavedChanges(false);
    }
}
