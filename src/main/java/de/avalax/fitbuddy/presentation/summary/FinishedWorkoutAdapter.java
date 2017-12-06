package de.avalax.fitbuddy.presentation.summary;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.presentation.MainActivity;

public class FinishedWorkoutAdapter
        extends RecyclerView.Adapter<FinishedWorkoutAdapter.ViewHolder> {
    private MainActivity activity;
    private List<FinishedWorkout> finishedWorkouts;

    FinishedWorkoutAdapter(MainActivity activity, List<FinishedWorkout> finishedWorkouts) {
        super();
        this.activity = activity;
        this.finishedWorkouts = finishedWorkouts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_finished_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FinishedWorkout workout = finishedWorkouts.get(position);
        holder.getTitleTextView().setText(workout.getName());
        holder.getView().setOnClickListener(view -> {
            FinishedWorkoutDetailFragment fragment
                    = FinishedWorkoutDetailFragment.newInstance(workout.getFinishedWorkoutId());
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_content, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;

        ViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.finished_workout_title);
        }

        TextView getTitleTextView() {
            return titleTextView;
        }

        public View getView() {
            return itemView;
        }
    }
}