package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.exercise.Exercises;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutRecyclerView;

import static de.avalax.fitbuddy.presentation.edit.workout.EditWorkoutActivity.EDIT_EXERCISE;

public class ExerciseListFragment extends Fragment {

    private ExercisesAdapter exercisesAdapter;
    private Exercises exercises;
    private WorkoutRecyclerView recyclerView;

    @Inject
    protected EditWorkoutApplicationService editWorkoutApplicationService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_workout, container, false);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        recyclerView = view.findViewById(android.R.id.list);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));
        Workout workout = (Workout) getActivity().getIntent().getSerializableExtra("workout");
        exercises = workout.getExercises();
        exercisesAdapter = new ExercisesAdapter(getActivity(), exercises);
        recyclerView.setAdapter(exercisesAdapter);
        return view;
    }

    public void addExercise() {
        exercisesAdapter.notifyItemInserted(exercises.size() - 1);
        recyclerView.updateEmptyView();
    }

    public void updateExercise(Integer position, Exercise exercise) {
        exercisesAdapter.notifyItemChanged(position);
        recyclerView.updateEmptyView();
    }

    private class ExercisesAdapter extends RecyclerView.Adapter<ViewHolder> {
        private Exercises exercises;
        private Context mContext;

        ExercisesAdapter(Context context, Exercises exercises) {
            super();
            mContext = context;
            this.exercises = exercises;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_exercise, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            try {
                Exercise exercise = exercises.get(position);
                String title = editWorkoutApplicationService.title(exercise);
                String subtitle = editWorkoutApplicationService.subtitle(exercise);

                holder.getTitleTextView().setText(title);
                holder.getSubtitleTextView().setText(subtitle);
                holder.getView().setOnClickListener(view -> {
                    Intent intent = new Intent(getActivity(), EditExerciseActivity.class);
                    intent.putExtra("exercise", exercise);
                    intent.putExtra("position", holder.getAdapterPosition());
                    getActivity().startActivityForResult(intent, EDIT_EXERCISE);
                });
            } catch (ExerciseException e) {
                Log.e("ExerciseException", e.getMessage(), e);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return exercises == null ? 0 : exercises.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView subtitleTextView;

        ViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.item_title);
            subtitleTextView = v.findViewById(R.id.item_subtitle);
        }

        TextView getTitleTextView() {
            return titleTextView;
        }

        TextView getSubtitleTextView() {
            return subtitleTextView;
        }

        public View getView() {
            return itemView;
        }
    }
}
