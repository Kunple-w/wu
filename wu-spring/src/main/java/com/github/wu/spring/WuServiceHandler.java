package com.github.wu.spring;

import com.github.wu.core.rpc.Exporter;
import com.github.wu.core.rpc.config.RegistryConfig;
import com.github.wu.core.rpc.config.ServiceConfig;
import com.github.wu.core.transport.Server;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author qiankewei
 */
@Component
public class WuServiceHandler implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    @Autowired
    private WuConfigurationProperties configurationProperties;

    private ApplicationContext applicationContext;

    private Server server;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        RegistryConfig registry = configurationProperties.getRegistry();
        ServiceConfig service = configurationProperties.getService();
        if (server == null) {
            createServer(service.getPort());
        }
        Class<? extends Annotation> annotationClass = WuService.class;
        Map<String, Object> beanWithAnnotation = applicationContext.getBeansWithAnnotation(annotationClass);
        Set<Map.Entry<String, Object>> entitySet = beanWithAnnotation.entrySet();
        for (Map.Entry<String, Object> entry : entitySet) {
            Class<? extends Object> clazz = entry.getValue().getClass();
            WuService wuService = AnnotationUtils.findAnnotation(clazz, WuService.class);
            Exporter exporter = new Exporter(wuService.interfaceClass() == Void.class ? clazz : wuService.interfaceClass(), entry.getValue());
            exporter.setPort(service.getPort());
            exporter.setProtocol(registry.getProtocol());
            exporter.setServer(server);
            exporter.export();
            System.out.println(exporter.getURL().toString());
        }

    }

    private void createServer(int port) {
        this.server = new Server(new InetSocketAddress(port));
        server.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
