package com.github.wu.common;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangyongxu
 */
public abstract class AbstractNode implements Node {

    private AtomicBoolean available = new AtomicBoolean(false);
    protected URL url;

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return available.get();
    }

    @Override
    public void destroy() {
        available.set(false);
    }
}
