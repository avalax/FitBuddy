package de.avalax.fitbuddy.presentation.workout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import de.avalax.fitbuddy.application.workout.WorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.RessourceNotFoundException;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.R;
import de.avalax.fitbuddy.presentation.helper.ExerciseViewHelper;
import de.avalax.fitbuddy.presentation.workout.swipeBar.SwipeBarOnTouchListener;
import de.avalax.fitbuddy.presentation.workout.swipeBar.VerticalProgressbarView;

import javax.inject.Inject;

public class CurrentExerciseFragment extends Fragment {

    private static final String ARGS_EXERCISE_INDEX = "exerciseIndex";
    @InjectView(R.id.leftProgressBar)
    protected VerticalProgressbarView repsProgressBar;
    @InjectView(R.id.rightProgressBar)
    protected VerticalProgressbarView setsProgressBar;
    @Inject
    WorkoutApplicationService workoutApplicationService;
    @Inject
    ExerciseViewHelper exerciseViewHelper;
    private int exerciseIndex;

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
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        ButterKnife.inject(this, view);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        exerciseIndex = getArguments().getInt(ARGS_EXERCISE_INDEX);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Exercise exercise = workoutApplicationService.requestExercise(exerciseIndex);
            int indexOfCurrentSet = exercise.indexOfCurrentSet();
            repsProgressBar.setOnTouchListener(new SwipeBarOnTouchListener(getActivity(), repsProgressBar, exerciseViewHelper.maxRepsOfExercise(exercise, indexOfCurrentSet)) {
                @Override
                public void onFlingEvent(int moved) {
                    try {
                        changeReps(moved);
                    } catch (RessourceNotFoundException e) {
                        Log.d("Can't execute onFlingEvent", e.getMessage(), e);
                    }
                }
            });

            setsProgressBar.setOnTouchListener(new SwipeBarOnTouchListener(getActivity(), setsProgressBar, exerciseViewHelper.setCountOfExercise(exercise)) {
                @Override
                public void onFlingEvent(int moved) {
                    try {
                        moveToSet(moved);
                    } catch (RessourceNotFoundException e) {
                        Log.d("Can't execute onFlingEvent", e.getMessage(), e);
                    }
                }
            });

            setViews();
        } catch (RessourceNotFoundException e) {
            Log.d("Can't create fragment", e.getMessage(), e);
        }
    }

    private void changeReps(int moved) throws RessourceNotFoundException {
        workoutApplicationService.addRepsToSet(exerciseIndex, moved);
        setViews();
        updateWorkoutProgress();
    }

    private void moveToSet(int moved) throws RessourceNotFoundException {
        workoutApplicationService.switchToSet(exerciseIndex, moved);
        setViews();
        updateWorkoutProgress();
        updatePage();
    }

    private void setViews() throws RessourceNotFoundException {
        Exercise exercise = workoutApplicationService.requestExercise(exerciseIndex);
        int indexOfCurrentSet = exercise.indexOfCurrentSet();
        Set set = exercise.setAtPosition(indexOfCurrentSet);
        repsProgressBar.updateProgressbar(set);
        setsProgressBar.updateProgressbar(exercise);
    }

    private void updateWorkoutProgress() {
        ((MainActivity) getActivity()).updateWorkoutProgress(exerciseIndex);
    }

    private void updatePage() {
        ((MainActivity) getActivity()).updatePage(exerciseIndex);
    }
}
