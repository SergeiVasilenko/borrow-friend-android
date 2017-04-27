package vision.genesis.network.request;

import okhttp3.ResponseBody;
import vision.genesis.network.request.util.BaseRequest;
import vision.genesis.network.request.util.RequestRouter;

public class GetFriendsOnLoginRequest extends BaseRequest<ResponseBody> {

	private long mUserId;


	protected GetFriendsOnLoginRequest(Builder builder) {
		super(builder);
		mUserId = builder.mUserId;
	}

	@Override
	public RequestRouter<ResponseBody> send() {
		send(getApi().getFriends(mUserId));
		return this;
	}

	public static Builder create() {
		return new Builder();
	}

	public static class Builder extends BaseRequest.Builder<ResponseBody> {

		private long mUserId;

		private Builder() {
		}

		public Builder userId(long userId) {
			mUserId = userId;
			return this;
		}

		@Override
		public RequestRouter<ResponseBody> build() {
			if (mUserId == 0) {
				throw new IllegalArgumentException("Set user id");
			}
			return new GetFriendsOnLoginRequest(this);
		}
	}
}
