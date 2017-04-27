package vision.genesis.app;

import java.util.ArrayList;
import java.util.List;

import vision.genesis.util.log.LogManager;

public class ApplicationLifecycle {

	private static final LogManager log = new LogManager("ApplicationLifecycle");

	public static final int ACTION_NOTHING   = 0;
	public static final int ACTION_OPENED    = 1;
	public static final int ACTION_MINIMIZED = 2;
	public static final int ACTION_DEPLOYED  = 3;
	public static final int ACTION_LEFT      = 4;

	public enum State {
		LEFT, OPENED, MINIMIZED
	}

	public interface Listener {
		void onApplicationOpened();
		void onApplicationDeployed();
		void onApplicationMinimized();
		void onApplicationLeft();
	}

	private State mApplicationState = State.LEFT;

	private List<Listener> mListeners = new ArrayList<>();

	public int changeApplicationState(State newState) {
		log.v("changeApplicationState: %s", newState);
		if (newState == mApplicationState) {
			return ACTION_NOTHING;
		}
		switch (mApplicationState) {
			case LEFT:
				if (newState == State.OPENED) {
					mApplicationState = newState;
					onApplicationOpened();
					return ACTION_OPENED;
				}
				log.w("Illegal combination of state and argument: currentState = " + mApplicationState
							  + " newState = " + newState);
				return -1;
			case OPENED:
				if (newState == State.MINIMIZED) {
					mApplicationState = newState;
					onApplicationMinimized();
					return ACTION_MINIMIZED;
				} else if (newState == State.LEFT) {
					mApplicationState = newState;
					onApplicationLeft();
					return ACTION_LEFT;
				}
				log.w("Illegal combination of state and argument: currentState = " + mApplicationState
							  + " newState = " + newState);
				return -1;
			case MINIMIZED:
				if (newState == State.OPENED) {
					mApplicationState = newState;
					onApplicationDeployed();
					return ACTION_DEPLOYED;
				}
				log.w("Illegal combination of state and argument: currentState = " + mApplicationState
							  + " newState = " + newState);
				return -1;
		}
		return -1;
	}

	public void addListener(Listener listener) {
		mListeners.add(listener);
	}

	public boolean removeListener(Listener listener) {
		return mListeners.remove(listener);
	}


	private void onApplicationOpened() {
		log.d("onApplicationOpened");
		for (Listener listener : mListeners) {
			listener.onApplicationOpened();
		}
	}

	private void onApplicationDeployed() {
		log.d("onApplicationDeployed");
		for (Listener listener : mListeners) {
			listener.onApplicationDeployed();
		}
	}

	private void onApplicationMinimized() {
		log.d("onApplicationMinimized");
		for (Listener listener : mListeners) {
			listener.onApplicationMinimized();
		}
	}

	private void onApplicationLeft() {
		log.d("onApplicationLeft");
		for (Listener listener : mListeners) {
			listener.onApplicationLeft();
		}
	}
}
