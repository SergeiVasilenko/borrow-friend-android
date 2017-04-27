package vision.genesis.network.request.util;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import vision.genesis.model.error.Error;

public interface ErrorHandler {

	Observable<Error> observe();

	void onNext(Result<?> result);
}
