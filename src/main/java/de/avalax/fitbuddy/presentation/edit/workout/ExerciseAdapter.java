package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.SetNotFoundException;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.R;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;

import javax.inject.Inject;
import java.util.List;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {
    private List<Exercise> exercises;
    @Inject
    ExerciseViewHelper exerciseViewHelper;

    public ExerciseAdapter(Context context, int textViewResourceId, List<Exercise> exercises) {
        super(context, textViewResourceId, exercises);
        ((FitbuddyApplication) context.getApplicationContext()).inject(this);
        this.exercises = exercises;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExerciseViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_exercise, null);
            holder = new ExerciseViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.toptext);
            holder.weight = (TextView) convertView.findViewById(R.id.weightTextView);
            holder.reps = (TextView) convertView.findViewById(R.id.repsTextView);
            holder.sets = (TextView) convertView.findViewById(R.id.setsTextView);
            convertView.setTag(holder);
        } else {
            holder = (ExerciseViewHolder) convertView.getTag();
        }

        holder.setFromExericse(exercises.get(position));

        return convertView;
    }

    private class ExerciseViewHolder {
        public TextView name;
        public TextView weight;
        public TextView sets;
        public TextView reps;

        public void setFromExericse(Exercise exercise) {
            try {
                int indexOfCurrentSet = exercise.indexOfCurrentSet();
                name.setText(exerciseViewHelper.nameOfExercise(exercise));
                weight.setText(exerciseViewHelper.weightOfExercise(exercise, indexOfCurrentSet));
                reps.setText(String.valueOf(exerciseViewHelper.maxRepsOfExercise(exercise, indexOfCurrentSet)));
                sets.setText(String.valueOf(exerciseViewHelper.setCountOfExercise(exercise)));
            } catch (SetNotFoundException e) {
                Log.d("can't set set informations without a set", e.getMessage(), e);
            }
        }
    }
}