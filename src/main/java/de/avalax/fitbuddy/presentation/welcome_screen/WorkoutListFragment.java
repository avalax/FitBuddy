package de.avalax.fitbuddy.presentation.welcome_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.workout.Workout;
import de.avalax.fitbuddy.domain.model.workout.WorkoutListEntry;
import de.avalax.fitbuddy.domain.model.workout.WorkoutRepository;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;

public class WorkoutListFragment extends Fragment {

    @Inject
    WorkoutRepository workoutRepository;

    private WorkoutAdapter workoutAdapter;

    private List<WorkoutListEntry> workoutListEntries;
    private WorkoutRecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);

        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        recyclerView = view.findViewById(android.R.id.list);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));
        workoutListEntries = workoutRepository.getWorkoutList();
        workoutAdapter = new WorkoutAdapter(getActivity(), workoutListEntries);
        recyclerView.setAdapter(workoutAdapter);
        return view;
    }

    public void addWorkout(Workout workout) {
        workoutListEntries.add(new WorkoutListEntry(workout.getWorkoutId(), workout.getName()));
        workoutAdapter.notifyItemInserted(workoutListEntries.size() - 1);
        recyclerView.updateEmptyView();
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<WorkoutListEntry> workoutListEntries;
        private Context mContext;

        WorkoutAdapter(Context context, List<WorkoutListEntry> workoutListEntries) {
            super();
            mContext = context;
            this.workoutListEntries = workoutListEntries;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.card_workout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            WorkoutListEntry workout = workoutListEntries.get(position);
            holder.getTitleTextView().setText(workout.getName());
            holder.getSubtitleTextView().setText("Executed 0 times");
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return workoutListEntries == null ? 0 : workoutListEntries.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView dateTextView;
        private final TextView subtitleTextView;

        ViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.card_title);
            subtitleTextView = v.findViewById(R.id.card_subtitle);
            dateTextView = v.findViewById(R.id.card_date);

        }

        TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }

        public TextView getSubtitleTextView() {
            return subtitleTextView;
        }
    }
}
