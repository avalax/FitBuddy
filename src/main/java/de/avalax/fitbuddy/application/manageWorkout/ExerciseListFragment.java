package de.avalax.fitbuddy.application.manageWorkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.AbsListView;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initContextualActionBar();
    }

    private void initContextualActionBar() {
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new MultiChoiceModeListener());
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
        Exercise exercise = manageWorkout.getWorkout().getExercises().get(position);
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

    private class MultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            setTitle(mode);
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //TODO: change menu,when more then one is selected
            mode.getMenuInflater().inflate(R.menu.manage_workout_cab_actions, menu);
            setTitle(mode);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            //TODO: add ActionItemClicked Events
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }

        private void setTitle(ActionMode mode) {
            if (getListView().getCheckedItemCount() > 1) {
                mode.setTitle(getResources().getText(R.string.cab_title_manage_exercises));
            } else {
                mode.setTitle(getResources().getText(R.string.cab_title_manage_exercise));
            }
        }
    }
}
