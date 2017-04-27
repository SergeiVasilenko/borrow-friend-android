package vision.genesis.network;

import android.os.Build;
import android.util.DisplayMetrics;

import javax.inject.Inject;

import vision.genesis.app.App;


public class UserAgent {

	private App mApp;

	private String mUserAgent;

	@Inject
	UserAgent(App app) {
		mApp = app;
	}

	@Override
	public String toString() {
		if (mUserAgent == null) {
			DisplayMetrics displayMetrics = mApp.getResources().getDisplayMetrics();
			String osVersion = Build.VERSION.RELEASE;
			String resolution = displayMetrics.widthPixels + "x" + displayMetrics.heightPixels;
			String model = "\"" + Build.MANUFACTURER + " " + Build.BRAND + " " + Build.MODEL + "\"";
			String progVersion = mApp.getVersion();

			mUserAgent = mApp.getPackageName() + "/" + progVersion + " Android/" + osVersion + " " + resolution + " " + model;
		}
		return mUserAgent;
	}
}
