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

import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends ArrayAdapter<Exercise> {
    private final List<Exercise> items;

    public static WorkoutAdapter newInstance(Context context, int textViewResourceId, Workout workout) {
        List<Exercise> items = new ArrayList<>();
        for (int i = 0; i < workout.getExerciseCount(); i++) {
            items.add(workout.getExercise(i));
        }
        return new WorkoutAdapter(context, textViewResourceId, items);
    }

    public WorkoutAdapter(Context context, int textViewResourceId, List<Exercise> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row, null);
        }

        Exercise o = items.get(position);
        TextView tt = (TextView) v.findViewById(R.id.toptext);
        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
        ImageView iv = (ImageView) v.findViewById(R.id.buttonOverflow);

        tt.setText(o.getName());
        bt.setText(String.valueOf(o.getWeight()));
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
        return v;
    }
}