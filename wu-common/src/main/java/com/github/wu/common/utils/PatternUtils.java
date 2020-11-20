package com.github.wu.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * regex utils
 *
 * @author wangyongxu
 */
public class PatternUtils {

    private static final Map<String, Pattern> cache = new ConcurrentHashMap<>();

    /**
     * @param regex : regex
     * @param input : input
     * @return boolean
     * @author wangyongxu
     * @see Matcher#matches()
     */
    public static boolean matchers(String regex, String input) {
        cache.putIfAbsent(regex, Pattern.compile(regex));
        return cache.get(regex).matcher(input).matches();
    }

}
