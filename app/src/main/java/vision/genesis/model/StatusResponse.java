package vision.genesis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusResponse {

	public static final String STATUS_OK = "ok";

	@JsonProperty("status")
	private String status;

	public String getStatus() {
		return status;
	}

	public boolean isOk() {
		return STATUS_OK.equals(status);
	}
}
