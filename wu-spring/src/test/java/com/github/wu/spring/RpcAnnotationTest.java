package com.github.wu.spring;

import com.github.wu.spring.biz.AdminService;
import com.github.wu.spring.biz.AdminServiceImpl;
import com.github.wu.spring.biz.EmailService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wangyongxu
 */
@SpringBootTest(classes = TestApplication.class)
public class RpcAnnotationTest {

//    @WuInject
    private EmailService emailService;

//    @WuInject
    private AdminService adminService;

//    @WuInject
    public void setEmailService(@WuInject EmailService emailService, @WuInject AdminService adminService){
        this.emailService = emailService;
        this.adminService = adminService;
    }

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
