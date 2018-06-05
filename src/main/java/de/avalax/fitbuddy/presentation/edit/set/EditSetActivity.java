package de.avalax.fitbuddy.presentation.edit.set;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import de.avalax.fitbuddy.R;
import de.avalax.fitbuddy.domain.model.set.Set;

import static android.support.v4.app.FragmentTransaction.TRANSIT_NONE;

public class EditSetActivity extends AppCompatActivity {

    private Set set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_set);
        ViewModelProviders.of(this).get(EditSetViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar_set_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        set = (Set) getIntent().getSerializableExtra("set");
        show(set);
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
            int position = getIntent().getIntExtra("position", -1);
            intent.putExtra("position", position);
            intent.putExtra("set", set);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return false;
    }

    public void onCancelButtonClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void show(Set set) {
        EditSetFragment fragment = EditSetFragment.forSet(set);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("set")
                .replace(R.id.fragment_content, fragment, null)
                .setTransition(TRANSIT_NONE)
                .commit();
    }
}