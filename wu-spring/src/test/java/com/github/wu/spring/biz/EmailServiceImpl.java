package com.github.wu.spring.biz;

import com.github.wu.spring.WuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangyongxu
 */
@WuService
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public String echo(String name, String msg) {
        logger.info("发送邮件成功, name: {}, msg: {}", name, msg);
        return name + " " + msg;
    }
}
