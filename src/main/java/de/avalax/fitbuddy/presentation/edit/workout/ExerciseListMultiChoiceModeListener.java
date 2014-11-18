package de.avalax.fitbuddy.presentation.edit.workout;

import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.RessourceNotFoundException;
import de.avalax.fitbuddy.R;

class ExerciseListMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {
    private ItemsWithCheckedState itemsChecked;
    private ExerciseListFragment exerciseListFragment;
    private EditWorkoutApplicationService editWorkoutApplicationService;
    private MenuItem moveExerciseUpMenuItem;
    private MenuItem moveExerciseDownMenuItem;
    private MenuItem deleteExerciseMenuItem;

    public ExerciseListMultiChoiceModeListener(ExerciseListFragment exerciseListFragment, EditWorkoutApplicationService editWorkoutApplicationService) {
        this.exerciseListFragment = exerciseListFragment;
        this.editWorkoutApplicationService = editWorkoutApplicationService;
        this.itemsChecked = new ItemsWithCheckedState();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked) {
            itemsChecked.addCheckedItem(position);
        } else {
            itemsChecked.removeCheckedItem(position);
        }
        updateMenuItems();
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.edit_exerciselist_actions, mode.getMenu());
        mode.setTitle(exerciseListFragment.getResources().getText(R.string.cab_title_manage_exercises));
        moveExerciseUpMenuItem = menu.findItem(R.id.action_move_exercise_up);
        moveExerciseDownMenuItem = menu.findItem(R.id.action_move_exercise_down);
        deleteExerciseMenuItem = menu.findItem(R.id.action_delete_exercise);
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
            editWorkoutApplicationService.createExercise();
        }
        if (item.getItemId() == R.id.action_move_exercise_up) {
            for (int position : itemsChecked.list()) {
                moveExerciseUp(position);
            }
        }

        if (item.getItemId() == R.id.action_move_exercise_down) {
            for (int position : itemsChecked.list()) {
                moveExerciseDown(position);
            }
        }

        if (item.getItemId() == R.id.action_delete_exercise) {
            deleteExercises();
            exerciseListFragment.initListView();
            mode.finish();
            return false;
        }
        exerciseListFragment.initListView();
        itemsChecked.clear();
        updateMenuItems();
        return false;
    }

    private void deleteExercises() {
        try {
            editWorkoutApplicationService.deleteExercise(itemsChecked.list());
        } catch (RessourceNotFoundException e) {
            Log.d("Can't delete exercises", e.getMessage(), e);
        }
    }

    private void moveExerciseUp(int position) {
        try {
            editWorkoutApplicationService.moveExerciseAtPositionUp(position);
        } catch (RessourceNotFoundException e) {
            Log.d("Can't move exercise up", e.getMessage(), e);
        }
    }

    private void moveExerciseDown(int position) {
        try {
            editWorkoutApplicationService.moveExerciseAtPositionDown(position);
        } catch (RessourceNotFoundException e) {
            Log.d("Can't move exercise down", e.getMessage(), e);
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.itemsChecked.clear();
    }

    private void updateMenuItems() {
        int checkedItemCount = itemsChecked.list().size();
        boolean isOneItemSelected = checkedItemCount == 1;
        boolean isMoreItemsSelected = checkedItemCount >= 1;
        moveExerciseUpMenuItem.setVisible(isOneItemSelected);
        moveExerciseDownMenuItem.setVisible(isOneItemSelected);
        deleteExerciseMenuItem.setVisible(isMoreItemsSelected);
    }
}
