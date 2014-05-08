package de.avalax.fitbuddy.app.manageWorkout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.squareup.otto.Bus;
import de.avalax.fitbuddy.app.FitbuddyApplication;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.app.manageWorkout.events.ExerciseChangedEvent;
import de.avalax.fitbuddy.app.manageWorkout.events.ExerciseDeletedEvent;
import de.avalax.fitbuddy.core.workout.Exercise;

import javax.inject.Inject;

public class EditExerciseDialogFragment extends DialogFragment {

    private static final String ARGS_EXERCISE = "exercise";
    private static final String ARGS_POSITION = "position";
    @Inject
    protected Bus bus;

    private int position;
    private Exercise exercise;

    public static EditExerciseDialogFragment newInstance(int position, Exercise exercise) {
        EditExerciseDialogFragment fragment = new EditExerciseDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_POSITION, position);
        args.putSerializable(ARGS_EXERCISE, exercise);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        return view;
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_edit_exercise, null);
        ButterKnife.inject(this, view);

        this.exercise = (Exercise) getArguments().getSerializable(ARGS_EXERCISE);
        this.position = getArguments().getInt(ARGS_POSITION);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(exercise.getName())
                .setView(view)
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        bus.post(new ExerciseDeletedEvent(position, exercise));
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        bus.post(new ExerciseChangedEvent(position, exercise));
                    }
                });
        return builder.create();
    }
}
