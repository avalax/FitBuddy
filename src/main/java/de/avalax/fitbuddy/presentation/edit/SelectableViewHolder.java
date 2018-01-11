package de.avalax.fitbuddy.presentation.edit;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SelectableViewHolder extends RecyclerView.ViewHolder {
    private final int backgroundColor;
    private final int highlightColor;
    private boolean selected;

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
    }

    private void deselect() {
        getView().setBackgroundColor(backgroundColor);
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }
}
