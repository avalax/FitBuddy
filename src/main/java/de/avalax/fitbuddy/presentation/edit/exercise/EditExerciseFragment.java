package de.avalax.fitbuddy.presentation.edit.exercise;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.databinding.EditExerciseBinding;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetException;
import de.avalax.fitbuddy.presentation.edit.exercise.SetAdapter.SetViewHolderCallback;
import de.avalax.fitbuddy.presentation.edit.set.EditSetActivity;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.ADD_SET;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.EDIT_SET;

public class EditExerciseFragment extends Fragment implements SetViewHolderCallback {

    private static final String KEY_EXERCISE = "exercise";
    private EditExerciseViewModel viewModel;
    private EditExerciseBinding binding;
    private SetAdapter setAdapter;
    private Exercise exercise;

    public static EditExerciseFragment forExercise(Exercise exercise) {
        EditExerciseFragment fragment = new EditExerciseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_EXERCISE, exercise);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity()).get(EditExerciseViewModel.class);
        setAdapter = new SetAdapter(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_exercise, container, false);
        binding.setEditExerciseViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.list.setEmptyView(binding.getRoot().findViewById(android.R.id.empty));
        binding.list.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        binding.list.setAdapter(setAdapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        exercise = (Exercise) getArguments().getSerializable(KEY_EXERCISE);
        viewModel.init(exercise);
        viewModel.getSets().observe(this, sets -> {
            setAdapter.setSets(sets);
            binding.list.updateEmptyView();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_delete_sets) {
            removeSelections();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_SET && resultCode == Activity.RESULT_OK) {
            Set set = (Set) data.getSerializableExtra("set");
            exercise.getSets().add(set);
            viewModel.getSets().setValue(exercise.getSets());
        }
        if (requestCode == EDIT_SET && resultCode == Activity.RESULT_OK) {
            Integer position = data.getIntExtra("position", -1);
            Set set = (Set) data.getSerializableExtra("set");
            exercise.getSets().set(position, set);
            viewModel.getSets().setValue(exercise.getSets());
        }
    }

    public void removeSelections() {
        java.util.Set<Set> selections = setAdapter.getSelections();
        for (Set selection : selections) {
            exercise.getSets().remove(selection);
        }
        setAdapter.clearSelections();
        viewModel.getSets().setValue(exercise.getSets());
        ((EditExerciseActivity) getActivity()).onSelectionChange(0);
    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            Set set = exercise.getSets().get(position);
            Intent intent = new Intent(getActivity(), EditSetActivity.class);
            intent.putExtra("set", set);
            intent.putExtra("position", position);
            getActivity().startActivityForResult(intent, EDIT_SET);

        } catch (SetException e) {
            Log.e("SetException", e.getMessage(), e);
        }
    }

    @Override
    public void onSelectionChange(int size) {
        ((EditExerciseActivity) getActivity()).onSelectionChange(size);
    }
}
