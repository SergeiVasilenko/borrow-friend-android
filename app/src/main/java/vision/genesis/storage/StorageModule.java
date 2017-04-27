package vision.genesis.storage;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import vision.genesis.app.App;

/**
 * Created on 23.02.17.
 *
 * @author Sergey Vasilenko (vasilenko.sn@gmail.com)
 */
@Module
public class StorageModule {
	@Singleton
	@Provides
	public MemoryStorage provideMemoryStorage() {
		return new MemoryStorage();
	}

	@Singleton
	@Provides
	public JacksonStorage provideJacksonStorage(App app, ObjectMapper objectMapper) {
		return new JacksonStorage(app, objectMapper);
	}

	@Singleton
	@Provides
	public PreferencesStorage providePreferencesStorage(App app) {
		return new PreferencesStorage(app);
	}

	@Singleton
	@Provides
	public DiskStorage provideDiskStorage(JacksonStorage jacksonStorage, PreferencesStorage preferencesStorage) {
		return new DiskStorage(jacksonStorage, preferencesStorage);
	}

	@Singleton
	@Provides
	public Storage provideStorage(MemoryStorage memoryStorage, DiskStorage diskStorage) {
		return new Storage(memoryStorage, diskStorage);
	}
}
