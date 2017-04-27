package vision.genesis.network.request;

import java.util.ArrayList;

import vision.genesis.model.Offer;
import vision.genesis.network.request.util.BaseRequest;
import vision.genesis.network.request.util.RequestRouter;

public class GetOffersRequest extends BaseRequest<ArrayList<Offer>> {

	private long mUserId;

	protected GetOffersRequest(Builder builder) {
		super(builder);
		mUserId = builder.mUserId;
	}

	@Override
	public RequestRouter<ArrayList<Offer>> send() {
		send(getApi().getOffers(mUserId));
		return this;
	}

	public static Builder create() {
		return new Builder();
	}

	public static class Builder extends BaseRequest.Builder<ArrayList<Offer>> {
		private long mUserId;

		private Builder() {
		}

		public Builder userId(long userId) {
			mUserId = userId;
			return this;
		}

		@Override
		public RequestRouter<ArrayList<Offer>> build() {
			if (mUserId == 0) {
				throw new IllegalArgumentException("Set user id");
			}
			return new GetOffersRequest(this);
		}
	}
}
