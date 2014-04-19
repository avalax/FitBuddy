package de.avalax.fitbuddy.app.manageWorkout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class WorkoutListFragment extends ListFragment {
    @Inject
    protected ManageWorkout manageWorkout;

    @InjectView(R.id.footer_undo)
    protected View footer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        super.onCreate(savedInstanceState);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        View view = inflater.inflate(R.layout.manage_workout, container, false);
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
        setListAdapter(WorkoutAdapter.newInstance(getActivity(), R.layout.row, manageWorkout.getWorkout()));
        footer.setVisibility(manageWorkout.unsavedChangesVisibility());
    }

    private void editExercise(final int position) {
        Exercise exercise = manageWorkout.getWorkout().getExercise(position);
        View view = getActivity().getLayoutInflater().inflate(R.layout.view_edit_exercise, null);
        new AlertDialog.Builder(getActivity())
                .setTitle(exercise.getName())
                .setView(view)
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteExercise(position);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getActivity(), "save", 1000);
                            }
                        }
                )
                .show();
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
}
