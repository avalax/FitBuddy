package de.avalax.fitbuddy.app.manageWorkout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.avalax.fitbuddy.app.FitbuddyApplication;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.core.workout.Exercise;

import javax.inject.Inject;

public class ExerciseListFragment extends ListFragment {
    @Inject
    protected ManageWorkout manageWorkout;

    @InjectView(R.id.footer_undo)
    protected View footer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        super.onCreate(savedInstanceState);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        ButterKnife.inject(this, view);
        init(savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(getListView());
        footer.setVisibility(manageWorkout.unsavedChangesVisibility());
    }

    private void init(Bundle savedInstanceState) {
        initListView();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        editExercise(position);
    }

    protected void initListView() {
        //TODO: setdata using adapter.setData(data);
        setListAdapter(new ExerciseAdapter(getActivity(), R.layout.item_exercise, manageWorkout.getWorkout().getExercises()));
        footer.setVisibility(manageWorkout.unsavedChangesVisibility());
    }

    private void editExercise(final int position) {
        Exercise exercise = manageWorkout.getWorkout().getExercise(position);
        EditExerciseDialogFragment.newInstance(exercise).show(getFragmentManager(),"fragment_edit_exercise");
    }

    private void deleteExercise(int exercisePosition) {
        manageWorkout.getWorkout().removeExercise(exercisePosition);
        manageWorkout.setUnsavedChanges(true);
        //TODO: undo remove exercise
        initListView();
    }

    @OnClick(R.id.button_undo)
    protected void undoChanges() {
        manageWorkout.undoUnsavedChanges();
        initListView();
    }

    @OnClick(android.R.id.empty)
    protected void addExercixe() {
        //TODO: add exercise
        Toast.makeText(getActivity(), "add exercise", Toast.LENGTH_LONG).show();
    }
}
