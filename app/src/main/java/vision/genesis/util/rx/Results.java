package vision.genesis.util.rx;


import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import retrofit2.adapter.rxjava2.Result;


public final class Results {
	private static final Predicate<Result<?>> SUCCESSFUL = new Predicate<Result<?>>() {
		@Override
		public boolean test(@NonNull Result<?> result) throws Exception {
			return !result.isError() && result.response().isSuccessful();
		}
	};

	public static Predicate<Result<?>> isSuccessful() {
		return SUCCESSFUL;
	}

	private Results() {
		throw new AssertionError("No instances.");
	}
}
