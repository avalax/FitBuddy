package de.avalax.fitbuddy.app.edit;

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
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row, null);
        }

        Exercise exercise = exercises.get(position);
        TextView name = (TextView) v.findViewById(R.id.toptext);
        TextView weight = (TextView) v.findViewById(R.id.weightTextView);
        TextView reps = (TextView) v.findViewById(R.id.repsTextView);
        TextView sets = (TextView) v.findViewById(R.id.setsTextView);
        ImageView iv = (ImageView) v.findViewById(R.id.buttonOverflow);

        name.setText(exercise.getName());
        weight.setText(getWeightText(exercise.getWeight()));
        reps.setText(String.valueOf(exercise.getMaxReps()));
        sets.setText(String.valueOf(exercise.getMaxSets()));
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
        return v;
    }

    private CharSequence getWeightText(double weight) {
        if (weight > 0) {
            return decimalFormat.format(weight);
        } else {
            return "-";
        }
    }
}