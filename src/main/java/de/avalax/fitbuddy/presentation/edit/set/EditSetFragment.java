package de.avalax.fitbuddy.presentation.edit.set;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.databinding.EditSetBinding;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.dialog.EditRepsDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditWeightDialogFragment;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseViewHelper;

import static de.avalax.fitbuddy.presentation.FitbuddyApplication.EDIT_REPS;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.EDIT_WEIGHT;

public class EditSetFragment extends Fragment implements
        EditWeightDialogFragment.DialogListener,
        EditRepsDialogFragment.DialogListener {

    private EditSetViewModel viewModel;

    @Inject
    protected EditExerciseViewHelper editExerciseViewHelper;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Set set = (Set) getArguments().getSerializable("set");
        viewModel.init(editExerciseViewHelper, set);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EditSetBinding binding = DataBindingUtil.inflate(inflater, R.layout.edit_set, container, false);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        viewModel = ViewModelProviders.of(this).get(EditSetViewModel.class);
        binding.setEditSetViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.getRoot().findViewById(R.id.set_weight).setOnClickListener(onClickListener -> changeWeight());
        binding.getRoot().findViewById(R.id.set_reps).setOnClickListener(onClickListener -> changeMaxReps());
        return binding.getRoot();
    }

    private void changeWeight() {
        double weight = viewModel.getSet().getWeight();
        EditWeightDialogFragment editWeightDialogFragment = EditWeightDialogFragment.newInstance(weight);
        editWeightDialogFragment.setTargetFragment(this, EDIT_WEIGHT);
        editWeightDialogFragment.show(getFragmentManager(), "fragment_edit_weight");
    }

    private void changeMaxReps() {
        int reps = viewModel.getSet().getMaxReps();
        EditRepsDialogFragment editRepsDialogFragment = EditRepsDialogFragment.newInstance(reps);
        editRepsDialogFragment.setTargetFragment(this, EDIT_REPS);
        editRepsDialogFragment.show(getFragmentManager(), "fragment_edit_max_reps");
    }

    @Override
    public void onDialogPositiveClick(EditRepsDialogFragment editRepsDialogFragment) {
        int reps = editRepsDialogFragment.getReps();
        viewModel.setReps(reps);
    }

    @Override
    public void onDialogPositiveClick(EditWeightDialogFragment editWeightDialogFragment) {
        double weight = editWeightDialogFragment.getWeight();
        viewModel.setWeight(weight);
    }
}
