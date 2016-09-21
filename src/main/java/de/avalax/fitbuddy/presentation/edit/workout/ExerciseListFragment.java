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

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity;

public class ExerciseListFragment extends ListFragment {
    @Inject
    protected EditWorkoutApplicationService editWorkoutService;
    private View footer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        TextView textView = (TextView) getView().findViewById(R.id.unsavedChangesTextView);
        if (editWorkoutService.hasDeletedExercise()) {
            textView.setText(R.string.has_deleted_exercise);
        } else if (editWorkoutService.hasDeletedWorkout()) {
            textView.setText(R.string.has_deleted_workout);
        }
    }

    private void initContextualActionBar() {
        ExerciseModeListener listener = new ExerciseModeListener(this, editWorkoutService);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(listener);
    }

    protected void initListView() {
        int visibility = editWorkoutService.hasUnsavedChanges() ? View.VISIBLE : View.GONE;
        footer.setVisibility(visibility);
        Workout workout = editWorkoutService.getWorkout();
        List<Exercise> exercises = workout.exercisesOfWorkout();
        ListAdapter adapter = new ExerciseAdapter(getActivity(), R.layout.item_exercise, exercises);
        setListAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        try {
            Exercise exercise = editWorkoutService.getWorkout().exerciseAtPosition(position);
            Intent intent = new Intent(getActivity(), EditExerciseActivity.class);
            intent.putExtra("exercise", exercise);
            intent.putExtra("position", position);
            getActivity().startActivityForResult(intent, EditWorkoutActivity.EDIT_EXERCISE);
        } catch (ExerciseException e) {
            Log.d("Can't edit exercise", e.getMessage(), e);
        }
    }

    private void undoChanges() {
        if (editWorkoutService.hasDeletedExercise()) {
            editWorkoutService.undoDeleteExercise();
        } else if (editWorkoutService.hasDeletedWorkout()) {
            editWorkoutService.undoDeleteWorkout();
            ((EditWorkoutActivity) getActivity()).initActionNavigationBar();
        }
        initListView();
    }

    protected void addExercise() {
        if (editWorkoutService.getWorkout() == null) {
            editWorkoutService.createWorkout();
            ((EditWorkoutActivity) getActivity()).initActionNavigationBar();
        }
        editWorkoutService.createExercise();
        initListView();
    }

}
