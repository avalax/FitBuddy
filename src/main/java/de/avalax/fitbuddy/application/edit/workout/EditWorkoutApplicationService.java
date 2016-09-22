package de.avalax.fitbuddy.application.edit.workout;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.avalax.fitbuddy.application.workout.WorkoutSession;
import de.avalax.fitbuddy.domain.model.ResourceException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkoutRepository;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetException;
import de.avalax.fitbuddy.domain.model.set.SetRepository;
import de.avalax.fitbuddy.domain.model.workout.BasicWorkout;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutListEntry;
import de.avalax.fitbuddy.domain.model.workout.WorkoutParseException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.domain.model.workout.WorkoutService;

public class EditWorkoutApplicationService {

    private FinishedWorkoutRepository finishedWorkoutRepository;

    private WorkoutRepository workoutRepository;

    private ExerciseRepository exerciseRepository;

    private SetRepository setRepository;

    private WorkoutService workoutService;

    private WorkoutSession workoutSession;

    private boolean unsavedChanges;

    private Workout workout;

    private Workout deletedWorkout;

    private Map<Integer, Exercise> deletedExercises;

    public EditWorkoutApplicationService(
            WorkoutSession workoutSession,
            FinishedWorkoutRepository finishedWorkoutRepository,
            WorkoutRepository workoutRepository,
            ExerciseRepository exerciseRepository,
            SetRepository setRepository,
            WorkoutService workoutService) {
        this.workoutSession = workoutSession;
        this.finishedWorkoutRepository = finishedWorkoutRepository;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.setRepository = setRepository;
        this.workoutService = workoutService;
        this.deletedExercises = new TreeMap<>();
    }

    private void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    public boolean hasUnsavedChanges() {
        return unsavedChanges;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(WorkoutId id) throws WorkoutException {
        this.workout = workoutRepository.load(id);
    }

    public void switchWorkout() throws WorkoutException {
        if (workoutSession.hasWorkout()) {
            Workout workoutToSave = workoutSession.getWorkout();
            finishedWorkoutRepository.saveWorkout(workoutToSave);
        }
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
        workout = workoutFromJson;
        workoutRepository.save(workoutFromJson);
        setUnsavedChanges(false);
    }

    public void deleteWorkout() {
        if (workout == null) {
            return;
        }
        workoutRepository.delete(workout.getWorkoutId());
        deletedExercises.clear();
        setUnsavedChanges(workout);
        workout = null;
    }

    private void setUnsavedChanges(Workout workout) {
        deletedWorkout = workout;
        setUnsavedChanges(true);
    }

    private void setUnsavedChanges(int index, Exercise exercise) {
        deletedExercises.put(index, exercise);
        setUnsavedChanges(true);
    }

    public void undoDeleteExercise() {
        for (Map.Entry<Integer, Exercise> deletedExercise : deletedExercises.entrySet()) {
            int index = deletedExercise.getKey();
            Exercise exercise = deletedExercise.getValue();
            workout.addExercise(index, exercise);
            exerciseRepository.save(workout.getWorkoutId(), index, exercise);
        }
        deletedExercises.clear();
        setUnsavedChanges(false);
    }

    public void undoDeleteWorkout() {
        workout = deletedWorkout;
        workoutRepository.save(deletedWorkout);
        deletedWorkout = null;
        setUnsavedChanges(false);
    }

    public void deleteExercise(Collection<Integer> positions) throws ExerciseException {
        for (Integer position : positions) {
            Exercise exercise = workout.exerciseAtPosition(position);
            exerciseRepository.delete(exercise.getExerciseId());
            workout.deleteExercise(exercise);
            setUnsavedChanges(position, exercise);
            deletedWorkout = null;
        }
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

    public boolean hasDeletedWorkout() {
        return deletedWorkout != null;
    }

    public boolean hasDeletedExercise() {
        return !deletedExercises.isEmpty();
    }

    public void changeName(String name) {
        workout.setName(name);
        workoutRepository.save(workout);
        setUnsavedChanges(false);
    }

    public void changeSetAmount(Exercise exercise, int amount) throws ResourceException {
        int countOfSets = exercise.countOfSets();
        if (amount == countOfSets) {
            return;
        }
        if (amount < countOfSets) {
            for (int i = 0; i < countOfSets - amount; i++) {
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
            } catch (SetException e) {
                weight = 0;
                maxReps = 0;
            }
            for (int i = 0; i < amount - countOfSets; i++) {
                Set set = exercise.createSet();
                set.setMaxReps(maxReps);
                set.setWeight(weight);
                setRepository.save(exercise.getExerciseId(), set);
            }
        }
        setUnsavedChanges(false);
    }


    public void moveExerciseAtPositionUp(int position) throws ResourceException {
        if (workout.moveExerciseAtPositionUp(position)) {
            workoutRepository.save(workout);
        }
        setUnsavedChanges(false);
    }

    public void moveExerciseAtPositionDown(int position) throws ResourceException {
        if (workout.moveExerciseAtPositionDown(position)) {
            workoutRepository.save(workout);
        }
        setUnsavedChanges(false);
    }
}