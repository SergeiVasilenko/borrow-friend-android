package vision.genesis.ui.auth;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import vision.genesis.R;
import vision.genesis.app.App;
import vision.genesis.model.LocalUser;
import vision.genesis.network.Api;
import vision.genesis.network.request.GetFriendsOnLoginRequest;
import vision.genesis.network.request.util.DefaultErrorHandler;
import vision.genesis.storage.Storage;
import vision.genesis.ui.common.BaseActivity;
import vision.genesis.ui.main.MainActivity;
import vision.genesis.util.log.LogManager;

public class AuthActivity extends BaseActivity {

	private static final LogManager log = new LogManager("Auth");

	@Inject
	Storage      mStorage;
	@Inject
	Api          mApi;
	@Inject
	ObjectMapper mObjectMapper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.get().getUiComponent().inject(this);
		setContentView(R.layout.activity_auth);
		findViewById(R.id.login).setOnClickListener(v -> login());
	}

	@Override
	protected void onStart() {
		super.onStart();
//		if (VKSdk.isLoggedIn()) {
//			handleLogin();
//		}
	}

	public static void show(Context context) {
		Intent intent = new Intent(context, AuthActivity.class);
		context.startActivity(intent);
	}

	private void login() {
		VKSdk.login(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
			@Override
			@DebugLog
			public void onResult(VKAccessToken res) {
				handleLogin(res);
			}

			@Override
			public void onError(VKError error) {
				log.e("VK auth error, %s", error);
				Toast.makeText(AuthActivity.this, R.string.error_auth, Toast.LENGTH_SHORT).show();
			}
		})) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@DebugLog
	private void handleLogin(VKAccessToken vkAccessToken) {
		long userId = Long.valueOf(vkAccessToken.userId);
		mStorage.setCurrentUser(new LocalUser(userId));
		mStorage.setVkToken(vkAccessToken.accessToken);

		GetFriendsOnLoginRequest.create()
								.userId(userId)
								.api(mApi)
								.errorHandler(new DefaultErrorHandler(mObjectMapper))
								.onResult(new Consumer<ResponseBody>() {
									@Override
									public void accept(@NonNull ResponseBody responseBody) throws Exception {

									}
								})
								.build()
								.send();
		MainActivity.show(this);
		finish();
	}
}