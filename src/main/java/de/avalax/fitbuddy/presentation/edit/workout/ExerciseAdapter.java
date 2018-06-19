package de.avalax.fitbuddy.presentation.edit.workout;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Set;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.databinding.ExerciseItemBinding;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.exercise.Exercises;
import de.avalax.fitbuddy.presentation.edit.SelectableViewHolder;

import static android.graphics.Color.TRANSPARENT;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>
        implements SelectableViewHolder.SelectionListener {
    private ExerciseViewHolderCallback callback;
    private Set<Exercise> selections;

    private Exercises exercises;

    ExerciseAdapter(ExerciseViewHolderCallback callback) {
        super();
        this.callback = callback;
        this.selections = new ArraySet<>();
    }

    public void setExercises(Exercises exercises) {
        notifyItemRangeRemoved(0, this.exercises == null ? 0 : this.exercises.size());
        this.exercises = exercises;
        notifyItemRangeInserted(0, exercises.size());
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExerciseItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.exercise_item,
                        parent, false);
        binding.setCallback(callback);
        int highlightColor = parent.getResources().getColor(R.color.primaryLightColor);
        ExerciseViewHolder exerciseViewHolder =
                new ExerciseViewHolder(binding, callback, selections, TRANSPARENT, highlightColor);
        exerciseViewHolder.setSelectionListener(this);
        return exerciseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        try {
            Exercise exercise = exercises.get(position);
            holder.getBinding().setExercise(exercise);
            holder.getBinding().executePendingBindings();
            holder.setSelected(selections.contains(exercise));
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

    public void clearSelections() {
        selections.clear();
    }

    @Override
    public void onSelect(SelectableViewHolder selectableViewHolder, boolean selected) {
        try {
            Exercise exercise = exercises.get(selectableViewHolder.getAdapterPosition());
            if (selected) {
                selections.add(exercise);
            } else {
                selections.remove(exercise);
            }
        } catch (ExerciseException e) {
            Log.e("ExerciseException", e.getMessage(), e);
        }
    }

    public Set<Exercise> getSelections() {
        return selections;
    }

    public static class ExerciseViewHolder extends SelectableViewHolder {
        private final ExerciseItemBinding binding;
        private final ExerciseViewHolderCallback callback;
        private final Set<Exercise> selections;

        ExerciseViewHolder(ExerciseItemBinding binding,
                           ExerciseViewHolderCallback callback,
                           Set<Exercise> selections,
                           int backgroundColor,
                           int highlightColor) {
            super(binding.getRoot(), backgroundColor, highlightColor);
            this.selections = selections;
            this.binding = binding;
            this.callback = callback;
            binding.getRoot().setOnClickListener(this);
            binding.getRoot().setOnLongClickListener(this);
        }

        ExerciseItemBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View view) {
            if (selections.isEmpty()) {
                callback.onItemClick(view, getAdapterPosition());
            } else {
                setSelected(!isSelected());
                callback.onSelectionChange(selections.size());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            setSelected(true);
            callback.onSelectionChange(selections.size());
            return true;
        }
    }

    public interface ExerciseViewHolderCallback {
        void onItemClick(View view, int position);

        void onSelectionChange(int size);
    }
}