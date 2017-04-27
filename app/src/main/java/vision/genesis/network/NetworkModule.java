package vision.genesis.network;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import vision.genesis.BuildConfig;
import vision.genesis.app.App;
import vision.genesis.network.request.util.DefaultErrorHandler;


@Module
public class NetworkModule {

	private static final HttpLoggingInterceptor.Level REQUEST_LOG_LEVEL = BuildConfig.DEBUG ?
																		  HttpLoggingInterceptor.Level.BODY :
																		  HttpLoggingInterceptor.Level.BASIC;

	public static final int CONNECT_TIMEOUT_MILLIS = 5 * 60_000;
	public static final int READ_TIMEOUT_MILLIS = 5 * 60_000;

	private String mBaseUrl;

	public NetworkModule(String baseUrl) {
		mBaseUrl = baseUrl;
	}

	@Singleton
	@Provides
	UserAgent provideUserAgent(final App app) {
		return new UserAgent(app);
	}

	@Singleton
	@Provides
	OkHttpClient provideOkHttpClient(final App app, final UserAgent userAgent) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
		builder.readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(REQUEST_LOG_LEVEL);
		builder.addInterceptor(interceptor);


		builder.addInterceptor(new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				String language = app.getResources().getConfiguration().locale.getLanguage();
				Request request = chain.request().newBuilder()
									   .addHeader("Accept-Language", language)
									   .addHeader("User-Agent", userAgent.toString())
									   .build();
				return chain.proceed(request);
			}
		});
		return builder.build();
	}

	@Singleton
	@Provides
	Retrofit provideRetrofitAdapter(OkHttpClient httpClient, ObjectMapper objectMapper) {
		return new Retrofit.Builder()
				.baseUrl(mBaseUrl)
				.addConverterFactory(JacksonConverterFactory.create(objectMapper))
				.client(httpClient)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.build();
	}

	@Singleton
	@Provides
	Api provideApi(Retrofit retrofit) {
		return retrofit.create(Api.class);
	}

	@Provides
	DefaultErrorHandler provideDefaultErrorHandler(ObjectMapper objectMapper) {
		return new DefaultErrorHandler(objectMapper);
	}
}
