package de.avalax.fitbuddy.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.app.edit.EditExerciseActivity;
import de.avalax.fitbuddy.app.edit.EditableExercise;
import de.avalax.fitbuddy.app.edit.ExistingEditableExercise;
import de.avalax.fitbuddy.app.edit.NewEditableExercise;
import de.avalax.fitbuddy.app.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.app.swipeBar.VerticalProgressbarView;
import de.avalax.fitbuddy.core.workout.Exercise;
import de.avalax.fitbuddy.core.workout.Workout;

import javax.inject.Inject;

public class CurrentExerciseFragment extends Fragment {

    private static final String ARGS_EXERCISE_INDEX = "exerciseIndex";
    private static final int ADD_EXERCISE_BEFORE = 1;
    private static final int EDIT_EXERCISE = 2;
    private static final int ADD_EXERCISE_AFTER = 3;
    @InjectView(R.id.leftProgressBar)
    protected VerticalProgressbarView repsProgressBar;
    @InjectView(R.id.rightProgressBar)
    protected VerticalProgressbarView setsProgressBar;
    @Inject
    protected WorkoutSession workoutSession;
    private int exercisePosition;
    private ViewPager viewPager;

    public static CurrentExerciseFragment newInstance(int exerciseIndex) {
        CurrentExerciseFragment fragment = new CurrentExerciseFragment();
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
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        ButterKnife.inject(this, view);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Workout workout = workoutSession.getWorkout();
        final Context context = getActivity();
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
        Workout workout = workoutSession.getWorkout();
        if (resultCode == Activity.RESULT_OK) {
            EditableExercise editableExercise = (EditableExercise) intent.getSerializableExtra(EditExerciseActivity.EXTRA_EDITABLE_EXERCISE);
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
            viewPager.getAdapter().notifyDataSetChanged();
            viewPager.invalidate();
        } else if (resultCode == Activity.RESULT_FIRST_USER && requestCode == EDIT_EXERCISE) {
            workout.removeExercise(exercisePosition);
            viewPager.getAdapter().notifyDataSetChanged();
            viewPager.invalidate();
        }
    }

    private void startEditExerciseActivity(Context context, EditableExercise editableExercise, int requestCode) {
        //TODO: merge with WorkoutFragment - startEditExerciseActivity
        Intent intent = new Intent(context, EditExerciseActivity.class);
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
        Workout workout = workoutSession.getWorkout();
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
        //TODO: only update ResultChartFragment
        viewPager.getAdapter().notifyDataSetChanged();
        viewPager.invalidate();
    }

    private void changeSets(int moved) {
        Workout workout = workoutSession.getWorkout();
        setSet(workout.getExercise(exercisePosition).getSetNumber() + moved);
        setViews(exercisePosition);
    }

    private void setReps(int moved) {
        Workout workout = workoutSession.getWorkout();
        Exercise exercise = workout.getExercise(exercisePosition);
        exercise.setReps(exercise.getReps() + moved);
    }

    private void setSet(int setNumber) {
        Workout workout = workoutSession.getWorkout();
        workout.getExercise(exercisePosition).setCurrentSet(setNumber);
    }

    private void setViews(int exercisePosition) {
        Workout workout = workoutSession.getWorkout();
        repsProgressBar.updateProgressbar(workout.getExercise(exercisePosition).getCurrentSet());
        setsProgressBar.updateProgressbar(workout, exercisePosition);
    }
}
