package vision.genesis.network.request;

import java.math.BigDecimal;

import vision.genesis.model.ItemType;
import vision.genesis.model.ItemTypeConverter;
import vision.genesis.model.StatusResponse;
import vision.genesis.network.request.util.BaseRequest;
import vision.genesis.network.request.util.RequestRouter;

public class PostOrderRequest extends BaseRequest<StatusResponse> {

	private long       mUserId;
	private String     mOrderType;
	private BigDecimal mAmount;
	private BigDecimal mPercentPerDay;
	private int        mDurationMin;
	private int        mDurationMax;
	private String     mTitle;


	protected PostOrderRequest(Builder builder) {
		super(builder);
		mUserId = builder.userId;
		mOrderType = ItemTypeConverter.convert(builder.orderType);
		mAmount = builder.amount;
		mPercentPerDay = builder.percentPerDay;
		mDurationMin = builder.durationMin;
		mDurationMax = builder.durationMax;
		mTitle = builder.title;
	}

	@Override
	public RequestRouter<StatusResponse> send() {
		send(getApi().postOrder(mUserId, mOrderType, mAmount, mPercentPerDay, mDurationMax, mDurationMin, mTitle));
		return this;
	}

	public static Builder create() {
		return new Builder();
	}

	public static class Builder extends BaseRequest.Builder<StatusResponse> {


		private long       userId;
		private ItemType   orderType;
		private BigDecimal amount;
		private BigDecimal percentPerDay;
		private int        durationMin;
		private int        durationMax;
		private String     title;

		private Builder() {
		}

		public Builder userId(long userId) {
			this.userId = userId;
			return this;
		}

		public Builder orderType(ItemType orderType) {
			this.orderType = orderType;
			return this;
		}

		public Builder amount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public Builder percentPerDay(BigDecimal percentPerDay) {
			this.percentPerDay = percentPerDay;
			return this;
		}

		public Builder durationMin(int durationMin) {
			this.durationMin = durationMin;
			return this;
		}

		public Builder durationMax(int durationMax) {
			this.durationMax = durationMax;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		@Override
		public RequestRouter<StatusResponse> build() {
			if (userId == 0) {
				throw new IllegalArgumentException("Set user id");
			}
			return new PostOrderRequest(this);
		}
	}
}
