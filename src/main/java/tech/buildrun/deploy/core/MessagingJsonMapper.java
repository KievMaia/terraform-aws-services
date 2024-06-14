package tech.buildrun.deploy.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class MessagingJsonMapper {
    private final ObjectMapper objectMapper;

    public MessagingJsonMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String toJson(final Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException("Error to parse object to JSON string", e);
        }
    }

    public <T> T fromJson(final String json, final Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (final Throwable e) {
            throw new RuntimeException("Error to parse JSON string to object", e);
        }
    }
}