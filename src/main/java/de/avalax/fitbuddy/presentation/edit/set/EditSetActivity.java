package de.avalax.fitbuddy.presentation.edit.set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import de.avalax.fitbuddy.R;

import static java.lang.Double.*;
import static java.lang.Integer.parseInt;

public class EditSetActivity extends AppCompatActivity {

    private EditText repsEditText;
    private EditText weightEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_set);

        Toolbar toolbar = findViewById(R.id.toolbar_edit_set);
        setSupportActionBar(toolbar);
        repsEditText = findViewById(R.id.edit_text_set_reps);
        weightEditText = findViewById(R.id.edit_text_set_weight);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_set, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_save_set) {
            Intent intent = new Intent();
            intent.putExtra("maxReps", parseInt(repsEditText.getText().toString()));
            intent.putExtra("weight", parseDouble(weightEditText.getText().toString()));
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return false;
    }
}