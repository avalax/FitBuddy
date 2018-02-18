package de.avalax.fitbuddy.runner;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import de.avalax.fitbuddy.presentation.DaggerFitbuddyApplication_ApplicationComponent;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.FitbuddyModule;

public class TestFitbuddyApplication extends FitbuddyApplication {

    @Override
    protected ApplicationComponent createComponent() {
        return DaggerTestFitbuddyApplication_TestComponent.builder().fitbuddyModule(new FitbuddyTestModule(this)).build();
    }

    @Singleton
    @Component(modules = FitbuddyModule.class)
    public interface TestComponent extends ApplicationComponent {
        void inject(FitbuddyActivityTestRule test);
    }
}
