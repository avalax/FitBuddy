package de.avalax.fitbuddy.presentation.edit.workout;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Set;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.exercise.Exercises;
import de.avalax.fitbuddy.presentation.edit.SelectableViewHolder;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity;

import static de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity.EDIT_EXERCISE;

public class ExerciseAdapter extends RecyclerView.Adapter<SelectableViewHolder> {
    private Exercises exercises;
    private EditWorkoutApplicationService editWorkoutApplicationService;
    private Activity activity;
    private Set<Exercise> selections;

    ExerciseAdapter(Activity activity,
                    Exercises exercises,
                    EditWorkoutApplicationService editWorkoutApplicationService) {
        super();
        this.activity = activity;
        this.exercises = exercises;
        this.editWorkoutApplicationService = editWorkoutApplicationService;
        this.selections = new ArraySet<>();
    }

    @Override
    public SelectableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_exercise, parent, false);
        return new SelectableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectableViewHolder holder, int position) {
        try {
            Exercise exercise = exercises.get(position);
            String title = editWorkoutApplicationService.title(exercise);
            String subtitle = editWorkoutApplicationService.subtitle(exercise);

            holder.getTitleTextView().setText(title);
            holder.getSubtitleTextView().setText(subtitle);
            holder.getView().setOnClickListener(view -> {
                if (selections.isEmpty()) {
                    Intent intent = new Intent(activity, EditExerciseActivity.class);
                    intent.putExtra("exercise", exercise);
                    intent.putExtra("position", holder.getAdapterPosition());
                    activity.startActivityForResult(intent, EDIT_EXERCISE);
                } else {
                    if (isSelected(holder)) {
                        deselect(holder);
                    } else {
                        select(holder);
                    }
                }
            });
            holder.getView().setOnLongClickListener(view -> {
                select(holder);
                return true;
            });
        } catch (ExerciseException e) {
            Log.e("ExerciseException", e.getMessage(), e);
        }
    }

    private boolean isSelected(SelectableViewHolder holder) {
        boolean isSelected = false;
        try {
            isSelected = selections.contains(exercises.get(holder.getAdapterPosition()));

        } catch (ExerciseException e) {
            Log.e("ExerciseException", e.getMessage(), e);
        }
        return isSelected;
    }

    private void deselect(SelectableViewHolder holder) {
        try {
            selections.remove(exercises.get(holder.getAdapterPosition()));
            holder.deselect();
            ((EditWorkoutActivity) activity).updateToolbar(selections.size());
        } catch (ExerciseException e) {
            Log.e("ExerciseException", e.getMessage(), e);
        }
    }

    private void select(SelectableViewHolder holder) {
        try {
            selections.add(exercises.get(holder.getAdapterPosition()));
            holder.select();
            ((EditWorkoutActivity) activity).updateToolbar(selections.size());
        } catch (ExerciseException e) {
            Log.e("ExerciseException", e.getMessage(), e);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return exercises == null ? 0 : exercises.size();
    }

    public void removeSelections() {
        exercises.removeAll(selections);
        selections.clear();
        notifyDataSetChanged();
    }
}