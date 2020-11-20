package com.github.wu.common;


/**
 * 节点信息
 *
 * @author wangyongxu
 * @date 2020/9/15 18:45
 */
public interface Node extends LifeCycle {

    URL getURL();

    boolean isAvailable();
}