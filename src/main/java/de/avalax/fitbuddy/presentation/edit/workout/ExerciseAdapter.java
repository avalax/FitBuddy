package de.avalax.fitbuddy.presentation.edit.workout;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Set;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.exercise.Exercises;
import de.avalax.fitbuddy.presentation.edit.SelectableViewHolder;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity;

import static android.graphics.Color.TRANSPARENT;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.EDIT_EXERCISE;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private Exercises exercises;
    private EditWorkoutViewHelper editWorkoutViewHelper;
    private Activity activity;
    private Set<Exercise> selections;

    ExerciseAdapter(Activity activity,
                    Exercises exercises,
                    EditWorkoutViewHelper editWorkoutViewHelper) {
        super();
        this.activity = activity;
        this.exercises = exercises;
        this.editWorkoutViewHelper = editWorkoutViewHelper;
        this.selections = new ArraySet<>();
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_exercise, parent, false);
        int highlightColor = view.getResources().getColor(R.color.primaryLightColor);
        return new ExerciseViewHolder(view, TRANSPARENT, highlightColor);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        try {
            Exercise exercise = exercises.get(position);
            String title = editWorkoutViewHelper.title(exercise);
            String subtitle = editWorkoutViewHelper.subtitle(exercise);
            String weight = editWorkoutViewHelper.weight(exercise);

            holder.getTitleTextView().setText(title);
            holder.getSubtitleTextView().setText(subtitle);
            holder.getWeightTextView().setText(weight);
            holder.setSelected(selections.contains(exercise));

            holder.getView().setOnClickListener(view -> {
                if (selections.isEmpty()) {
                    Intent intent = new Intent(activity, EditExerciseActivity.class);
                    intent.putExtra("exercise", exercise);
                    intent.putExtra("position", holder.getAdapterPosition());
                    activity.startActivityForResult(intent, EDIT_EXERCISE);
                } else {
                    if (isSelected(exercise)) {
                        deselect(holder.getAdapterPosition(), exercise);
                    } else {
                        select(holder.getAdapterPosition(), exercise);
                    }
                }
            });
            holder.getView().setOnLongClickListener(view -> {
                select(holder.getAdapterPosition(), exercise);
                return true;
            });
        } catch (ExerciseException e) {
            Log.e("ExerciseException", e.getMessage(), e);
        }
    }

    private boolean isSelected(Exercise exercise) {
        return selections.contains(exercise);
    }

    private void deselect(int position, Exercise exercise) {
        selections.remove(exercise);
        notifyItemChanged(position);
        ((EditWorkoutActivity) activity).updateToolbar(selections.size());
    }

    private void select(int position, Exercise exercise) {
        selections.add(exercise);
        notifyItemChanged(position);
        ((EditWorkoutActivity) activity).updateToolbar(selections.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return exercises == null ? 0 : exercises.size();
    }

    void removeSelections() {
        for (Exercise exercise : selections) {
            exercises.remove(exercise);
        }
        selections.clear();
        notifyDataSetChanged();
    }

    static class ExerciseViewHolder extends SelectableViewHolder {
        private final TextView weightTextView;
        private final TextView titleTextView;
        private final TextView subtitleTextView;

        ExerciseViewHolder(View view, int backgroundColor, int highlightColor) {
            super(view, backgroundColor, highlightColor);
            weightTextView = view.findViewById(R.id.item_weight);
            titleTextView = view.findViewById(R.id.item_title);
            subtitleTextView = view.findViewById(R.id.item_subtitle);
        }

        TextView getWeightTextView() {
            return weightTextView;
        }

        TextView getTitleTextView() {
            return titleTextView;
        }

        TextView getSubtitleTextView() {
            return subtitleTextView;
        }
    }
}