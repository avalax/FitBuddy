package de.avalax.fitbuddy.presentation.welcome_screen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class WorkoutRecyclerView extends RecyclerView {
    private View mEmptyView;

    public WorkoutRecyclerView(Context context) {
        super(context);
    }

    public WorkoutRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WorkoutRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        updateEmptyView();
    }

    public void updateEmptyView() {
        if (mEmptyView != null && getAdapter() != null) {
            boolean showEmptyView = getAdapter().getItemCount() == 0;
            mEmptyView.setVisibility(showEmptyView ? VISIBLE : GONE);
            setVisibility(showEmptyView ? GONE : VISIBLE);
        }
    }
}
