package com.github.wu.common.spi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * class name generator
 *
 * @author wangyongxu
 */
public interface NameGenerator {

    /**
     * @param cls :
     * @return java.lang.String
     * @author wangyongxu
     */
    String generate(Class<?> cls);


    class NameGeneratorCache {
        private static final Map<String, NameGenerator> cache = new ConcurrentHashMap<>();

        public static void put(NameGenerator nameGenerator) {
            cache.put(nameGenerator.getClass().getName(), nameGenerator);
        }


        public static NameGenerator get(String name) {
            return cache.get(name);
        }

        public static NameGenerator newInstance(Class<? extends NameGenerator> nameGenerator) {
            try {
                return nameGenerator.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                return null;
            }
        }

        public static NameGenerator get(Class<? extends NameGenerator> cls) {
            cache.putIfAbsent(cls.getName(), newInstance(cls));
            return cache.get(cls.getName());
        }
    }
}
