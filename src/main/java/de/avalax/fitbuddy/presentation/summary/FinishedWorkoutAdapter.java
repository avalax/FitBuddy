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
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkout;

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
        FinishedWorkoutViewHolder holder;
        if (convertView == null) {
            String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflaterService);
            convertView = vi.inflate(resource, parent, false);
            holder = new FinishedWorkoutViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.finished_workout_title);
            holder.date = (TextView) convertView.findViewById(R.id.finished_workout_date);
            convertView.setTag(holder);
        } else {
            holder = (FinishedWorkoutViewHolder) convertView.getTag();
        }

        holder.setFromFinishedWorkout(finishedWorkouts.get(position));

        return convertView;
    }

    class FinishedWorkoutViewHolder {
        TextView name;
        TextView date;

        void setFromFinishedWorkout(FinishedWorkout finishedWorkout) {
            name.setText(finishedWorkout.getName());
            date.setText(finishedWorkout.getCreated());
        }
    }
}