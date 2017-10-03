package de.avalax.fitbuddy.presentation.edit.exercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetException;
import de.avalax.fitbuddy.domain.model.set.Sets;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.edit.set.EditSetActivity;
import de.avalax.fitbuddy.presentation.welcome_screen.WorkoutRecyclerView;

import static android.graphics.Color.TRANSPARENT;
import static de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity.EDIT_SET;

public class SetListFragment extends Fragment {

    private SetAdapter setAdapter;
    private Sets sets;
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
        Exercise exercise = (Exercise) getActivity().getIntent().getSerializableExtra("exercise");
        sets = exercise.getSets();
        setAdapter = new SetAdapter(getActivity(), sets);
        recyclerView.setAdapter(setAdapter);
        return view;
    }

    public void notifyItemInserted() {
        setAdapter.notifyItemInserted(sets.size() - 1);
        recyclerView.updateEmptyView();
    }

    public void notifyItemChanged(int position) {
        setAdapter.notifyItemChanged(position);
        recyclerView.updateEmptyView();
    }

    public void removeSelections() {
        setAdapter.removeSelections();
        recyclerView.updateEmptyView();
        ((EditExerciseActivity) getActivity()).updateToolbar(0);
    }

    private class SetAdapter extends RecyclerView.Adapter<ViewHolder> {
        private Sets sets;
        private Context context;
        private java.util.Set<Set> selections;

        SetAdapter(Context context, Sets sets) {
            super();
            this.context = context;
            this.sets = sets;
            this.selections = new ArraySet<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_set, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            try {
                Set set = sets.get(position);
                String title = editExerciseApplicationService.title(set);
                String subtitle = editExerciseApplicationService.subtitle(set);

                holder.getTitleTextView().setText(title);
                holder.getSubtitleTextView().setText(subtitle);
                holder.deselect();

                holder.getView().setOnClickListener(view -> {
                    if (!selections.isEmpty()) {
                        if (isSelected(holder)) {
                            deselect(holder);
                        } else {
                            select(holder);
                        }
                    } else {
                        Intent intent = new Intent(getActivity(), EditSetActivity.class);
                        intent.putExtra("set", set);
                        intent.putExtra("position", holder.getAdapterPosition());
                        getActivity().startActivityForResult(intent, EDIT_SET);
                    }
                });
                holder.getView().setOnLongClickListener(view -> {
                    select(holder);
                    return true;
                });
            } catch (SetException e) {
                Log.e("SetException", e.getMessage(), e);
            }
        }

        private boolean isSelected(ViewHolder holder) {
            boolean isSelected = false;
            try {
                isSelected = selections.contains(sets.get(holder.getAdapterPosition()));

            } catch (SetException e) {
                Log.e("SetException", e.getMessage(), e);
            }
            return isSelected;
        }

        private void deselect(ViewHolder holder) {
            try {
                selections.remove(sets.get(holder.getAdapterPosition()));
                holder.deselect();
                ((EditExerciseActivity) getActivity()).updateToolbar(selections.size());
            } catch (SetException e) {
                Log.e("SetException", e.getMessage(), e);
            }
        }

        private void select(ViewHolder holder) {
            try {
                selections.add(sets.get(holder.getAdapterPosition()));
                holder.select();
                ((EditExerciseActivity) getActivity()).updateToolbar(selections.size());
            } catch (SetException e) {
                Log.e("SetException", e.getMessage(), e);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return sets == null ? 0 : sets.size();
        }

        private void removeSelections() {
            sets.removeAll(selections);
            selections.clear();
            notifyDataSetChanged();
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

        View getView() {
            return itemView;
        }

        void select() {
            int color = itemView.getResources().getColor(R.color.primaryDarkColor);
            itemView.setBackgroundColor(color);
        }

        void deselect() {
            itemView.setBackgroundColor(TRANSPARENT);
        }
    }
}
