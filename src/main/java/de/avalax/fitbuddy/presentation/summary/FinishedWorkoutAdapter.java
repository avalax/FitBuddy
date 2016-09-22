package de.avalax.fitbuddy.presentation.summary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;

public class FinishedWorkoutAdapter extends ArrayAdapter<FinishedWorkout> {
    private int resource;
    private List<FinishedWorkout> finishedWorkouts;

    public FinishedWorkoutAdapter(Context context, int resource,
                                  List<FinishedWorkout> finishedWorkouts) {
        super(context, resource, finishedWorkouts);
        this.resource = resource;
        this.finishedWorkouts = finishedWorkouts;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        FinishedWorkout finishedWorkout = finishedWorkouts.get(position);
        if (convertView == null) {
            View newConvertView = newConvertView(parent);
            newConvertView.setTag(createViewHolder(newConvertView, finishedWorkout));
            return newConvertView;
        }

        FinishedWorkoutViewHolder holder = (FinishedWorkoutViewHolder) convertView.getTag();
        holder.setFromFinishedWorkout(finishedWorkout);
        return convertView;
    }

    @NonNull
    private FinishedWorkoutViewHolder createViewHolder(
            View convertView,
            FinishedWorkout finishedWorkout) {
        FinishedWorkoutViewHolder holder = new FinishedWorkoutViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.finished_workout_title);
        holder.date = (TextView) convertView.findViewById(R.id.finished_workout_date);
        holder.setFromFinishedWorkout(finishedWorkout);
        return holder;
    }

    private View newConvertView(@NonNull ViewGroup parent) {
        View convertView;
        String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflaterService);
        convertView = vi.inflate(resource, parent, false);
        return convertView;
    }

    static class FinishedWorkoutViewHolder {
        TextView name;
        TextView date;

        void setFromFinishedWorkout(FinishedWorkout finishedWorkout) {
            name.setText(finishedWorkout.getName());
            date.setText(finishedWorkout.getCreated());
        }
    }
}