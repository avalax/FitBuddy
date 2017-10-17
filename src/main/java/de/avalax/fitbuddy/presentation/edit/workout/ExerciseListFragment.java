package de.avalax.fitbuddy.presentation.edit.workout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercises;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutRecyclerView;

public class ExerciseListFragment extends Fragment {

    private ExerciseAdapter adapter;
    private Exercises exercises;
    private WorkoutRecyclerView recyclerView;

    @Inject
    protected EditWorkoutViewHelper editWorkoutApplicationService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_workout, container, false);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        recyclerView = view.findViewById(android.R.id.list);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));
        Workout workout = (Workout) getActivity().getIntent().getSerializableExtra("workout");
        exercises = workout.getExercises();
        adapter = new ExerciseAdapter(getActivity(), exercises, editWorkoutApplicationService);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void notifyItemInserted() {
        adapter.notifyItemInserted(exercises.size() - 1);
        recyclerView.updateEmptyView();
    }

    public void notifyItemChanged(Integer position) {
        adapter.notifyItemChanged(position);
        recyclerView.updateEmptyView();
    }

    public void removeSelections() {
        adapter.removeSelections();
        recyclerView.updateEmptyView();
        ((EditWorkoutActivity) getActivity()).updateToolbar(0);
    }
}
