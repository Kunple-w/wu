package com.github.wu.spring;

import com.github.wu.core.rpc.config.ExportConfig;
import com.github.wu.core.rpc.config.RegistryConfig;
import com.github.wu.core.rpc.config.ServiceConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author qiankewei
 */
public class WuServiceListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private WuConfigurationProperties configurationProperties;

    private ApplicationContext applicationContext;

    public WuServiceListener(WuConfigurationProperties wuConfigurationProperties) {
        this.configurationProperties = wuConfigurationProperties;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        RegistryConfig registry = configurationProperties.getRegistry();
        ServiceConfig service = configurationProperties.getService();
        Class<? extends Annotation> annotationClass = WuService.class;
        Map<String, Object> beanWithAnnotation = applicationContext.getBeansWithAnnotation(annotationClass);
        Set<Map.Entry<String, Object>> entitySet = beanWithAnnotation.entrySet();
        for (Map.Entry<String, Object> entry : entitySet) {
            Class<? extends Object> clazz = entry.getValue().getClass();
            WuService wuService = AnnotationUtils.findAnnotation(clazz, WuService.class);
            ExportConfig exportConfig = new ExportConfig(wuService.interfaceClass() == Void.class ? clazz.getInterfaces()[0]: wuService.interfaceClass(), entry.getValue(), registry, service);
            exportConfig.export();
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
