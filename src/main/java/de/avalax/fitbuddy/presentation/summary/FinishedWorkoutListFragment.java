package de.avalax.fitbuddy.presentation.summary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdView;

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.application.summary.FinishedWorkoutService;
import de.avalax.fitbuddy.domain.model.finished_workout.FinishedWorkout;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.MainActivity;
import de.avalax.fitbuddy.presentation.ad_mob.AdMobProvider;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutRecyclerView;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class FinishedWorkoutListFragment extends Fragment {

    @Inject
    protected FinishedWorkoutService finishedWorkoutService;
    @Inject
    protected FinishedWorkoutViewHelper finishedWorkoutViewHelper;
    @Inject
    protected AdMobProvider adMobProvider;

    private List<FinishedWorkout> finishedWorkouts;
    private WorkoutRecyclerView recyclerView;
    private FinishedWorkoutAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);

        View view = inflater.inflate(R.layout.fragment_finished_workout_list, container, false);
        recyclerView = view.findViewById(android.R.id.list);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        finishedWorkouts = finishedWorkoutService.loadAllFinishedWorkouts();
        adapter = new FinishedWorkoutAdapter((MainActivity) getActivity(),
                finishedWorkoutViewHelper, finishedWorkouts, finishedWorkoutService);
        recyclerView.setAdapter(adapter);
        Toolbar toolbar = view.findViewById(R.id.toolbar_main);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        AdView adView = view.findViewById(R.id.adView);
        adMobProvider.initAdView(adView);
        return view;
    }

    public void removeFinishedWorkout() {
        adapter.removeSelections();
        recyclerView.updateEmptyView();
    }
}
