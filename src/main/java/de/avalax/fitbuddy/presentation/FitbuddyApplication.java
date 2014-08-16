package de.avalax.fitbuddy.presentation;

import android.app.Application;
import dagger.ObjectGraph;

public class FitbuddyApplication extends Application {
    private ObjectGraph graph;

    @Override
    public void onCreate() {
        super.onCreate();
        graph = ObjectGraph.create(new FitbuddyModule(this));
    }

    public void inject(Object object) {
        graph.inject(object);
    }
}
