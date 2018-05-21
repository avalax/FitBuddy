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

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.databinding.EditExerciseBinding;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class EditExerciseFragment extends Fragment {

    private EditExerciseViewModel viewModel;
    private EditExerciseBinding binding;

    @Inject
    protected EditExerciseViewHelper editExerciseViewHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_exercise, container, false);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        viewModel = ViewModelProviders.of(this).get(EditExerciseViewModel.class);
        binding.setEditExerciseViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.list.setEmptyView(binding.getRoot().findViewById(android.R.id.empty));
        binding.list.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Exercise exercise = (Exercise) getActivity().getIntent().getSerializableExtra("exercise");
        viewModel.init(getActivity(), editExerciseViewHelper, exercise);
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
