package vision.genesis.manager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PermissionModule {

	@Singleton
	@Provides
	PermissionManager providePermissionManager() {
		return new PermissionManager();
	}
}
