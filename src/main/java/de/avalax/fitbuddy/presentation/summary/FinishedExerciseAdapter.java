package de.avalax.fitbuddy.presentation.summary;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.finished_exercise.FinishedExercise;
import de.avalax.fitbuddy.presentation.edit.SelectableViewHolder;

import static android.graphics.Color.TRANSPARENT;

public class FinishedExerciseAdapter
        extends RecyclerView.Adapter<FinishedExerciseAdapter.ExerciseViewHolder> {
    private List<FinishedExercise> finishedExercises;
    private Activity activity;

    public FinishedExerciseAdapter(Activity activity, List<FinishedExercise> finishedExercises) {
        super();
        this.activity = activity;
        this.finishedExercises = finishedExercises;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.card_finished_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        FinishedExercise finishedExercise = finishedExercises.get(position);
        String title = finishedExercise.getName();
        holder.getTitleTextView().setText(title);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return finishedExercises.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;

        ExerciseViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.item_title);
        }

        TextView getTitleTextView() {
            return titleTextView;
        }
    }
}
