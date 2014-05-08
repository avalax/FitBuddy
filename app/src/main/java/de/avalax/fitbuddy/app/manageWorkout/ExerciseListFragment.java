package de.avalax.fitbuddy.app.manageWorkout;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import de.avalax.fitbuddy.app.FitbuddyApplication;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.manageWorkout.events.ExerciseListInvalidatedEvent;
import de.avalax.fitbuddy.core.workout.Exercise;

import javax.inject.Inject;

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
        setListAdapter(new ExerciseAdapter(getActivity(), R.layout.item_exercise, manageWorkout.getWorkout().getExercises()));
        footer.setVisibility(manageWorkout.unsavedChangesVisibility());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(getListView());
        footer.setVisibility(manageWorkout.unsavedChangesVisibility());
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
        EditExerciseDialogFragment.newInstance(position, exercise).show(getFragmentManager(), "fragment_edit_exercise");
    }

    @OnClick(R.id.button_undo)
    protected void undoChanges() {
        manageWorkout.undoUnsavedChanges();
        bus.post(new ExerciseListInvalidatedEvent());
    }

    @OnClick(android.R.id.empty)
    protected void addExercise() {
        manageWorkout.addNewExercise();
        bus.post(new ExerciseListInvalidatedEvent());
    }

    @Subscribe
    public void onExerciseListInvalidated(ExerciseListInvalidatedEvent event) {
        initListView();
    }
}
