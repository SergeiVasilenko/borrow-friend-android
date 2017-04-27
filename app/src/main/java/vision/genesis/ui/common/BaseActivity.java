package vision.genesis.ui.common;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import vision.genesis.app.App;
import vision.genesis.app.ApplicationLifecycle;
import vision.genesis.manager.PermissionManager;
import vision.genesis.model.error.Error;
import vision.genesis.util.log.LogManager;

public abstract class BaseActivity extends AppCompatActivity {

	protected final String     TAG = getClass().getSimpleName();
	private final   LogManager log = new LogManager(TAG);

	private ActivityState mActivityState = ActivityState.LEFT;

	@Inject
	ApplicationLifecycle mApplicationLifecycle;
	@Inject
	PermissionManager    mPermissionManager;

	public enum ActivityState {
		LEFT, OPENED, MINIMIZED
	}

	private static final double KEYBOARD_ASPECT = 0.75;

	private static final int KEYBOARD_STATE_UNKNOWN = 0;
	private static final int KEYBOARD_STATE_OPENED  = 1;
	private static final int KEYBOARD_STATE_CLOSED  = -1;

	private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
	private View                                    mContentView;
	private int                                     mKeyboardState;

	private BehaviorSubject<ActivityState> mActivityStateObservable = BehaviorSubject.createDefault(mActivityState);

	/*
	   TRACKING FOR ACTIVITY AND APPLICATION STATES
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		App.get().getUiComponent().injectToBaseActivity(this);
	}

	@Override
	protected void onStart() {
		log.d("onStart");
		super.onStart();
	}

	@DebugLog
	@Override
	protected void onResume() {
		log.d("onResume");
		changeActivityState(ActivityState.OPENED);
		super.onResume();}

	@Override
	protected void onPause() {
		log.d("onPause");
		changeActivityState(ActivityState.MINIMIZED);
		super.onPause();
	}

	@Override
	protected void onStop() {
		log.d("onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if (isKeyboardEventListener() && mContentView != null && mOnGlobalLayoutListener != null) {
			mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
		}
		super.onDestroy();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		mPermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	public Observable<ActivityState> getActivityState() {
		return mActivityStateObservable;
	}

	public Observable<Boolean> isActivityOnTop() {
		return mActivityStateObservable.map(state -> state == ActivityState.OPENED);
	}

	public void showError(Error error) {
		log.e("There is error:\n -- code:%s\n -- message:%s", error.getCode(), error.getMessage());
		Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		changeActivityState(ActivityState.LEFT);
		super.startActivityForResult(intent, requestCode);
	}

	/*@Override
	public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
		changeActivityState(ActivityState.LEFT);
		super.startActivityFromFragment(fragment, intent, requestCode);
	}*/

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
		changeActivityState(ActivityState.LEFT);
		super.startActivityForResult(intent, requestCode, options);
	}

	@Override
	public void startActivity(Intent intent) {
		changeActivityState(ActivityState.LEFT);
		super.startActivity(intent);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void startActivity(Intent intent, Bundle options) {
		changeActivityState(ActivityState.LEFT);
		super.startActivity(intent, options);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void startActivities(Intent[] intents) {
		changeActivityState(ActivityState.LEFT);
		super.startActivities(intents);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void startActivities(Intent[] intents, Bundle options) {
		changeActivityState(ActivityState.LEFT);
		super.startActivities(intents, options);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void startActivityFromFragment(@NonNull android.app.Fragment fragment, Intent intent, int requestCode) {
		changeActivityState(ActivityState.LEFT);
		super.startActivityFromFragment(fragment, intent, requestCode);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void startActivityFromFragment(@NonNull android.app.Fragment fragment, Intent intent, int requestCode, Bundle options) {
		changeActivityState(ActivityState.LEFT);
		super.startActivityFromFragment(fragment, intent, requestCode, options);
	}

	@Override
	public void onBackPressed() {
		changeActivityState(ActivityState.LEFT);
		super.onBackPressed();
	}

	@Override
	public void finish() {
		changeActivityState(ActivityState.LEFT);
		super.finish();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void finishAffinity() {
		log.v("finishAffinity");
		changeActivityState(ActivityState.LEFT);
		super.finishAffinity();
	}


	@Override
	public void finishActivity(int requestCode) {
		changeActivityState(ActivityState.LEFT);
		super.finishActivity(requestCode);
	}

	/**
	 * Change activity state. See also ActivityState. <br>
	 * Warning! Not all of transitions are supported. Some of state can not be moved to some other states.
	 * Only follow transitions are supported: left - opened, opened - left, opened - minimized, minimized - opened.
	 * </br>
	 *
	 * @param newState ActivityState new state activity
	 */
	protected void changeActivityState(ActivityState newState) {

		if (mActivityState == newState) {
			return;
		}
		log.d("changeActivityState: before: " + mActivityState + ", new: " + newState + ", activity " + getClass().getSimpleName());
		ActivityState oldState = mActivityState;
		switch (oldState) {
			case LEFT:
				if (newState == ActivityState.OPENED) {
					int action = mApplicationLifecycle.changeApplicationState(ApplicationLifecycle.State.OPENED);
					setActivityState(newState);
					if (action == ApplicationLifecycle.ACTION_OPENED) {
						onApplicationOpened();
					}
				}
				break;
			case OPENED:
				if (newState == ActivityState.MINIMIZED) {
					int action = mApplicationLifecycle.changeApplicationState(ApplicationLifecycle.State.MINIMIZED);
					setActivityState(newState);
					if (action == ApplicationLifecycle.ACTION_MINIMIZED) {
						onApplicationMinimized();
					}
				} else if (newState == ActivityState.LEFT) {
					setActivityState(newState);
				}
				break;
			case MINIMIZED:
				if (newState == ActivityState.OPENED) {
					int action = mApplicationLifecycle.changeApplicationState(ApplicationLifecycle.State.OPENED);
					setActivityState(newState);
					if (action == ApplicationLifecycle.ACTION_DEPLOYED) {
						onApplicationDeployed();
					}
				}
				break;
		}
	}

	private void setActivityState(ActivityState state) {
		mActivityState = state;
		mActivityStateObservable.onNext(state);
	}

	protected void onApplicationOpened() {
		log.d("onApplicationOpened");
	}

	protected void onApplicationMinimized() {
		log.d("onApplicationMinimized");
	}

	protected void onApplicationDeployed() {
		log.d("onApplicationDeployed");
	}

	protected void onApplicationLeft() {
		log.d("onApplicationLeft");
	}

	protected boolean isKeyboardEventListener() {
		return false;
	}

	protected void onKeyboardOpened(int contentHeight) {
	}

	protected void onKeyboardClosed(int contentHeight) {
	}
}