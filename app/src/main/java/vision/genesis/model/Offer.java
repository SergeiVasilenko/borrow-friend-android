package vision.genesis.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Offer implements Item, Parcelable {

	@JsonProperty("Id")
	private long       id;
	@JsonProperty("UserId")
	private long       userId;
	@JsonProperty("Amount")
	private int        amount;
	@JsonProperty("PercentPerDay")
	private BigDecimal percentPerDay;
	@JsonProperty("Rating")
	private int        rating;
	@JsonProperty("UserName")
	private String     name;
	@JsonProperty("Photo")
	private String     avatar;
	@JsonProperty("VkId")
	private long vkId;
	@JsonProperty("DurationMin")
	private int durationMin;
	@JsonProperty("DurationMax")
	private int durationMax;
	@JsonProperty("SocialRating")
	private int socialRating;

	@Override
	public long getId() {
		return id;
	}

	@Override
	public long getUserId() {
		return userId;
	}

	@Override
	public long getVkId() {
		return vkId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public BigDecimal getPercentPerDay() {
		return percentPerDay;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public int getRating() {
		return socialRating;
	}

	@Override
	public String getAvatar() {
		return avatar;
	}

	@Override
	public ItemType getType() {
		return ItemType.OFFER;
	}

	@Override
	public int getDurationMin() {
		return durationMin;
	}

	@Override
	public int getDurationMax() {
		return durationMax;
	}

	@Override
	public String getTitle() {
		return "";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeLong(this.userId);
		dest.writeInt(this.amount);
		dest.writeSerializable(this.percentPerDay);
		dest.writeInt(this.rating);
		dest.writeString(this.name);
		dest.writeString(this.avatar);
		dest.writeLong(this.vkId);
		dest.writeInt(this.durationMin);
		dest.writeInt(this.durationMax);
		dest.writeInt(this.socialRating);
	}

	public Offer() {
	}

	protected Offer(Parcel in) {
		this.id = in.readLong();
		this.userId = in.readLong();
		this.amount = in.readInt();
		this.percentPerDay = (BigDecimal) in.readSerializable();
		this.rating = in.readInt();
		this.name = in.readString();
		this.avatar = in.readString();
		this.vkId = in.readLong();
		this.durationMin = in.readInt();
		this.durationMax = in.readInt();
		this.socialRating = in.readInt();
	}

	public static final Creator<Offer> CREATOR = new Creator<Offer>() {
		@Override
		public Offer createFromParcel(Parcel source) {
			return new Offer(source);
		}

		@Override
		public Offer[] newArray(int size) {
			return new Offer[size];
		}
	};
}
