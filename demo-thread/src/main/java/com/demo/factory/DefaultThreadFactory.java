package com.demo.factory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 工厂模式屏蔽对象创建的过程
 * */
public class DefaultThreadFactory implements ThreadFactory {
    private static final AtomicInteger GROUP_COUNTER = new AtomicInteger(1);

    private static final ThreadGroup group = new ThreadGroup("customThreadPool-" + GROUP_COUNTER.getAndDecrement());

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    @Override
    public Thread createThread(Runnable runnable) {
        return new Thread(group , runnable , "thread-pool-" + COUNTER.getAndDecrement());
    }
}
