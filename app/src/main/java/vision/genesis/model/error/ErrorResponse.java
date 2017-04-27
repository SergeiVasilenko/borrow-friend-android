
package vision.genesis.model.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import vision.genesis.util.log.LogManager;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

	private static final String     TAG = "ErrorResponse";
	private static final LogManager log = new LogManager(TAG);

	@JsonProperty("error")
	private Error error;

	@JsonProperty("Message")
	private String message;

	/**
	 * @return The error
	 */
	public Error getError() {
		return error;
	}

	private static Function<Result<?>, ErrorResponse> sMapper;

	public static Function<Result<?>, ErrorResponse> mapper(ObjectMapper mapper) {
		if (sMapper == null) {
			sMapper = result -> {
				Response response = result.response();
				// handle inner error
				if (response == null) {
					Throwable t = result.error();

					ErrorResponse error = new ErrorResponse();
					if (t instanceof IOException) {
						if (t instanceof SocketTimeoutException) {
							error.error = new Error(Error.SERVER_DOWN_ERROR, "Server is unavailable");
						} else if (t instanceof JsonMappingException) {
							log.e("parse error: ", t);
							error.error = new Error(Error.CLIENT_PARSE_ERROR, "There is parsing error");
						} else /*if (t instanceof ConnectException)*/ {
							error.error = new Error(Error.CLIENT_NO_INTERNET, "No internet");
						}
					} else {
						log.e("Error in request", t);
						error.error = new Error(Error.CLIENT_INNER_ERROR, t.getMessage());
					}

					return error;
				}
				int code = response.code();
				if (code / 100 == 5) {
					log.w("server side error: %d", response.code());
					return createResponse(Error.SERVER_SIDE_ERROR, "Something went wrong");
				}
				ResponseBody body = result.response().errorBody();
				try {
					ErrorResponse errorResponse = mapper.readValue(body.byteStream(), ErrorResponse.class);
					if (errorResponse.error == null) {
						return createResponse(String.valueOf(code), errorResponse.message);
					}
					return errorResponse;
				} catch (IOException e) {
					log.e("Error parse error", e);
					return createResponse(Error.CLIENT_PARSE_ERROR, e.getMessage());
				}
			};
		}
		return sMapper;
	}

	private static ErrorResponse createResponse(String code, String message) {
		ErrorResponse error = new ErrorResponse();
		error.error = new Error(code, message);
		return error;
	}

	@JsonIgnore
	private boolean isNoInternetThrowable(Throwable t) {
		return t instanceof UnknownHostException
				|| t instanceof SocketTimeoutException
				|| t instanceof ConnectException
				|| t instanceof InterruptedIOException && t.getMessage().contains("timeout")
				|| t instanceof SSLPeerUnverifiedException
				|| t instanceof CertificateException
				|| t instanceof SSLHandshakeException
				|| t instanceof CertPathValidatorException;
	}

	@JsonIgnore
	private boolean isSslException(Throwable t) {
		return t instanceof SSLPeerUnverifiedException
				|| t instanceof CertificateException
				|| t instanceof SSLHandshakeException
				|| t instanceof CertPathValidatorException;
	}
}
