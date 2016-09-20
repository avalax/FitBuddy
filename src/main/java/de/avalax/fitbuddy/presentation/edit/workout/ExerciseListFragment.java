package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseNotFoundException;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity;

public class ExerciseListFragment extends ListFragment {
    @Inject
    protected EditWorkoutApplicationService editWorkoutApplicationService;
    private View footer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        footer = view.findViewById(R.id.footer_undo);
        view.findViewById(R.id.button_undo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                undoChanges();
            }
        });

        view.findViewById(android.R.id.empty).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addExercise();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initContextualActionBar();
        initListView();


        TextView unsavedChangesTextView = (TextView) getView().findViewById(R.id.unsavedChangesTextView);
        if (editWorkoutApplicationService.hasDeletedExercise()) {
            unsavedChangesTextView.setText(R.string.has_deleted_exercise);
        } else if (editWorkoutApplicationService.hasDeletedWorkout()) {
            unsavedChangesTextView.setText(R.string.has_deleted_workout);
        }
    }

    private void initContextualActionBar() {
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new ExerciseListMultiChoiceModeListener(this, editWorkoutApplicationService));
    }

    protected void initListView() {
        footer.setVisibility(editWorkoutApplicationService.unsavedChangesVisibility());
        //TODO: setdata using adapter.setData(data);
        Workout workout = editWorkoutApplicationService.getWorkout();
        List<Exercise> exercises = getExercises(workout);
        ListAdapter adapter = new ExerciseAdapter(getActivity(), R.layout.item_exercise, exercises);
        setListAdapter(adapter);
    }

    private List<Exercise> getExercises(Workout workout) {
        //TODO: move to service
        if (workout == null) {
            return Collections.emptyList();
        }
        List<Exercise> exercises = new ArrayList<>();
        for (int i = 0; i < workout.countOfExercises(); i++) {
            try {
                Exercise exercise = workout.exerciseAtPosition(i);
                exercises.add(exercise);
            } catch (ExerciseNotFoundException e) {
                Log.d("Can't add exercise", e.getMessage(), e);
            }
        }
        return exercises;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        try {
            Exercise exercise = editWorkoutApplicationService.getWorkout().exerciseAtPosition(position);
            Intent intent = new Intent(getActivity(), EditExerciseActivity.class);
            intent.putExtra("exercise", exercise);
            intent.putExtra("position", position);
            getActivity().startActivityForResult(intent, EditWorkoutActivity.EDIT_EXERCISE);
        } catch (ExerciseNotFoundException e) {
            Log.d("Can't edit exercise", e.getMessage(), e);
        }
    }

    private void undoChanges() {
        if (editWorkoutApplicationService.hasDeletedExercise()) {
            editWorkoutApplicationService.undoDeleteExercise();
        } else if (editWorkoutApplicationService.hasDeletedWorkout()) {
            editWorkoutApplicationService.undoDeleteWorkout();
            ((EditWorkoutActivity) getActivity()).initActionNavigationBar();
        }
        initListView();
    }

    protected void addExercise() {
        if (editWorkoutApplicationService.getWorkout() == null) {
            editWorkoutApplicationService.createWorkout();
            ((EditWorkoutActivity) getActivity()).initActionNavigationBar();
        }
        editWorkoutApplicationService.createExercise();
        initListView();
    }

}
