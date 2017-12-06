package de.avalax.fitbuddy.presentation.welcome_screen;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.MainActivity;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private EditWorkoutApplicationService workoutSession;
    private List<Workout> workouts;
    private MainActivity activity;
    private int selectedPosition = RecyclerView.NO_POSITION;

    WorkoutAdapter(MainActivity activity, EditWorkoutApplicationService workoutSession, List<Workout> workouts) {
        super();
        this.activity = activity;
        this.workoutSession = workoutSession;
        this.workouts = workouts;
    }

    @Override
    public WorkoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_workout, parent, false);
        return new WorkoutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkoutAdapter.ViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.getTitleTextView().setText(workout.getName());
        holder.getSubtitleTextView().setText("Executed 0 times");
        holder.setSelected(selectedPosition == position);
        if (workoutSession.isActiveWorkout(workout)) {
            holder.getStatusTextView().setText(R.string.workout_active);
        } else {
            holder.getStatusTextView().setText(R.string.workout_not_active);
        }
        holder.getView().setOnClickListener(view -> {
            activity.selectWorkout(workout);
        });
        holder.getView().setOnLongClickListener(view -> {
            int oldSelectedPosition = selectedPosition;
            selectedPosition = position;
            if (oldSelectedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(oldSelectedPosition);
            }
            notifyItemChanged(position);

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
        return workouts == null ? 0 : workouts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView dateTextView;
        private final TextView subtitleTextView;
        private final TextView statusTextView;
        private final CardView cardView;

        ViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.card);
            titleTextView = v.findViewById(R.id.card_title);
            subtitleTextView = v.findViewById(R.id.card_subtitle);
            dateTextView = v.findViewById(R.id.card_date);
            statusTextView = v.findViewById(R.id.card_status);
        }

        TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }

        public TextView getSubtitleTextView() {
            return subtitleTextView;
        }

        public View getView() {
            return itemView;
        }

        public TextView getStatusTextView() {
            return statusTextView;
        }


        public void setSelected(boolean selected) {
            int backgroundColor = itemView.getResources().getColor(R.color.cardsColor);
            if (selected) {
                backgroundColor = itemView.getResources().getColor(R.color.primaryLightColor);
            }
            cardView.setCardBackgroundColor(backgroundColor);
        }
    }
}
