package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutRecyclerView;

public class ExerciseListFragment extends Fragment {

    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> exercises;
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
        exercises = new ArrayList<>();
        exerciseAdapter = new ExerciseAdapter(getActivity(), exercises);
        recyclerView.setAdapter(exerciseAdapter);
        return view;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
        exerciseAdapter.notifyItemInserted(exercises.size() - 1);
        recyclerView.updateEmptyView();
    }

    private class ExerciseAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Exercise> exercises;
        private Context mContext;

        ExerciseAdapter(Context context, List<Exercise> exercises) {
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
            Exercise exercise = exercises.get(position);
            String title = editWorkoutApplicationService.title(exercise);
            String subtitle = editWorkoutApplicationService.subtitle(exercise);

            holder.getTitleTextView().setText(title);
            holder.getSubtitleTextView().setText(subtitle);
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
    }
}
