package de.avalax.fitbuddy.presentation.welcome_screen;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.workout.WorkoutService;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.MainActivity;
import de.avalax.fitbuddy.presentation.edit.SelectableViewHolder;
import de.avalax.fitbuddy.presentation.summary.FinishedWorkoutViewHelper;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private WorkoutService workoutService;
    private List<Workout> workouts;
    private MainActivity activity;
    private FinishedWorkoutViewHelper finishedWorkoutViewHelper;
    private int selectedPosition = RecyclerView.NO_POSITION;

    WorkoutAdapter(MainActivity activity,
                   WorkoutService workoutService,
                   FinishedWorkoutViewHelper finishedWorkoutViewHelper,
                   List<Workout> workouts) {
        super();
        this.activity = activity;
        this.workoutService = workoutService;
        this.finishedWorkoutViewHelper = finishedWorkoutViewHelper;
        this.workouts = workouts;
    }

    @Override
    public WorkoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_workout, parent, false);
        int backgroundColor = view.getResources().getColor(R.color.cardsColor);
        int highlightColor = view.getResources().getColor(R.color.primaryLightColor);
        return new WorkoutAdapter.ViewHolder(view, backgroundColor, highlightColor);
    }

    @Override
    public void onBindViewHolder(WorkoutAdapter.ViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.getTitleTextView().setText(workout.getName());
        holder.getSubtitleTextView().setText(finishedWorkoutViewHelper.executions(workout.getWorkoutId()));
        holder.getDateTextView().setText(finishedWorkoutViewHelper.lastExecutionDate(workout.getWorkoutId()));
        holder.setSelected(selectedPosition == position);
        if (workoutService.isActiveWorkout(workout)) {
            holder.getStatusTextView().setText(R.string.workout_active);
        } else {
            holder.getStatusTextView().setText(R.string.workout_not_active);
        }
        holder.getView().setOnClickListener(view -> {
            if (holder.isSelected()) {
                selectedPosition = RecyclerView.NO_POSITION;
                notifyItemChanged(holder.getAdapterPosition());
                activity.mainToolbar();
            } else {
                activity.selectWorkout(workout);
            }
        });
        holder.getView().setOnLongClickListener(view -> {
            int oldSelectedPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            if (oldSelectedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(oldSelectedPosition);
            }
            notifyItemChanged(holder.getAdapterPosition());

            activity.updateEditToolbar(holder.getAdapterPosition(), workout);
            return true;
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return workouts == null ? 0 : workouts.size();
    }

    void removeSelection() {
        int oldSelectedPosition = selectedPosition;
        selectedPosition = RecyclerView.NO_POSITION;
        notifyItemChanged(oldSelectedPosition);
    }

    static class ViewHolder extends SelectableViewHolder {
        private final TextView dateTextView;
        private final TextView statusTextView;
        private final CardView cardView;
        private final TextView titleTextView;
        private final TextView subtitleTextView;

        ViewHolder(View view, int backgroundColor, int highlightColor) {
            super(view, backgroundColor, highlightColor);
            cardView = view.findViewById(R.id.card);
            titleTextView = view.findViewById(R.id.item_title);
            subtitleTextView = view.findViewById(R.id.item_subtitle);
            dateTextView = view.findViewById(R.id.card_date);
            statusTextView = view.findViewById(R.id.card_status);
        }

        TextView getDateTextView() {
            return dateTextView;
        }

        TextView getStatusTextView() {
            return statusTextView;
        }

        TextView getTitleTextView() {
            return titleTextView;
        }

        TextView getSubtitleTextView() {
            return subtitleTextView;
        }

        public View getView() {
            return cardView;
        }
    }
}
