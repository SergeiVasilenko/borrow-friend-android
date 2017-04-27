package vision.genesis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalUser {

	@JsonProperty("vkId")
	private long vkId;
	@JsonProperty("photo")
	private String photo;
	@JsonProperty("UserName")
	private String name;

	private LocalUser() {}

	public LocalUser(long vkId) {
		this.vkId = vkId;
	}

	public LocalUser(long vkId, String photo, String name) {
		this.vkId = vkId;
		this.photo = photo;
		this.name = name;
	}

	@JsonProperty("vkId")
	public long getVkId() {
		return vkId;
	}

	@JsonProperty("photo")
	public String getPhoto() {
		return photo;
	}

	@JsonProperty("UserName")
	public String getName() {
		return name;
	}
}
