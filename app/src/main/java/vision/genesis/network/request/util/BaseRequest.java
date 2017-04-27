package vision.genesis.network.request.util;


import vision.genesis.network.Api;

public abstract class BaseRequest<T> extends RequestRouter<T> {

	private Api mApi;

	protected BaseRequest(Builder<T> builder) {
		super(builder);
		mApi = builder.api;
	}

	protected Api getApi() {
		return mApi;
	}

	public static abstract class Builder<T> extends RequestRouter.Builder<T> {
		private Api api;

		public Builder<T> api(Api api) {
			this.api = api;
			return this;
		}
	}
}
