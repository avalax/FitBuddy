package de.avalax.fitbuddy;

import javax.inject.Singleton;

import dagger.Component;
import de.avalax.fitbuddy.presentation.FitbuddyApplication.ApplicationComponent;
import de.avalax.fitbuddy.presentation.FitbuddyModule;

@Singleton
@Component(modules = FitbuddyModule.class)
public interface TestComponent extends ApplicationComponent {
    void inject(MainActivityTest test);
}