package de.avalax.fitbuddy.presentation.edit.exercise;

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

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutRecyclerView;

public class SetListFragment extends Fragment {

    private SetAdapter setAdapter;
    private List<Set> sets;
    private WorkoutRecyclerView recyclerView;

    @Inject
    protected EditExerciseApplicationService editExerciseApplicationService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_exercise, container, false);
        ((FitbuddyApplication) getActivity().getApplication()).getComponent().inject(this);
        recyclerView = view.findViewById(android.R.id.list);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));
        sets = new ArrayList<>();
        setAdapter = new SetAdapter(getActivity(), sets);
        recyclerView.setAdapter(setAdapter);
        return view;
    }

    public void addSet(Set set) {
        sets.add(set);
        setAdapter.notifyItemInserted(sets.size() - 1);
        recyclerView.updateEmptyView();
    }

    private class SetAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Set> sets;
        private Context context;

        SetAdapter(Context context, List<Set> sets) {
            super();
            this.context = context;
            this.sets = sets;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_set, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Set set = sets.get(position);
            String title = editExerciseApplicationService.title(set);
            String subtitle = editExerciseApplicationService.subtitle(set);

            holder.getTitleTextView().setText(title);
            holder.getSubtitleTextView().setText(subtitle);
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

        TextView getSubtitleTextView() {
            return subtitleTextView;
        }
    }
}
