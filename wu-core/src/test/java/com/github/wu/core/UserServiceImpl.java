package com.github.wu.core;

import com.github.wu.common.spi.SPIAlias;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyongxu
 */
@SPIAlias
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public String hello(String msg) {
        return "hello " + msg;
    }

    @Override
    public void hi(String name, String msg) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name is empty");
        }
        logger.info("hi, {}, {}", name, msg);
    }

    @Override
    public void hi(String name, String msg, Integer count) {
        for (int i = 0; i < count; i++) {
            hi(name, msg);
        }
    }

    @Override
    public List<String> search(String msg, Integer size) throws Exception {
        if (StringUtils.isEmpty(msg)) {
            throw new Exception("入参有误");
        }
        List<String> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add("msg is " + msg + " " + i);
        }
        return result;
    }
}
