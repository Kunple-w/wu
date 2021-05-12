package com.github.wu.core.register;

import com.github.wu.core.rpc.Invoker;

/**
 * 消费端本地调用
 *
 * @author wangyongxu
 * @see com.github.wu.core.rpc.filter.WuFilter
 */
public interface Consumer<T> extends Invoker<T> {

    // TODO: 2021-05-10 05:26:19 统一客户端和服务端代理模型, 不使用jdk 动态代理 by wangyongxu

}
