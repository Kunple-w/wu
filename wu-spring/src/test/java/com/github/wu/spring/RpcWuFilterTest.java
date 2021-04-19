package com.github.wu.spring;

import com.github.wu.core.rpc.filter.WuFilter;
import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;
import com.github.wu.spring.biz.AdminService;
import com.github.wu.spring.biz.EmailService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * @author wangyongxu
 */
@SpringBootTest(classes = TestApplication.class)
@Import(RpcWuFilterTest.AuthFilter.class)
public class RpcWuFilterTest {

    //    @WuInject
    private EmailService emailService;

    //    @WuInject
    private AdminService adminService;

    //    @WuInject
    public void setEmailService(@WuInject EmailService emailService, @WuInject AdminService adminService) {
        this.emailService = emailService;
        this.adminService = adminService;
    }

    @Test
    public void testWuService() {
        Assertions.assertThrows(IllegalAccessException.class, () -> emailService.echo("wu", "hello world"), "user auth error");
    }


    /**
     * @author wangyongxu
     */
    public static class AuthFilter implements WuFilter {
        private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

        @Override
        public boolean before(Invocation invocation, ApiResult apiResult) throws Exception {
            logger.info("invocation: {}", invocation);
//            boolean b = RandomUtils.nextBoolean();
//            if (b) {
//                logger.info("success! ");
//                return true;
//            }
            throw new IllegalAccessException("user auth error");
        }

        @Override
        public void after(Invocation invocation, ApiResult apiResult) throws Exception {
            logger.info("after,result: {} ", apiResult);
        }

        @Override
        public void complete(Invocation invocation, ApiResult apiResult, Exception ex) {
            logger.info("complete, result: {}", apiResult);
        }
    }
}
