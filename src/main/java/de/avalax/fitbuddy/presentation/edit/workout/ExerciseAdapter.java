package de.avalax.fitbuddy.presentation.edit.workout;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;
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
        this.callback = callback;
        this.selections = new ArraySet<>();
    }

    public void setExercises(Exercises exercises) {
        if (this.exercises == null) {
            this.exercises = exercises;
            notifyItemRangeInserted(0, exercises.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return ExerciseAdapter.this.exercises.size();
                }

                @Override
                public int getNewListSize() {
                    return exercises.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    try {
                        Exercise old = ExerciseAdapter.this.exercises.get(oldItemPosition);
                        Exercise exercise = exercises.get(newItemPosition);
                        return old.getExerciseId() == exercise.getExerciseId();
                    } catch (ExerciseException e) {
                        Log.e("ExerciseException", e.getMessage(), e);
                        return false;
                    }
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    try {
                        Exercise old = ExerciseAdapter.this.exercises.get(oldItemPosition);
                        Exercise exercise = exercises.get(newItemPosition);
                        return old.getExerciseId() == exercise.getExerciseId()
                                && Objects.equals(old.getName(), exercise.getName())
                                && Objects.equals(old.getSets(), exercise.getSets());
                    } catch (ExerciseException e) {
                        Log.e("ExerciseException", e.getMessage(), e);
                        return false;
                    }
                }
            });

            diffResult.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExerciseItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.exercise_item,
                        parent, false);
        binding.setCallback(callback);
        int highlightColor = parent.getResources().getColor(R.color.primaryLightColor);
        ExerciseViewHolder exerciseViewHolder = new ExerciseViewHolder(binding, TRANSPARENT, highlightColor);
        exerciseViewHolder.setSelectionListener(this);
        return exerciseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        try {
            Exercise exercise = exercises.get(position);
            holder.binding.setExercise(exercise);
            holder.binding.executePendingBindings();
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

    void removeSelections() {
        for (Exercise selection : selections) {
            exercises.remove(selection);
        }
        selections.clear();
        notifyDataSetChanged();
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

    public class ExerciseViewHolder extends SelectableViewHolder {
        private final ExerciseItemBinding binding;

        public ExerciseViewHolder(ExerciseItemBinding binding, int backgroundColor, int highlightColor) {
            super(binding.getRoot(), backgroundColor, highlightColor);
            binding.getRoot().setOnClickListener(this);
            binding.getRoot().setOnLongClickListener(this);
            this.binding = binding;
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