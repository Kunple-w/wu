package com.github.wu.spring;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

/**
 * @author wangyongxu
 */
public class WuAutowiredAnnotationBeanPostProcessor extends AutowiredAnnotationBeanPostProcessor {
    public WuAutowiredAnnotationBeanPostProcessor() {
        setAutowiredAnnotationType(WuInject.class);
    }





}
