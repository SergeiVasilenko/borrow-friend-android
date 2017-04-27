package vision.genesis.storage;

import javax.inject.Inject;

import vision.genesis.model.Session;
import vision.genesis.model.LocalUser;

public class DiskStorage {

	private JacksonStorage mJacksonStorage;

	private PreferencesStorage mPreferencesStorage;

	@Inject
	DiskStorage(JacksonStorage jacksonStorage, PreferencesStorage preferencesStorage) {
		mJacksonStorage = jacksonStorage;
		mPreferencesStorage = preferencesStorage;
	}

	public void saveCurrentUser(LocalUser user) {
		mJacksonStorage.setCurrentUser(user);
	}

	public void saveSession(Session session) {
		mJacksonStorage.setSession(session);
	}

	public LocalUser loadCurrentUser() {
		return mJacksonStorage.getCurrentUser();
	}

	public Session loadSession() {
		return mJacksonStorage.getSession();
	}

	public void saveVkToken(String vkToken) {
		mPreferencesStorage.setVkToken(vkToken);
	}

	public String loadVkToken() {
		return mPreferencesStorage.getVkToken();
	}

}
