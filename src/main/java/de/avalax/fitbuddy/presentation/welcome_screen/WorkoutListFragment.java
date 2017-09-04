package de.avalax.fitbuddy.presentation.welcome_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.workout.WorkoutListEntry;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;

public class WorkoutListFragment extends Fragment {

    private WorkoutRecyclerView recyclerView;

    private WorkoutAdapter mAdapter;
    @Inject
    WorkoutRepository workoutRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);

        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        recyclerView = view.findViewById(android.R.id.list);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));
        updateWorkoutsFromRepository();
        return view;
    }

    public void updateWorkoutsFromRepository() {
        List<WorkoutListEntry> workouts = workoutRepository.getWorkoutList();
        mAdapter = new WorkoutAdapter(getActivity(), workouts);
        recyclerView.setAdapter(mAdapter);
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<ViewHolder> {

        public List<WorkoutListEntry> mAttractionList;
        private Context mContext;

        public WorkoutAdapter(Context context, List<WorkoutListEntry> attractions) {
            super();
            mContext = context;
            mAttractionList = attractions;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.card_workout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            WorkoutListEntry workout = mAttractionList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mAttractionList == null ? 0 : mAttractionList.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }
}
