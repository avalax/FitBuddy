package de.avalax.fitbuddy.app.edit;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.avalax.fitbuddy.app.R;

public class EditExercisePagerAdapter extends FragmentStatePagerAdapter {

    private EditableExercise editableExercise;
    private String newExerciseName;
    private String editWeightTitle;
    private String editEffortTitle;
    private String editConfirmationTitle;

    public EditExercisePagerAdapter(FragmentManager fm, Context context, EditableExercise editableExercise) {
        super(fm);
        init(context, editableExercise);
    }

    private void init(Context context, EditableExercise editableExercise) {
        Resources resources = context.getResources();
        this.newExerciseName = resources.getString(R.string.new_exercise_name);
        this.editWeightTitle = resources.getString(R.string.edit_weight_title);
        this.editEffortTitle = resources.getString(R.string.edit_effort_title);
        this.editConfirmationTitle = resources.getString(R.string.edit_confirmation_title);
        this.editableExercise = editableExercise;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return WeightExerciseFragment.newInstance(editableExercise);
        } else if (position == 1) {
            return EffortExerciseFragment.newInstance(editableExercise);
        } else {
            return ConfirmationExerciseFragment.newInstance(editableExercise);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = editableExercise.getName();
        if (title == null) {
            title = newExerciseName;
        }
        return getTitleFor(position, title);
    }

    private CharSequence getTitleFor(int position, String name) {
        if (position == 0) {
            return String.format(editWeightTitle, name);
        } else if (position == 1) {
            return String.format(editEffortTitle, name);
        } else {
            return String.format(editConfirmationTitle, name);
        }
    }
}
