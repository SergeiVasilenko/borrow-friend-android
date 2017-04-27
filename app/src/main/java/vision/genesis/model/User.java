package vision.genesis.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	@JsonProperty("UserId")
	private long userId;

	@JsonProperty("FriendsCount")
	private int friendsCount;

	@JsonProperty("Raiting")
	private int rating;

	@JsonProperty("ProviderUserId")
	private long providerUserId;

	@JsonProperty("Provider")
	private String provider;

	@JsonProperty("Foto")
	private String photo;

	@JsonProperty("UserName")
	private String userName;

	@JsonProperty("SocialRating")
	private int socialRating;

	public long getUserId() {
		return userId;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public int getRating() {
		return rating;
	}

	public long getProviderUserId() {
		return providerUserId;
	}

	public String getProvider() {
		return provider;
	}

	public String getPhoto() {
		return photo;
	}

	public String getUserName() {
		return userName;
	}

	public int getSocialRating() {
		return socialRating;
	}
}
