package com.github.wu.core.config;

import com.github.wu.common.URL;
import com.github.wu.common.URLConstant;
import lombok.Data;

/**
 * registry config
 *
 * @author wangyongxu
 */
@Data
public class RegistryConfig {

    public RegistryConfig() {
    }

    public RegistryConfig(String address) {
        URL url = URL.of(address);
        this.url = url;
        this.protocol = url.getProtocol();
        this.host = url.getHost();
        this.port = url.getPort();
        this.username = url.getUsername();
        this.password = url.getPassword();
        this.timeout = Integer.parseInt(url.getParam(URLConstant.TIMEOUT_KEY, "5000"));
    }


    private String protocol;
    private String host;
    private int port;
    private String username;
    private String password;
    private int timeout;
    private URL url;

    public URL getUrl() {
        if (url == null) {
            url = new URL(protocol, host, port, "");
        }
        return url;
    }
}
