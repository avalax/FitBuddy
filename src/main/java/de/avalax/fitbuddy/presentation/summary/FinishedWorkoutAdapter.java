package de.avalax.fitbuddy.presentation.summary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkout;

public class FinishedWorkoutAdapter extends ArrayAdapter<FinishedWorkout> {
    private int textViewResourceId;
    private List<FinishedWorkout> finishedWorkouts;

    public FinishedWorkoutAdapter(Context context, int textViewResourceId, List<FinishedWorkout> finishedWorkouts) {
        super(context, textViewResourceId, finishedWorkouts);
        this.textViewResourceId = textViewResourceId;
        this.finishedWorkouts = finishedWorkouts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FinishedWorkoutViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(textViewResourceId, parent, false);
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

    protected class FinishedWorkoutViewHolder {
        public TextView name;
        public TextView date;

        public void setFromFinishedWorkout(FinishedWorkout finishedWorkout) {
            name.setText(finishedWorkout.getName());
            date.setText(finishedWorkout.getCreated());
        }
    }
}