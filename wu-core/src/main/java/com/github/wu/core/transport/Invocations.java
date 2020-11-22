package com.github.wu.core.transport;

import com.github.wu.common.utils.ReflectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * @author wangyongxu
 */
public class Invocations {
    public static Method parseMethod(Invocation invocation) {
        String methodName = invocation.getMethodName();
        String argsDesc = invocation.getArgsDesc();
        Class<?> serviceClass = ReflectUtils.getClass(invocation.getServiceName());
        String[] argTypes = StringUtils.split(argsDesc, ",");
        Class<?>[] argClasses = new Class[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            argClasses[i] = ReflectUtils.getClass(argTypes[i]);
        }
        return ReflectUtils.getMethod(serviceClass, methodName, argClasses);
    }

    public static Invocation parseInvocation(Class<?> serviceClass, String method, Object[] args) {
        Invocation invocation = new Invocation();
        invocation.setServiceName(serviceClass.getName());
        invocation.setMethodName(method);
        invocation.setArgs(args);
        return invocation;
    }

    public static Invocation parseInvocation(Method method, Object[] args) {
        Class<?> declaringClass = method.getDeclaringClass();
        return parseInvocation(declaringClass, method.getName(), args);
    }
}
