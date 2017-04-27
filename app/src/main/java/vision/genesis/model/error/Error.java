
package vision.genesis.model.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {

	// Client-side errors
	public static final String CLIENT_PARSE_ERROR   = "Client_PARSE_ERROR";
	public static final String CLIENT_INNER_ERROR   = "Client_INNER_ERROR";
	public static final String CLIENT_NO_INTERNET   = "Client_NO_INTERNET";
	public static final String SERVER_DOWN_ERROR    = "Server_DOWN_ERROR";
	public static final String SERVER_SIDE_ERROR    = "SERVER_SIDE_ERROR";

	@JsonProperty("code")
	private String code;
	@JsonProperty("message")
	private String message;
	private Error() {
	}

	@JsonIgnore
	public Error(String code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * @return The code
	 */
	@JsonIgnore
	public String getCode() {
		return code;
	}

	/**
	 * @return The message
	 */
	@JsonIgnore
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Error{" +
				"code='" + code + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}