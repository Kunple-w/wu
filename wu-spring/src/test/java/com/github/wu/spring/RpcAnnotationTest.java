package com.github.wu.spring;

import com.github.wu.spring.biz.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wangyongxu
 */
@SpringBootTest(classes = TestApplication.class)
public class RpcAnnotationTest {

    @WuInject
    private EmailService emailService;

    @Test
    public void testWuService() {
        String echo = emailService.echo("wu", "hello world");
        Assertions.assertEquals("wu hello world", echo);
    }
}
