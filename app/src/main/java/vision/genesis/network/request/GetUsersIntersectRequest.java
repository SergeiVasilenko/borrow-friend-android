package vision.genesis.network.request;

import vision.genesis.model.UsersIntersects;
import vision.genesis.network.request.util.BaseRequest;
import vision.genesis.network.request.util.RequestRouter;

public class GetUsersIntersectRequest extends BaseRequest<UsersIntersects> {

	private long mFirstId;
	private long mSecondId;

	protected GetUsersIntersectRequest(Builder builder) {
		super(builder);
		mFirstId = builder.mFirstId;
		mSecondId = builder.mSecondId;
	}

	@Override
	public RequestRouter<UsersIntersects> send() {
		send(getApi().getUsersIntersect(mFirstId, mSecondId));
		return this;
	}

	public static Builder create() {
		return new Builder();
	}

	public static class Builder extends BaseRequest.Builder<UsersIntersects> {

		private long mFirstId;
		private long mSecondId;

		private Builder() {
		}

		public Builder firstId(long userId) {
			mFirstId = userId;
			return this;
		}

		public Builder secondId(long mSecondId) {
			this.mSecondId = mSecondId;
			return this;
		}

		@Override
		public RequestRouter<UsersIntersects> build() {
			if (mFirstId == 0) {
				throw new IllegalArgumentException("Set user id");
			}
			if (mSecondId == 0) {
				throw new IllegalArgumentException("Set user id");
			}
			return new GetUsersIntersectRequest(this);
		}
	}
}
