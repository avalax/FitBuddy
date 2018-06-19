package de.avalax.fitbuddy.presentation.edit.workout;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.databinding.EditWorkoutBinding;
import de.avalax.fitbuddy.domain.model.workout.Workout;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class EditWorkoutFragment extends Fragment {

    private static final String KEY_WORKOUT = "workout";
    private EditWorkoutViewModel viewModel;
    private EditWorkoutBinding binding;

    public static EditWorkoutFragment forWorkout(Workout workout) {
        EditWorkoutFragment fragment = new EditWorkoutFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_WORKOUT, workout);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_workout, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(EditWorkoutViewModel.class);
        binding.setEditWorkoutViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.list.setEmptyView(binding.getRoot().findViewById(android.R.id.empty));
        binding.list.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Workout workout = (Workout) getArguments().getSerializable(KEY_WORKOUT);
        viewModel.init((EditWorkoutActivity) getActivity(), workout);
        binding.list.setAdapter(viewModel.getExerciseAdapter());
    }

    public void notifyItemInserted() {
        viewModel.getExerciseAdapter().notifyItemInserted(viewModel.getExerciseAdapter().getItemCount() - 1);
        binding.list.updateEmptyView();
    }

    public void notifyItemChanged(int position) {
        viewModel.getExerciseAdapter().notifyItemChanged(position);
        binding.list.updateEmptyView();
    }

    public void removeSelections() {
        viewModel.getExerciseAdapter().removeSelections();
        binding.list.updateEmptyView();
        ((EditWorkoutActivity) getActivity()).onSelectionChange(0);
    }
}
