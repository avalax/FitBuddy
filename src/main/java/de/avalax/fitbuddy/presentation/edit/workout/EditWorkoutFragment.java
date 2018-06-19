package de.avalax.fitbuddy.presentation.edit.workout;

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

import java.util.Set;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.databinding.EditWorkoutBinding;
import de.avalax.fitbuddy.domain.model.exercise.BasicExercise;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.ADD_EXERCISE;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.EDIT_EXERCISE;
import static de.avalax.fitbuddy.presentation.edit.workout.ExerciseAdapter.ExerciseViewHolderCallback;

public class EditWorkoutFragment extends Fragment {

    private static final String KEY_WORKOUT = "workout";
    private EditWorkoutViewModel viewModel;
    private EditWorkoutBinding binding;
    private ExerciseAdapter exerciseAdapter;
    private Workout workout;

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
        viewModel = ViewModelProviders.of(getActivity()).get(EditWorkoutViewModel.class);
        exerciseAdapter = new ExerciseAdapter(editWorkoutCallback);
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_workout, container, false);
        binding.setEditWorkoutViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.list.setEmptyView(binding.getRoot().findViewById(android.R.id.empty));
        binding.list.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        binding.list.setAdapter(exerciseAdapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        workout = (Workout) getArguments().getSerializable(KEY_WORKOUT);
        viewModel.init(workout);
        viewModel.getExercises().observe(this, exercises -> {
            exerciseAdapter.setExercises(exercises);
            binding.list.updateEmptyView();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_delete_exercices) {
            removeSelections();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_EXERCISE && resultCode == Activity.RESULT_OK) {
            Exercise exercise = (Exercise) data.getSerializableExtra("exercise");
            workout.getExercises().add(exercise);
            viewModel.getExercises().setValue(workout.getExercises());
        }
        if (requestCode == EDIT_EXERCISE && resultCode == Activity.RESULT_OK) {
            Integer position = data.getIntExtra("position", -1);
            Exercise exercise = (Exercise) data.getSerializableExtra("exercise");
            workout.getExercises().set(position, exercise);
            viewModel.getExercises().setValue(workout.getExercises());
        }
    }

    private void removeSelections() {
        Set<Exercise> selections = exerciseAdapter.getSelections();
        for (Exercise selection : selections) {
            workout.getExercises().remove(selection);
        }
        exerciseAdapter.clearSelections();
        viewModel.getExercises().setValue(workout.getExercises());
        ((EditWorkoutActivity) getActivity()).onSelectionChange(0);
    }

    private final ExerciseViewHolderCallback editWorkoutCallback = new ExerciseViewHolderCallback() {
        @Override
        public void onItemClick(View view, int position) {
            try {
                Exercise exercise = workout.getExercises().get(position);
                Intent intent = new Intent(getActivity(), EditExerciseActivity.class);
                intent.putExtra("exercise", exercise);
                intent.putExtra("position", position);
                getActivity().startActivityForResult(intent, EDIT_EXERCISE);
            } catch (ExerciseException e) {
                Log.e("ExerciseException", e.getMessage(), e);
            }
        }

        @Override
        public void onSelectionChange(int size) {
            ((EditWorkoutActivity) getActivity()).onSelectionChange(size);
        }
    };
}
