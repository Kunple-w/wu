package com.github.wu.spring;

import com.github.wu.spring.biz.AdminService;
import com.github.wu.spring.biz.AdminServiceImpl;
import com.github.wu.spring.biz.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wangyongxu
 */
@SpringBootTest(classes = TestApplication.class)
public class RpcAnnotationTest {

    @WuInject
    private EmailService emailService;

//    @Autowired
    @WuInject

    private AdminService adminService;

    @Test
    public void testWuService() {
        String echo = emailService.echo("wu", "hello world");
        Assertions.assertEquals("wu hello world", echo);
    }

    @Test
    public void testAdmin() {
        String print = adminService.admin("print");
        Assertions.assertEquals("admin print", print);
    }
}
