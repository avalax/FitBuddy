package de.avalax.fitbuddy.presentation.summary;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.finishedWorkout.FinishedWorkout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;

public class FinishedWorkoutListFragment extends ListFragment {

    @Inject
    protected FinishedWorkoutApplicationService finishedWorkoutApplicationService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((FitbuddyApplication) getActivity().getApplication()).inject(this);
        View view = inflater.inflate(R.layout.fragment_finished_workout_list, container, false);
        ButterKnife.bind(this, view);
        initListView();
        return view;
    }

    protected void initListView() {
        List<FinishedWorkout> finishedWorkouts = finishedWorkoutApplicationService.allFinishedWorkouts();
        ListAdapter adapter = new FinishedWorkoutAdapter(getActivity(), R.layout.item_finished_workout, finishedWorkouts);
        setListAdapter(adapter);
    }

    @OnClick(android.R.id.empty)
    protected void finishActivity() {
        getActivity().finish();
    }

}
