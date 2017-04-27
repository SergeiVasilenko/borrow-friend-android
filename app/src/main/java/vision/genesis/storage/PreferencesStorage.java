package vision.genesis.storage;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import vision.genesis.app.App;


public class PreferencesStorage {

	private static final String PREFS = "storage.pref";

	private static final String PREF_VK_TOKEN = "PREF_VK_TOKEN";
	private App mApp;

	@Inject
	PreferencesStorage(App app) {
		mApp = app;
	}

	public void setVkToken(String vkToken) {
		getPrefs().edit().putString(PREF_VK_TOKEN, vkToken).apply();
	}

	public String getVkToken() {
		return getPrefs().getString(PREF_VK_TOKEN, null);
	}

	private SharedPreferences getPrefs() {
		return mApp.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
	}
}
