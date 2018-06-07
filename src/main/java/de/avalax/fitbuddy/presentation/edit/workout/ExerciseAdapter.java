package de.avalax.fitbuddy.presentation.edit.workout;

import android.app.Activity;
import android.support.annotation.NonNull;
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

import static android.graphics.Color.TRANSPARENT;
import static de.avalax.fitbuddy.presentation.edit.workout.ExerciseBindingAdapter.setTitleFromExercise;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> implements SelectableViewHolder.SelectionListener {
    private ItemClickListener clickListener;
    private Exercises exercises;
    private Activity activity;
    private Set<Exercise> selections;

    ExerciseAdapter(Activity activity,
                    Exercises exercises,
                    ItemClickListener clickListener) {
        super();
        this.activity = activity;
        this.exercises = exercises;
        this.clickListener = clickListener;
        this.selections = new ArraySet<>();
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_exercise, parent, false);
        int highlightColor = view.getResources().getColor(R.color.primaryLightColor);
        ExerciseViewHolder exerciseViewHolder = new ExerciseViewHolder(view, TRANSPARENT, highlightColor);
        exerciseViewHolder.setSelectionListener(this);
        return exerciseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        try {
            Exercise exercise = exercises.get(position);

            setTitleFromExercise(holder.getTitleTextView(), exercise);
            ExerciseBindingAdapter.setRepsFromExercise(holder.getSubtitleTextView(), exercise);
            ExerciseBindingAdapter.setWeightFromExercise(holder.getWeightTextView(), exercise);

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
        private final TextView weightTextView;
        private final TextView titleTextView;
        private final TextView subtitleTextView;

        ExerciseViewHolder(View view, int backgroundColor, int highlightColor) {
            super(view, backgroundColor, highlightColor);
            weightTextView = view.findViewById(R.id.item_weight);
            titleTextView = view.findViewById(R.id.item_title);
            subtitleTextView = view.findViewById(R.id.item_subtitle);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
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

        @Override
        public void onClick(View view) {
            if (selections.isEmpty()) {
                clickListener.onItemClick(view, getAdapterPosition());
            } else {
                setSelected(!isSelected());
                ((EditWorkoutActivity) activity).updateToolbar(selections.size());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            setSelected(true);
            ((EditWorkoutActivity) activity).updateToolbar(selections.size());
            return true;
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}