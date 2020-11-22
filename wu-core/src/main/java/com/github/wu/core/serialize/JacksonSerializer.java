package com.github.wu.core.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wu.common.exception.WuRuntimeException;

import java.io.IOException;

/**
 * @author wangyongxu
 */
public class JacksonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        try {
            return getObjectMapper().writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new WuRuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            return getObjectMapper().readValue(bytes, clazz);
        } catch (IOException e) {
            throw new WuRuntimeException(e);
        }
    }

    private ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
