package com.demo.pool;

public interface Service {
    //关闭线程池
    void shutdown();

    //查看线程池是否已经被shutdown
    boolean isShutdown();

    //提交任务到线程池
    void execute(Runnable runnable) throws Exception;
}
