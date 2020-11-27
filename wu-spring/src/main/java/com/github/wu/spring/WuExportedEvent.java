package com.github.wu.spring;

import org.springframework.context.ApplicationEvent;

/**
 * @author wangyongxu
 */
public class WuExportedEvent extends ApplicationEvent {
    public WuExportedEvent() {
        this("");
    }

    public WuExportedEvent(Object source) {
        super(source);
    }
}
