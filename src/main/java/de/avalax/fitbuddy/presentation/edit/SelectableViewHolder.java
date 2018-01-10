package de.avalax.fitbuddy.presentation.edit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.avalax.fitbuddy.R;

public class SelectableViewHolder extends RecyclerView.ViewHolder {
    private final TextView titleTextView;
    private final TextView subtitleTextView;
    private final int backgroundColor;
    private final int highlightColor;
    private boolean selected;

    public SelectableViewHolder(View view, int backgroundColor, int highlightColor) {
        super(view);
        titleTextView = view.findViewById(R.id.item_title);
        subtitleTextView = view.findViewById(R.id.item_subtitle);
        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getSubtitleTextView() {
        return subtitleTextView;
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
