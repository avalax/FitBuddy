package de.avalax.fitbuddy.presentation.summary;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.presentation.MainActivity;
import de.avalax.fitbuddy.presentation.edit.SelectableViewHolder;

import static de.avalax.fitbuddy.presentation.RequestCodes.FINISHED_WORKOUT_DETAILS;
import static de.avalax.fitbuddy.presentation.summary.FinishedWorkoutDetailFragment.ARGS_FINISHED_WORKOUT;

public class FinishedWorkoutAdapter
        extends RecyclerView.Adapter<FinishedWorkoutAdapter.ViewHolder> {
    private MainActivity activity;
    private List<FinishedWorkout> finishedWorkouts;
    private FinishedWorkoutViewHelper finishedWorkoutViewHelper;

    FinishedWorkoutAdapter(MainActivity activity,
                           FinishedWorkoutViewHelper finishedWorkoutViewHelper,
                           List<FinishedWorkout> finishedWorkouts) {
        super();
        this.activity = activity;
        this.finishedWorkoutViewHelper = finishedWorkoutViewHelper;
        this.finishedWorkouts = finishedWorkouts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_finished_workout, parent, false);
        int backgroundColor = view.getResources().getColor(R.color.cardsColor);
        int highlightColor = view.getResources().getColor(R.color.primaryLightColor);
        return new ViewHolder(view, backgroundColor, highlightColor);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FinishedWorkout workout = finishedWorkouts.get(position);
        holder.getTitleTextView().setText(workout.getName());
        holder.getSubtitleTextView().setText(finishedWorkoutViewHelper.creationDate(workout));
        holder.getView().setOnClickListener(view -> {
            Intent intent = new Intent(activity, FinishedWorkoutDetailActivity.class);
            intent.putExtra(ARGS_FINISHED_WORKOUT, workout);
            intent.putExtra("position", holder.getAdapterPosition());
            activity.startActivityForResult(intent, FINISHED_WORKOUT_DETAILS);
        });
        holder.getView().setOnLongClickListener(view -> {
            activity.updateEditToolbar(position, workout);
            return true;
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return finishedWorkouts.size();
    }

    static class ViewHolder extends SelectableViewHolder {
        private final TextView titleTextView;
        private final TextView subtitleTextView;

        ViewHolder(View view, int backgroundColor, int highlightColor) {
            super(view, backgroundColor, highlightColor);
            titleTextView = view.findViewById(R.id.item_title);
            subtitleTextView = view.findViewById(R.id.item_subtitle);
        }

        TextView getTitleTextView() {
            return titleTextView;
        }

        TextView getSubtitleTextView() {
            return subtitleTextView;
        }
    }
}