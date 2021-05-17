package com.github.wu.core.rpc.remoting.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
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
        ObjectMapper objectMapper = new ObjectMapper();
        // fix issue#2
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        return objectMapper;
    }
}
