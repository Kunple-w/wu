package com.github.wu.core;

import com.github.wu.common.spi.SPI;

import java.util.List;

/**
 * @author wangyongxu
 */
@SPI
public interface UserService {
    String hello(String msg);

    void hi(String name, String msg);
    void hi(String name, String msg, Integer count);

    List<String> search(String msg, Integer size) throws Exception;
}
