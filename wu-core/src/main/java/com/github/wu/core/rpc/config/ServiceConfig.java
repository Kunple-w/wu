package com.github.wu.core.rpc.config;

import lombok.Data;

/**
 * service config
 *
 * @author wangyongxu
 */
@Data
public class ServiceConfig {
    private String protocol;
    private String host;
    private int port;
}
