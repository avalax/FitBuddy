package de.avalax.fitbuddy.presentation.edit.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import de.avalax.fitbuddy.R;

public class EditExerciseActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText setsEditText;
    private EditText repsEditText;
    private EditText weightEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);

        Toolbar toolbar = findViewById(R.id.toolbar_edit_exercise);
        setSupportActionBar(toolbar);
        nameEditText = findViewById(R.id.edit_text_exercise_name);
        setsEditText = findViewById(R.id.edit_text_exercise_sets);
        repsEditText = findViewById(R.id.edit_text_exercise_reps);
        weightEditText = findViewById(R.id.edit_text_exercise_weight);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_exercise, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_save_exercise) {
            Intent intent = new Intent();
            intent.putExtra("name", nameEditText.getText().toString());
            intent.putExtra("sets", setsEditText.getText().toString());
            intent.putExtra("reps", repsEditText.getText().toString());
            intent.putExtra("weight", weightEditText.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return false;
    }
}