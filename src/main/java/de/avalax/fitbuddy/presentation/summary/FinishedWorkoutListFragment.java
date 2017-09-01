package de.avalax.fitbuddy.presentation.summary;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;

public class FinishedWorkoutListFragment extends ListFragment {

    @Inject
    protected FinishedWorkoutApplicationService applicationService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_finished_workout_list, container, false);
        initListView();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        /**view.findViewById(android.R.id.empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });**/
    }

    protected void initListView() {
        List<FinishedWorkout> finishedWorkouts = applicationService.allFinishedWorkouts();
        ListAdapter adapter = new FinishedWorkoutAdapter(getActivity(),
                R.layout.item_finished_workout, finishedWorkouts);
        setListAdapter(adapter);
    }

    private void finishActivity() {
        getActivity().finish();
    }

}
