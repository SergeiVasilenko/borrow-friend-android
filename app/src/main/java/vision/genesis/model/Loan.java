package vision.genesis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Loan {
	@JsonProperty("Amount")
	private int amount;

	@JsonProperty("CreatedDate")
	private String createdDate;

	@JsonProperty("PaymentDeadline")
	private String paymentDeadline;

	@JsonProperty("PercentPerDay")
	private BigDecimal percentPerDay;

	@JsonProperty("ProviderUserId")
	private long vkUserId;

	@JsonProperty("UserName")
	private String name;

	@JsonProperty("Foto")
	private String photo;

	@JsonIgnore
	private ItemType mItemType;

	public int getAmount() {
		return amount;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public String getPaymentDeadline() {
		return paymentDeadline;
	}

	public BigDecimal getPercentPerDay() {
		return percentPerDay;
	}

	public long getVkUserId() {
		return vkUserId;
	}

	public String getName() {
		return name;
	}

	public String getPhoto() {
		return photo;
	}

	public ItemType getItemType() {
		return mItemType;
	}

	public void setItemType(ItemType itemType) {
		mItemType = itemType;
	}
}
