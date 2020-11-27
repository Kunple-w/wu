package com.github.wu.spring;

import com.github.wu.core.rpc.config.ReferenceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyongxu
 */
@Component
public class WuAutowiredAnnotationBeanPostProcessor implements ApplicationListener<ApplicationEvent>, BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(WuAutowiredAnnotationBeanPostProcessor.class);

    @Autowired
    private WuConfigurationProperties wuConfigurationProperties;

    private final Map<Class<?>, ReferenceConfig<?>> cachedReference = new HashMap<>();


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithLocalFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                WuInject fieldAnnotation = field.getAnnotation(WuInject.class);
                if (fieldAnnotation != null) {
                    logger.info("{}", fieldAnnotation.toString());
                    Object value = getOrCreateRef(field.getType());
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, bean, value);
                }
            }
        });
        return bean;
    }

    private Object getOrCreateRef(Class<?> interfaceClass) {
        ReferenceConfig<?> referenceConfig = cachedReference.get(interfaceClass);
        if (referenceConfig == null) {
            referenceConfig = new ReferenceConfig<>(interfaceClass, wuConfigurationProperties.getRegistry());
            cachedReference.put(interfaceClass, referenceConfig);
        }
        return referenceConfig.refer();
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof WuExportedEvent) {
            startReference();
        } else if (event instanceof ContextClosedEvent) {
            destroy();
        }
    }

    private void startReference() {
        cachedReference.values().forEach(ReferenceConfig::init);

    }

    private void destroy() {
        cachedReference.values().forEach(ReferenceConfig::destroy);
    }
}
