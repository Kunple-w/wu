package com.github.wu.spring;


import com.github.wu.core.rpc.Reference;
import com.github.wu.core.rpc.config.RegistryConfig;
import com.github.wu.core.rpc.config.ServiceConfig;
import com.github.wu.core.transport.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author qiankewei
 */
@Component
public class WuInjectBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(WuInjectBeanPostProcessor.class);
    @Autowired
    private WuConfigurationProperties configurationProperties;

    private ApplicationContext applicationContext;

    private Client client;


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = bean.getClass();
        do {
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                if (hasAnnotation(field.getAnnotations(), WuInject.class.getName())) {
                    setField(bean, field);
                }
            }
        }
        while ((beanClass = beanClass.getSuperclass()) != null);
        return bean;
    }

    private void setField(Object bean, Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            Object fieldBean;
            RegistryConfig registry = configurationProperties.getRegistry();
            if (client == null) {
                client = new Client(new InetSocketAddress(registry.getHost(),registry.getPort()));
            }
            fieldBean = new Reference(client, field.getType()).refer();
            /*try {
                fieldBean = applicationContext.getBean(field.getType());
            } catch (NoSuchBeanDefinitionException e) {
                RegistryConfig registry = configurationProperties.getRegistry();
                if (client == null) {
                    client = new Client(new InetSocketAddress(registry.getPort()));
                }
                fieldBean = new Reference(client, bean.getClass()).refer();
            }*/
            field.set(bean, fieldBean);

        } catch (IllegalAccessException e) {
            logger.error("set field error.", e);
        }
    }

    private boolean hasAnnotation(Annotation[] annotations, String annotationName) {
        if (annotations.length == 0) {
            return false;
        }
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getName().equals(annotationName)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        RegistryConfig registry = configurationProperties.getRegistry();
        ServiceConfig service = configurationProperties.getService();
        if (client == null) {
            client = new Client(new InetSocketAddress(registry.getPort()));
        }
        Class<? extends Annotation> annotationClass = WuInject.class;
        Map<String, Object> beanWithAnnotation = applicationContext.getBeansWithAnnotation(annotationClass);
        Set<Map.Entry<String, Object>> entitySet = beanWithAnnotation.entrySet();
        for (Map.Entry<String, Object> entry : entitySet) {
            Class<? extends Object> clazz = entry.getValue().getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (hasAnnotation(field.getDeclaredAnnotations(), WuInject.class.getName())) {
                    setField(entry.getValue(), field);
                }
            }
        }
    }
}
