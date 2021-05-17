package com.github.wu.core.config;

/**
 * service config
 *
 * @author wangyongxu
 */
public class ServiceConfig {
    private String protocol = "wu";
    private String host = "localhost";
    private int port = 11996;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
