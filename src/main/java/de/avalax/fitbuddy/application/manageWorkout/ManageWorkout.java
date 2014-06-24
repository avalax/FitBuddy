package de.avalax.fitbuddy.application.manageWorkout;

import android.view.View;
import de.avalax.fitbuddy.application.ExerciseFactory;
import de.avalax.fitbuddy.application.WorkoutFactory;
import de.avalax.fitbuddy.application.WorkoutSession;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutListEntry;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

import java.util.List;

public class ManageWorkout {

    private WorkoutFactory workoutFactory;

    private ExerciseFactory exerciseFactory;

    private WorkoutRepository workoutRepository;

    private ExerciseRepository exerciseRepository;

    private WorkoutSession workoutSession;

    private boolean unsavedChanges;

    private Workout workout;
    private Workout deletedWorkout;
    private Exercise deletedExercise;
    private Integer deletedExerciseIndex;

    public ManageWorkout(WorkoutSession workoutSession, WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, WorkoutFactory workoutFactory, ExerciseFactory exerciseFactory) {
        this.workoutSession = workoutSession;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
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
        this.workout = workoutRepository.load(id);
    }

    public void switchWorkout() {
        workoutSession.switchWorkout(workout.getWorkoutId());
    }

    public List<WorkoutListEntry> getWorkoutList() {
        return workoutRepository.getWorkoutList();
    }

    public void createWorkout() {
        workout = workoutFactory.createNew();
        workoutRepository.save(workout);
    }

    public void createWorkoutFromJson(String json) {
        Workout workoutFromJson = workoutFactory.createFromJson(json);
        if (workoutFromJson != null) {
            workout = workoutFromJson;
            workoutRepository.save(workoutFromJson);
            unsavedChanges = false;
        }
    }

    public void deleteWorkout() {
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
        Exercise exercise = exerciseFactory.createNew();
        workout.addExercise(exercise);
        exerciseRepository.save(workout.getWorkoutId(), workout.getExerciseCount() - 1, exercise);
    }

    public void createExerciseBefore(Exercise exercise) {
        Exercise newExercise = exerciseFactory.createNew();
        List<Exercise> exercises = workout.getExercises();
        workout.addExercise(exercises.indexOf(exercise), newExercise);
        exerciseRepository.save(workout.getWorkoutId(), exercises.indexOf(newExercise), newExercise);
    }

    public void createExerciseAfter(Exercise exercise) {
        Exercise newExercise = exerciseFactory.createNew();
        List<Exercise> exercises = workout.getExercises();
        workout.addExerciseAfter(exercises.indexOf(exercise), newExercise);
        exerciseRepository.save(workout.getWorkoutId(), exercises.indexOf(newExercise), newExercise);
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
    }
}
