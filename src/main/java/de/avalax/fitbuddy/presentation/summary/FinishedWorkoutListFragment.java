package de.avalax.fitbuddy.presentation.summary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.MainActivity;

public class FinishedWorkoutListFragment extends Fragment {

    @Inject
    protected FinishedWorkoutApplicationService applicationService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);

        View view = inflater.inflate(R.layout.fragment_finished_workout_list, container, false);
        RecyclerView recycleView = view.findViewById(android.R.id.list);
        List<FinishedWorkout> finishedWorkouts = applicationService.loadAllFinishedWorkouts();
        FinishedWorkoutAdapter adapter = new FinishedWorkoutAdapter((MainActivity) getActivity(), finishedWorkouts);
        recycleView.setAdapter(adapter);
        Toolbar toolbar = view.findViewById(R.id.toolbar_main);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return view;
    }

}
