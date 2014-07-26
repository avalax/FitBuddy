package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.avalax.fitbuddy.presentation.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;

import java.text.DecimalFormat;
import java.util.List;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {
    private List<Exercise> exercises;
    private DecimalFormat decimalFormat;

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
        //TODO: 3x times - unnamed exercise from resources & move to a ui helper
        holder.name.setText(getNameText(exercise));
        holder.weight.setText(getWeightText(exercise));
        holder.reps.setText(getMaxRepsText(exercise));
        holder.sets.setText(getSetsText(exercise));
        return convertView;
    }

    private String getNameText(Exercise exercise) {
        return exercise.getName().length() > 0 ? exercise.getName() : "unnamed exercise";
    }

    private String getSetsText(Exercise exercise) {
        return exercise.getSets().isEmpty() ? "0" : String.valueOf(exercise.getSets().size());
    }

    private String getMaxRepsText(Exercise exercise) {
        return exercise.getSets().isEmpty() ? "0" : String.valueOf(exercise.getCurrentSet().getMaxReps());
    }

    private String getWeightText(Exercise exercise) {
        if (exercise.getSets().isEmpty()) {
            return "-";
        }
        double weight = exercise.getCurrentSet().getWeight();
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