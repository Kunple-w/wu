package com.github.wu.spring;

import com.github.wu.common.exception.RpcException;
import com.github.wu.core.rpc.filter.FilterScope;
import com.github.wu.core.rpc.filter.WuFilter;
import com.github.wu.core.transport.ApiResult;
import com.github.wu.core.transport.Invocation;
import com.github.wu.spring.biz.AdminService;
import com.github.wu.spring.biz.EmailService;
import org.apache.curator.test.TestingServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.IOException;

/**
 * @author wangyongxu
 */
@SpringBootTest(classes = TestApplication.class, properties = "wu.registry.port=52321")
@Import(RpcWuFilterTest.AuthFilter.class)
public class RpcWuFilterTest {

    private EmailService emailService;

    private AdminService adminService;


    public void setEmailService(@WuInject EmailService emailService, @WuInject AdminService adminService) {
        this.emailService = emailService;
        this.adminService = adminService;
    }

    private static TestingServer server;

    @BeforeAll
    static void setup() throws Exception {
        server = new TestingServer(52321, true);
        server.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        server.close();
    }

    @Test
    public void testWuService() {
        Assertions.assertThrows(RpcException.class, () -> emailService.echo("wu", "hello world"), "user auth error");
    }


    /**
     * @author wangyongxu
     */
    public static class AuthFilter implements WuFilter {
        private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

        @Override
        public boolean before(Invocation invocation, ApiResult apiResult) throws RpcException {
            logger.info("invocation: {}", invocation);
            throw new RpcException("user auth error");
        }

        @Override
        public void after(Invocation invocation, ApiResult apiResult) throws RpcException {
            logger.info("after,result: {} ", apiResult);
        }

        @Override
        public void complete(Invocation invocation, ApiResult apiResult, Throwable ex) {
            logger.info("complete, result: {}", apiResult);
        }

        @Override
        public FilterScope[] scope() {
            return new FilterScope[]{FilterScope.SERVER};
        }
    }
}
