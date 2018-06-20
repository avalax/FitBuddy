package de.avalax.fitbuddy.presentation.edit.exercise;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.exercise.Exercise;
import de.avalax.fitbuddy.domain.model.set.BasicSet;
import de.avalax.fitbuddy.presentation.edit.set.EditSetActivity;

import static android.support.v4.app.FragmentTransaction.TRANSIT_NONE;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static de.avalax.fitbuddy.presentation.FitbuddyApplication.ADD_SET;
import static java.lang.String.valueOf;

public class EditExerciseActivity extends AppCompatActivity {
    private Exercise exercise;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);
        ViewModelProviders.of(this).get(EditExerciseViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar_exercise_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        exercise = (Exercise) getIntent().getSerializableExtra("exercise");
        show(exercise);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_edit_exercise, menu);

        return true;
    }

    public void onAddSetButtonClick(View view) {
        Intent intent = new Intent(this, EditSetActivity.class);
        intent.putExtra("set", new BasicSet());
        startActivityForResult(intent, ADD_SET);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_save_exercise) {
            if (exercise.getSets().size() == 0) {
                Context context = getApplicationContext();
                makeText(context, R.string.message_save_exercise_without_sets, LENGTH_SHORT)
                        .show();
            } else {
                Intent intent = new Intent();
                EditText nameEditText = findViewById(R.id.edit_text_exercise_name);
                exercise.setName(nameEditText.getText().toString());
                intent.putExtra("exercise", exercise);
                int position = getIntent().getIntExtra("position", -1);
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                finish();
            }
            return true;
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        return fragment.onOptionsItemSelected(item);
    }

    public void onSelectionChange(int selectionCount) {
        if (selectionCount > 0) {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_edit_exercise_delete_sets, menu);
            MenuItem item = menu.findItem(R.id.toolbar_delete_sets);
            item.setTitle(valueOf(selectionCount));
        } else {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_edit_exercise, menu);
        }
    }

    public void onCancelButtonClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void show(Exercise exercise) {
        EditExerciseFragment fragment = EditExerciseFragment.forExercise(exercise);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("exercise")
                .replace(R.id.fragment_content, fragment, null)
                .setTransition(TRANSIT_NONE)
                .commit();
    }
}