package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutRecyclerView;

import static java.lang.String.valueOf;

public class EditExerciseFragment extends Fragment {

    private SetAdapter exerciseAdapter;
    private List<Set> exerciseListEntries;
    private WorkoutRecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_exercise, container, false);
        recyclerView = view.findViewById(android.R.id.list);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));
        exerciseListEntries = new ArrayList<>();
        exerciseAdapter = new SetAdapter(getActivity(), exerciseListEntries);
        recyclerView.setAdapter(exerciseAdapter);
        return view;
    }

    public void addSet(Set exercise) {
        exerciseListEntries.add(exercise);
        exerciseAdapter.notifyItemInserted(exerciseListEntries.size() - 1);
        recyclerView.updateEmptyView();
    }

    private class SetAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<Set> sets;
        private Context mContext;

        SetAdapter(Context context, List<Set> sets) {
            super();
            mContext = context;
            this.sets = sets;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_set, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Set set = sets.get(position);
            holder.getTitleTextView().setText(valueOf(set.getMaxReps()));
            holder.getSubtitleTextView().setText(valueOf(set.getWeight()));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return sets == null ? 0 : sets.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView subtitleTextView;

        ViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.item_title);
            subtitleTextView = v.findViewById(R.id.item_subtitle);
        }

        TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getSubtitleTextView() {
            return subtitleTextView;
        }
    }
}
