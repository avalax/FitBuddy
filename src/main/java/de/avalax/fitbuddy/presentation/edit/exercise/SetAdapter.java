package de.avalax.fitbuddy.presentation.edit.exercise;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetException;
import de.avalax.fitbuddy.domain.model.set.Sets;
import de.avalax.fitbuddy.presentation.edit.SelectableViewHolder;
import de.avalax.fitbuddy.presentation.edit.set.EditSetActivity;

import static de.avalax.fitbuddy.presentation.edit.exercise.EditExerciseActivity.EDIT_SET;

public class SetAdapter extends RecyclerView.Adapter<SelectableViewHolder> {
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
    public SelectableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_set, parent, false);
        return new SelectableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectableViewHolder holder, int position) {
        try {
            Set set = sets.get(position);
            String title = editExerciseViewHelper.title(set);
            String subtitle = editExerciseViewHelper.subtitle(set);

            holder.getTitleTextView().setText(title);
            holder.getSubtitleTextView().setText(subtitle);
            holder.deselect();

            holder.getView().setOnClickListener(view -> {
                if (selections.isEmpty()) {
                    Intent intent = new Intent(activity, EditSetActivity.class);
                    intent.putExtra("set", set);
                    intent.putExtra("position", holder.getAdapterPosition());
                    activity.startActivityForResult(intent, EDIT_SET);
                } else {
                    if (isSelected(holder)) {
                        deselect(holder);
                    } else {
                        select(holder);
                    }
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

    private boolean isSelected(SelectableViewHolder holder) {
        boolean isSelected = false;
        try {
            isSelected = selections.contains(sets.get(holder.getAdapterPosition()));

        } catch (SetException e) {
            Log.e("SetException", e.getMessage(), e);
        }
        return isSelected;
    }

    private void deselect(SelectableViewHolder holder) {
        try {
            selections.remove(sets.get(holder.getAdapterPosition()));
            holder.deselect();
            ((EditExerciseActivity) activity).updateToolbar(selections.size());
        } catch (SetException e) {
            Log.e("SetException", e.getMessage(), e);
        }
    }

    private void select(SelectableViewHolder holder) {
        try {
            selections.add(sets.get(holder.getAdapterPosition()));
            holder.select();
            ((EditExerciseActivity) activity).updateToolbar(selections.size());
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

    public void removeSelections() {
        sets.removeAll(selections);
        selections.clear();
        notifyDataSetChanged();
    }
}
