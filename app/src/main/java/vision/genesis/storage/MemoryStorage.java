package vision.genesis.storage;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import vision.genesis.model.Session;
import vision.genesis.model.LocalUser;

public class MemoryStorage {

	private volatile BehaviorSubject<LocalUser> mCurrentUserObserver = BehaviorSubject.create();
	private volatile LocalUser mCurrentUser;
	private volatile Session   mCurrentSession;

	MemoryStorage() {
	}

	void setCurrentUser(LocalUser user) {
		mCurrentUser = user;
		mCurrentUserObserver.onNext(user);
	}

	LocalUser getCurrentUser() {
		return mCurrentUser;
	}

	Observable<LocalUser> getCurrentUserObserver() {
		return mCurrentUserObserver;
	}

	boolean hasCurrentUser() {
		return mCurrentUserObserver.hasValue();
	}

	void setSession(Session session) {
		mCurrentSession = session;
	}

	Session getSession() {
		return mCurrentSession;
	}
}
