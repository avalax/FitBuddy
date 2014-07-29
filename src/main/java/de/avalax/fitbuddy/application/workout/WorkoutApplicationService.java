package de.avalax.fitbuddy.application.workout;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseId;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.set.SetId;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutNotFoundException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

import java.util.List;

public class WorkoutApplicationService {
    public static final String WORKOUT_ID_SHARED_KEY = "lastWorkoutId";

    private ExerciseRepository exerciseRepository;
    private WorkoutRepository workoutRepository;
    private WorkoutSession workoutSession;

    public WorkoutApplicationService(ExerciseRepository exerciseRepository, WorkoutRepository workoutRepository, WorkoutSession workoutSession) {
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
        this.workoutSession = workoutSession;
    }

    public int countOfExercises(String workoutId) {
        List<Exercise> exercises = exerciseRepository.allExercisesBelongsTo(new WorkoutId(workoutId));
        return exercises.size();
    }

    public Exercise exerciseFromPosition(String workoutId, int position) throws ExerciseNotFoundException {
        Exercise exercise = exerciseRepository.loadExerciseFromWorkoutWithPosition(new WorkoutId(workoutId), position);
        Integer index = workoutSession.selectedSetOfExercise(exercise.getExerciseId());
        if (exercise.getSets().size() > index) {
            exercise.setCurrentSet(index);
            exercise.getCurrentSet().setReps(workoutSession.repsForSet(exercise.getCurrentSet().getSetId()));
        }
        return exercise;
    }

    public Workout requestWorkout(String workoutId) throws WorkoutNotFoundException {
        return workoutRepository.load(new WorkoutId(workoutId));
    }

    public void setSelectedSetOfExercise(ExerciseId exerciseId, Integer position) {
        workoutSession.setSelectedSetOfExercise(exerciseId, position);
    }

    public void setRepsOfSet(SetId setId, int reps) {
        workoutSession.setRepsOfSet(setId, reps);
    }

    public void setSelectedExercise(int selectedExercise) {
        workoutSession.setSelectedExercise(selectedExercise);
    }

    public int selectedExercise() {
        return workoutSession.selectedExercise();
    }
}
