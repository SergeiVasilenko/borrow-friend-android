package vision.genesis.util.rx;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

public final class Funcs {

	public static <T> Predicate<T> not(final Predicate<T> func) {
		return new Predicate<T>() {
			@Override
			public boolean test(@NonNull T t) throws Exception {
				return !func.test(t);
			}
		};
	}

	private Funcs() {
		throw new AssertionError("No instances.");
	}

}
