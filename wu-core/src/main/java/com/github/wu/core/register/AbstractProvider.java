package com.github.wu.core.register;

import com.github.wu.common.URL;
import com.github.wu.common.utils.PatternUtils;
import com.github.wu.common.utils.ReflectUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author wangyongxu
 */
public abstract class AbstractProvider<T> implements Provider<T> {

    protected Class<T> cls;

    protected boolean isAvailable = false;

    protected Map<String, Method> methodMap = new HashMap<>();

    protected URL url;

    public AbstractProvider(URL url, Class<T> cls) {
        this.url = url;
        this.cls = cls;
        init();
    }

    @Override
    public void init() {
        methodMap = ReflectUtils.getMethodListDesc(cls);
        isAvailable = true;
    }

    @Override
    public Class<T> getInterface() {
        return cls;
    }

    @Nullable
    @Override
    public Method lookupMethod(String methodName, String paramDesc) {
        String methodSignature = ReflectUtils.getMethodSignature(methodName, paramDesc);
        Method method = methodMap.get(methodSignature);
        if (method != null) {
            return method;
        }
        String reg = methodSignature.replace("null", ".*").replace("(", "\\(").replace(")", "\\)");
        Optional<Map.Entry<String, Method>> optional = Optional.empty();
        for (Map.Entry<String, Method> entry : methodMap.entrySet()) {
            if (PatternUtils.matchers(reg, entry.getKey())) {
                optional = Optional.of(entry);
            }
        }
        return optional.map(Map.Entry::getValue).orElse(null);
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public String toString() {
        return url.toString();
    }

    @Override
    public void destroy() {
        isAvailable = false;
    }
}
