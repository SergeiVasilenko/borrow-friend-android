package vision.genesis.app;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

	private App mApp;

	public AppModule(App app) {
		mApp = app;
	}

	@Singleton
	@Provides
	App provideApp() {
		return mApp;
	}

	@Singleton
	@Provides
	ApplicationLifecycle provideApplicationLifecycle() {
		return new ApplicationLifecycle();
	}
}
