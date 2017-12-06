package de.avalax.fitbuddy.presentation.edit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.avalax.fitbuddy.R;

import static android.graphics.Color.TRANSPARENT;

public class SelectableViewHolder extends RecyclerView.ViewHolder {
    private final TextView titleTextView;
    private final TextView subtitleTextView;

    public SelectableViewHolder(View v) {
        super(v);
        titleTextView = v.findViewById(R.id.item_title);
        subtitleTextView = v.findViewById(R.id.item_subtitle);
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
        int color = itemView.getResources().getColor(R.color.primaryLightColor);
        itemView.setBackgroundColor(color);
    }

    private void deselect() {
        itemView.setBackgroundColor(TRANSPARENT);
    }
}
