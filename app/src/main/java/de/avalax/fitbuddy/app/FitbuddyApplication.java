package de.avalax.fitbuddy.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import dagger.ObjectGraph;

public class FitbuddyApplication extends Application {
    private ObjectGraph graph;

    @Override public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences("de.avalax.fitbuddy", Context.MODE_PRIVATE);
        graph = ObjectGraph.create(new FitbuddyModule(sharedPreferences));
    }

    public void inject(Object object) {
        graph.inject(object);
    }
}
