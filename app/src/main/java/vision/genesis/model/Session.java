package vision.genesis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Session {
	@JsonProperty("token")
	private String token;

	public Session(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
