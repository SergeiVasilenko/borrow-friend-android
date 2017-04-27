package vision.genesis.network.request.util;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import vision.genesis.model.error.Error;


public class RequestUiCallbacks<T> {

	private Consumer<Error> errorConsumer;
	private Consumer<T>     resultConsumer;
	private Action          progressStartedAction;
	private Action          progressEndedAction;

	private RequestUiCallbacks(Builder<T> builder) {
		errorConsumer = builder.errorConsumer;
		resultConsumer = builder.resultConsumer;
		progressStartedAction = builder.progressStartedAction;
		progressEndedAction = builder.progressEndedAction;
	}

	public Consumer<Error> getErrorConsumer() {
		return errorConsumer;
	}

	public Consumer<T> getResultConsumer() {
		return resultConsumer;
	}

	public Action getProgressStartedAction() {
		return progressStartedAction;
	}

	public Action getProgressEndedAction() {
		return progressEndedAction;
	}

	public static class Builder<T> {
		private Consumer<Error> errorConsumer;
		private Consumer<T>     resultConsumer;
		private Action          progressStartedAction;
		private Action          progressEndedAction;

		public Builder<T> error(Consumer<Error> errorConsumer) {
			this.errorConsumer = errorConsumer;
			return this;
		}

		public Builder<T> result(Consumer<T> resultConsumer) {
			this.resultConsumer = resultConsumer;
			return this;
		}

		public Builder<T> progressStarted(Action progressStartedAction) {
			this.progressStartedAction = progressStartedAction;
			return this;
		}

		public Builder<T> progressEnded(Action progressEndedAction) {
			this.progressEndedAction = progressEndedAction;
			return this;
		}

		public RequestUiCallbacks<T> build() {
			return new RequestUiCallbacks<>(this);
		}
	}
}
