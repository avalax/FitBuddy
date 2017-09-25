package de.avalax.fitbuddy.presentation.edit.set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.set.Set;
import de.avalax.fitbuddy.presentation.dialog.EditRepsDialogFragment;
import de.avalax.fitbuddy.presentation.dialog.EditWeightDialogFragment;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

public class EditSetActivity extends AppCompatActivity implements
        EditWeightDialogFragment.DialogListener,
        EditRepsDialogFragment.DialogListener {

    private TextView weightTextView;
    private TextView repsTextView;
    private Set set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_set);

        Toolbar toolbar = findViewById(R.id.toolbar_edit_set);
        setSupportActionBar(toolbar);
        set = (Set) getIntent().getSerializableExtra("set");
        repsTextView = findViewById(R.id.set_reps_text_view);
        weightTextView = findViewById(R.id.set_weight_text_view);
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
            set.setMaxReps(parseInt(repsTextView.getText().toString()));
            set.setWeight(parseDouble(weightTextView.getText().toString()));
            int position = getIntent().getIntExtra("position", -1);
            intent.putExtra("position", position);
            intent.putExtra("set", set);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onDialogPositiveClick(EditRepsDialogFragment editRepsDialogFragment) {
        int reps = editRepsDialogFragment.getReps();
        repsTextView.setText(valueOf(reps));
    }

    @Override
    public void onDialogPositiveClick(EditWeightDialogFragment editWeightDialogFragment) {
        double weight = editWeightDialogFragment.getWeight();
        weightTextView.setText(valueOf(weight));
    }
}