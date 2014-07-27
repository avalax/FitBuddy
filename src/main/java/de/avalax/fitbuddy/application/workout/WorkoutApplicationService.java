package de.avalax.fitbuddy.application.workout;

import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseRepository;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.domain.model.workout.WorkoutNotFoundException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;

import java.util.List;

public class WorkoutApplicationService {
    public static final String WORKOUT_ID_SHARED_KEY = "lastWorkoutId";

    private ExerciseRepository exerciseRepository;
    private WorkoutRepository workoutRepository;

    public WorkoutApplicationService(ExerciseRepository exerciseRepository, WorkoutRepository workoutRepository) {
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
    }

    public int countOfExercises(String workoutId) {
        List<Exercise> exercises = exerciseRepository.allExercisesBelongsTo(new WorkoutId(workoutId));
        return exercises.size();
    }

    public Exercise exerciseFromPosition(String workoutId, int position) throws ExerciseNotFoundException {
        //TODO: set currentset and reps after loading from repo
        return exerciseRepository.loadExerciseFromWorkoutWithPosition(new WorkoutId(workoutId), position);
    }

    public Workout requestWorkout(String workoutId) throws WorkoutNotFoundException {
        return workoutRepository.load(new WorkoutId(workoutId));
    }
}
