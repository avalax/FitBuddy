package de.avalax.fitbuddy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_main)
public class HelloAndroidActivity extends RoboActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

