package de.avalax.fitbuddy.runner;

import javax.inject.Singleton;

import dagger.Component;
import de.avalax.fitbuddy.presentation.FitbuddyApplication;
import de.avalax.fitbuddy.presentation.FitbuddyModule;

public class TestFitbuddyApplication extends FitbuddyApplication {

    @Override
    protected ApplicationComponent createComponent() {
        return DaggerTestFitbuddyApplication_TestComponent.builder()
                .fitbuddyModule(new FitbuddyModule(this)).build();
    }

    @Singleton
    @Component(modules = FitbuddyModule.class)
    public interface TestComponent extends ApplicationComponent {
        void inject(FitbuddyActivityTestRule test);
    }
}
