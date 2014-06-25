package de.avalax.fitbuddy.application.manageWorkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.squareup.otto.Bus;
import de.avalax.fitbuddy.application.FitbuddyApplication;
import de.avalax.fitbuddy.application.R;
import de.avalax.fitbuddy.application.manageWorkout.editExercise.EditExerciseActivity;
import de.avalax.fitbuddy.application.manageWorkout.events.WorkoutListInvalidatedEvent;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.workout.Workout;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

public class ExerciseListFragment extends ListFragment {
    @Inject
    protected ManageWorkout manageWorkout;
    @Inject
    protected Bus bus;
    @InjectView(R.id.footer_undo)
    protected View footer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        ButterKnife.inject(this, view);
        initListView();
        return view;
    }

    protected void initListView() {
        //TODO: setdata using adapter.setData(data);
        Workout workout = manageWorkout.getWorkout();
        List<Exercise> exercises = workout != null ? workout.getExercises() : Collections.<Exercise>emptyList();
        ListAdapter adapter = new ExerciseAdapter(getActivity(), R.layout.item_exercise, exercises);
        setListAdapter(adapter);
        footer.setVisibility(manageWorkout.unsavedChangesVisibility());
        TextView unsavedChangesTextView = (TextView) footer.findViewById(R.id.unsavedChangesTextView);
        if (manageWorkout.hasDeletedExercise()) {
            unsavedChangesTextView.setText(R.string.has_deleted_exercise);
        } else if (manageWorkout.hasDeletedWorkout()) {
            unsavedChangesTextView.setText(R.string.has_deleted_workout);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Exercise exercise = manageWorkout.getWorkout().getExercise(position);
        Intent intent = new Intent(getActivity(), EditExerciseActivity.class);
        intent.putExtra("exercise", exercise);
        intent.putExtra("position", position);
        getActivity().startActivityForResult(intent, ManageWorkoutActivity.EDIT_EXERCISE);
    }

    @OnClick(R.id.button_undo)
    protected void undoChanges() {
        if (manageWorkout.hasDeletedExercise()) {
            manageWorkout.undoDeleteExercise();
            initListView();
        } else if (manageWorkout.hasDeletedWorkout()) {
            manageWorkout.undoDeleteWorkout();
            bus.post(new WorkoutListInvalidatedEvent());
        }
    }

    @OnClick(android.R.id.empty)
    protected void addExercise() {
        if (manageWorkout.getWorkout() == null) {
            manageWorkout.createWorkout();
            bus.post(new WorkoutListInvalidatedEvent());
        }
        manageWorkout.createExercise();
        initListView();
    }
}
