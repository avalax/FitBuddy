package de.avalax.fitbuddy.presentation.edit.exercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Sets;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutRecyclerView;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class SetListFragment extends Fragment {

    private SetAdapter adapter;
    private Sets sets;
    private WorkoutRecyclerView recyclerView;

    @Inject
    protected EditExerciseViewHelper editExerciseViewHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_exercise, container, false);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        recyclerView = view.findViewById(android.R.id.list);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        Exercise exercise = (Exercise) getActivity().getIntent().getSerializableExtra("exercise");
        sets = exercise.getSets();
        adapter = new SetAdapter(getActivity(), sets, editExerciseViewHelper);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void notifyItemInserted() {
        adapter.notifyItemInserted(sets.size() - 1);
        recyclerView.updateEmptyView();
    }

    public void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
        recyclerView.updateEmptyView();
    }

    public void removeSelections() {
        adapter.removeSelections();
        recyclerView.updateEmptyView();
        ((EditExerciseActivity) getActivity()).updateToolbar(0);
    }
}
