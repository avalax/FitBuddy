package de.avalax.fitbuddy.app.manageWorkout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import butterknife.ButterKnife;
import de.avalax.fitbuddy.app.R;
import de.avalax.fitbuddy.core.workout.Exercise;

public class EditExerciseDialogFragment extends DialogFragment {

    private static final String ARGS_EXERCISE = "exercise";
    private Exercise exercise;

    public interface DialogListener {
        public void onDialogDeleteClick(EditExerciseDialogFragment editWeightDialogFragment);
    }

    DialogListener listener;

    public static EditExerciseDialogFragment newInstance(Exercise exercise) {
        EditExerciseDialogFragment fragment = new EditExerciseDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_EXERCISE, exercise);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EditExerciseDialogFragment.DialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_edit_exercise, null);
        ButterKnife.inject(this, view);

        this.exercise = (Exercise) getArguments().getSerializable(ARGS_EXERCISE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(exercise.getName())
                .setView(view)
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        listener.onDialogDeleteClick(EditExerciseDialogFragment.this);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                EditExerciseDialogFragment.this.getDialog().cancel();
                            }
                        }
                );
        return builder.create();
    }
}
