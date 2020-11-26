package com.github.wu.spring;

import com.github.wu.core.rpc.config.ExportConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangyongxu
 */
@Component
public class WuServiceBeanPostProcessor implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {

    private ApplicationContext applicationContext;

    private Environment environment;

    private ClassLoader classLoader;

    private ResourceLoader resourceLoader;

    private ConfigurableListableBeanFactory beanFactory;

    private BeanFactory beanFactory2;

    @Autowired
    private WuConfigurationProperties wuConfigurationProperties;

    private Map<String, Object> needExportBean = new HashMap<>();

    private AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        boolean isCandidate = AnnotatedElementUtils.isAnnotated(bean.getClass(), WuService.class);
        if (isCandidate) {
            needExportBean.put(beanName, bean);
        }
        return bean;
    }


    private Class<?> parseExportInterfaceClass(Object bean) {
        WuService wuService = AnnotationUtils.findAnnotation(bean.getClass(), WuService.class);
        if (wuService.interfaceClass() == Void.class) {
            Class<?>[] allInterfaces = ClassUtils.getAllInterfaces(bean);
            if (allInterfaces.length == 1) {
                return allInterfaces[0];
            } else {
                throw new IllegalArgumentException(bean.toString() + "@WuService interfaceClass can't be Void.class");
            }
        } else {
            return wuService.interfaceClass();
        }
    }


    private void export(Object bean) {
        Class<?> interfaceClass = parseExportInterfaceClass(bean);
        ExportConfig exportConfig = new ExportConfig(interfaceClass, bean, wuConfigurationProperties.getRegistry(), wuConfigurationProperties.getService());
        exportConfig.export();
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (started.compareAndSet(false, true)) {
            for (Object bean : needExportBean.values()) {
                export(bean);
            }
        }
    }
}
