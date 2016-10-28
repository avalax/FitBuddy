package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {
    private List<Exercise> exercises;
    @Inject
    ExerciseViewHelper exerciseViewHelper;
    private int textViewResourceId;

    public ExerciseAdapter(Context context, int textViewResourceId, List<Exercise> exercises) {
        super(context, textViewResourceId, exercises);
        this.textViewResourceId = textViewResourceId;
        ((FitbuddyApplication) context.getApplicationContext()).component().inject(this);
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Exercise exercise = exercises.get(position);
        if (convertView == null) {
            View newConvertView = createConvertView(parent);
            newConvertView.setTag(createViewHolder(newConvertView, exercise));
            return newConvertView;
        }

        ExerciseViewHolder holder = (ExerciseViewHolder) convertView.getTag();
        holder.setFromExercise(exercise);
        return convertView;
    }

    @NonNull
    private ExerciseViewHolder createViewHolder(View convertView, Exercise exercise) {
        ExerciseViewHolder holder = new ExerciseViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.exercise_title);
        holder.weight = (TextView) convertView.findViewById(R.id.weightTextView);
        holder.reps = (TextView) convertView.findViewById(R.id.repsTextView);
        holder.sets = (TextView) convertView.findViewById(R.id.setsTextView);
        holder.setFromExercise(exercise);
        return holder;
    }

    private View createConvertView(@NonNull ViewGroup parent) {
        String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflaterService);
        return vi.inflate(textViewResourceId, parent, false);
    }

    class ExerciseViewHolder {
        public TextView name;
        public TextView weight;
        public TextView sets;
        public TextView reps;

        void setFromExercise(Exercise exercise) {
            name.setText(exerciseViewHelper.nameOfExercise(exercise));
            weight.setText(exerciseViewHelper.weightOfExercise(exercise));
            reps.setText(String.valueOf(exerciseViewHelper.maxRepsOfExercise(exercise)));
            sets.setText(String.valueOf(exerciseViewHelper.setCountOfExercise(exercise)));
        }
    }
}