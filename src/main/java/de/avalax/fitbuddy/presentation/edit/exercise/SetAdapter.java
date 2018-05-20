package de.avalax.fitbuddy.presentation.edit.exercise;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetException;
import de.avalax.fitbuddy.domain.model.set.Sets;
import de.avalax.fitbuddy.presentation.edit.SelectableViewHolder;
import de.avalax.fitbuddy.presentation.edit.set.EditSetActivity;

import static android.graphics.Color.TRANSPARENT;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.EDIT_SET;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.SetViewHolder> {
    private Sets sets;
    private EditExerciseViewHelper editExerciseViewHelper;
    private Activity activity;
    private java.util.Set<Set> selections;

    public SetAdapter(Activity activity,
                      Sets sets,
                      EditExerciseViewHelper editExerciseViewHelper) {
        super();
        this.activity = activity;
        this.sets = sets;
        this.editExerciseViewHelper = editExerciseViewHelper;
        this.selections = new ArraySet<>();
    }

    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_set, parent, false);
        int highlightColor = view.getResources().getColor(R.color.primaryLightColor);
        return new SetViewHolder(view, TRANSPARENT, highlightColor);
    }

    @Override
    public void onBindViewHolder(SetViewHolder holder, int position) {
        try {
            Set set = sets.get(position);
            String title = editExerciseViewHelper.repsFrom(set);
            String subtitle = editExerciseViewHelper.weightFrom(set);

            holder.getTitleTextView().setText(title);
            holder.getSubtitleTextView().setText(subtitle);
            holder.setSelected(selections.contains(set));

            holder.getView().setOnClickListener(view -> {
                if (selections.isEmpty()) {
                    Intent intent = new Intent(activity, EditSetActivity.class);
                    intent.putExtra("set", set);
                    intent.putExtra("position", holder.getAdapterPosition());
                    activity.startActivityForResult(intent, EDIT_SET);
                } else {
                    if (isSelected(set)) {
                        deselect(holder.getAdapterPosition(), set);
                    } else {
                        select(holder.getAdapterPosition(), set);
                    }
                }
            });
            holder.getView().setOnLongClickListener(view -> {
                select(holder.getAdapterPosition(), set);
                return true;
            });
        } catch (SetException e) {
            Log.e("SetException", e.getMessage(), e);
        }
    }

    private boolean isSelected(Set set) {
        return selections.contains(set);
    }

    private void deselect(int position, Set set) {
        selections.remove(set);
        notifyItemChanged(position);
        ((EditExerciseActivity) activity).updateToolbar(selections.size());
    }

    private void select(int position, Set set) {
        selections.add(set);
        notifyItemChanged(position);
        ((EditExerciseActivity) activity).updateToolbar(selections.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return sets == null ? 0 : sets.size();
    }

    void removeSelections() {
        for (Set set : selections) {
            sets.remove(set);
        }
        selections.clear();
        notifyDataSetChanged();
    }

    public static class SetViewHolder extends SelectableViewHolder {
        private final TextView titleTextView;
        private final TextView subtitleTextView;

        public SetViewHolder(View view, int backgroundColor, int highlightColor) {
            super(view, backgroundColor, highlightColor);
            titleTextView = view.findViewById(R.id.item_title);
            subtitleTextView = view.findViewById(R.id.item_subtitle);
        }

        TextView getTitleTextView() {
            return titleTextView;
        }

        TextView getSubtitleTextView() {
            return subtitleTextView;
        }
    }
}
