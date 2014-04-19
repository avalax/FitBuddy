package de.avalax.fitbuddy.app.manageWorkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.core.workout.Exercise;

import java.text.DecimalFormat;
import java.util.List;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {
    private List<Exercise> exercises;
    DecimalFormat decimalFormat;

    public ExerciseAdapter(Context context, int textViewResourceId, List<Exercise> exercises) {
        super(context, textViewResourceId, exercises);
        this.exercises = exercises;
        this.decimalFormat = new DecimalFormat("###.#");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_exercise, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.toptext);
            holder.weight = (TextView) convertView.findViewById(R.id.weightTextView);
            holder.reps = (TextView) convertView.findViewById(R.id.repsTextView);
            holder.sets = (TextView) convertView.findViewById(R.id.setsTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Exercise exercise = exercises.get(position);
        holder.name.setText(exercise.getName());
        holder.weight.setText(getWeightText(exercise.getWeight()));
        holder.reps.setText(String.valueOf(exercise.getMaxReps()));
        holder.sets.setText(String.valueOf(exercise.getMaxSets()));
        return convertView;
    }

    private CharSequence getWeightText(double weight) {
        if (weight > 0) {
            return decimalFormat.format(weight);
        } else {
            return "-";
        }
    }

    private static class ViewHolder {
        public TextView name;
        public TextView weight;
        public TextView sets;
        public TextView reps;
    }
}