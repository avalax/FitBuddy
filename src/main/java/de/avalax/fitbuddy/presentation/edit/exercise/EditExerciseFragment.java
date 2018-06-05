package de.avalax.fitbuddy.presentation.edit.exercise;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.databinding.EditExerciseBinding;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class EditExerciseFragment extends Fragment {

    private static final String KEY_EXERCISE = "exercise";
    private EditExerciseViewModel viewModel;
    private EditExerciseBinding binding;

    public static EditExerciseFragment forExercise(Exercise exercise) {
        EditExerciseFragment fragment = new EditExerciseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_EXERCISE, exercise);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_exercise, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(EditExerciseViewModel.class);
        binding.setEditExerciseViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.list.setEmptyView(binding.getRoot().findViewById(android.R.id.empty));
        binding.list.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Exercise exercise = (Exercise) getArguments().getSerializable(KEY_EXERCISE);
        viewModel.init(getActivity(), exercise);
        binding.list.setAdapter(viewModel.getSetAdapter());
    }

    public void notifyItemInserted() {
        viewModel.getSetAdapter().notifyItemInserted(viewModel.getSetAdapter().getItemCount() - 1);
        binding.list.updateEmptyView();
    }

    public void notifyItemChanged(int position) {
        viewModel.getSetAdapter().notifyItemChanged(position);
        binding.list.updateEmptyView();
    }

    public void removeSelections() {
        viewModel.getSetAdapter().removeSelections();
        binding.list.updateEmptyView();
        ((EditExerciseActivity) getActivity()).updateToolbar(0);
    }
}
