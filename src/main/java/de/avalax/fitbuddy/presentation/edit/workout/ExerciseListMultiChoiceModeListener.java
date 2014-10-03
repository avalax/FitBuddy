package de.avalax.fitbuddy.presentation.edit.workout;

import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import de.avalax.fitbuddy.application.edit.workout.EditWorkoutApplicationService;
import de.avalax.fitbuddy.domain.model.RessourceNotFoundException;
import de.avalax.fitbuddy.presentation.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ExerciseListMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {
    private List<Integer> itemsChecked;
    private ExerciseListFragment exerciseListFragment;
    private EditWorkoutApplicationService editWorkoutApplicationService;
    private MenuItem moveExerciseUpMenuItem;
    private MenuItem moveExerciseDownMenuItem;
    private MenuItem deleteExerciseMenuItem;

    public ExerciseListMultiChoiceModeListener(ExerciseListFragment exerciseListFragment, EditWorkoutApplicationService editWorkoutApplicationService) {
        this.exerciseListFragment = exerciseListFragment;
        this.editWorkoutApplicationService = editWorkoutApplicationService;
        this.itemsChecked = new ArrayList<>();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked && !itemsChecked.contains(position)) {
            itemsChecked.add(position);
        } else {
            if (itemsChecked.contains(position)) {
                itemsChecked.remove(itemsChecked.indexOf((position)));
            }
        }
        Collections.sort(itemsChecked);
        Collections.reverse(itemsChecked);
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
            moveExerciseUp();
        }

        if (item.getItemId() == R.id.action_move_exercise_down) {
            moveExerciseDown();
        }

        if (item.getItemId() == R.id.action_delete_exercise) {
            deleteExercises();
            exerciseListFragment.initListView();
            mode.finish();
            return false;
        }
        exerciseListFragment.initListView();
        this.itemsChecked.clear();
        updateMenuItems();
        return false;
    }

    private void deleteExercises() {
        //TODO: delete more then one exercise with undo and move to applicationservice
        for (Integer position : itemsChecked) {
            try {
                editWorkoutApplicationService.deleteExercise(position);
            } catch (RessourceNotFoundException e) {
                Log.d("Can't delete exercises", e.getMessage(), e);
            }
        }
    }

    private void moveExerciseUp() {
        //TODO: move array edit to applicationservice
        for (Integer position : itemsChecked) {
            try {
                editWorkoutApplicationService.moveExerciseAtPositionUp(position);
            } catch (RessourceNotFoundException e) {
                Log.d("Can't move exercise up", e.getMessage(), e);
            }
        }
    }

    private void moveExerciseDown() {
        //TODO: move array edit to applicationservice
        for (Integer position : itemsChecked) {
            try {
                editWorkoutApplicationService.moveExerciseAtPositionDown(position);
            } catch (RessourceNotFoundException e) {
                Log.d("Can't move exercise down", e.getMessage(), e);
            }
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.itemsChecked.clear();
    }

    private void updateMenuItems() {
        int checkedItemCount = itemsChecked.size();
        boolean isOneItemSelected = checkedItemCount == 1;
        boolean isMoreItemsSelected = checkedItemCount >= 1;
        moveExerciseUpMenuItem.setVisible(isOneItemSelected);
        moveExerciseDownMenuItem.setVisible(isOneItemSelected);
        deleteExerciseMenuItem.setVisible(isMoreItemsSelected);
    }
}
