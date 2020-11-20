package com.github.wu.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author wangyongxu
 */
public class ReflectUtils {

    public static final String EMPTY_PARAM = "void";

    public static Class<?> getClass(String className) {
        try {
            return org.apache.commons.lang3.ClassUtils.getClass(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("class not found. ", e);
        }
    }

    public static String getClassName(Class<?> clazz) {
        return clazz.getName();
    }

    public static Method getMethod(Class<?> className, String methodName, Class<?>[] args) {
        return MethodUtils.getMatchingMethod(className, methodName, args);
    }

    public static String getMethodSignature(String methodName, String paramDesc) {
        if (StringUtils.isEmpty(paramDesc)) {
            return methodName;
        }
        return methodName + "(" + paramDesc + ")";
    }

    public static String getMethodSignature(Method method) {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            return getMethodSignature(methodName, EMPTY_PARAM);
        }

        StringJoiner sj = new StringJoiner(",");
        for (Class<?> parameterType : parameterTypes) {
            sj.add(parameterType.getTypeName());
        }
        return getMethodSignature(methodName, sj.toString());
    }
    public static String getClassMethodNames(Class<?> clazz) {
        Map<String, Method> methodListDesc = getMethodListDesc(clazz);
        StringJoiner sj = new StringJoiner(",");
        for (Map.Entry<String, Method> entry : methodListDesc.entrySet()) {
            sj.add(entry.getValue().getName());
        }
        return sj.toString();
    }

    public static Map<String, Method> getMethodListDesc(Class<?> cls) {
        Map<String, Method> map = new HashMap<>();
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            map.put(getMethodSignature(method), method);
        }
        return map;
    }
}
