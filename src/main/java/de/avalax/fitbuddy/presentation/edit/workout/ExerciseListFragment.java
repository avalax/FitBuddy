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

import com.google.common.collect.Lists;

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.exercise.ExerciseException;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutException;
import de.avalax.fitbuddy.domain.model.workout.WorkoutId;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity;

public class ExerciseListFragment extends ListFragment {
    @Inject
    protected EditWorkoutApplicationService editWorkoutService;
    private View footer;
    private Workout workout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        footer = view.findViewById(R.id.footer_undo);
        view.findViewById(R.id.button_undo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    undoChanges();
                } catch (WorkoutException e) {
                    Log.d("Can't undo", e.getMessage(), e);
                }
            }
        });

        view.findViewById(android.R.id.empty).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    addExercise();
                } catch (WorkoutException e) {
                    Log.d("Can't create exercise", e.getMessage(), e);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initContextualActionBar();
        try {
            initListView((WorkoutId) getArguments().getSerializable("workoutId"));
        } catch (WorkoutException e) {
            Log.d("Can't init fragment", e.getMessage(), e);
        }

        TextView textView = (TextView) getView().findViewById(R.id.unsavedChangesTextView);
        if (editWorkoutService.hasDeletedExercise()) {
            textView.setText(R.string.has_deleted_exercise);
        } else if (editWorkoutService.hasDeletedWorkout()) {
            textView.setText(R.string.has_deleted_workout);
        }
    }

    private void initContextualActionBar() {
        ExerciseModeListener listener = new ExerciseModeListener(workout, this, editWorkoutService);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(listener);
    }

    protected void initListView(WorkoutId workoutId) throws WorkoutException {
        int visibility = editWorkoutService.hasUnsavedChanges() ? View.VISIBLE : View.GONE;
        footer.setVisibility(visibility);
        workout = editWorkoutService.loadWorkout(workoutId);
        List<Exercise> exercises = Lists.newArrayList(workout.getExercises());
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
            Exercise exercise = workout.getExercises().exerciseAtPosition(position);
            Intent intent = new Intent(getActivity(), EditExerciseActivity.class);
            intent.putExtra("workoutId", workout.getWorkoutId());
            intent.putExtra("exercise", exercise);
            intent.putExtra("position", position);
            getActivity().startActivityForResult(intent, EditWorkoutActivity.EDIT_EXERCISE);
        } catch (ExerciseException e) {
            Log.d("Can't edit exercise", e.getMessage(), e);
        }
    }

    private void undoChanges() throws WorkoutException {
        if (editWorkoutService.hasDeletedExercise()) {
            editWorkoutService.undoDeleteExercise(workout);
        } else if (editWorkoutService.hasDeletedWorkout()) {
            editWorkoutService.undoDeleteWorkout();
            ((EditWorkoutActivity) getActivity()).initActionNavigationBar();
        }
        initListView(workout.getWorkoutId());
    }

    protected void addExercise() throws WorkoutException {
        if (workout == null) {
            workout = editWorkoutService.createWorkout();
            ((EditWorkoutActivity) getActivity()).initActionNavigationBar();
        }
        editWorkoutService.createExercise(workout);
        initListView(workout.getWorkoutId());
    }

}
