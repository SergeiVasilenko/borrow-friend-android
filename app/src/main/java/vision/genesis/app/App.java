package vision.genesis.app;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

import com.vk.sdk.VKSdk;

import hugo.weaving.DebugLog;
import vision.genesis.BuildConfig;
import vision.genesis.network.NetworkModule;
import vision.genesis.ui.UiComponent;
import vision.genesis.util.log.LogManager;


public class App extends MultiDexApplication  {

	private static final LogManager log = new LogManager("App");

	private static final String BASE_URL   = "http://52.164.212.9/api/";

	private static App app;

	private AppComponent mAppComponent;

	private UiComponent mUiComponent;

	private String mVersion;

	public static App get() {
		return app;
	}

	@DebugLog
	@Override
	public void onCreate() {
		LogManager.setDebugMode(BuildConfig.DEBUG);

		super.onCreate();

		app = this;

		buildAppComponent();

		VKSdk.initialize(this);
	}

	public static AppComponent getAppComponent() {
		return app.mAppComponent;
	}

	public UiComponent getUiComponent() {
		if (mUiComponent == null) {
			buildUiComponent();
		}
		return mUiComponent;
	}

	public String getVersion() {
		if (mVersion == null) {
			PackageManager manager = getPackageManager();
			PackageInfo info = null;
			try {
				info = manager.getPackageInfo(getPackageName(), 0);
			} catch (PackageManager.NameNotFoundException e) {
				// do nothing
			}
			mVersion = info != null ? info.versionName : "nulled";
		}
		return mVersion;
	}

	public boolean isLoggedIn() {
		return mAppComponent.storage().getVkToken() != null;
	}

	@DebugLog
	protected void buildAppComponent() {
		mAppComponent = DaggerAppComponent.builder()
										  .appModule(new AppModule(this))
										  .networkModule(new NetworkModule(BASE_URL))
										  .build();
	}

	protected void buildUiComponent() {
		mUiComponent = mAppComponent.uiComponentBuilder().build();
	}
}
