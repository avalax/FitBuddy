package de.avalax.fitbuddy.presentation.edit.workout;

import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import de.avalax.fitbuddy.presentation.R;

class ExerciseListMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {
    private ExerciseListFragment exerciseListFragment;
    private MenuItem addExerciseMenuItem;
    private MenuItem moveExerciseUpMenuItem;
    private MenuItem moveExerciseDownMenuItem;

    public ExerciseListMultiChoiceModeListener(ExerciseListFragment exerciseListFragment) {
        this.exerciseListFragment = exerciseListFragment;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        updateMenuItems();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.edit_exerciselist_actions, mode.getMenu());
        mode.setTitle(exerciseListFragment.getResources().getText(R.string.cab_title_manage_exercises));
        addExerciseMenuItem = menu.findItem(R.id.action_add_exercise);
        moveExerciseUpMenuItem = menu.findItem(R.id.action_move_exercise_up);
        moveExerciseDownMenuItem = menu.findItem(R.id.action_move_exercise_down);
        updateMenuItems();
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.action_add_exercise) {
            Log.d("onActionItemClicked","add exercise");
        }
        if (item.getItemId() == R.id.action_move_exercise_up) {
            Log.d("onActionItemClicked","move exercise up");
        }
        if (item.getItemId() == R.id.action_move_exercise_down) {
            Log.d("onActionItemClicked","move exercise down");
        }
        if (item.getItemId() == R.id.action_delete_exercise) {
            Log.d("onActionItemClicked","add exercise after");
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    private void updateMenuItems() {
        boolean multiSelect = exerciseListFragment.getListView().getCheckedItemCount() > 1;
        addExerciseMenuItem.setVisible(!multiSelect);
        moveExerciseUpMenuItem.setVisible(!multiSelect);
        moveExerciseDownMenuItem.setVisible(!multiSelect);
    }
}
