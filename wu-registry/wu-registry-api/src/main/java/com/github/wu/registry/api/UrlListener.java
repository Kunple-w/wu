package com.github.wu.registry.api;

import com.github.wu.common.URL;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * event listener
 *
 * @author wangyongxu
 */
public interface UrlListener {

    void onEvent(URLChanged context);

    @Data
    @AllArgsConstructor
    class URLChanged {

        private Set<URL> now;

    }
}
