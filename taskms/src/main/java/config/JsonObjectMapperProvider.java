package config;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
public class JsonObjectMapperProvider implements ContextResolver<ObjectMapper> {

    final ObjectMapper defaultObjectMapper;

    public JsonObjectMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
    }

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        return defaultObjectMapper;
    }


    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//        mapper.disable(MapperFeature.USE_GETTERS_AS_SETTERS);
        return mapper;
    }

}