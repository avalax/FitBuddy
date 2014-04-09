package de.avalax.fitbuddy.app.manageWorkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends ArrayAdapter<Exercise> {
    private final List<Exercise> exercises;
    DecimalFormat decimalFormat;

    public static WorkoutAdapter newInstance(Context context, int textViewResourceId, Workout workout) {
        List<Exercise> items = new ArrayList<>();
        for (int i = 0; i < workout.getExerciseCount(); i++) {
            items.add(workout.getExercise(i));
        }
        return new WorkoutAdapter(context, textViewResourceId, items);
    }

    public WorkoutAdapter(Context context, int textViewResourceId, List<Exercise> exercises) {
        super(context, textViewResourceId, exercises);
        this.exercises = exercises;
        this.decimalFormat = new DecimalFormat("###.#");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.row, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.toptext);
            holder.weight = (TextView) convertView.findViewById(R.id.weightTextView);
            holder.reps = (TextView) convertView.findViewById(R.id.repsTextView);
            holder.sets = (TextView) convertView.findViewById(R.id.setsTextView);
            holder.iv = (ImageView) convertView.findViewById(R.id.buttonOverflow);
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.showContextMenu();
                }
            });
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
        public ImageView iv;
    }
}