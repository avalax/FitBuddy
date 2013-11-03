package de.avalax.fitbuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import de.avalax.fitbuddy.edit.EditExerciseActivity;
import de.avalax.fitbuddy.edit.EditableExercise;
import de.avalax.fitbuddy.edit.ExistingEditableExercise;
import de.avalax.fitbuddy.edit.NewEditableExercise;
import de.avalax.fitbuddy.progressBar.ProgressBarOnTouchListener;
import de.avalax.fitbuddy.progressBar.VerticalProgressBar;
import de.avalax.fitbuddy.workout.Exercise;
import de.avalax.fitbuddy.workout.Workout;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class ExerciseFragment extends RoboFragment {

    private static final int ADD_EXERCISE_BEFORE = 1;
    private static final int EDIT_EXERCISE = 2;
    private static final int ADD_EXERCISE_AFTER = 3;
    @Inject
    Context context;
    @Inject
    private Workout workout;
    @InjectView(R.id.leftProgressBar)
    private VerticalProgressBar repsProgressBar;
    @InjectView(R.id.rightProgressBar)
    private VerticalProgressBar setsProgressBar;
    private int exercisePosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        exercisePosition = getArguments().getInt("exerciseIndex");
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repsProgressBar.setOnTouchListener(new ProgressBarOnTouchListener(context, repsProgressBar, getMaxMoveForReps(workout)) {
            @Override
            public void onFlingEvent(int moved) {
                changeReps(moved);
            }

            @Override
            protected void onLongPressedLeftEvent() {
                addExerciseBeforeCurrentExercise();
            }

            @Override
            protected void onLongPressedRightEvent() {
                editCurrentExercise();
            }
        });

        setsProgressBar.setOnTouchListener(new ProgressBarOnTouchListener(context, setsProgressBar, getMaxMoveForSets(workout)) {
            @Override
            public void onFlingEvent(int moved) {
                changeSets(moved);
            }

            @Override
            protected void onLongPressedLeftEvent() {
                editCurrentExercise();
            }

            @Override
            protected void onLongPressedRightEvent() {
                addExerciseAfterCurrentExercise();
            }
        });

        setViews(exercisePosition);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            EditableExercise editableExercise = (EditableExercise) intent.getSerializableExtra("editableExercise");
            Exercise exercise = editableExercise.createExercise();
            switch (requestCode) {
                case ADD_EXERCISE_BEFORE:
                    workout.addExerciseBefore(exercisePosition, exercise);
                    break;
                case ADD_EXERCISE_AFTER:
                    workout.addExerciseAfter(exercisePosition, exercise);
                    break;
                case EDIT_EXERCISE:
                    workout.setExercise(exercisePosition, exercise);
                    break;
            }
        } else if(resultCode == Activity.RESULT_CANCELED && requestCode == EDIT_EXERCISE) {
            //TODO: delete exercise, show anythingelse, when last exercise is deleted
            Log.d("DeleteExercise", "onActivityResult()");
        }
    }

    private void addExerciseBeforeCurrentExercise() {
        startActivityForResult(getIntent(createNewEditableExercise()), ADD_EXERCISE_BEFORE);
    }

    private void addExerciseAfterCurrentExercise() {
        startActivityForResult(getIntent(createNewEditableExercise()), ADD_EXERCISE_AFTER);
    }

    private void editCurrentExercise() {
        startActivityForResult(getIntent(createExistingEditableExercise()), EDIT_EXERCISE);
    }

    private NewEditableExercise createNewEditableExercise() {
        NewEditableExercise newEditableExercise = new NewEditableExercise();
        //TODO: extract to resources
        newEditableExercise.setWeight(2.5);
        newEditableExercise.setWeightRaise(1.25);
        newEditableExercise.setReps(12);
        newEditableExercise.setSets(3);
        return newEditableExercise;
    }

    private EditableExercise createExistingEditableExercise() {
        Exercise exercise = workout.getExercise(exercisePosition);
        return new ExistingEditableExercise(exercise);
    }

    private Intent getIntent(EditableExercise editableExercise) {
        Intent intent = new Intent(getActivity().getApplicationContext(), EditExerciseActivity.class);
        intent.putExtra("editableExercise", editableExercise);
        return intent;
    }

    private int getMaxMoveForSets(Workout workout) {
        return workout.getExercise(exercisePosition).getMaxSets();
    }

    private int getMaxMoveForReps(Workout workout) {
        return workout.getExercise(exercisePosition).getMaxReps();
    }

    private void changeReps(int moved) {
        setReps(moved);
        setViews(exercisePosition);
    }

    private void changeSets(int moved) {
        setSet(workout.getExercise(exercisePosition).getSetNumber() + moved);
    }

    private void setReps(int moved) {
        Exercise exercise = workout.getExercise(exercisePosition);
        exercise.setReps(exercise.getReps() + moved);
    }

    private void setSet(int setNumber) {
        workout.getExercise(exercisePosition).setCurrentSet(setNumber);
        setViews(exercisePosition);
    }

    private void setViews(int exercisePosition) {
        repsProgressBar.setProgressBar(workout.getExercise(exercisePosition).getCurrentSet());
        setsProgressBar.setProgressBar(workout, exercisePosition);
    }
}
