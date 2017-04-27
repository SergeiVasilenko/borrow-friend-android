package vision.genesis.network.request;

import java.util.ArrayList;

import vision.genesis.model.Ask;
import vision.genesis.network.request.util.BaseRequest;
import vision.genesis.network.request.util.RequestRouter;

public class GetAsksRequest extends BaseRequest<ArrayList<Ask>> {

	private long mUserId;


	protected GetAsksRequest(Builder builder) {
		super(builder);
		mUserId = builder.mUserId;
	}

	@Override
	public RequestRouter<ArrayList<Ask>> send() {
		send(getApi().getAsks(mUserId));
		return this;
	}

	public static Builder create() {
		return new Builder();
	}

	public static class Builder extends BaseRequest.Builder<ArrayList<Ask>> {

		private long mUserId;

		private Builder() {
		}

		public Builder userId(long userId) {
			mUserId = userId;
			return this;
		}

		@Override
		public RequestRouter<ArrayList<Ask>> build() {
			if (mUserId == 0) {
				throw new IllegalArgumentException("Set user id");
			}
			return new GetAsksRequest(this);
		}
	}
}
