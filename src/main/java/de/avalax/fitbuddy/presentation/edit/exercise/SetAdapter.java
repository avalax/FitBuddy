package de.avalax.fitbuddy.presentation.edit.exercise;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.databinding.SetItemBinding;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.domain.model.set.SetException;
import de.avalax.fitbuddy.domain.model.set.Sets;
import de.avalax.fitbuddy.presentation.edit.SelectableViewHolder;

import static android.graphics.Color.TRANSPARENT;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.SetViewHolder> implements SelectableViewHolder.SelectionListener {
    private Sets sets;

    private java.util.Set<Set> selections;

    private SetViewHolderCallback callback;

    SetAdapter(SetViewHolderCallback callback) {
        super();
        this.callback = callback;
        this.selections = new ArraySet<>();
    }

    public void setSets(Sets sets) {
        notifyItemRangeRemoved(0, this.sets == null ? 0 : this.sets.size());
        this.sets = sets;
        notifyItemRangeInserted(0, sets.size());
    }

    public java.util.Set<Set> getSelections() {
        return selections;
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SetItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.set_item,
                        parent, false);
        int highlightColor = parent.getResources().getColor(R.color.primaryLightColor);
        SetViewHolder setViewHolder =
                new SetViewHolder(binding, callback, selections, TRANSPARENT, highlightColor);
        setViewHolder.setSelectionListener(this);
        return setViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        try {
            Set set = sets.get(position);
            holder.getBinding().setSet(set);
            holder.getBinding().executePendingBindings();
            holder.setSelected(selections.contains(set));
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

    void clearSelections() {
        selections.clear();
    }

    @Override
    public void onSelect(SelectableViewHolder selectableViewHolder, boolean selected) {
        try {
            Set set = sets.get(selectableViewHolder.getAdapterPosition());
            if (selected) {
                selections.add(set);
            } else {
                selections.remove(set);
            }
        } catch (SetException e) {
            Log.e("SetException", e.getMessage(), e);
        }
    }

    public static class SetViewHolder extends SelectableViewHolder {
        private SetItemBinding binding;
        private final SetViewHolderCallback callback;
        private final java.util.Set<Set> selections;

        public SetViewHolder(SetItemBinding binding,
                             SetViewHolderCallback callback,
                             java.util.Set<Set> selections,
                             int backgroundColor,
                             int highlightColor) {
            super(binding.getRoot(), backgroundColor, highlightColor);
            this.binding = binding;
            this.callback = callback;
            this.selections = selections;
            binding.getRoot().setOnClickListener(this);
            binding.getRoot().setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (selections.isEmpty()) {
                callback.onItemClick(view, getAdapterPosition());
            } else {
                setSelected(!isSelected());
                callback.onSelectionChange(selections.size());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            setSelected(true);
            callback.onSelectionChange(selections.size());
            return true;
        }

        public SetItemBinding getBinding() {
            return binding;
        }
    }

    public interface SetViewHolderCallback {
        void onItemClick(View view, int position);

        void onSelectionChange(int size);
    }
}
