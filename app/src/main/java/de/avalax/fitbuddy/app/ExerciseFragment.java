package de.avalax.fitbuddy.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import de.avalax.fitbuddy.app.edit.*;
import de.avalax.fitbuddy.app.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.app.swipeBar.VerticalProgressbarView;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class ExerciseFragment extends RoboFragment {

    private static final String ARGS_EXERCISE_INDEX = "exerciseIndex";
    private static final int ADD_EXERCISE_BEFORE = 1;
    private static final int EDIT_EXERCISE = 2;
    private static final int ADD_EXERCISE_AFTER = 3;
    @Inject
    Context context;
    @Inject
    private Workout workout;
    @InjectView(R.id.leftProgressBar)
    private VerticalProgressbarView repsProgressBar;
    @InjectView(R.id.rightProgressBar)
    private VerticalProgressbarView setsProgressBar;
    private int exercisePosition;
    private UpdateableActivity updateableActivity;

    public static ExerciseFragment newInstance(int exerciseIndex) {
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_EXERCISE_INDEX, exerciseIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        exercisePosition = getArguments().getInt(ARGS_EXERCISE_INDEX);
        updateableActivity = (UpdateableActivity) getActivity();
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repsProgressBar.setOnTouchListener(new SwipeBarOnTouchListener(context, repsProgressBar, getMaxMoveForReps(workout)) {
            @Override
            public void onFlingEvent(int moved) {
                changeReps(moved);
            }

            @Override
            protected void onLongPressedLeftEvent() {
                startEditExerciseActivity(context, createNewEditableExercise(), ADD_EXERCISE_BEFORE);
            }

            @Override
            protected void onLongPressedRightEvent() {
                startEditExerciseActivity(context, createExistingEditableExercise(), EDIT_EXERCISE);
            }
        });

        setsProgressBar.setOnTouchListener(new SwipeBarOnTouchListener(context, setsProgressBar, getMaxMoveForSets(workout)) {
            @Override
            public void onFlingEvent(int moved) {
                changeSets(moved);
            }

            @Override
            protected void onLongPressedLeftEvent() {
                startEditExerciseActivity(context, createExistingEditableExercise(), EDIT_EXERCISE);
            }

            @Override
            protected void onLongPressedRightEvent() {
                startEditExerciseActivity(context, createNewEditableExercise(), ADD_EXERCISE_AFTER);
            }
        });

        setViews(exercisePosition);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            EditableExercise editableExercise = (EditableExercise) intent.getSerializableExtra(EditExerciseActivity.EXTRA_EDITABLE_EXERCISE) ;
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
            setViews(exercisePosition);
            updateableActivity.notifyDataSetChanged();
        } else if (resultCode == Activity.RESULT_FIRST_USER && requestCode == EDIT_EXERCISE) {
            workout.removeExercise(exercisePosition);
            updateableActivity.notifyDataSetChanged();
        }
    }

    private void startEditExerciseActivity(Context context, EditableExercise editableExercise, int requestCode) {
        //TODO: merge with WorkoutFragment - startEditExerciseActivity
        Intent intent = new Intent(context,EditExerciseActivity.class);
        intent.putExtra(EditExerciseActivity.EXTRA_EDITABLE_EXERCISE, editableExercise);
        startActivityForResult(intent, requestCode);
    }

    private NewEditableExercise createNewEditableExercise() {
        NewEditableExercise newEditableExercise = new NewEditableExercise();
        //TODO: extract to resources / factory pattern
        newEditableExercise.setWeight(2.5);
        newEditableExercise.setWeightRaise(1.25);
        newEditableExercise.setReps(12);
        newEditableExercise.setSets(3);
        return newEditableExercise;
    }

    private EditableExercise createExistingEditableExercise() {
        //TODO: factory pattern
        Exercise exercise = workout.getExercise(exercisePosition);
        return new ExistingEditableExercise(exercise);
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
        updateableActivity.notifyDataSetChanged();
    }

    private void changeSets(int moved) {
        setSet(workout.getExercise(exercisePosition).getSetNumber() + moved);
        setViews(exercisePosition);
    }

    private void setReps(int moved) {
        Exercise exercise = workout.getExercise(exercisePosition);
        exercise.setReps(exercise.getReps() + moved);
    }

    private void setSet(int setNumber) {
        workout.getExercise(exercisePosition).setCurrentSet(setNumber);
    }

    private void setViews(int exercisePosition) {
        repsProgressBar.updateProgressbar(workout.getExercise(exercisePosition).getCurrentSet());
        setsProgressBar.updateProgressbar(workout, exercisePosition);
    }
}
