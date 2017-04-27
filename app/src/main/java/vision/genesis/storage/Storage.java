package vision.genesis.storage;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import vision.genesis.model.Session;
import vision.genesis.model.LocalUser;


public class Storage {

	private MemoryStorage mMemoryStorage;

	private DiskStorage mDiskStorage;

	@DebugLog
	@Inject
	Storage(MemoryStorage memoryStorage, DiskStorage diskStorage) {
		mMemoryStorage = memoryStorage;
		mDiskStorage = diskStorage;

		mMemoryStorage.setSession(mDiskStorage.loadSession());
		mMemoryStorage.setCurrentUser(mDiskStorage.loadCurrentUser());
	}

	public Session getSession() {
		return mMemoryStorage.getSession();
	}

	public void setCurrentUser(LocalUser user) {
		mDiskStorage.saveCurrentUser(user);
		mMemoryStorage.setCurrentUser(user);
	}

	public LocalUser getCurrentUser() {
		return mMemoryStorage.getCurrentUser();
	}

	public String getVkToken() {
		return mDiskStorage.loadVkToken();
	}

	public void setVkToken(String vkToken) {
		mDiskStorage.saveVkToken(vkToken);
	}

	public void logout() {
		mMemoryStorage.setSession(null);
		mMemoryStorage.setCurrentUser(null);
		mDiskStorage.saveCurrentUser(null);
		mDiskStorage.saveSession(null);
		mDiskStorage.saveVkToken(null);
	}
}
