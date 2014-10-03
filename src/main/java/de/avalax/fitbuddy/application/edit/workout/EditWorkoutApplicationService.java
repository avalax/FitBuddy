package de.avalax.fitbuddy.application.edit.workout;

import android.view.View;
import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.RessourceNotFoundException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetNotFoundException;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.*;

import java.io.IOException;
import java.util.List;

public class EditWorkoutApplicationService {

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

    public EditWorkoutApplicationService(WorkoutSession workoutSession, WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, SetRepository setRepository, WorkoutService workoutService) {
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

    public void switchWorkout() throws IOException {
        workoutSession.switchWorkout(workout);
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

    public void deleteExercise(int position) throws ExerciseNotFoundException {
        Exercise exercise = workout.exerciseAtPosition(position);
        exerciseRepository.delete(exercise.getExerciseId());
        workout.deleteExercise(exercise);
        setUnsavedChanges(position, exercise);
        deletedWorkout = null;
    }

    public void saveExercise(Exercise exercise, int position) {
        workout.replaceExercise(exercise);
        exerciseRepository.save(workout.getWorkoutId(), position, exercise);
        setUnsavedChanges(false);
    }

    public void createExercise() {
        Exercise exercise = workout.createExercise();
        exerciseRepository.save(workout.getWorkoutId(), workout.countOfExercises() - 1, exercise);
        setUnsavedChanges(false);
    }

    public void createExerciseAfterPosition(int position) {
        workout.createExercise(position + 1);
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

    public void changeSetAmount(Exercise exercise, int newSetAmount) throws RessourceNotFoundException {
        int countOfSets = exercise.countOfSets();
        if (newSetAmount == countOfSets) {
            return;
        }
        if (newSetAmount < countOfSets) {
            for (int i = 0; i < countOfSets - newSetAmount; i++) {
                Set set = exercise.setAtPosition(i);
                exercise.removeSet(set);
                setRepository.delete(set.getSetId());
            }
        } else {
            double weight;
            int maxReps;
            try {
                int indexOfCurrentSet = exercise.indexOfCurrentSet();
                Set set = exercise.setAtPosition(indexOfCurrentSet);
                weight = set.getWeight();
                maxReps = set.getMaxReps();
            } catch (SetNotFoundException e) {
                weight = 0;
                maxReps = 0;
            }
            for (int i = 0; i < newSetAmount - countOfSets; i++) {
                Set set = exercise.createSet();
                set.setMaxReps(maxReps);
                set.setWeight(weight);
                setRepository.save(exercise.getExerciseId(), set);
            }
        }
        setUnsavedChanges(false);
    }

    public void moveExerciseAtPositionUp(int position) throws RessourceNotFoundException {
        //TODO: move to workout
        if (position <= 0) {
            return;
        }
        Exercise exercise = workout.exerciseAtPosition(position);
        workout.deleteExercise(exercise);
        workout.addExercise(position - 1, exercise);

        workoutRepository.save(workout);
        setUnsavedChanges(false);
    }

    public void moveExerciseAtPositionDown(int position) throws RessourceNotFoundException {
        //TODO: move to workout
        if (position + 1 >= workout.countOfExercises()) {
            return;
        }
        Exercise exercise = workout.exerciseAtPosition(position);
        workout.deleteExercise(exercise);
        workout.addExercise(position + 1, exercise);

        workoutRepository.save(workout);
        setUnsavedChanges(false);
    }
}