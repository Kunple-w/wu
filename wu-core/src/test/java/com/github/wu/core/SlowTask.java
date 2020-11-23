package com.github.wu.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.*;

/**
 * @author wangyongxu
 */
public class SlowTask {
    private static final Logger logger = LoggerFactory.getLogger(SlowTask.class);

    @SengTask(cron = "* * 1 * * *")
    void doSomeThing(String a, String b) {
        logger.info("任务执行: {}, {}", a, b);
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface SengTask {

        String cron() default "";
    }
}
