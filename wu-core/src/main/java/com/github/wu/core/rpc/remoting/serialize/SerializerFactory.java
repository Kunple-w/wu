package com.github.wu.core.rpc.remoting.serialize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化工厂
 *
 * @author wangyongxu
 */
public class SerializerFactory {
    /**
     * 缓存
     */
    private static Map<Integer, Serializer> cache = new ConcurrentHashMap<>();

    /**
     * 获得序列化器
     *
     * @param serializeId : 序列化id
     * @return {@link Serializer} 返回序列化器
     * @author wangyongxu
     */
    public static Serializer getSerializer(int serializeId) {
        return cache.computeIfAbsent(serializeId, id -> newSerializer(serializeId));
    }

    private static Serializer newSerializer(int serializeId) {
        switch (serializeId) {
            case 1: {
                return new JacksonSerializer();
            }
            case 2: {
                return new JacksonSerializer();
            }
            default:
                throw new IllegalArgumentException("not support serializeId: " + serializeId);
        }
    }
}
