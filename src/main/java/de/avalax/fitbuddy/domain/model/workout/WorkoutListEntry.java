package de.avalax.fitbuddy.domain.model.workout;

@Deprecated
// Use Workout instead
public class WorkoutListEntry {
    private final WorkoutId workoutId;

    private final String name;

    public WorkoutListEntry(WorkoutId workoutId, String name) {
        this.workoutId = workoutId;
        this.name = name;
    }

    public WorkoutId getWorkoutId() {
        return workoutId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (name.length() == 0) {
            return "unnamed workout";
        }
        return name;
    }
}
