package vision.genesis.network.request.util;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.Result;
import vision.genesis.model.error.Error;
import vision.genesis.util.log.LogManager;
import vision.genesis.util.rx.Funcs;
import vision.genesis.util.rx.Results;


public abstract class RequestRouter<T> implements Disposable {

	private final LogManager log = new LogManager(getClass().getSimpleName());

	private final Action mProgressStartedAction;
	private final Action mProgressEndedAction;

	private final ErrorHandler mErrorHandler;

	private final Consumer<T>     mResultConsumer;
	private final Consumer<T>     mCacheAction;
	private final Consumer<Error> mErrorConsumer;

	private final CompositeDisposable mDisposables = new CompositeDisposable();

	private boolean mIsSent;

	protected RequestRouter(final Builder<T> builder) {
		mErrorHandler = builder.errorHandler;
		mResultConsumer = builder.resultConsumer;
		mCacheAction = builder.cacheAction;
		mProgressStartedAction = builder.progressStartedAction;
		mProgressEndedAction = builder.progressEndedAction;
		mErrorConsumer = builder.errorConsumer;
		mDisposables.add(mErrorHandler.observe()
									  .observeOn(AndroidSchedulers.mainThread())
									  .subscribe(error -> {
										  log.w("Request error: %s", error);
										  if (mErrorConsumer != null) {
											  mErrorConsumer.accept(error);
										  }
									  }));
	}

	@Override
	public void dispose() {
		mDisposables.dispose();
	}

	@Override
	public boolean isDisposed() {
		return mDisposables.isDisposed();
	}

	protected ErrorHandler getErrorHandler() {
		return mErrorHandler;
	}

	protected void addSubscription(Disposable disposable) {
		mDisposables.add(disposable);
	}

	protected void send(Observable<Result<T>> result) {
		if (mIsSent) {
			throw new IllegalStateException("Request can be sent only once");
		}
		mIsSent = true;
		result = result.subscribeOn(Schedulers.io())
					   .observeOn(AndroidSchedulers.mainThread())
					   .doOnSubscribe(v -> {
						   if (mProgressStartedAction != null) {
							   mProgressStartedAction.run();
						   }
					   })
					   .doAfterTerminate(() -> {
						   if (mProgressEndedAction != null) {
							   mProgressEndedAction.run();
						   }
					   })
					   .observeOn(Schedulers.computation())
					   .share();

		mDisposables.add(result.filter(Results.isSuccessful())
							   .map(response -> response.response().body())
							   .doOnNext(t -> {
								   if (mCacheAction != null) {
									   mCacheAction.accept(t);
								   }
							   })
							   .observeOn(AndroidSchedulers.mainThread())
							   .subscribe(t -> {
								   if (mResultConsumer != null) {
									   mResultConsumer.accept(t);
								   }
								   mDisposables.clear();
							   })
		);

		mDisposables.add(result.filter(Funcs.not(Results.isSuccessful()))
							   .subscribe(error -> {
								   mErrorHandler.onNext(error);
								   mDisposables.clear();
							   }));
	}

	public abstract RequestRouter<T> send();

	public abstract static class Builder<T> {
		private ErrorHandler    errorHandler;
		private Consumer<Error> errorConsumer;
		private Consumer<T>     resultConsumer;
		private Consumer<T>     cacheAction;
		private Action          progressStartedAction;
		private Action          progressEndedAction;

		public Builder<T> errorHandler(@NonNull ErrorHandler errorHandler) {
			this.errorHandler = errorHandler;
			return this;
		}

		public Builder<T> onError(@NonNull Consumer<Error> errorConsumer) {
			this.errorConsumer = errorConsumer;
			return this;
		}

		public Builder<T> onResult(@NonNull Consumer<T> resultConsumer) {
			this.resultConsumer = resultConsumer;
			return this;
		}

		public Builder<T> cache(@NonNull Consumer<T> cacheAction) {
			this.cacheAction = cacheAction;
			return this;
		}

		public Builder<T> onProgressStart(@NonNull Action progressStartAction) {
			this.progressStartedAction = progressStartAction;
			return this;
		}

		public Builder<T> onProgressEnd(@NonNull Action progressEndAction) {
			this.progressEndedAction = progressEndAction;
			return this;
		}

		public abstract RequestRouter<T> build();
	}
}
