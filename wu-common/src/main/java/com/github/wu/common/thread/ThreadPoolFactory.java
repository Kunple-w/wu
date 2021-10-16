package com.github.wu.common.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangyongxu
 */
public class ThreadPoolFactory {

    public static ThreadPoolExecutor newFixed(int nThreads, String name) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(Integer.MAX_VALUE), new NamedThreadFactory(name, true));
    }

    public static ThreadPoolExecutor newSingle(int nThreads, String name) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(Integer.MAX_VALUE), new NamedThreadFactory(name, true));
    }


}
