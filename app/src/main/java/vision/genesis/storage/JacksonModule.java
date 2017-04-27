package vision.genesis.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class JacksonModule {

	@Singleton
	@Provides
	public ObjectMapper provideObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		return mapper;
	}
}
