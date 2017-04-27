package vision.genesis.network.request;

import okhttp3.ResponseBody;
import vision.genesis.network.request.util.BaseRequest;
import vision.genesis.network.request.util.RequestRouter;

public class WithdrawBalanceRequest extends BaseRequest<ResponseBody> {

	private long mUserId;

	private int mAmount;

	protected WithdrawBalanceRequest(Builder builder) {
		super(builder);
		mUserId = builder.mUserId;
		mAmount = builder.mAmount;
	}

	@Override
	public RequestRouter<ResponseBody> send() {
		send(getApi().withdrawBalance(mUserId, mAmount));
		return this;
	}

	public static Builder create() {
		return new Builder();
	}

	public static class Builder extends BaseRequest.Builder<ResponseBody> {

		private long mUserId;

		private int mAmount;

		private Builder() {
		}

		public Builder userId(long userId) {
			mUserId = userId;
			return this;
		}

		public Builder amount(int amount) {
			mAmount = amount;
			return this;
		}

		@Override
		public RequestRouter<ResponseBody> build() {
			if (mUserId == 0) {
				throw new IllegalArgumentException("Set user id");
			}
			if (mAmount <= 0) {
				throw new IllegalArgumentException("Amount should be more than 0");
			}
			return new WithdrawBalanceRequest(this);
		}
	}
}
