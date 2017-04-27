package vision.genesis.network.request;

import vision.genesis.model.StatusResponse;
import vision.genesis.network.request.util.BaseRequest;
import vision.genesis.network.request.util.RequestRouter;

public class PostDealRequest extends BaseRequest<StatusResponse> {

	private long mUserId;
	private long mOrderId;


	protected PostDealRequest(Builder builder) {
		super(builder);
		mUserId = builder.userId;
		mOrderId = builder.orderId;
	}

	@Override
	public RequestRouter<StatusResponse> send() {
		send(getApi().postDeal(mUserId, mOrderId));
		return this;
	}

	public static Builder create() {
		return new Builder();
	}

	public static class Builder extends BaseRequest.Builder<StatusResponse> {

		private long userId;
		private long orderId;

		private Builder() {
		}

		public Builder userId(long userId) {
			this.userId = userId;
			return this;
		}

		public Builder orderId(long orderId) {
			this.orderId = orderId;
			return this;
		}

		@Override
		public RequestRouter<StatusResponse> build() {
			if (userId == 0) {
				throw new IllegalArgumentException("Set user id");
			}
			return new PostDealRequest(this);
		}
	}
}
