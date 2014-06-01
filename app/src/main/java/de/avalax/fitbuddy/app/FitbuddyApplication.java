package de.avalax.fitbuddy.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import dagger.ObjectGraph;

public class FitbuddyApplication extends Application {
    private ObjectGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        graph = ObjectGraph.create(new FitbuddyModule(sharedPreferences));
    }

    public void inject(Object object) {
        graph.inject(object);
    }
}
