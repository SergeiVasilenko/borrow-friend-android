package vision.genesis.network.request;

import vision.genesis.model.Balance;
import vision.genesis.network.request.util.BaseRequest;
import vision.genesis.network.request.util.RequestRouter;

public class GetBalanceRequest extends BaseRequest<Balance> {

	private long mUserId;


	protected GetBalanceRequest(Builder builder) {
		super(builder);
		mUserId = builder.mUserId;
	}

	@Override
	public RequestRouter<Balance> send() {
		send(getApi().getBalance(mUserId));
		return this;
	}

	public static Builder create() {
		return new Builder();
	}

	public static class Builder extends BaseRequest.Builder<Balance> {

		private long mUserId;

		private Builder() {
		}

		public Builder userId(long userId) {
			mUserId = userId;
			return this;
		}

		@Override
		public RequestRouter<Balance> build() {
			if (mUserId == 0) {
				throw new IllegalArgumentException("Set user id");
			}
			return new GetBalanceRequest(this);
		}
	}
}
