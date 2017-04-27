package vision.genesis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersIntersects {

	@JsonProperty("First")
	private User first;

	@JsonProperty("Second")
	private User second;

	@JsonProperty("HasIntersect")
	private boolean hasIntersect;

	@JsonProperty("FriendsDirect")
	private List<User> friendsDirect;

	@JsonProperty("FriendsIndirect")
	private List<List<User>> friendsIndirect;

	public User getFirst() {
		return first;
	}

	public User getSecond() {
		return second;
	}

	public boolean isHasIntersect() {
		return hasIntersect;
	}

	public List<User> getFriendsDirect() {
		return friendsDirect;
	}

	public List<List<User>> getFriendsIndirect() {
		return friendsIndirect;
	}
}
