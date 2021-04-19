package com.github.wu.spring.configurer;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wangyongxu
 */
public class ConfigurerSupport {
    private List<WuConfigurer> wuConfigurerList;

    public List<WuConfigurer> getWuConfigurerList() {
        return wuConfigurerList;
    }

    @Autowired(required = false)
    public void setWuConfigurerList(List<WuConfigurer> wuConfigurerList) {
        this.wuConfigurerList = wuConfigurerList;
    }

}
