package vision.genesis.network.request.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import retrofit2.adapter.rxjava2.Result;
import vision.genesis.model.error.Error;
import vision.genesis.model.error.ErrorResponse;
import vision.genesis.util.rx.Funcs;
import vision.genesis.util.rx.Results;

public class DefaultErrorHandler implements ErrorHandler {

	private PublishSubject<Result<?>> mErrorSubject = PublishSubject.create();
	private Observable<Error> mError;

	public DefaultErrorHandler(ObjectMapper mapper) {
		mError = createObservable(mapper);
	}

	private Observable<Error> createObservable(ObjectMapper mapper) {
		return mErrorSubject
				.filter(Funcs.not(Results.isSuccessful()))
				.map(ErrorResponse.mapper(mapper))
				.map(ErrorResponse::getError);
	}

	@Override
	public Observable<Error> observe() {
		return mError;
	}

	@Override
	public void onNext(Result<?> result) {
		mErrorSubject.onNext(result);
	}
}
