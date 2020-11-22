package com.github.wu.registry.api;

/**
 * event listener
 *
 * @author wangyongxu
 */
public interface EventListener {

    void onEvent(LocalRegisterService.URLChanged context);
}
