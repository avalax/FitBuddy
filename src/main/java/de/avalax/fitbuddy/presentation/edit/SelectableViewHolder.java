package de.avalax.fitbuddy.presentation.edit;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class SelectableViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {
    private final int backgroundColor;
    private final int highlightColor;
    private boolean selected;
    private SelectionListener listener;

    public SelectableViewHolder(View view, int backgroundColor, int highlightColor) {
        super(view);
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
        this.selected = false;
    }

    public View getView() {
        return itemView;
    }

    public void setSelected(boolean selected) {
        if (selected) {
            select();
        } else {
            deselect();
        }
    }

    private void select() {
        getView().setBackgroundColor(highlightColor);
        selected = true;
        if (listener != null) {
            listener.onSelect(this, true);
        }
    }

    private void deselect() {
        getView().setBackgroundColor(backgroundColor);
        selected = false;
        if (listener != null) {
            listener.onSelect(this, false);
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelectionListener(SelectionListener listener) {
        this.listener = listener;
    }

    public interface SelectionListener {
        void onSelect(SelectableViewHolder selectableViewHolder, boolean selected);
    }
}
